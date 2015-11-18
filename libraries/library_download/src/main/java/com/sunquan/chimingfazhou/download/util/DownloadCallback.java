package com.sunquan.chimingfazhou.download.util;


import com.sunquan.chimingfazhou.download.exception.RequestException;

/**
 * 作    者: sunquan
 * 文件描述:
 * 回调基类，用于异步回调
 */
public abstract class DownloadCallback<T> extends CommonCallback<T> {
    /**
     * 操作完成回调此方法
     *
     * @param returnValue   返回值
     * @param httpException 不为空则表示操作异常
     */
    public abstract void internalDone(T returnValue, RequestException httpException);

    /**
     * 下载进度更新
     *
     * @param progress
     * @param total
     */
    public abstract void onDownloadProgressChanged(int progress, int total);
}