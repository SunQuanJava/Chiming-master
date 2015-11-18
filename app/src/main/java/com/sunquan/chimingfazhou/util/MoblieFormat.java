package com.sunquan.chimingfazhou.util;

/**
 * 电话号码输出格式化 3+4+4
 * Created by Administrator on 2015/6/9.
 */
public class MoblieFormat {
    public static String format(String str) {
        final char[] str1 = str.toCharArray();
        final StringBuilder strNew = new StringBuilder();
        for (int i = 0; str1.length > i; i++) {
            strNew.append(str1[i]);
            if (i == 2 || i == 6) {
                strNew.append(" ");
            }
        }
        return strNew.toString();
    }
}
