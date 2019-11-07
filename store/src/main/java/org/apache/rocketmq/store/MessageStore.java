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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageExtBatch;

/**
 * This class defines contracting interfaces to implement, allowing third-party vendor to use customized message store.
 */
<<<<<<< HEAD
=======
/**
 * 存储层对外提供的接口
 */
>>>>>>> rmq/master
public interface MessageStore {

    /**
     * Load previously stored messages.
     * @return true if success; false otherwise.
     */
<<<<<<< HEAD
=======
	  /**
     * 重启时，加载数据
     */
>>>>>>> rmq/master
    boolean load();

    /**
     * Launch this message store.
     * @throws Exception if there is any error.
     */
<<<<<<< HEAD
=======
    /**
     * 启动服务
     */
>>>>>>> rmq/master
    void start() throws Exception;

    /**
     * Shutdown this message store.
     */
<<<<<<< HEAD
=======
    /**
     * 关闭服务
     */
>>>>>>> rmq/master
    void shutdown();

    /**
     * Destroy this message store. Generally, all persistent files should be removed after invocation.
     */
<<<<<<< HEAD
=======
    /**
     * 删除所有文件，单元测试会使用
     */
>>>>>>> rmq/master
    void destroy();

    /**
     * Store a message into store.
     * @param msg Message instance to store
     * @return result of store operation.
     */
<<<<<<< HEAD
=======
    /**
     * 存储消息
     */
>>>>>>> rmq/master
    PutMessageResult putMessage(final MessageExtBrokerInner msg);

    /**
     * Store a batch of messages.
     * @param messageExtBatch Message batch.
     * @return result of storing batch messages.
     */
    PutMessageResult putMessages(final MessageExtBatch messageExtBatch);

    /**
     * Query at most <code>maxMsgNums</code> messages belonging to <code>topic</code> at <code>queueId</code> starting
     * from given <code>offset</code>. Resulting messages will further be screened using provided message filter.
     *
     * @param group Consumer group that launches this query.
     * @param topic Topic to query.
     * @param queueId Queue ID to query.
     * @param offset Logical offset to start from.
     * @param maxMsgNums Maximum count of messages to query.
     * @param messageFilter Message filter used to screen desired messages.
     * @return Matched messages.
     */
<<<<<<< HEAD
=======
    /**
     * 读取消息，如果types为null，则不做过滤
     */
>>>>>>> rmq/master
    GetMessageResult getMessage(final String group, final String topic, final int queueId,
        final long offset, final int maxMsgNums, final MessageFilter messageFilter);

    /**
     * Get maximum offset of the topic queue.
     * @param topic Topic name.
     * @param queueId Queue ID.
     * @return Maximum offset at present.
     */
<<<<<<< HEAD
=======
    /**
     * 获取指定队列最大Offset 如果队列不存在，返回-1
     */
>>>>>>> rmq/master
    long getMaxOffsetInQueue(final String topic, final int queueId);

    /**
     * Get the minimum offset of the topic queue.
     * @param topic Topic name.
     * @param queueId Queue ID.
     * @return Minimum offset at present.
     */
<<<<<<< HEAD
=======
    /**
     * 获取指定队列最小Offset 如果队列不存在，返回-1
     */
>>>>>>> rmq/master
    long getMinOffsetInQueue(final String topic, final int queueId);

    /**
     * Get the offset of the message in the commit log, which is also known as physical offset.
     * @param topic Topic of the message to lookup.
     * @param queueId Queue ID.
     * @param consumeQueueOffset offset of consume queue.
     * @return physical offset.
     */
<<<<<<< HEAD
=======
    /**
     * 获取消费队列记录的CommitLog Offset
     */
>>>>>>> rmq/master
    long getCommitLogOffsetInQueue(final String topic, final int queueId, final long consumeQueueOffset);

