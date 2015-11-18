package com.sunquan.chimingfazhou.util;

import android.content.Context;
import android.text.TextUtils;

import com.sunquan.chimingfazhou.constants.Constants;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;

/**
 * 网络地址url获取工具类
 * <p/>
 * Created by Administrator on 2015/5/19.
 */
public final class ParamsUtil {

    /**
     * 获取思的详情url
     *
     * @param context
     * @param id
     * @return
     */
    public static String getSiDetailUrl(Context context, String id) {
        final StringBuilder builder = new StringBuilder();
        builder.append(Constants.HOST).append(Constants.SI_DETAIL_URL).append("?uid=").append(GlobalDataHolder.getInstance(context).getUid()).append("&id=").append(id);
        return builder.toString();
    }

    /**
     * 获取闻的详情url
     *
     * @param context
     * @param id
     * @return
     */
    public static String getWenDetailUrl(Context context, String id) {
        final StringBuilder builder = new StringBuilder();
        builder.append(Constants.HOST).append(Constants.WEN_DETAIL_URL).append("?uid=").append(GlobalDataHolder.getInstance(context).getUid()).append("&id=").append(id);
        return builder.toString();
    }

    /**
     * 根据类型获取一级页面数据的url
     *
     * @param context
     * @param type
     * @param subType
     * @return
     */
    public static String getFirstPageUrlByType(Context context, String type, String subType) {
        final StringBuilder builder = new StringBuilder();
        builder.append(Constants.HOST).append(Constants.FIRST_PAGE_URL).append("?uid=").append(GlobalDataHolder.getInstance(context).getUid()).append("&type=").append(type);
        if(!TextUtils.isEmpty(subType)) {
            builder.append("&sub_type=").append(subType);
        }
        return builder.toString();
    }

    /**
     * 根据类型获取一级页面数据的url
     *
     * @param context
     * @param type
     * @return
     */
    public static String getFirstPageUrlByType(Context context, String type) {
        return getFirstPageUrlByType(context,type,null);
    }
}
