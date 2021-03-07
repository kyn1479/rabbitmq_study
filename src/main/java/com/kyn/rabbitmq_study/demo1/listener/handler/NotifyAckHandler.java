package com.kyn.rabbitmq_study.demo1.listener.handler;

import com.kyn.rabbitmq_study.demo1.service.NotifyAckService;
import com.kyn.rabbitmq_study.demo1.service.model.PayNotifyContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotifyAckHandler {

@Autowired
private NotifyAckService notifyAckService;


    public boolean handle(PayNotifyContent payNotifyContent){
      return notifyAckService.handleAck(payNotifyContent);
    }
}
