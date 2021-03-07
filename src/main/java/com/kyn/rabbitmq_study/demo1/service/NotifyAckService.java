package com.kyn.rabbitmq_study.demo1.service;


import com.kyn.rabbitmq_study.demo1.service.model.PayNotifyContent;

/**
 * @author Kangyanan
 * @Description: NotifyAckService.java
 * @date 2021/3/2
 */
public interface NotifyAckService {

    boolean handleAck(PayNotifyContent payNotifyContent);
}
