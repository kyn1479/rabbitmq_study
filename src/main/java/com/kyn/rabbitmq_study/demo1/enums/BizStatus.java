package com.kyn.rabbitmq_study.demo1.enums;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Kangyanan
 * @Description: 业务状态枚举
 * @date 2021/2/28
 */
public enum BizStatus {

    /** 初始化*/
    INIT("INIT", "成功"),

    /** 成功*/
    SUCCESS("SUCCESS", "成功"),

    /** 处理中*/
    DOING("DOING", "处理中"),

    /** 失败*/
    FAILED("FAILED", "失败"),

    /** 退票 */
    REFUND("REFUND", "退票"),

    /** 已解约*/
    UNSIGNED("UNSIGNED", "已解约"),

    ;

    /** 枚举值 */
    private String code;

    /** 枚举描述 */
    private String message;

    /**
     * 构造一个<code>BizStatus</code>枚举对象
     *
     * @param code
     * @param message
     */
    private BizStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return Returns the code.
     */
    public String code() {
        return code;
    }

    /**
     * @return Returns the message.
     */
    public String message() {
        return message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return BizStatus
     */
    public static BizStatus getByCode(String code) {
        for (BizStatus _enum : values()) {
            if (_enum.getCode().equals(code)) {
                return _enum;
            }
        }
        return null;
    }



    /**
     * 获取全部枚举
     *
     * @return List<BizStatus>
     */
    public List<BizStatus> getAllEnum() {
        List<BizStatus> list = new ArrayList<BizStatus>();
        for (BizStatus _enum : values()) {
            list.add(_enum);
        }
        return list;
    }

    /**
     * 获取全部枚举值
     *
     * @return List<String>
     */
    public List<String> getAllEnumCode() {
        List<String> list = new ArrayList<String>();
        for (BizStatus _enum : values()) {
            list.add(_enum.code());
        }
        return list;
    }

}