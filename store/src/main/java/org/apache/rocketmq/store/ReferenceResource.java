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
package org.apache.rocketmq.store;

import java.util.concurrent.atomic.AtomicLong;

<<<<<<< HEAD
=======
/**
 * 引用计数基类，类似于C++智能指针实现
 */
>>>>>>> rmq/master
public abstract class ReferenceResource {
    protected final AtomicLong refCount = new AtomicLong(1);
    protected volatile boolean available = true;
    protected volatile boolean cleanupOver = false;
    private volatile long firstShutdownTimestamp = 0;

<<<<<<< HEAD
=======
    /**
     * 资源是否能HOLD住
     */
>>>>>>> rmq/master
    public synchronized boolean hold() {
        if (this.isAvailable()) {
            if (this.refCount.getAndIncrement() > 0) {
                return true;
            } else {
                this.refCount.getAndDecrement();
            }
        }

        return false;
    }

<<<<<<< HEAD
=======
    /**
     * 资源是否可用，即是否可被HOLD
     */
>>>>>>> rmq/master
    public boolean isAvailable() {
        return this.available;
    }

<<<<<<< HEAD
=======
    /**
     * 禁止资源被访问 shutdown不允许调用多次，最好是由管理线程调用
     */
>>>>>>> rmq/master
    public void shutdown(final long intervalForcibly) {
        if (this.available) {
            this.available = false;
            this.firstShutdownTimestamp = System.currentTimeMillis();
            this.release();
<<<<<<< HEAD
        } else if (this.getRefCount() > 0) {
=======
        } 
        // 强制shutdown
        else if (this.getRefCount() > 0) {
>>>>>>> rmq/master
            if ((System.currentTimeMillis() - this.firstShutdownTimestamp) >= intervalForcibly) {
                this.refCount.set(-1000 - this.getRefCount());
                this.release();
            }
        }
    }

<<<<<<< HEAD
=======
    /**
     * 释放资源
     */
>>>>>>> rmq/master
    public void release() {
        long value = this.refCount.decrementAndGet();
        if (value > 0)
            return;

        synchronized (this) {
<<<<<<< HEAD

=======
        	// cleanup内部要对是否clean做处理
>>>>>>> rmq/master
            this.cleanupOver = this.cleanup(value);
        }
    }

    public long getRefCount() {
        return this.refCount.get();
    }

    public abstract boolean cleanup(final long currentRef);

<<<<<<< HEAD
=======
    /**
     * 资源是否被清理完成
     */
>>>>>>> rmq/master
    public boolean isCleanupOver() {
        return this.refCount.get() <= 0 && this.cleanupOver;
    }
}
