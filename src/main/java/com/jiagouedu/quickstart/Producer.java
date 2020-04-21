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
package com.jiagouedu.quickstart;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 普通发送消息类
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        /*
         *  创建生产者
         */
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_namea");
        //配置Producer
        producer.setNamesrvAddr("localhost:9876");
        //启动Producer
        producer.start();
        for (int i = 0; i < 10; i++) {
            try {
                /*
                 * 组装message
                 *  message=topic+tag+body
                 */
                String body="wukong"+i;
                Message msg = new Message("wukong-quickstart" /* Topic */,
                    "TagA" /* Tag */,
                  body.getBytes()); /* Message body */
                /*
                 * 发送数据，并且返回结果
                 */
                SendResult sendResult = producer.send(msg);
                //producer.sendOneway();Oneway：比较简单，发出去后，什么都不管直接返回。
               // producer.send();
                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }
       // producer.shutdown();
    }
}
