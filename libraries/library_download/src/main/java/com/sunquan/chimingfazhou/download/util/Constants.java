package com.sunquan.chimingfazhou.download.util;

import android.os.Environment;

/**
 * 作 者: sunquan
 * 文件描述:
 */
public class Constants {

    /**
     * 响应字段：code
     */
    public static final String RESPONSE_CODE = "code";

    /**
     * 响应字段code的值：200
     */
    public static final int RESPONSE_CODE_200 = 200;
    /**
     * 响应字段code的值：400
     */
    public static final int RESPONSE_CODE_400 = 400;
    /**
     * 响应字段code的值：400
     */
    public static final int RESPONSE_CODE_401 = 401;
    /**
     * 响应字段code的值：500
     */
    public static final int RESPONSE_CODE_500 = 500;
    /**
     * 是否是测试模式，测试模式联网请求全部使用假数据
     */
    public static final boolean isTest = false;
    public static final String APP_DATA_PATH = Environment.getExternalStorageDirectory()
            + "//" + "chimingfazhou"+"//"+"download";

}