    /**
     * Look up the physical offset of the message whose store timestamp is as specified.
     * @param topic Topic of the message.
     * @param queueId Queue ID.
     * @param timestamp Timestamp to look up.
     * @return physical offset which matches.
     */
<<<<<<< HEAD
=======
    /**
     * 根据消息时间获取某个队列中对应的offset 1、如果指定时间（包含之前之后）有对应的消息，则获取距离此时间最近的offset（优先选择之前）
     * 2、如果指定时间无对应消息，则返回0
     */
>>>>>>> rmq/master
    long getOffsetInQueueByTime(final String topic, final int queueId, final long timestamp);

    /**
     * Look up the message by given commit log offset.
     * @param commitLogOffset physical offset.
     * @return Message whose physical offset is as specified.
     */
<<<<<<< HEAD
=======
    /**
     * 通过物理队列Offset，查询消息。 如果发生错误，则返回null
     */
>>>>>>> rmq/master
    MessageExt lookMessageByOffset(final long commitLogOffset);

    /**
     * Get one message from the specified commit log offset.
     * @param commitLogOffset commit log offset.
     * @return wrapped result of the message.
     */
<<<<<<< HEAD
=======
    /**
     * 通过物理队列Offset，查询消息。 如果发生错误，则返回null
     */
>>>>>>> rmq/master
    SelectMappedBufferResult selectOneMessageByOffset(final long commitLogOffset);

    /**
     * Get one message from the specified commit log offset.
     * @param commitLogOffset commit log offset.
     * @param msgSize message size.
     * @return wrapped result of the message.
     */
    SelectMappedBufferResult selectOneMessageByOffset(final long commitLogOffset, final int msgSize);

    /**
     * Get the running information of this store.
     * @return message store running info.
     */
<<<<<<< HEAD
=======
    /**
     * 获取运行时统计数据
     */
>>>>>>> rmq/master
    String getRunningDataInfo();

    /**
     * Message store runtime information, which should generally contains various statistical information.
     * @return runtime information of the message store in format of key-value pairs.
     */
<<<<<<< HEAD
=======
    /**
     * 获取运行时统计数据
     */
>>>>>>> rmq/master
    HashMap<String, String> getRuntimeInfo();

    /**
     * Get the maximum commit log offset.
     * @return maximum commit log offset.
     */
<<<<<<< HEAD
=======
    /**
     * 获取物理队列最大offset
     */
>>>>>>> rmq/master
    long getMaxPhyOffset();

    /**
     * Get the minimum commit log offset.
     * @return minimum commit log offset.
     */
    long getMinPhyOffset();

    /**
     * Get the store time of the earliest message in the given queue.
     * @param topic Topic of the messages to query.
     * @param queueId Queue ID to find.
     * @return store time of the earliest message.
     */
<<<<<<< HEAD
=======
    /**
     * 获取队列中最早的消息时间
     */
>>>>>>> rmq/master
    long getEarliestMessageTime(final String topic, final int queueId);

    /**
     * Get the store time of the earliest message in this store.
     * @return timestamp of the earliest message in this store.
     */
    long getEarliestMessageTime();

    /**
     * Get the store time of the message specified.
     * @param topic message topic.
     * @param queueId queue ID.
     * @param consumeQueueOffset consume queue offset.
     * @return store timestamp of the message.
     */
    long getMessageStoreTimeStamp(final String topic, final int queueId, final long consumeQueueOffset);

    /**
     * Get the total number of the messages in the specified queue.
     * @param topic Topic
     * @param queueId Queue ID.
     * @return total number.
     */
<<<<<<< HEAD
=======
    /**
     * 获取队列中的消息总数
     */
>>>>>>> rmq/master
    long getMessageTotalInQueue(final String topic, final int queueId);

    /**
     * Get the raw commit log data starting from the given offset, which should used for replication purpose.
     * @param offset starting offset.
     * @return commit log data.
     */
<<<<<<< HEAD
=======
    /**
     * 数据复制使用：获取CommitLog数据
     */
>>>>>>> rmq/master
    SelectMappedBufferResult getCommitLogData(final long offset);

