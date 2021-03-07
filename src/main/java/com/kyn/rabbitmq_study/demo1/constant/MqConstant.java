
package com.kyn.rabbitmq_study.demo1.constant;



/**
 * @author Kangyanan
 * @Description: 消息队列常量配置
 * @date 2021/3/2
 */
public class MqConstant {
    /** 订阅 cnu_provider 的Topic交换器*/
    public static final String CNU_PROVIDER_TOPIC_EXCHANGE = "CNU-PROVIDER-TOPIC-EXCHANGE";
    /** 订阅 cnu_provide 消息绑定的队列**/
    public final static String CNU_PROVIDER_FRONT_NOTIFY_QUEUE = "CNU-PROVIDER-FRONT-NOTIFY-QUEUE";
    /** cnu_provider 通知 cnu_front notifyKey*/
    public final static String CNU_PROVIDER_FRONT_NOTIFY_KEY = "provider.front.notify.key";
    /** cnu_front 重试队列**/
    public final static String CNU_FRONT_RETRY_QUEUE = "CNU-FRONT-RETRY-QUEUE";
    /** cnu_front 重试路由key**/
    public final static String CNU_FRONT_RETRY_ROUTING_KEY = "front.retry.key";
    /** cnu_front 的Topic交换器**/
    public static final String CNU_FRONT_TOPIC_EXCHANGE = "CNU-FRONT-TOPIC-EXCHANGE";
    /** cnu_front 的Fanout交换器**/
    public static final String  CNU_FRONT_TFANOUT_EXCHANGE = "CNU-FRONT-FANOUT-EXCHANGE";
    /** 默认重试次数**/
    public final static String CNU_FRONT_COMMON_MQ_RETRY_COUNT = "CNU-FRONT-COMMON-MQ-RETRY-COUNT";
    /** 延迟时间因子：10s。具体延迟时间计算方式：2^count*10s */
    public static final int CNU_FRONT_QUEUE_TRANS_MESSAGE_DELAY_MILLISECONDS = 10000;
    /** cnu_front 应用重试次数headers头部常量**/
    public final static String CNU_FRONT_MQ_RETRY_COUNT = "CNU-FRONT-MQ-RETRY-COUNT";
    /** 死信消息失效时间计算方式：指数方式 **/
    public static final String MESSAGE_EXPIRATION_TYPE_EXPONENTIAL = "MESSAGE_EXPIRATION_TYPE_EXPONENTIAL";
}
