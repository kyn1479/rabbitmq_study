package com.kyn.rabbitmq_study.demo1.service.mq;

/**
 * @author Kangyanan
 * @Description: rabbitMq服务接口
 * @date 2021/3/2
 */
public interface RabbitMqService {

    /**
     * 发送信息 TOPIC_EXCHANGE
     * @param routingKey
     * @param obj
     */
    void send(String routingKey, Object obj);

    /**
     * 发送信息 TFANOUT_EXCHANGE
     * @param routingKey
     * @param obj
     */
    void sendFanOut(String routingKey, Object obj);

    /**
     * 发送消息到死信队列重试
     * @param exchange 消息交换机名称
     * @param routingKey 路由key
     * @param obj 真实的mq消息
     * @param expiration 消息失效时间（当消息在死信队列中失效时，会进入设置的处理队列）
     * @param retryCount 重试次数
     */
    void sendRetry(String exchange, String routingKey, Object obj, String expiration, int retryCount);

    /**
     * 发送消息到死信队列重试
     * @param exchange 消息交换机名称
     * @param routingKey 路由key
     * @param obj 真实的mq消息
     * @param expiration 消息失效时间（当消息在死信队列中失效时，会进入设置的处理队列）
     * @param retryCount 重试次数
     */
    void sendRetry(String exchange, String routingKey, Object obj, String expiration,String retryKey, int retryCount);

}