package com.kyn.rabbitmq_study.demo1.service.mq;

import com.kyn.rabbitmq_study.demo1.constant.MqConstant;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CnuFrontRetryQueueManager extends RetryQueueManager{
    private final static Logger logger = LoggerFactory.getLogger(CnuFrontRetryQueueManager.class);

    @Autowired
    private AmqpAdmin rabbitAdmin;

    /**
     * 指数型重试队列列表
     */
    private List<Queue> exponentialRetryQueues = new ArrayList<>();

    /**
     * 最大重试次数
     * **/
    private final static int MAX_RETRY_COUNT = 10;

    /**
     * 生成指数型重试队列
     */
    @Override
    public void generateExponentialRetryQueues(){
        logger.info("申明“指数型”重试队列开始...");
        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            String queueName = null;
            try {
                Map<String, Object> args = new HashMap<String, Object>();
                //指定当成为死信时，重定向到 CNU_PROVIDER_TOPIC_EXCHANGE
                args.put("x-dead-letter-exchange", MqConstant.CNU_PROVIDER_TOPIC_EXCHANGE);
                args.put("x-dead-letter-routing-key", MqConstant.CNU_PROVIDER_FRONT_NOTIFY_KEY);

                String expiration = String.valueOf(Double.valueOf(Math.pow(2, i)).intValue()*MqConstant.CNU_FRONT_QUEUE_TRANS_MESSAGE_DELAY_MILLISECONDS);
                queueName = MqConstant.CNU_FRONT_RETRY_QUEUE + "." + expiration;

                //声明重试队列，将参数带入
                Queue queue = QueueBuilder.durable(queueName).withArguments(args).build();
                rabbitAdmin.declareQueue(queue);
                logger.info("申明“指数型”重试队列成功[queueName:{}]", queueName);

                exponentialRetryQueues.add(queue);
            }catch (Throwable e){
                logger.error("申明“指数型”重试队列失败[i:{}, queueName:{}, e.message：{}]，异常:", i, queueName, e.getMessage(), e);
            }
        }
        logger.info("申明“指数型”重试队列结束...");
    }

    /**
     * 根据消息超时时间取得重试队列名称
     * @param expiration
     * @return
     */
    @Override
    public String getRetryQueueName(String expiration){
        String queueName = null;
        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            int queueDelayMilliseconds = Double.valueOf(Math.pow(2, i)).intValue()*MqConstant.CNU_FRONT_QUEUE_TRANS_MESSAGE_DELAY_MILLISECONDS;

            int expirationIntValue = Integer.valueOf(expiration);
            queueName = MqConstant.CNU_FRONT_RETRY_QUEUE + "." + String.valueOf(queueDelayMilliseconds);
            if(expirationIntValue <= queueDelayMilliseconds){
                break;
            }
        }
        return queueName;
    }

    /**
     * 获取指数重试队列列表
     * @return
     */
    @Override
    public List<Queue> getExponentialRetryQueues() {
        return exponentialRetryQueues;
    }

    /**
     * 获取消息的重试次数
     * @param message
     * @return
     */
    public int getMessageRetryCount(Message message){
        //初始为0
        int count = 0;

        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        if(headers.containsKey(MqConstant.CNU_FRONT_MQ_RETRY_COUNT)){
            count = NumberUtils.toInt((String) message.getMessageProperties().getHeaders().get(MqConstant.CNU_FRONT_MQ_RETRY_COUNT), 0);
        }
        return count;
    }

}
