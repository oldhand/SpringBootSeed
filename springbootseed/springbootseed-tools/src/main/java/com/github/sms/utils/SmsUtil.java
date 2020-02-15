package com.github.sms.utils;


import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * 短信验证码
 *
 */
public class SmsUtil {

    /**
     * make 生成六位随机验证码
     */
    public static String make() {
       return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer();
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append("00");
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }
    /**
     * 第三方短信发送接口代码：
     * @param mobile 手机号码
     * @param regioncode 国际区号
     * @param verifycode 验证码
     * @return 服务端返回的结果 ok:业务id 或者 错误代码
     */
    public  static String sendSms(String mobile,String regioncode,String verifycode) throws Exception {
        try {
            System.out.println("--------sendSms------+" + regioncode + mobile + "------"+verifycode+"-----------");
//            String msg = String.format("【PaiChat】欢迎使用派聊，您的验证码是：%s", verifycode);
//            String message = String.format("3010005000610069004300680061007430116B228FCE4F7F75286D3E804AFF0C60A876849A8C8BC17801662FFF1A%s", bin2hex(verifycode));
//            String username = "baozi007";
//            String password = "1qazxsw2";
//            String smsurl = String.format("http://m.isms360.com:8085/mt/MT3.ashx?src=%s&pwd=%s&ServiceID=SEND&dest=%s%s&sender=106577777&msg=%s&codec=8",username,password,regioncode,mobile,message);
//            URL url = new URL(smsurl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//            return in.readLine();
            SendSmsResponse response = AliyunSms.send(mobile,regioncode,verifycode);
            System.out.println("--------response------" + response.getCode() + "-----------");
            System.out.println("--------response------" + response.getBizId() + "-----------");
            System.out.println("--------response------" + response.getRequestId() + "-----------");
            System.out.println("--------response------" + response.getMessage() + "-----------");
            if (response.getCode().compareTo("OK") == 0) {
                return "ok";
            }
            throw new Exception(response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}