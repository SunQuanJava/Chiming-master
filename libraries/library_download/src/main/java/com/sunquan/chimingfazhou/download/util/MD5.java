package com.sunquan.chimingfazhou.download.util;

import java.security.MessageDigest;

/**
 * 对外提供getMD5(String)方法
 *
 * @author sunquan
 */
public final class MD5 {

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();

        for (byte aByteArray : byteArray) {
            if (Integer.toHexString(0xFF & aByteArray).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
        }
        //16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toUpperCase();
    }
}  