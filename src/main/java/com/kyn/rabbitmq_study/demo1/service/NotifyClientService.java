package com.kyn.rabbitmq_study.demo1.service;

/**
 * @author Kangyanan
 * @Description: 异步通知发送请求类
 * @date 2021/3/2
 */
public interface NotifyClientService {
    /**
     * 异步通知发送请求类
     * @param reqParamsStr 异步通知请求实体类
     * @param url 异步通知请求地址
     * @return 是否通知成功
     */
    boolean post(String url, String reqParamsStr);

}
