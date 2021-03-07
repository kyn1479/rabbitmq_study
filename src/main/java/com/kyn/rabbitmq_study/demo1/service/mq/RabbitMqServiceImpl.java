package com.kyn.rabbitmq_study.demo1.service.mq;

import com.kyn.rabbitmq_study.demo1.constant.MqConstant;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Kangyanan
 * @Description: rabbitMq服务实现类
 * @date 2021/3/2
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(String routingKey, Object obj) {
        this.rabbitTemplate.convertAndSend(MqConstant.CNU_FRONT_TOPIC_EXCHANGE, routingKey, obj);
    }

    @Override
    public void sendFanOut(String routingKey, Object obj) {
        this.rabbitTemplate.convertAndSend(MqConstant.CNU_FRONT_TFANOUT_EXCHANGE, routingKey, obj);
    }

    @Override
    public void sendRetry(String exchange, String routingKey, Object obj, String expiration, int retryCount) {
        this.rabbitTemplate.convertAndSend(exchange, routingKey, obj, messageSource -> {
            //设置发送模式为持久化
            messageSource.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            //设置失效时间TTL
            messageSource.getMessageProperties().setExpiration(expiration);
            //设置重试次数
            messageSource.getMessageProperties().setHeader(MqConstant.CNU_FRONT_COMMON_MQ_RETRY_COUNT, String.valueOf(retryCount+1));

            return messageSource;
        });
    }
    @Override
    public void sendRetry(String exchange, String routingKey, Object obj, String expiration,String retryKey, int retryCount) {
        this.rabbitTemplate.convertAndSend(exchange, routingKey, obj, messageSource -> {
            //设置发送模式为持久化
            messageSource.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            //设置失效时间TTL
            messageSource.getMessageProperties().setExpiration(expiration);
            //设置重试次数
            messageSource.getMessageProperties().setHeader(retryKey, String.valueOf(retryCount+1));

            return messageSource;
        });
    }
}