package com.kyn.rabbitmq_study.demo1.service.mq;

import com.kyn.rabbitmq_study.demo1.constant.MqConstant;
import com.kyn.rabbitmq_study.demo1.utils.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author Kangyanan
 * @Description: 重试队列工具
 * @date 2021/3/2
 */
public abstract class RetryQueueManager implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(RetryQueueManager.class);

    /**
     * rabbitMq服务接口
     */
    @Autowired
    protected RabbitMqService rabbitMqService;

    /**
     * 生成指数型重试队列
     */
    public abstract void generateExponentialRetryQueues();

    /**
     * 根据消息超时时间取得重试队列名称
     * @param expiration
     * @return
     */
    public abstract String getRetryQueueName(String expiration);

    /**
     * 获取指数重试队列列表
     * @return
     */
    public abstract List<Queue> getExponentialRetryQueues();

    /**
     * 根据消息有效期（expiration）的不同，将重试消息放入不同的重试（死信）队列，以避免队首消息超时时间太长挡住后面的消息出队
     */
    public void sendRetryMessage(Object obj, String expiration, int retryCount) {
        logger.info("消息重发开始[expiration:{}, retryCount:{}]", expiration, retryCount);

        String queueName = getRetryQueueName(expiration);
        logger.info("消息重发获取重试队列[expiration:{}, retryCount:{}, queueName:{}]", expiration, retryCount, queueName);

        rabbitMqService.sendRetry("", queueName, obj, expiration, retryCount);
        logger.info("消息重发结束[expiration:{}, retryCount:{}]", expiration, retryCount);
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

        if(headers.containsKey(MqConstant.CNU_FRONT_COMMON_MQ_RETRY_COUNT)){
            count = NumberUtils.toInt((String) message.getMessageProperties().getHeaders().get(MqConstant.CNU_FRONT_COMMON_MQ_RETRY_COUNT), 0);
        }
        return count;
    }

    /**
     * 根据条件计算重试消息的“超时时间” 超时类型：指数型
     * @param expirationType
     * @param messageRetryCount
     * @return
     */
    public String getRetryMessageExpiration(String expirationType,int messageRetryCount) {
        // 死信消息失效时间计算方式：指数方式
        if(StringUtils.equals(MqConstant.MESSAGE_EXPIRATION_TYPE_EXPONENTIAL, expirationType)){
            // 计算公式：2^n*基数  其中：n为重试次数  基数为10秒
            return String.valueOf(Double.valueOf(Math.pow(2, messageRetryCount)).intValue()*MqConstant.CNU_FRONT_QUEUE_TRANS_MESSAGE_DELAY_MILLISECONDS);
        }
        // 死信消息失效时间计算方式错误，则默认返回“1”
        return "1";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 生成指数型重试队列
        generateExponentialRetryQueues();
    }

}