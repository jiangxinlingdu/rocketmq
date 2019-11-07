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
package org.apache.rocketmq.store.config;

import java.io.File;

public class StorePathConfigHelper {

    public static String getStorePathConsumeQueue(final String rootDir) {
        return rootDir + File.separator + "consumequeue";
    }

    public static String getStorePathConsumeQueueExt(final String rootDir) {
        return rootDir + File.separator + "consumequeue_ext";
    }

    public static String getStorePathIndex(final String rootDir) {
        return rootDir + File.separator + "index";
    }

    public static String getStoreCheckpoint(final String rootDir) {
<<<<<<< HEAD
        return rootDir + File.separator + "checkpoint";
    }

    public static String getAbortFile(final String rootDir) {
        return rootDir + File.separator + "abort";
=======
        return rootDir + File.separator + "checkpoint"; //checkpoint作用是当异常恢复时需要根据checkpoint点来恢复消息
    }

    public static String getAbortFile(final String rootDir) {
        return rootDir + File.separator + "abort"; // 当存在abort文件，系统认为是异常恢复
>>>>>>> rmq/master
    }

    public static String getDelayOffsetStorePath(final String rootDir) {
        return rootDir + File.separator + "config" + File.separator + "delayOffset.json";
    }

    public static String getTranStateTableStorePath(final String rootDir) {
        return rootDir + File.separator + "transaction" + File.separator + "statetable";
    }

    public static String getTranRedoLogStorePath(final String rootDir) {
<<<<<<< HEAD
        return rootDir + File.separator + "transaction" + File.separator + "redolog";
=======
        return rootDir + File.separator + "transaction" + File.separator + "redolog"; //oracle mysql都存在该概念
>>>>>>> rmq/master
    }

}
