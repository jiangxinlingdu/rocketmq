/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.remoting.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.apache.rocketmq.remoting.ChannelEventListener;
import org.apache.rocketmq.remoting.InvokeCallback;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.Pair;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.common.SemaphoreReleaseOnlyOnce;
import org.apache.rocketmq.remoting.common.ServiceThread;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;
import org.apache.rocketmq.remoting.protocol.RemotingSysResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD

=======
/**
 * Server与Client公用抽象类
 */
>>>>>>> rmq/master
public abstract class NettyRemotingAbstract {

    /**
     * Remoting logger instance.
     */
    private static final Logger PLOG = LoggerFactory.getLogger(RemotingHelper.ROCKETMQ_REMOTING);

    /**
     * Semaphore to limit maximum number of on-going one-way requests, which protects system memory footprint.
     */
<<<<<<< HEAD
=======
    // 信号量，Oneway情况会使用，防止本地Netty缓存请求过多
>>>>>>> rmq/master
    protected final Semaphore semaphoreOneway;

    /**
     * Semaphore to limit maximum number of on-going asynchronous requests, which protects system memory footprint.
     */
<<<<<<< HEAD
=======
    // 信号量，异步调用情况会使用，防止本地Netty缓存请求过多
>>>>>>> rmq/master
    protected final Semaphore semaphoreAsync;

    /**
     * This map caches all on-going requests.
     */
<<<<<<< HEAD
=======
    // 缓存所有对外请求
>>>>>>> rmq/master
    protected final ConcurrentMap<Integer /* opaque */, ResponseFuture> responseTable =
        new ConcurrentHashMap<Integer, ResponseFuture>(256);

    /**
     * This container holds all processors per request code, aka, for each incoming request, we may look up the
     * responding processor in this map to handle the request.
     */
<<<<<<< HEAD
=======
    // 注册的各个RPC处理器
>>>>>>> rmq/master
    protected final HashMap<Integer/* request code */, Pair<NettyRequestProcessor, ExecutorService>> processorTable =
        new HashMap<Integer, Pair<NettyRequestProcessor, ExecutorService>>(64);

    /**
     * Executor to feed netty events to user defined {@link ChannelEventListener}.
     */
    protected final NettyEventExecutor nettyEventExecutor = new NettyEventExecutor();

    /**
     * The default request processor to use in case there is no exact match in {@link #processorTable} per request code.
     */
<<<<<<< HEAD
=======
    // 默认请求代码处理器
>>>>>>> rmq/master
    protected Pair<NettyRequestProcessor, ExecutorService> defaultRequestProcessor;

    /**
     * Constructor, specifying capacity of one-way and asynchronous semaphores.
     * @param permitsOneway Number of permits for one-way requests.
     * @param permitsAsync Number of permits for asynchronous requests.
     */
    public NettyRemotingAbstract(final int permitsOneway, final int permitsAsync) {
        this.semaphoreOneway = new Semaphore(permitsOneway, true);
        this.semaphoreAsync = new Semaphore(permitsAsync, true);
    }

    /**
     * Custom channel event listener.
     * @return custom channel event listener if defined; null otherwise.
     */
    public abstract ChannelEventListener getChannelEventListener();

    /**
     * Put a netty event to the executor.
     * @param event Netty event instance.
     */
    public void putNettyEvent(final NettyEvent event) {
        this.nettyEventExecutor.putNettyEvent(event);
    }

