package com.kyn.rabbitmq_study.demo1.config;

import com.kyn.rabbitmq_study.demo1.constant.MqConstant;
import com.kyn.rabbitmq_study.demo1.enums.SystemErrorCode;
import com.kyn.rabbitmq_study.demo1.utils.AssertUtils;
import com.kyn.rabbitmq_study.demo1.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kangyanan
 * @Description: RabbitMq 配置类
 * @date 2021/3/2
 */
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
@EnableRabbit
public class MqConfigration {
    /** RabbitMq配置类 */
    @Autowired
    private RabbitProperties rabbitProperties;

    /** 最大同时消费数量*/
    @Value("${spring.rabbitmq.maxConcurrentConsumers}")
    private int maxConcurrentConsumers;

    /** 当前消费数量*/
    @Value("${spring.rabbitmq.concurrentConsumers}")
    private int concurrentConsumers;

    @Bean
    public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * 配置messageConverter使用默认的Converter
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }

    /**
     * 配置连接工厂
     * @return
     * @throws Exception
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        AssertUtils.isNotBlank(rabbitProperties.getHost(), SystemErrorCode.SYSTEM_ERROR);
        AssertUtils.isNotBlank(rabbitProperties.getUsername(), SystemErrorCode.SYSTEM_ERROR);
        AssertUtils.isNotBlank(rabbitProperties.getPassword(), SystemErrorCode.SYSTEM_ERROR);
        AssertUtils.isNotBlank(rabbitProperties.getVirtualHost(), SystemErrorCode.SYSTEM_ERROR);
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        //兼容单机和集群配置
        if (StringUtils.isBlank(rabbitProperties.getAddresses())){
            connectionFactory.setHost(rabbitProperties.getHost());
            connectionFactory.setPort(rabbitProperties.getPort());
        }else{
            connectionFactory.setAddresses(rabbitProperties.getAddresses());
        }
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        connectionFactory.setPublisherConfirms(true); //必须要设置，才能进行消息的回调
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean("smallConcurrentContainerFactory")
    public SimpleRabbitListenerContainerFactory smallConcurrentContainerFactory(ConnectionFactory connectionFactory,
                                                                                MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setMaxConcurrentConsumers(2);
        factory.setConcurrentConsumers(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    /**
     * rabbitmq消息模板
     * @param connectionFactory
     * @param messageConverter
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    /***
     * 配置cnu_provider模块的topic类型exchange
     * */
    @Bean
    public TopicExchange cnuProviderTopicExchange(AmqpAdmin rabbitAdmin) {
        TopicExchange topicExchange = new TopicExchange(MqConstant.CNU_PROVIDER_TOPIC_EXCHANGE, true, false);
        rabbitAdmin.declareExchange(topicExchange);
        return topicExchange;
    }

    /**
     *配置订阅到cnu_provide消息Queue
     */
    @Bean
    public Queue cnuProviderAckQueue(AmqpAdmin rabbitAdmin) {
        Queue queue = new Queue(MqConstant.CNU_PROVIDER_FRONT_NOTIFY_QUEUE, true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    /**
     *将Queue根据路由键值绑定到Exchange
     */
    @Bean
    public Binding bindingPayCoreAckQueue(Queue cnuProviderAckQueue, TopicExchange cnuProviderTopicExchange, AmqpAdmin rabbitAdmin) {
        Binding binding = BindingBuilder.bind(cnuProviderAckQueue).to(cnuProviderTopicExchange).with(MqConstant.CNU_PROVIDER_FRONT_NOTIFY_KEY);
        rabbitAdmin.declareBinding(binding);
        return binding;
    }

    /**
     *声明重试队列cnuFrontRetryQueue
     * @param rabbitAdmin
     * @return
     */
    @Bean
    public Queue cnuFrontRetryQueue(AmqpAdmin rabbitAdmin) {
        Map<String, Object> args = new HashMap<String, Object>();
        //important 指定当成为死信时，重定向到 CNU_PROVIDER_TOPIC_EXCHANGE
        args.put("x-dead-letter-exchange", MqConstant.CNU_PROVIDER_TOPIC_EXCHANGE);
        args.put("x-dead-letter-routing-key", MqConstant.CNU_PROVIDER_FRONT_NOTIFY_KEY);
        //声明重试队列，将参数带入
        Queue queue = new Queue(MqConstant.CNU_FRONT_RETRY_QUEUE, false, false, false, args);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    /**
     * 声明cnuFrontRetryQueue 绑定 cnuProviderTopicExchange 交换机的 CNU_FRONT_RETRY_ROUTING_KEY 路由
     */
    @Bean
    public Binding bindingCnuFrontRetryQueue(Queue cnuFrontRetryQueue, TopicExchange cnuProviderTopicExchange, AmqpAdmin rabbitAdmin) {
        Binding binding = BindingBuilder.bind(cnuFrontRetryQueue).to(cnuProviderTopicExchange).with(MqConstant.CNU_FRONT_RETRY_ROUTING_KEY);
        rabbitAdmin.declareBinding(binding);
        return binding;
    }

}
