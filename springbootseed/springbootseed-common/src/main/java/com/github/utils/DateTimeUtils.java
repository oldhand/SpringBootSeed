package com.github.utils;

import org.springframework.web.bind.ServletRequestDataBinder;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 时间工具类,
 */
public class DateTimeUtils {
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
    /**
     * 获取时间偏移量
     */
    public static void timestampRequestDataBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (!text.isEmpty()) {
                    if (ValidationUtil.isDateTime(text)) {
                        setValue(Timestamp.valueOf(text));
                    }
                    else if (ValidationUtil.isDate(text)) {
                        setValue(Timestamp.valueOf(text + " 00:00:00"));
                    }
                    else if (ValidationUtil.isNumeric(text)) {
                        setValue(new Timestamp(Long.parseLong(text)));
                    }
                }
            }
        });

    }
    /**
     * 获取当前时间
     */
    public static String getDatetime() {
        return getDatetime("yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 获取指定格式的当前时间
     */
    public static String getDatetime(String foramat) {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat(foramat);
        return dateFormat.format(date);
    }

}