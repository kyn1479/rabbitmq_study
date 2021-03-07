package com.kyn.rabbitmq_study.demo1.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Kangyanan
 * @Description: 支付类异步通知参数
 * @date 2021/3/2
 */
public class PayNotifyContent implements Serializable {

    /** 请求流水 */
    private String req_id;

    private String notifyUrl;

    /** 商户号 **/
    private String mer_no;

    /** 支付成功金额 **/
    private BigDecimal successAmt ;

    /** 业务处理状态 **/
    private String bizStatus;

    /** 响应码 **/
    private String respCode;

    /** 响应信息 **/
    private String respMsg;


    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getMer_no() {
        return mer_no;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public BigDecimal getSuccessAmt() {
        return successAmt;
    }

    public void setSuccessAmt(BigDecimal successAmt) {
        this.successAmt = successAmt;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
