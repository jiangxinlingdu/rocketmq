package org.apache.rocketmq.common;

import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.List;

public class AllocateMessageQueueConsistentHashTest {
    public static void main(String[] args) {


        //public List<MessageQueue> allocate(String consumerGroup, String currentCID, List<MessageQueue> mqAll,
        //        ) {


        List<String> cidAll = new ArrayList<String>();

        cidAll.add("172.0.0.1@102");
        cidAll.add("172.0.0.2@102");
        cidAll.add("172.0.0.3@102");
        cidAll.add("172.0.0.4@102");
        //cidAll.add("172.0.0.5@102");

        List<MessageQueue> mqAll = new ArrayList<MessageQueue>();

        int i1 = 3;
        for (int i = 0; i < i1; i++) {
            //String topic, String brokerName, int queueId
            mqAll.add(new MessageQueue("test","brokera",i));
        }

        for (int i = 0; i < i1; i++) {
            //String topic, String brokerName, int queueId
            mqAll.add(new MessageQueue("test","brokerb",i));
        }


        for (int i = 0; i < i1; i++) {
            //String topic, String brokerName, int queueId
            mqAll.add(new MessageQueue("test","brokerc",i));
        }



        AllocateMessageQueueConsistentHash allocateMessageQueueConsistentHash = new AllocateMessageQueueConsistentHash();


        for (String currentCID : cidAll) {
            List<MessageQueue>list  = allocateMessageQueueConsistentHash.allocate("test",currentCID,mqAll,cidAll);
            System.out.println("allocateMessageQueueConsistentHash currentCID:"+currentCID);
            for (MessageQueue messageQueue : list) {
                System.out.println(messageQueue);
            }
        }


        System.out.println("==========================");


        AllocateMessageQueueAveragely allocateMessageQueueAveragely = new AllocateMessageQueueAveragely();


        for (String currentCID : cidAll) {
            List<MessageQueue>list  = allocateMessageQueueAveragely.allocate("test",currentCID,mqAll,cidAll);
            System.out.println("allocateMessageQueueAveragely currentCID:" + currentCID);
            for (MessageQueue messageQueue : list) {
                System.out.println(messageQueue);
            }
        }


        /**
         * allocateMessageQueueConsistentHash currentCID:172.0.0.1@102
         * MessageQueue [topic=test, brokerName=brokerb, queueId=0]
         * allocateMessageQueueConsistentHash currentCID:172.0.0.2@102
         * MessageQueue [topic=test, brokerName=brokerb, queueId=1]
         * MessageQueue [topic=test, brokerName=brokerb, queueId=2]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=1]
         * allocateMessageQueueConsistentHash currentCID:172.0.0.3@102
         * MessageQueue [topic=test, brokerName=brokera, queueId=0]
         * MessageQueue [topic=test, brokerName=brokera, queueId=2]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=2]
         * allocateMessageQueueConsistentHash currentCID:172.0.0.4@102
         * MessageQueue [topic=test, brokerName=brokera, queueId=1]
         * allocateMessageQueueConsistentHash currentCID:172.0.0.5@102
         * MessageQueue [topic=test, brokerName=brokerc, queueId=0]
         * ==========================
         * allocateMessageQueueAveragely currentCID:172.0.0.1@102
         * MessageQueue [topic=test, brokerName=brokera, queueId=0]
         * MessageQueue [topic=test, brokerName=brokera, queueId=1]
         * allocateMessageQueueAveragely currentCID:172.0.0.2@102
         * MessageQueue [topic=test, brokerName=brokera, queueId=2]
         * MessageQueue [topic=test, brokerName=brokerb, queueId=0]
         * allocateMessageQueueAveragely currentCID:172.0.0.3@102
         * MessageQueue [topic=test, brokerName=brokerb, queueId=1]
         * MessageQueue [topic=test, brokerName=brokerb, queueId=2]
         * allocateMessageQueueAveragely currentCID:172.0.0.4@102
         * MessageQueue [topic=test, brokerName=brokerc, queueId=0]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=1]
         * allocateMessageQueueAveragely currentCID:172.0.0.5@102
         * MessageQueue [topic=test, brokerName=brokerc, queueId=2]
         *
         */


        /**
         *
         * allocateMessageQueueConsistentHash currentCID:172.0.0.1@102
         * MessageQueue [topic=test, brokerName=brokerb, queueId=0]
         * allocateMessageQueueConsistentHash currentCID:172.0.0.2@102
         * MessageQueue [topic=test, brokerName=brokerb, queueId=1]
         * MessageQueue [topic=test, brokerName=brokerb, queueId=2]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=1]
         * allocateMessageQueueConsistentHash currentCID:172.0.0.3@102
         * MessageQueue [topic=test, brokerName=brokera, queueId=0]
         * MessageQueue [topic=test, brokerName=brokera, queueId=2]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=2]
         * allocateMessageQueueConsistentHash currentCID:172.0.0.4@102
         * MessageQueue [topic=test, brokerName=brokera, queueId=1]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=0]
         * ==========================
         * allocateMessageQueueAveragely currentCID:172.0.0.1@102
         * MessageQueue [topic=test, brokerName=brokera, queueId=0]
         * MessageQueue [topic=test, brokerName=brokera, queueId=1]
         * MessageQueue [topic=test, brokerName=brokera, queueId=2]
         * allocateMessageQueueAveragely currentCID:172.0.0.2@102
         * MessageQueue [topic=test, brokerName=brokerb, queueId=0]
         * MessageQueue [topic=test, brokerName=brokerb, queueId=1]
         * allocateMessageQueueAveragely currentCID:172.0.0.3@102
         * MessageQueue [topic=test, brokerName=brokerb, queueId=2]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=0]
         * allocateMessageQueueAveragely currentCID:172.0.0.4@102
         * MessageQueue [topic=test, brokerName=brokerc, queueId=1]
         * MessageQueue [topic=test, brokerName=brokerc, queueId=2]
         */

    }
}