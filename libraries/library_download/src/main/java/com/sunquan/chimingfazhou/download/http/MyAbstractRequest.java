package com.sunquan.chimingfazhou.download.http;

import android.content.Context;

import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.util.CommonCallback;
import com.sunquan.chimingfazhou.download.util.TaskExecutor;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @param <T> 请求返回对象的类型，例如，返回JSONObject
 * @author sunquan
 */
public abstract class MyAbstractRequest<T> {
    /**
     * 是否是测试，若是，则直接返回buildTestData()返回的数据
     */
    public boolean isTest = false;
    protected boolean hasInitHeader = false;
    protected Context mContext;
    protected String url;


    public MyAbstractRequest(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static final int HTTP_GET = MyConnection.REQ_TYPE_GET;
    public static final int HTTP_POST = MyConnection.REQ_TYPE_POST;
    public static final int HTTP_PUT = MyConnection.REQ_TYPE_PUT;
    private static final String TAG = MyAbstractRequest.class.getSimpleName();


    /**
     * 子类需要实现此方法，并且返回请求类型:{@link #HTTP_GET}、{@link #HTTP_POST}、
     * {@link #HTTP_PUT}
     *
     * @return 代表请求类型的int值
     */
    public abstract int getRequestMethod();


    /**
     * 子类需要实现此方法，并返回POST请求所需要的参数键值对
     *
     * @return 返回POST请求所需要的参数键值对
     */
    public abstract HashMap<String, String> getRequestParams();


    /**
     * 子类需要实现此方法，并返回请求所需的请求头列表
     *
     * @return 返回请求所需的请求头列表
     */
    public ArrayList<Header> getHeaders() {
        hasInitHeader = true;
        ArrayList<Header> headersList = new ArrayList<Header>();
        return headersList;
    }


    /**
     * 子类需要实现此方法，在方法内部解析请求返回的结果，并返回解析后的对象
     *
     * @param result 请求结果（可能是String或者流类型）
     * @return 解析后的结果对象
     */
    protected abstract T parseResult(Object result) throws Exception;


    protected T buildTestData() {
        return null;
    }


    public T sendRequest() throws RequestException {
        if (isTest) {
            return buildTestData();
        }

        int method = getRequestMethod();
        HashMap<String, String> params = getRequestParams();
        ArrayList<Header> headers = getHeaders();
        if (!hasInitHeader) {
            throw new RequestException(
                    "subclasses must call 'super()' method in their 'getHeaders()' method");
        }

        MyConnection hc = new MyConnection();
        hc.setParams(params);
        hc.setHeaders(headers);
        Object result = hc.request(mContext, method, url);
        T resultT = null;
        try {
            resultT = parseResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestException(e.getMessage());
        }
        return resultT;
    }


    public TaskExecutor<T> sendRequestInBg(CommonCallback<T> callback) {
        TaskExecutor<T> task = new TaskExecutor<T>(callback) {
            @Override
            public T run() throws RequestException {
                return sendRequest();
            }
        };
        TaskExecutor.executeTask(task);
        return task;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
