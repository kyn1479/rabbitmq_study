package com.kyn.rabbitmq_study.demo1.exception;


import java.text.MessageFormat;

/**
 * @author Kangyanan
 * @Description: 支付CnuFront异常
 * @date 2021/2/28
 */
public class CnuFrontException extends RuntimeException {

    /**
     * @Description 错误码
     * @Params
     * @Return
     * @Exceptions
     */
    private ErrorCode errorCode;

    /**
     * @Description 异常参数，用于传参给最终的错误码
     * @Params
     * @Return
     * @Exceptions
     */
    private Object[] args;

    public CnuFrontException(ErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
    }

    public CnuFrontException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CnuFrontException(ErrorCode errorCode, Object... args) {
        super((errorCode.getCode()));
        this.errorCode = errorCode;
        this.args = args;
    }

    public CnuFrontException(ErrorCode errorCode, String args) {
        super((errorCode.getCode()));
        this.errorCode = errorCode;
        String [] temp = {args};
        this.args = temp;
    }

    public CnuFrontException(String message, ErrorCode errorCode, Object[] args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    public CnuFrontException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CnuFrontException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CnuFrontException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }


    public String format(ErrorCode errorCode, Object[] args) {
        return MessageFormat.format(errorCode.getMessage(), args);
    }

    @Override
    public String getMessage() {
        return getErrorMsg();
    }

    public String getErrorMsg() {
        if (args != null) {
            return format(errorCode, args);
        }
        return errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
