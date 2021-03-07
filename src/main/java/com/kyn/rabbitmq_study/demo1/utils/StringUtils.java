package com.kyn.rabbitmq_study.demo1.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Kangyanan
 * @Description: String 工具类
 * @date 2021/2/28
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    public static boolean isNotBlank(String str) {
        return !org.apache.commons.lang.StringUtils.isBlank(str);
    }
    /**
     * 根据编码转换字节数组为字符串
     *
     * @param bytes
     * @param charset
     * @return 不支持的编码返回null
     */
    public static String newStr(final byte[] bytes, String charset) {
        try {
            return bytes == null ? null : new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            logger.error("",e);
            return null;
        }
    }

    /**
     * 将字符串转换为指定字符编码的字节数组
     * @param str
     * @param encode
     * @return
     */
    public static byte[] getByteArray(String str, String encode) {
        if (isNotEmpty(str)) {
            try {
                return str.getBytes(encode);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * @Description 对象转String
     * @Params
     * @Return
     * @Exceptions
     */
    public static String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }


    /**
     * @Description 判断空
     * @Params
     * @Return
     * @Exceptions
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || valueOf(obj).length() == 0;
    }

    /**
     * @Description 是否为空白
     * @Params
     * @Return true: 是   false:不是
     * @Exceptions
     */
    public static boolean isBlank(Object obj) {
        return isBlank(valueOf(obj));
    }

    /**
     * @Description 是否不是空白
     * @Params
     * @Return true: 不是   false:是
     * @Exceptions
     */
    public static boolean isNotBlank(Object obj) {
        return isNotBlank(valueOf(obj));
    }

    /**
     * @Description 将首字母转换为小写
     * @Params
     * @Return
     * @Exceptions
     */
    public static String convertFirstCharToLower(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }


    /***
     * 拼接字符串,如果包含空值(null或"")则已"null"拼接
     * */
    public static String join(String... args) {
        StringBuffer sb = new StringBuffer();
        for (String s : args) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * @Description 替换回车换行
     * @Params
     * @Return
     * @Exceptions
     */
    public static String replaceSpace(String str) {
        if (null != str) {
            return str.replace("\r\n", "").replace("\n", "");
        }
        return str;
    }

    /**
     * @Description 替换所有的回车换行
     * @Params
     * @Return
     * @Exceptions
     */
    public static String replaceAllSpace(String str) {
        String newStr = str;
        if (null != str) {
            newStr = str.replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\r", "");
        }
        return newStr;
    }

    /**
     * @Description 替换所有的空格、回车换行
     * @Params
     * @Return
     * @Exceptions
     */
    public static String replaceAllBland(String str) {
        String newStr = str;
        if (null != str) {
            newStr = str.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
        }
        return newStr;
    }

    /**
     * @Description 得到数组第一个值
     * @Params
     * @Return
     * @Exceptions
     */
    public static String getFirstByArray(String[] strArray) {
        if (null != strArray && strArray.length > 0) {
            return strArray[0];
        }
        return "";
    }

    /**
     * unicode转中文
     *
     * @param unicode
     * @return
     */
    public static String unicodeTo(String unicode) {
        if (StringUtils.isBlank(unicode)) return "";
        String[] arr = unicode.split("\\\\u", -1);
        StringBuffer sb = new StringBuffer();
        if (arr != null && arr.length > 0) {
            for (String str : arr) {
                if (str.length() == 4) {
                    sb.append((char) Integer.parseInt(str, 16));
                }
            }
        }

        return sb.toString();
    }

    /**
     * 获取后几位字符
     *
     * @param str
     * @param num
     * @return
     */
    public static String getLastStrWithNum(String str, int num) {
        return StringUtils.substring(str, str.length() - num);
    }

    /**
     * 去除回车换行符
     *
     * @param str
     * @return
     */
    public static String trimLine(String str) {
        if (str == null) {
            return null;
        }
        if (!str.endsWith("\n") && !str.endsWith("\r")) {
            return str;
        }
        return trimLine(str.substring(0, str.length() - 1));
    }

    /**
     * 空转默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String nullToDefault(String obj, String defaultValue) {
        if (StringUtils.isBlank(obj)) {
            return defaultValue;
        }
        return obj;
    }

    /**
     * 生成指定长度随机数，最大支持18位长
     *
     * @param len 需要的随机数长度，最大18
     * @return
     */
    public static String random(int len) {
        Random rm = new Random();
        double t = rm.nextDouble();
        Double pross = (1 + t) * Math.pow(10, len);
        String fixstr = String.valueOf(pross.longValue());

        if (null != fixstr) {
            int strlen = fixstr.length();
            if (fixstr.length() > len) {
                fixstr = fixstr.substring(strlen - len, fixstr.length());
            } else {
                for (int i = 0; i < len - strlen; i++) {
                    fixstr = "0" + fixstr;
                }
            }
        }
        return fixstr;
    }

    /**
     * trim null->"null"
     *
     * @param str
     * @return
     */
    public static String trimToNull(String str) {
        return String.valueOf(org.apache.commons.lang.StringUtils.trimToNull(str));
    }

    /**
     * 报文长度左补内容
     *
     * @param lengthStr   需要补充的字符串
     * @param totalLength 补充后字符串的长度
     * @param padding     需要补充的内容
     * @return
     */
    public static String appendLeftStr(String lengthStr, int totalLength, String padding) {
        if (null == lengthStr) {
            lengthStr = "";
        }
        int len = lengthStr.length();
        if (len >= totalLength) {
            return lengthStr;
        }
        int ss = totalLength - len;
        String res = "";
        for (int i = 0; i < ss; i++) {
            res = res + padding;
        }
        return res + lengthStr;
    }

    /**
     * 报文长度右补内容
     *
     * @param lengthStr   需要补充的字符串
     * @param totalLength 补充后字符串的长度
     * @param padding     需要补充的内容
     * @return
     */
    public static String appendRightStr(String lengthStr, int totalLength, String padding) {
        if (null == lengthStr) {
            lengthStr = "";
        }
        int len = 0;
        try {
            len = lengthStr.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (len >= totalLength) {
            return lengthStr;
        }
        int ss = totalLength - len;
        String res = "";
        for (int i = 0; i < ss; i++) {
            res = res + padding;
        }
        return lengthStr + res;
    }

    public static String newString(byte[] bytes, String format) {
        try {
            return new String(bytes, format);
        } catch (UnsupportedEncodingException e) {
            //LoggerUtil.error(logger, "字节数组编码为字符串异常[bytes:{}, format:{}]", bytes, format, e);
            return null;
        }
    }

    /**
     * 将固定长度字符串按每段长度拆分成字符串数组
     * 例如：splitByFixedLen("1234567890", 0, 1,1,1,1,6)
     *      表示“将1234567890从第0位开始按1,1,1,1,6长度拆分”
     *      拆分结果为：[1, 2, 3, 4, 567890]
     * @param source 被拆分的字符串
     * @param start 开始位置
     * @param lengths 字段长度参数
     * @return
     */
    public static String[] splitByFixedLen(String source, int start, int...lengths){
        logger.info("字符串拆分输入参数[source:{}, start:{}, lengths:{}]", source, start, lengths);

        try {

            if(StringUtils.isEmpty(source)){
                logger.warn("被拆分的字符串为空");
                return null;
            }

            if(lengths == null || lengths.length == 0){
                logger.warn("拆分字段长度参数为空");
                return null;
            }

            int msgLen = source.length();

            List<String> resultList = new ArrayList<>();
            for(int len : lengths){
                if(start < msgLen){
                    resultList.add(StringUtils.substring(source, start, start+len));
                    start += len;
                }
            }

            return resultList.toArray(new String[resultList.size()]);
        }
        catch (Exception ex){
            logger.error("字符串拆分异常", ex);
            return null;
        }
    }

    /**
     * 将固定长度字符串按每段长度拆分成字符串数组
     * 例如：splitByFixedLen("1234567890", 0, 1,1,1,1,6)
     *      表示“将1234567890从第0位开始按1,1,1,1,6长度拆分”
     *      拆分结果为：[1, 2, 3, 4, 567890]
     * @param source 被拆分的字符串
     * @param start 开始位置
     * @param lengths 字段长度参数
     * @return
     */
    public static String[] splitByFixedLen(byte[] source, String charset, int start, int...lengths){
        logger.info("字节数组拆分输入参数[source:{}, start:{}, lengths:{}]", newString(source, charset), start, lengths);

        try {

            if(source == null || source.length == 0){
                logger.warn("被拆分的字节数组为空");
                return null;
            }

            if(lengths == null || lengths.length == 0){
                logger.warn("拆分字段长度参数为空");
                return null;
            }

            int msgLen = source.length;

            List<String> resultList = new ArrayList<>();
            for(int len : lengths){
                if(start < msgLen){
                    resultList.add(new String(Arrays.copyOfRange(source, start, start+len), charset));
                    start += len;
                }
            }

            return resultList.toArray(new String[resultList.size()]);
        }
        catch (Exception ex){
            logger.error("字节数组拆分异常", ex);
            return null;
        }
    }
}
