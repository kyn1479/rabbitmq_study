package com.kyn.rabbitmq_study.demo1.service.impl;

import com.alibaba.fastjson.JSON;
import com.kyn.rabbitmq_study.demo1.enums.BizStatus;
import com.kyn.rabbitmq_study.demo1.listener.RabbitMqListener;
import com.kyn.rabbitmq_study.demo1.service.NotifyAckService;
import com.kyn.rabbitmq_study.demo1.service.NotifyClientService;
import com.kyn.rabbitmq_study.demo1.service.model.PayNotifyContent;
import com.kyn.rabbitmq_study.demo1.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Kangyanan
 * @Description:
 * @date 2021/3/2
 */
@Component
public class NotifyAckServiceImpl implements NotifyAckService {
    private static final Logger logger = LoggerFactory.getLogger(NotifyAckServiceImpl.class);
    @Autowired
    NotifyClientService notifyClientService;

    /**
     * 消费Cnu_Provider的MQ消息，将成功或失败结果通知外部系统
     * @param payNotifyContent
     * @return true 通知 Cnu_Provider 的MQ重新发送
     */
    @Override
    public boolean handleAck(PayNotifyContent payNotifyContent) {
        if (payNotifyContent == null) {
            return true;
        }
        //判断交易状态是否为终态
        if (!StringUtils.equals(BizStatus.SUCCESS.getCode(), payNotifyContent.getBizStatus()) && !StringUtils.equals(BizStatus.FAILED.getCode(), payNotifyContent.getBizStatus())) {
            logger.warn("cnu_front前置 异步通知 Req_id={} cnu_provider状态为不为终态, 返回true", payNotifyContent.getReq_id());
            return true;
        }
        // TODO 返回外部系统参数到 先进行签名
        String reqParamsStr = this.sign(payNotifyContent, payNotifyContent.getMer_no());
        boolean isSuccess = notifyClientService.post(reqParamsStr, payNotifyContent.getNotifyUrl());

        return isSuccess;
    }

    /**
     * 通知外部系统请求参数到 paymanager签名
     * @param payNotifyContent
     * @return
     */
    private String sign(PayNotifyContent payNotifyContent, String mchNo) {

        Map<String, String> params = this.signReqParam(payNotifyContent);

        String source = this.buildSignSource(params);

//        String signedSource = payManagerClientService.doSign(source, mchNo);

//        params.put("sign", signedSource);

        return JSON.toJSONString(params);
    }
    /**
     * 组装请求参数到paymanager签名
     * @param payNotifyContent
     * @return
     */
    private Map<String, String> signReqParam(PayNotifyContent payNotifyContent) {
        Map<String, String> params = new HashMap<>();
        params.put("bizStatus", payNotifyContent.getBizStatus());
        params.put("respCode", payNotifyContent.getRespCode());
        params.put("respMsg", payNotifyContent.getRespMsg());
        if (null != payNotifyContent.getSuccessAmt()) {
            params.put("successAmt", payNotifyContent.getSuccessAmt().toString());
        }
        params.put("requestId", payNotifyContent.getReq_id());
        return params;
    }

    /**
     * 对签名Map排序
     * @param params
     * @return
     */
    private String buildSignSource(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        String signKey = "sign";
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key.equalsIgnoreCase(signKey)) {
                continue;
            }
            if (params.get(key) != null) {
                sb.append(key).append("=")
                        .append(String.valueOf(params.get(key)))
                        .append("&");
            }
        }

        return sb.substring(0, sb.length() - 1);
    }
}
