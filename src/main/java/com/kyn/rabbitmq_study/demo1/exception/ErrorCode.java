package com.kyn.rabbitmq_study.demo1.exception;


import com.kyn.rabbitmq_study.demo1.enums.ExceptionTypeEnum;

/**
 * @author Kangyanan
 * @Description: 异常信息接口
 * @date 2021/2/28
 */
public interface ErrorCode {

     /**
       * @Description 获取异常类型
       *
       * @Params
       * @Return
       * @Exceptions
       */
    ExceptionTypeEnum getExceptionType();

     /**
       * @Description 获取错误编码
       *
       * @Params
       * @Return
       * @Exceptions
       */
    String getCode();

     /**
       * @Description 获取错误消息（获取到的是key，可能需要进行资源文件转换成真正的错误消息）
       *
       * @Params
       * @Return
       * @Exceptions
       */
    String getMessage();


}
