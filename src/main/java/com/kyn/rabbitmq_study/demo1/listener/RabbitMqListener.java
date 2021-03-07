package com.kyn.rabbitmq_study.demo1.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kyn.rabbitmq_study.demo1.constant.MqConstant;
import com.kyn.rabbitmq_study.demo1.listener.handler.NotifyAckHandler;
import com.kyn.rabbitmq_study.demo1.service.model.PayNotifyContent;
import com.kyn.rabbitmq_study.demo1.service.mq.CnuFrontRetryQueueManager;
import com.kyn.rabbitmq_study.demo1.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kangyanan
 * @Description: 队列监听类
 * @date 2021/3/2
 */
@Component
public class RabbitMqListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);
    /** 最大重试次数 */
    private final static int MAX_RETRY_COUNT = 5;

    /** 重试队列Manager */
    @Autowired
    private CnuFrontRetryQueueManager cnuFrontRetryQueueManager;

    /** 异步通知AckHandler */
    @Autowired
    private NotifyAckHandler notifyAckHandler;

    /**
     * MQ消息监听
     */
    @RabbitListener(queues = MqConstant.CNU_PROVIDER_FRONT_NOTIFY_QUEUE, containerFactory = RabbitListenerAnnotationBeanPostProcessor.DEFAULT_RABBIT_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    public void paycoreAckListener(Channel channel, Message messageSource) {

        /**获取重试次数 初始值0*/
        int count = cnuFrontRetryQueueManager.getMessageRetryCount(messageSource);
        logger.info("消息重试次数:{}", count);

        /** 1.入参校验 校验已经通知次数，通知内容是否为空,其他校验待补充*/
        String jsonString =null;
        PayNotifyContent payNotifyContent=null;
        try {
            SimpleMessageConverter messageConverter=new SimpleMessageConverter();
            jsonString = (String) messageConverter.fromMessage(messageSource);
            payNotifyContent = JSON.parseObject(jsonString, new TypeReference<PayNotifyContent>() {
            }.getType());
            if (count > MAX_RETRY_COUNT || StringUtils.isBlank(jsonString)||payNotifyContent==null) {
                //如果报文关键信息为空，则直接从队列中ack删除
                channel.basicAck(messageSource.getMessageProperties().getDeliveryTag(), false);
                logger.info("cnu_front 支付MQ通知,通知入参不满足条件,退出后续处理,已经通知次数count：{}",count);
                return;
            }
        } catch (Exception e) {
            //解析对象出错，打印错误日志；TODO 待确认是否直接ACK还是直接返回不发送Nack让MQ发送到其他机器
            logger.error("返回报文解析或basicAck异常", e);
            //直接返回
            return;
        }
        logger.info("cnu_fornt支付MQ通知,通知实体 payNotifyContent={}",JSON.toJSONString(payNotifyContent));

         /** 2.幂等控制*/
         //TODO 结果类通知重复通知 不重要后续添加

        /** 3.业务处理*/
        boolean isSuccess = false;

        //3.1 处理报文，如果处理器抛出异常，MQ自动重试
        isSuccess=  notifyAckHandler.handle(payNotifyContent);

        try {
            //1. 如果失败，发布延迟消息
            if (!isSuccess) {
                cnuFrontRetryQueueManager.sendRetryMessage(messageSource,
                        cnuFrontRetryQueueManager.getRetryMessageExpiration(MqConstant.MESSAGE_EXPIRATION_TYPE_EXPONENTIAL,count),
                        count);
            }

            // 2. ack消费掉此消息 注意：如果发生延迟消息成功，ack消息失败，则消息存在于两个队列中，handleAck必须考虑业务幂等
            channel.basicAck(messageSource.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("消息发送或basicAck异常", e);
        }
    }



}
