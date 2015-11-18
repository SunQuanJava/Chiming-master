package com.sunquan.chimingfazhou.download.util;

import android.annotation.TargetApi;
import android.os.AsyncTask;

import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.http.MyConnection;


/**
 * 作 者: sunquan
 * 文件描述: 后台任务执行器
 */
public abstract class TaskExecutor<T> extends AsyncTask<Void, Integer, Void> {
    private CommonCallback<T> callback;
    private T result;
    private RequestException exception;
    private MyConnection curConnection;


    public TaskExecutor(CommonCallback<T> callback) {
        this.result = null;
        this.exception = null;
        this.callback = callback;
    }


    /**
     * 执行方法
     *
     * @return 执行结果
     * @throws com.sunquan.chimingfazhou.download.exception.RequestException 执行失败抛出此异常
     */
    public abstract T run() throws RequestException;


    public void updateProgress(Integer... values) {
        publishProgress(values);
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            this.result = run();
            return null;
        } catch (RequestException e) {
            this.exception = e;
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (this.callback instanceof DownloadCallback) {
            int curProgress = 0;
            int total = 0;
            if (values != null) {
                if (values.length > 0) {
                    curProgress = values[0];
                }
                if (values.length > 1) {
                    total = values[1];
                }
            }
            ((DownloadCallback) this.callback).onDownloadProgressChanged(
                    curProgress, total);
        }
    }


    /**
     * 执行结束分发结果
     *
     * @param v 执行结果
     */
    @Override
    protected void onPostExecute(Void v) {
        if (this.callback != null) {
            this.callback.internalDone(this.result, this.exception);
        }
    }


    @SuppressWarnings("unused")
    void executeInThisThread() {
        doInBackground();
        onPostExecute(null);
    }


    /**
     * 执行任务
     *
     * @param task 任务对象
     * @return 0
     */
    @SuppressWarnings("unchecked")
    public static int executeTask(TaskExecutor task) {
        //3.0之前用execute，之后用executeOnExecutor，
        //因为3.0之后android的AsyncTask默认是顺序执行，所以必须要指定它不顺序执行
        if (android.os.Build.VERSION.SDK_INT < 11) {
            task.execute(new Void[0]);
        } else {
            executeTaskOnNewSDK(task);
        }
        return 0;
    }

    @TargetApi(11)
    @SuppressWarnings("unchecked")
    private static void executeTaskOnNewSDK(TaskExecutor task) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public MyConnection getCurConnection() {
        return curConnection;
    }


    public void setCurConnection(MyConnection curConnection) {
        this.curConnection = curConnection;
    }
}