    /**
     * Entry of incoming command processing.
     *
     * <p>
     *     <strong>Note:</strong>
     *     The incoming remoting command may be
     *     <ul>
     *         <li>An inquiry request from a remote peer component;</li>
     *         <li>A response to a previous request issued by this very participant.</li>
     *     </ul>
     * </p>
     * @param ctx Channel handler context.
     * @param msg incoming remoting command.
     * @throws Exception if there were any error while processing the incoming command.
     */
<<<<<<< HEAD
=======
    //消息接收处理
>>>>>>> rmq/master
    public void processMessageReceived(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
        final RemotingCommand cmd = msg;
        if (cmd != null) {
            switch (cmd.getType()) {
                case REQUEST_COMMAND:
                    processRequestCommand(ctx, cmd);
                    break;
                case RESPONSE_COMMAND:
                    processResponseCommand(ctx, cmd);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Process incoming request command issued by remote peer.
     * @param ctx channel handler context.
     * @param cmd request command.
     */
    public void processRequestCommand(final ChannelHandlerContext ctx, final RemotingCommand cmd) {
        final Pair<NettyRequestProcessor, ExecutorService> matched = this.processorTable.get(cmd.getCode());
        final Pair<NettyRequestProcessor, ExecutorService> pair = null == matched ? this.defaultRequestProcessor : matched;
        final int opaque = cmd.getOpaque();

        if (pair != null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        RPCHook rpcHook = NettyRemotingAbstract.this.getRPCHook();
                        if (rpcHook != null) {
<<<<<<< HEAD
=======
                        	//处理前
>>>>>>> rmq/master
                            rpcHook.doBeforeRequest(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), cmd);
                        }

                        final RemotingCommand response = pair.getObject1().processRequest(ctx, cmd);
                        if (rpcHook != null) {
                            rpcHook.doAfterResponse(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), cmd, response);
                        }

                        if (!cmd.isOnewayRPC()) {
                            if (response != null) {
                                response.setOpaque(opaque);
                                response.markResponseType();
                                try {
                                    ctx.writeAndFlush(response);
                                } catch (Throwable e) {
                                    PLOG.error("process request over, but response failed", e);
                                    PLOG.error(cmd.toString());
                                    PLOG.error(response.toString());
                                }
                            } else {
<<<<<<< HEAD

=======
                            	// 收到请求，但是没有返回应答，可能是processRequest中进行了应答，忽略这种情况
>>>>>>> rmq/master
                            }
                        }
                    } catch (Throwable e) {
                        PLOG.error("process request exception", e);
                        PLOG.error(cmd.toString());

                        if (!cmd.isOnewayRPC()) {
                            final RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR, //
                                RemotingHelper.exceptionSimpleDesc(e));
                            response.setOpaque(opaque);
                            ctx.writeAndFlush(response);
                        }
                    }
                }
            };

            if (pair.getObject1().rejectRequest()) {
                final RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_BUSY,
                    "[REJECTREQUEST]system busy, start flow control for a while");
                response.setOpaque(opaque);
                ctx.writeAndFlush(response);
                return;
            }

            try {
                final RequestTask requestTask = new RequestTask(run, ctx.channel(), cmd);
<<<<<<< HEAD
                pair.getObject2().submit(requestTask);
            } catch (RejectedExecutionException e) {
=======
              // 这里需要做流控，要求线程池对应的队列必须是有大小限制的
                pair.getObject2().submit(requestTask);
            } catch (RejectedExecutionException e) {
            	// 每个线程10s打印一次
>>>>>>> rmq/master
                if ((System.currentTimeMillis() % 10000) == 0) {
                    PLOG.warn(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) //
                        + ", too many requests and system thread pool busy, RejectedExecutionException " //
                        + pair.getObject2().toString() //
                        + " request code: " + cmd.getCode());
                }

                if (!cmd.isOnewayRPC()) {
                    final RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_BUSY,
                        "[OVERLOAD]system busy, start flow control for a while");
                    response.setOpaque(opaque);
                    ctx.writeAndFlush(response);
                }
            }
        } else {
            String error = " request type " + cmd.getCode() + " not supported";
            final RemotingCommand response =
                RemotingCommand.createResponseCommand(RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED, error);
            response.setOpaque(opaque);
            ctx.writeAndFlush(response);
            PLOG.error(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) + error);
        }
    }

    /**
     * Process response from remote peer to the previous issued requests.
     * @param ctx channel handler context.
     * @param cmd response command instance.
     */
    public void processResponseCommand(ChannelHandlerContext ctx, RemotingCommand cmd) {
        final int opaque = cmd.getOpaque();
        final ResponseFuture responseFuture = responseTable.get(opaque);
        if (responseFuture != null) {
            responseFuture.setResponseCommand(cmd);

            responseFuture.release();

            responseTable.remove(opaque);

            if (responseFuture.getInvokeCallback() != null) {
                executeInvokeCallback(responseFuture);
            } else {
                responseFuture.putResponse(cmd);
            }
        } else {
            PLOG.warn("receive response, but not matched any request, " + RemotingHelper.parseChannelRemoteAddr(ctx.channel()));
            PLOG.warn(cmd.toString());
        }
    }

    /**
     * Execute callback in callback executor. If callback executor is null, run directly in current thread
     * @param responseFuture
     */
    private void executeInvokeCallback(final ResponseFuture responseFuture) {
        boolean runInThisThread = false;
        ExecutorService executor = this.getCallbackExecutor();
        if (executor != null) {
            try {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            responseFuture.executeInvokeCallback();
                        } catch (Throwable e) {
                            PLOG.warn("execute callback in executor exception, and callback throw", e);
                        }
                    }
                });
            } catch (Exception e) {
                runInThisThread = true;
                PLOG.warn("execute callback in executor exception, maybe executor busy", e);
            }
        } else {
            runInThisThread = true;
        }

        if (runInThisThread) {
            try {
                responseFuture.executeInvokeCallback();
            } catch (Throwable e) {
                PLOG.warn("executeInvokeCallback Exception", e);
            }
        }
    }

    /**
     * Custom RPC hook.
     * @return RPC hook if specified; null otherwise.
     */
    public abstract RPCHook getRPCHook();

    /**
     * This method specifies thread pool to use while invoking callback methods.
     * @return Dedicated thread pool instance if specified; or null if the callback is supposed to be executed in the
     * netty event-loop thread.
     */
    public abstract ExecutorService getCallbackExecutor();

    /**
     * <p>
     *    This method is periodically invoked to scan and expire deprecated request.
     * </p>
     */
    public void scanResponseTable() {
        final List<ResponseFuture> rfList = new LinkedList<ResponseFuture>();
        Iterator<Entry<Integer, ResponseFuture>> it = this.responseTable.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ResponseFuture> next = it.next();
            ResponseFuture rep = next.getValue();

            if ((rep.getBeginTimestamp() + rep.getTimeoutMillis() + 1000) <= System.currentTimeMillis()) {
                rep.release();
                it.remove();
                rfList.add(rep);
                PLOG.warn("remove timeout request, " + rep);
            }
        }

        for (ResponseFuture rf : rfList) {
            try {
                executeInvokeCallback(rf);
            } catch (Throwable e) {
                PLOG.warn("scanResponseTable, operationComplete Exception", e);
            }
        }
    }

