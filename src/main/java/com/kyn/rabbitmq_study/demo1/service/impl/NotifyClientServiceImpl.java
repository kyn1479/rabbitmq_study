package com.kyn.rabbitmq_study.demo1.service.impl;

import com.kyn.rabbitmq_study.demo1.service.NotifyClientService;
import com.kyn.rabbitmq_study.demo1.utils.HttpUtil;
import com.kyn.rabbitmq_study.demo1.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Kangyanan
 * @Description: 异步通知发送请求类实现类
 * @date 2021/3/2
 */
@Service
public class NotifyClientServiceImpl implements NotifyClientService {
    private Logger logger = LoggerFactory.getLogger(NotifyClientServiceImpl.class);


    @Override
    public boolean post(String reqParamsStr, String url) {
        try {
            logger.info("cnu_front 异步通知,通知外部系统 reqParamsStr={},通知地址={}",reqParamsStr,url);
            String resp = HttpUtil.postJsonBody(url, reqParamsStr, null, HttpUtil.DEFAULT_ENCODING);
            if(StringUtils.isNotBlank(resp) && "SUCCESS".equals(resp)) {
                return true;
            }else {
                logger.info("cnu_front前置 异步通知业务方返回:{}",resp);
            }
        } catch (Exception ex) {
            logger.error("cnu_front前置 异步通知,通知外部系统异常 {}",ex);
        }
        return false;
    }
}