    /**
     * Append data to commit log.
     * @param startOffset starting offset.
     * @param data data to append.
     * @return true if success; false otherwise.
     */
<<<<<<< HEAD
=======
    /**
     * 数据复制使用：向CommitLog追加数据，并分发至各个Consume Queue
     */
>>>>>>> rmq/master
    boolean appendToCommitLog(final long startOffset, final byte[] data);

    /**
     * Execute file deletion manually.
     */
<<<<<<< HEAD
=======
    /**
     * 手动触发删除文件
     */
>>>>>>> rmq/master
    void executeDeleteFilesManually();

    /**
     * Query messages by given key.
     * @param topic topic of the message.
     * @param key message key.
     * @param maxNum maximum number of the messages possible.
     * @param begin begin timestamp.
     * @param end end timestamp.
     * @return
     */
<<<<<<< HEAD
=======
    /**
     * 根据消息Key查询消息
     */
>>>>>>> rmq/master
    QueryMessageResult queryMessage(final String topic, final String key, final int maxNum, final long begin,
        final long end);

    /**
     * Update HA master address.
     * @param newAddr new address.
     */
    void updateHaMasterAddress(final String newAddr);

    /**
     * Return how much the slave falls behind.
     * @return number of bytes that slave falls behind.
     */
<<<<<<< HEAD
=======
    /**
     * Slave落后Master多少，单位字节
     */
>>>>>>> rmq/master
    long slaveFallBehindMuch();

    /**
     * Return the current timestamp of the store.
     * @return current time in milliseconds since 1970-01-01.
     */
    long now();

    /**
     * Clean unused topics.
     * @param topics all valid topics.
     * @return number of the topics deleted.
     */
    int cleanUnusedTopic(final Set<String> topics);

    /**
     * Clean expired consume queues.
     */
<<<<<<< HEAD
=======
    /**
     * 清除失效的消费队列
     */
>>>>>>> rmq/master
    void cleanExpiredConsumerQueue();

    /**
     * Check if the given message has been swapped out of the memory.
     * @param topic topic.
     * @param queueId queue ID.
     * @param consumeOffset consume queue offset.
     * @return true if the message is no longer in memory; false otherwise.
     */
<<<<<<< HEAD
=======
    /**
     * 判断消息是否在磁盘
     */
>>>>>>> rmq/master
    boolean checkInDiskByConsumeOffset(final String topic, final int queueId, long consumeOffset);

    /**
     * Get number of the bytes that have been stored in commit log and not yet dispatched to consume queue.
     * @return number of the bytes to dispatch.
     */
    long dispatchBehindBytes();

    /**
     * Flush the message store to persist all data.
     * @return maximum offset flushed to persistent storage device.
     */
    long flush();

    /**
     * Reset written offset.
     * @param phyOffset new offset.
     * @return true if success; false otherwise.
     */
    boolean resetWriteOffset(long phyOffset);

    /**
     * Get confirm offset.
     * @return confirm offset.
     */
    long getConfirmOffset();

    /**
     * Set confirm offset.
     * @param phyOffset confirm offset to set.
     */
    void setConfirmOffset(long phyOffset);

    /**
     * Check if the operation system page cache is busy or not.
     * @return true if the OS page cache is busy; false otherwise.
     */
    boolean isOSPageCacheBusy();

    /**
     * Get lock time in milliseconds of the store by far.
     * @return lock time in milliseconds.
     */
    long lockTimeMills();

    /**
     * Check if the transient store pool is deficient.
     * @return true if the transient store pool is running out; false otherwise.
     */
    boolean isTransientStorePoolDeficient();

    /**
     * Get the dispatcher list.
     * @return list of the dispatcher.
     */
    LinkedList<CommitLogDispatcher> getDispatcherList();

    /**
     * Get consume queue of the topic/queue.
     * @param topic Topic.
     * @param queueId Queue ID.
     * @return Consume queue.
     */
    ConsumeQueue getConsumeQueue(String topic, int queueId);
}