<<<<<<< HEAD
=======
    //同步调用实现
>>>>>>> rmq/master
    public RemotingCommand invokeSyncImpl(final Channel channel, final RemotingCommand request, final long timeoutMillis)
        throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {
        final int opaque = request.getOpaque();

        try {
            final ResponseFuture responseFuture = new ResponseFuture(opaque, timeoutMillis, null, null);
            this.responseTable.put(opaque, responseFuture);
            final SocketAddress addr = channel.remoteAddress();
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        responseFuture.setSendRequestOK(true);
                        return;
                    } else {
                        responseFuture.setSendRequestOK(false);
                    }

                    responseTable.remove(opaque);
                    responseFuture.setCause(f.cause());
                    responseFuture.putResponse(null);
                    PLOG.warn("send a request command to channel <" + addr + "> failed.");
                }
            });

            RemotingCommand responseCommand = responseFuture.waitResponse(timeoutMillis);
            if (null == responseCommand) {
                if (responseFuture.isSendRequestOK()) {
                    throw new RemotingTimeoutException(RemotingHelper.parseSocketAddressAddr(addr), timeoutMillis,
                        responseFuture.getCause());
                } else {
                    throw new RemotingSendRequestException(RemotingHelper.parseSocketAddressAddr(addr), responseFuture.getCause());
                }
            }

            return responseCommand;
        } finally {
            this.responseTable.remove(opaque);
        }
    }

<<<<<<< HEAD
=======
    //异步调用实现
>>>>>>> rmq/master
    public void invokeAsyncImpl(final Channel channel, final RemotingCommand request, final long timeoutMillis,
        final InvokeCallback invokeCallback)
        throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {
        final int opaque = request.getOpaque();
<<<<<<< HEAD
        boolean acquired = this.semaphoreAsync.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
        if (acquired) {
            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreAsync);
=======
        boolean acquired = this.semaphoreAsync.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);//控制异步请求的个数以及超时
        if (acquired) {
            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreAsync);//使用布尔原子变量，信号量保证只释放一次
