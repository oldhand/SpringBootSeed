package com.github.profile.service.utils;

import java.math.BigInteger;
import java.util.Random;

/**
 * 工具类,
 */
public class ProfileUtils {
    /**
     * 获取短唯一码
     */
    public static String makeProfileId() {
            long timespan = System.currentTimeMillis(); //毫秒
            Long microsecs = timespan % 1000; // 微秒
            Long secs = timespan / 1000; // 秒
            Random r = new Random();
            int ran = r.nextInt(99999);
            String List = String.format("%03d%09d%05d", microsecs,secs,ran);
            BigInteger biginteger = new BigInteger(List);
            return biginteger.toString(36);
    }

}