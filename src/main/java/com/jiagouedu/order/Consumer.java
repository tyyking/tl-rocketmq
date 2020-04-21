package com.jiagouedu.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 顺序消息消费
 */
public class Consumer {

	public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_Consumer");
		consumer.setNamesrvAddr("192.168.0.31:9876");

		/**
		 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
		 * 如果非第一次启动，那么按照上次消费的位置继续消费
		 */
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

		consumer.subscribe("TopicOrderTest", "*");
		
		/**
		 * 实现了MessageListenerOrderly表示一个队列只会被一个线程取到	
		 *，第二个线程无法访问这个队列
		 */
		consumer.registerMessageListener(new MessageListenerOrderly() {
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				// 设置自动提交
				context.setAutoCommit(true);
				for (MessageExt msg : msgs) {
					System.out.println(msg + ",内容：" + new String(msg.getBody()));
				}

				try {
					TimeUnit.SECONDS.sleep(5L);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				;

				return ConsumeOrderlyStatus.SUCCESS;
			}
		});

		consumer.start();

		System.out.println("Consumer Started.");
	}

}