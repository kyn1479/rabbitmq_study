/**
 * msxf.com Inc.
 * Copyright (c) 2016-2026 All Rights Reserved.
 */
package com.kyn.rabbitmq_study.demo1.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Kangyanan
 * @Description: 日期工具类
 * @date 2021/3/1
 */
public class DateUtil {

    public final static String pattern1 = "yyyyMMdd";
    public final static String pattern2 = "yyyy-MM-dd";
    public final static String pattern3 = "yyyy-MM-dd HH:mm:ss";
    public final static String pattern4 = "yyyyMMddHHmmss";

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDateTime() {
        return DateTime.now().toDate();
    }

    /**
     * 获取前minutes分的时间
     *
     * @param minutes
     * @return
     */
    public static Date getLastDateTimeByMinutes(int minutes) {
        return DateTime.now().minusMinutes(minutes).toDate();
    }

    /**
     * 获取前days日的日期
     *
     * @param days
     * @return
     */
    public static java.sql.Date getLastDateByDays(int days) {
        return new java.sql.Date(DateTime.now().minusDays(days).withMillisOfDay(0).toDate().getTime());
    }
    /**
     * 获取minutes分之后的时间
     * @param minutes
     * @return
     */
    public static Date getNextDateTimeByMinutes(int minutes) {
        return DateTime.now().plusMinutes(minutes).toDate();
    }

     /**
      * @Description 返回yyyyMMddHHmmss格式
      * @Params
      * @Return
      * @Exceptions
      */
    public static String getTime14() {
        DateTime dt = new DateTime();
        return dt.toString("yyyyMMddHHmmss");
    }

    /**
     * @Description 返回yyyyMMddHHmmss格式
     * @Params
     * @Return
     * @Exceptions
     */
    public static String getTime14(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

     /**
      * @Description 获取格yyyy-MM-dd时间
      * @Params
      * @Return
      * @Exceptions
      */
    public static String getDate10() {
        DateTime dt = new DateTime();
        return dt.toString("yyyy-MM-dd");
    }

     /**
      * @Description 获取格yyyyMMdd时间
      * @Params
      * @Return
      * @Exceptions
      */
    public static String getDate8() {
        DateTime dt = new DateTime();
        return dt.toString("yyyyMMdd");
    }
    public static String getDate8(Date date) {
        DateTime dt = new DateTime(date.getTime());
        return dt.toString("yyyyMMdd");
    }

    /**
     * 时间解析
     *
     * @param dateTime
     * @param pattren
     * @return
     */
    public static Date parseDateTime(String dateTime, String pattren) {
        return DateTimeFormat.forPattern(pattren).parseDateTime(dateTime).toDate();
    }

    /**
     * 日期/时间格式化
     *
     * @param date
     * @param pattren
     * @return
     */
    public static String format(Date date, String pattren) {
        return new DateTime(date).toString(pattren);
    }
}
