package com.sunquan.chimingfazhou.download.http;

import android.content.Context;
import android.text.TextUtils;

import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.util.DownloadCallback;
import com.sunquan.chimingfazhou.download.util.TaskExecutor;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @param <T> 请求返回对象的类型，例如，返回JSONObject
 * @author sunquan
 */
public abstract class MyDownloadAbstractRequest<T> extends MyAbstractRequest<T>
        implements IResponseParser<T> {
    protected boolean paused = false;


    public MyDownloadAbstractRequest(Context context) {
        super(context);
    }

    private TaskExecutor<T> task;


    public abstract String getFileSavePath();


    @Override
    public TaskExecutor<T> getTaskRefer() {
        return task;
    }


    public TaskExecutor<T> sendDownloadRequestInBg(
            DownloadCallback<T> callback) {
        paused = false;
        task = new TaskExecutor<T>(callback) {
            @Override
            public T run() throws RequestException {
                T resultT;
                if (isTest) {
                    resultT = buildTestData();
                    paused = true;
                    return resultT;
                }
                if (TextUtils.isEmpty(getFileSavePath())) {
                    paused = true;
                    throw new RequestException("file path is empty");
                }
                int method = getRequestMethod();
                HashMap<String, String> params = getRequestParams();
                ArrayList<Header> headers = getHeaders();
                if (!hasInitHeader) {
                    paused = true;
                    throw new RequestException(
                            "subclasses must call 'super()' method in their 'getHeaders()' method");
                }
                MyConnection hc = new MyConnection();
                this.setCurConnection(hc);
                hc.setResponseParser(MyDownloadAbstractRequest.this);
                hc.setParams(params);
                hc.setHeaders(headers);
                Object result = hc.request(mContext, method, url);

                try {
                    resultT = parseResult(result);
                    paused = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    paused = true;
                    throw new RequestException(e.getMessage());
                }
                return resultT;
            }
        };
        TaskExecutor.executeTask(task);
        return task;
    }


    public boolean hasPaused() {
        return paused;
    }


    public void abortRequest() {
        paused = true;
        if (task != null) {
            MyConnection con = task.getCurConnection();
            if (con != null) {
                con.cancleRequest();
            }
        }
    }
}
