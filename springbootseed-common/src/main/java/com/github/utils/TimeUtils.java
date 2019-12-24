package com.github.utils;

/**
 * 时间工具类,
 */
public class TimeUtils {
    /**
     * 获取格林威治时间戳
     */
    public static long gettimeStamp() {
            java.util.Calendar cal = java.util.Calendar.getInstance() ;
            cal.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
            long timeStamp = cal.getTimeInMillis() - 28800000;
            return timeStamp / 1000;

    }
    /**
     * 获取时间偏移量
     */
    public static int getzoneOffset() {
        java.util.Calendar cal = java.util.Calendar.getInstance() ;
        return cal.get(java.util.Calendar.ZONE_OFFSET);
    }
}