package com.baizhi.baseapp.controller;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.baizhi.baseapp.application.BaseApplication;
import com.baizhi.baseapp.util.NetworkState;

/**
 * 加载数据类
 *
 * Created by Administrator on 2015/7/2.
 */
public class LoadController {

    public static final int HTTP_FIRST = 0;
    public static final int HTTP_ONLY = 1;
    public static final int CACHE_FIRST = 2;
    public static final int CACHE_ONLY = 3;

    private static LoadController instance;


    private static final long EXPIRE_TIME = -1;
    private static final long EXPIRE_CACHE_TIME = Long.MAX_VALUE;

    private AQuery mAQuery;
    private Context mContext;

    public static LoadController getInstance(Context context) {
        if (instance == null) {
            instance = new LoadController(context);
        }
        return instance;
    }

    private LoadController(Context context) {
        this.mContext = context.getApplicationContext();
        this.mAQuery = BaseApplication.getAQuery(mContext);
    }

    /**
     * 获取会员列表
     *
     * @param url
     * @param dataCallback
     */
    public <T>void loadData(final String url, final DataCallback<T> dataCallback, final Class<T> clazz, final int type) {
        switch (type) {
            case HTTP_FIRST:
                loadData1(url, dataCallback, clazz, false);
                break;
            case HTTP_ONLY:
                loadData2(url, dataCallback, clazz, true);
                break;
            case CACHE_FIRST:
                loadData2(url, dataCallback, clazz, false);
                break;
            case CACHE_ONLY:
                loadData1(url, dataCallback, clazz, true);
                break;
        }

    }

    /**
     * 通过递归的方式判断网络数据
     *
     * @param url
     * @param dataCallback
     * @param isFromCache
     */
    private <T>void loadData1(final String url, final DataCallback<T> dataCallback, final Class<T> clazz, final boolean isFromCache) {
        if (!isFromCache) {
            if (!NetworkState.isActiveNetworkConnected(mContext)) {
                loadData1(url, dataCallback, clazz, true);
                return;
            }
            mAQuery.transformer(new GsonTransformer()).ajax(url, clazz, EXPIRE_TIME, new AjaxCallback<T>() {
                @Override
                public void callback(String url, T object, AjaxStatus status) {
                    if (status.getCode() == 200 && object != null) {
                        dataCallback.success(object);
                    } else {
                        loadData1(url, dataCallback, clazz, true);
                    }
                }
            });
        } else {
            mAQuery.transformer(new GsonTransformer()).ajax(url, clazz, EXPIRE_CACHE_TIME, new AjaxCallback<T>() {
                @Override
                public void callback(String url, T object, AjaxStatus status) {
                    if (status.getCode() == 200 && object != null) {
                        dataCallback.success(object);
                    } else {
                        dataCallback.fail();
                    }
                }
            });
        }
    }

    /**
     * 通过递归的方式判断网络数据
     *
     * @param url
     * @param dataCallback
     */
    private <T>void loadData2(final String url, final DataCallback<T> dataCallback, final Class<T> clazz, final boolean isFromHttp) {
        if (!isFromHttp) {
            mAQuery.transformer(new GsonTransformer()).ajax(url, clazz, EXPIRE_CACHE_TIME, new AjaxCallback<T>() {
                @Override
                public void callback(String url, T object, AjaxStatus status) {
                    if (status.getCode() == 200 && object != null) {
                        dataCallback.success(object);
                    } else {
                        loadData2(url, dataCallback, clazz, true);
                    }
                }
            });
        } else {
            if (!NetworkState.isActiveNetworkConnected(mContext)) {
                AQUtility.post(new Runnable() {
                    @Override
                    public void run() {
                        dataCallback.fail();
                    }
                });
                return;
            }
            mAQuery.transformer(new GsonTransformer()).ajax(url, clazz, EXPIRE_TIME, new AjaxCallback<T>() {
                @Override
                public void callback(String url, T object, AjaxStatus status) {
                    if (status.getCode() == 200 && object != null) {
                        dataCallback.success(object);
                    } else {
                        dataCallback.fail();
                    }
                }
            });
        }
    }

    /**
     * 数据回调
     *
     * @param <T>
     */
    public static interface DataCallback<T> {
        void success(T t);

        void fail(Object... objects);
    }

}
