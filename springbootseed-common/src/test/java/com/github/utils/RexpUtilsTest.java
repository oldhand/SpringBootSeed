package com.github.utils;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.utils.StringUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RexpUtilsTest {

    @Test
    public void testisDate() {
        /**
         * 日期格式正确
         */
        String date1 = "2014-01-03";
        /**
         * 日期范围不正确---平年二月没有29号
         */
        String date2 = "2014-02-29";
        /**
         * 日期月份范围不正确---月份没有13月
         */
        String date3 = "2014-13-03";
        /**
         * 日期范围不正确---六月没有31号
         */
        String date4 = "2014-06-31";
        /**
         * 日期范围不正确 ----1月超过31天
         */
        String date5 = "2014-01-32";
        /**
         * 这个测试年份
         */
        String date6 = "0014-01-03";

        System.out.println("____isDate____" + ValidationUtil.isDate(date1) + "____" + date1  + "______");
        System.out.println("____isDate____" + ValidationUtil.isDate(date2) + "____" + date2  + "______");
        System.out.println("____isDate____" + ValidationUtil.isDate(date3) + "____" + date3  + "______");
        System.out.println("____isDate____" + ValidationUtil.isDate(date4) + "____" + date4  + "______");
        System.out.println("____isDate____" + ValidationUtil.isDate(date5) + "____" + date5  + "______");
        System.out.println("____isDate____" + ValidationUtil.isDate(date6) + "____" + date6  + "______");
    }

    @Test
    public void testisDatetime() {
        /**
         * 日期格式正确
         */
        String datetime1 = "2014-01-03 00:00:00";
        /**
         * 日期范围不正确---平年二月没有29号
         */
        String datetime2 = "2014-02-29 00:00:00";
        /**
         * 日期月份范围不正确---月份没有13月
         */
        String datetime3 = "2014-13-03 00:00:00";
        /**
         * 日期范围不正确---六月没有31号
         */
        String datetime4 = "2014-06-31 00:00:00";
        /**
         * 日期范围不正确 ----1月超过31天
         */
        String datetime5 = "2014-01-32 00:00:00";
        /**
         * 这个测试年份
         */
        String datetime6 = "0014-01-03 00:00:00";

        System.out.println("____isDateTime____" + ValidationUtil.isDateTime(datetime1) + "____" + datetime1  + "______");
        System.out.println("____isDateTime____" + ValidationUtil.isDateTime(datetime2) + "____" + datetime2  + "______");
        System.out.println("____isDateTime____" + ValidationUtil.isDateTime(datetime3) + "____" + datetime3  + "______");
        System.out.println("____isDateTime____" + ValidationUtil.isDateTime(datetime4) + "____" + datetime4  + "______");
        System.out.println("____isDateTime____" + ValidationUtil.isDateTime(datetime5) + "____" + datetime5  + "______");
        System.out.println("____isDateTime____" + ValidationUtil.isDateTime(datetime6) + "____" + datetime6  + "______");
    }



}