>>>>>>> rmq/master

            final ResponseFuture responseFuture = new ResponseFuture(opaque, timeoutMillis, invokeCallback, once);
            this.responseTable.put(opaque, responseFuture);
            try {
                channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        if (f.isSuccess()) {
                            responseFuture.setSendRequestOK(true);
                            return;
                        } else {
                            responseFuture.setSendRequestOK(false);
                        }

                        responseFuture.putResponse(null);
                        responseTable.remove(opaque);
                        try {
                            executeInvokeCallback(responseFuture);
                        } catch (Throwable e) {
                            PLOG.warn("excute callback in writeAndFlush addListener, and callback throw", e);
                        } finally {
                            responseFuture.release();
                        }

                        PLOG.warn("send a request command to channel <{}> failed.", RemotingHelper.parseChannelRemoteAddr(channel));
                    }
                });
            } catch (Exception e) {
                responseFuture.release();
                PLOG.warn("send a request command to channel <" + RemotingHelper.parseChannelRemoteAddr(channel) + "> Exception", e);
                throw new RemotingSendRequestException(RemotingHelper.parseChannelRemoteAddr(channel), e);
            }
        } else {
            if (timeoutMillis <= 0) {
                throw new RemotingTooMuchRequestException("invokeAsyncImpl invoke too fast");
            } else {
                String info =
                    String.format("invokeAsyncImpl tryAcquire semaphore timeout, %dms, waiting thread nums: %d semaphoreAsyncValue: %d", //
                        timeoutMillis, //
                        this.semaphoreAsync.getQueueLength(), //
                        this.semaphoreAsync.availablePermits()//
                    );
                PLOG.warn(info);
                throw new RemotingTimeoutException(info);
            }
        }
    }

<<<<<<< HEAD
=======
    //oneway调用实现
>>>>>>> rmq/master
    public void invokeOnewayImpl(final Channel channel, final RemotingCommand request, final long timeoutMillis)
        throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {
        request.markOnewayRPC();
        boolean acquired = this.semaphoreOneway.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
        if (acquired) {
            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreOneway);
            try {
                channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        once.release();
                        if (!f.isSuccess()) {
                            PLOG.warn("send a request command to channel <" + channel.remoteAddress() + "> failed.");
                        }
                    }
                });
            } catch (Exception e) {
                once.release();
                PLOG.warn("write send a request command to channel <" + channel.remoteAddress() + "> failed.");
                throw new RemotingSendRequestException(RemotingHelper.parseChannelRemoteAddr(channel), e);
            }
        } else {
            if (timeoutMillis <= 0) {
                throw new RemotingTooMuchRequestException("invokeOnewayImpl invoke too fast");
            } else {
                String info = String.format(
                    "invokeOnewayImpl tryAcquire semaphore timeout, %dms, waiting thread nums: %d semaphoreAsyncValue: %d", //
                    timeoutMillis, //
                    this.semaphoreOneway.getQueueLength(), //
                    this.semaphoreOneway.availablePermits()//
                );
                PLOG.warn(info);
                throw new RemotingTimeoutException(info);
            }
        }
    }

    class NettyEventExecutor extends ServiceThread {
<<<<<<< HEAD
=======
    	// //使用LinkedBlockingQueue存NettyEvent
>>>>>>> rmq/master
        private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
        private final int maxSize = 10000;

        public void putNettyEvent(final NettyEvent event) {
<<<<<<< HEAD
            if (this.eventQueue.size() <= maxSize) {
                this.eventQueue.add(event);
            } else {
                PLOG.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(), event.toString());
=======
        	//这里的size没有坑 有些size并不是常量时间内返回的，主要是分桶策略减少集合的线程竞争才有这里问题。
            if (this.eventQueue.size() <= maxSize) { 
                this.eventQueue.add(event);
            } else {
                PLOG.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(), event.toString());
                //反正源码在此，可以自己考虑一些处理
>>>>>>> rmq/master
            }
        }

        @Override
        public void run() {
            PLOG.info(this.getServiceName() + " service started");

            final ChannelEventListener listener = NettyRemotingAbstract.this.getChannelEventListener();

            while (!this.isStopped()) {
                try {
                    NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                    if (event != null && listener != null) {
                        switch (event.getType()) {
                            case IDLE:
                                listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                                break;
                            case CLOSE:
                                listener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                                break;
                            case CONNECT:
                                listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                                break;
                            case EXCEPTION:
                                listener.onChannelException(event.getRemoteAddr(), event.getChannel());
                                break;
                            default:
                                break;

                        }
                    }
                } catch (Exception e) {
                    PLOG.warn(this.getServiceName() + " service has exception. ", e);
                }
            }

            PLOG.info(this.getServiceName() + " service end");
        }

        @Override
        public String getServiceName() {
            return NettyEventExecutor.class.getSimpleName();
        }
    }
}
