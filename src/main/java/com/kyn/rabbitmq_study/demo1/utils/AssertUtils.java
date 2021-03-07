package com.kyn.rabbitmq_study.demo1.utils;

import com.kyn.rabbitmq_study.demo1.exception.CnuFrontException;
import com.kyn.rabbitmq_study.demo1.exception.ErrorCode;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author Kangyanan
 * @Description: 断言工具，自定义异常
 * @date 2021/3/1
 */
public class AssertUtils {

    /**
     * @Description 判断是否空
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isNotNull(Object object, ErrorCode errorCode) {
        isNotNull(object, errorCode, null);
    }

    /**
     * @Description 判断是否空
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isNotNull(Object object, ErrorCode errorCode, Object[] args) {
        if (object == null) {
            throw new CnuFrontException(errorCode, args);
        }
    }

    /**
     * @Description 判断是否空白
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isNotBlank(String text, ErrorCode errorCode) {
        isNotBlank(text, errorCode, null);
    }

    /**
     * @Description 判断是否空白
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isNotBlank(String text, ErrorCode errorCode, Object[] args) {
        if (StringUtils.isBlank(text)) {
            throw new CnuFrontException(errorCode, args);
        }
    }

    /**
     * @Description 检测容器是否为空
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isNotEmpty(Collection<?> collection, ErrorCode errorCode)
            throws CnuFrontException {
        isNotEmpty(collection, errorCode, null);
    }

    /**
     * @Description 检测容器是否为空
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isNotEmpty(Collection<?> collection, ErrorCode errorCode, Object[] args)
            throws CnuFrontException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new CnuFrontException(errorCode, args);
        }
    }

    /**
     * @Description 判断真
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isTrue(boolean expression, ErrorCode errorCode) {
        isTrue(expression, errorCode, null);
    }

    /**
     * @Description 判断真
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isTrue(boolean expression, ErrorCode errorCode, Object[] args) {
        if (!expression) {
            throw new CnuFrontException(errorCode, args);
        }
    }

    /**
     * @Description 判断假
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isFalse(boolean expression, ErrorCode errorCode) {
        isFalse(expression, errorCode, null);
    }

    /**
     * @Description 判断假
     * @Params
     * @Return
     * @Exceptions
     */
    public static void isFalse(boolean expression, ErrorCode errorCode, Object[] args) {
        if (expression) {
            throw new CnuFrontException(errorCode, args);
        }
    }


}
