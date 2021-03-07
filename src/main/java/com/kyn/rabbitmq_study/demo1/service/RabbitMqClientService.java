package com.kyn.rabbitmq_study.demo1.service;


import com.kyn.rabbitmq_study.demo1.exception.CnuFrontException;
import org.springframework.amqp.core.Message;

/**
 * @author Kangyanan
 * @Description: MQ发送客户端服务
 * @date 2021/3/2
 */
public interface RabbitMqClientService {


    /**
     * 发关到业务端的消息通过字节数组
     *
     * @param routingKey MQ的路由键
     * @param bytes      消息内容
     * @throws CnuFrontException
     */
    void notifyToBizByBytes(String routingKey, byte[] bytes) throws CnuFrontException;


    /**
     * 发关到业务端的消息
     *
     * @param routingKey MQ的路由键
     * @param content    消息内容
     * @throws CnuFrontException
     */
    public void notifyToBiz(String routingKey, String content) throws CnuFrontException;

    /**
     * 发送TTL有效消息
     * 实现延迟消息目的
     *
     * @param exchange
     * @param routingKey
     * @param messageSource
     * @param expiration
     * @return
     */
    public void sendExpMessage(String exchange, String routingKey, final Message messageSource, final int expiration) throws CnuFrontException;


    public void sendExpMessage(String exchange, String routingKey, final Object messageSource) throws CnuFrontException;

    public void sendExpMessageJson(String exchange, String routingKey, final String jsonString) throws CnuFrontException;


    /**
     * 发送消息到死信队列重试
     * @param exchange 消息交换机名称
     * @param routingKey 路由key
     * @param obj 真实的mq消息
     * @param expiration 消息失效时间（当消息在死信队列中失效时，会进入设置的处理队列）
     * @param retryCountKey 重试次数键值
     * @param retryCount 重试次数
     */
    void sendRetry(String exchange, String routingKey, Object obj, String expiration, String retryCountKey, int retryCount);

}
