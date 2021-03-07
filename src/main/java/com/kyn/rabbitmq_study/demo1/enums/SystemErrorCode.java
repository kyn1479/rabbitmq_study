package com.kyn.rabbitmq_study.demo1.enums;



import com.kyn.rabbitmq_study.demo1.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kangyanan
 * @Description: 系统异常错误码
 * @date 2021/2/28
 */
public enum SystemErrorCode implements ErrorCode {
    SUCCESS("0000", "交易成功"),
    PROCESS("G001", "交易处理中"),
    FAIL("G002", "交易失败"),
    ACCEPT("G003", "交易已受理"),
    SYSTEM_ERROR("G201", "系统内部异常"),
    BIZ_CHECK_ERROR("G252", "业务参数校验异常{0}") ,
    DATA_NOT_EXIST("G237", "数据{0}不存在"),
    TRANS_CODE_UNDEFINED("G245", "交易码({0})未定义"),
    SERVICE_NAME_UNDEFINED("G246", "处理器Service({0})未定义"),
    DATA_ACCESS_ERROR("G202","数据访问异常[{0}]"),
    UNIQUE_CHECK_ERROR("G231", "幂等性验证异常[{0}]"),
    /** cnu-provider调用异常*/
    PAYGW_INVOKE_ERROR("G301", "cnu-provider调用异常[{0}]"),
    COMMUNICATION_EXCEPTION("G315", "通讯异常"),
    ILLEGAL_PARAM("G252", "业务参数校验异常{0}"),
    VALIDATE_ERROR("G240", "参数错误格式错误"),


    ;


    /**
     * 枚举值
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String message;



    /**
     * 构造一个<code>SystemErrorCode</code>枚举对象
     * @param code
     * @param message
     */
    private SystemErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
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
     * @param code
     * @return SystemErrorCode
     */
    public static SystemErrorCode getByCode(String code) {
        for (SystemErrorCode _enum : values()) {
            if (_enum.getCode().equals(code)) {
                return _enum;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举
     * @return List<SystemErrorCode>
     */
    public static List<SystemErrorCode> getAllEnum() {
        List<SystemErrorCode> list = new ArrayList<SystemErrorCode>();
        for (SystemErrorCode _enum : values()) {
            list.add(_enum);
        }
        return list;
    }

    /**
     * 获取全部枚举值
     * @return List<String>
     */
    public  static List<String> getAllEnumCode() {
        List<String> list = new ArrayList<String>();
        for (SystemErrorCode _enum : values()) {
            list.add(_enum.code());
        }
        return list;
    }

    /**
     * @Description 获取异常类型
     * @Params
     * @Return
     * @Exceptions
     */
    @Override
    public ExceptionTypeEnum getExceptionType() {
        return ExceptionTypeEnum.SYSTEM_ERROR;
    }

    /**
     * @Description 获取错误编码
     * @Params
     * @Return
     * @Exceptions
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * @Description 获取错误消息（获取到的是key，可能需要进行资源文件转换成真正的错误消息）
     * @Params
     * @Return
     * @Exceptions
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    public static void main(String[] args) {

    }


}
