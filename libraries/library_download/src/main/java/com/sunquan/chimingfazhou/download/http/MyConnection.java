package com.sunquan.chimingfazhou.download.http;

import android.content.Context;
import android.text.TextUtils;

import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.module.ProxyInfo;
import com.sunquan.chimingfazhou.download.util.ConnectionUtil;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class MyConnection {
    /**
     * 请求类型GET
     */
    public static final int REQ_TYPE_GET = 0;
    /**
     * 请求类型POST
     */
    public static final int REQ_TYPE_POST = 1;
    /**
     * 请求类型PUT
     */
    public static final int REQ_TYPE_PUT = 2;

    private static final String TAG = MyConnection.class.getSimpleName();

    /**
     * post请求参数
     */
    private HashMap<String, String> params;

    private ArrayList<Header> headers;
    /**
     * 代理信息
     */
    private ProxyInfo proxyInfo = null;
    /**
     * httpClient
     */
    private HttpClient httpClient;

    private boolean isGetStreamResult = false;
    private HttpUriRequest curRequest;
    private IResponseParser responseParser;

    //代码仅供交流学习，请勿盗作他用，转载请注明出处。创造良好的共同学习环境，大家才会有动力去分享。
    public Object request(Context context, int requestType, String url) throws RequestException {
        Object result = null;
        httpClient = ConnectionUtil.getHttpClient();
        try {
            if (requestType == REQ_TYPE_GET) {
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader(new BasicHeader("Accept-Charset", HTTP.UTF_8));
                httpGet.addHeader(new BasicHeader("Connection", "close"));
                curRequest = httpGet;
                addHeaders(httpGet);
                result = processRequest(context, httpGet);
            } else if (requestType == REQ_TYPE_PUT) {
                HttpPut httpPut = new HttpPut(url);
                httpPut.addHeader(new BasicHeader("Content-Type",
                        "application/json"));
                httpPut.addHeader(new BasicHeader("Accept-Charset", HTTP.UTF_8));
                httpPut.addHeader(new BasicHeader("Connection", "close"));
                curRequest = httpPut;
                addHeaders(httpPut);
                try {
                    if (params != null) {
                        StringEntity entity = new StringEntity(new JSONObject(
                                params).toString(), HTTP.UTF_8);
                        httpPut.setEntity(entity);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result = processRequest(context, httpPut);
            } else {
                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader(new BasicHeader("Content-Type",
                        "application/json"));
                httpPost.addHeader(new BasicHeader("Accept-Charset", HTTP.UTF_8));
                httpPost.addHeader(new BasicHeader("Connection", "close"));
                curRequest = httpPost;
                addHeaders(httpPost);
                try {
                    if (params != null) {
                        String pEntity = new JSONObject(params).toString();
                        pEntity = URLEncoder.encode(pEntity, HTTP.UTF_8);
                        StringEntity entity = new StringEntity(pEntity, HTTP.UTF_8);
//                        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
//                        parameters.add(new BasicNameValuePair("params",
//                            new JSONObject(params).toString()));
//                        UrlEncodedFormEntity ueEntity = new UrlEncodedFormEntity(
//                            parameters, HTTP.UTF_8);
                        httpPost.setEntity(entity);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result = processRequest(context, httpPost);
            }
        } catch (ClientProtocolException e) {
            throw new RequestException(e.getClass().toString() + ":" + e.getMessage());
        } catch (IOException e) {
            throw new RequestException(e.getClass().toString() + ":" + e.getMessage());
        } finally {
            shutdown(httpClient);
        }

        return result;
    }


    private Object processRequest(Context context, HttpUriRequest request)
            throws ClientProtocolException, IOException, RequestException {
        proxyInfo = ConnectionUtil.getProxyConfig(context);
        // 设置代理信息
        if (proxyInfo != null && !TextUtils.isEmpty(proxyInfo.getProxyHost())) {
            HttpHost proxy = new HttpHost(proxyInfo.getProxyHost(),
                    proxyInfo.getProxyPort());
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                    proxy);
        }
        HttpResponse response = httpClient.execute(request);
        //如果外部定义了响应解析器，那么使用此解析�?
        if (responseParser != null) {
            return responseParser.parseResponse(response);
        }
        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == HttpStatus.SC_OK) {
            if (isGetStreamResult) {
                return response.getEntity().getContent();
            } else {
                String strResult = EntityUtils.toString(
                        response.getEntity(), HTTP.UTF_8);
                return strResult;
            }
        } else {
            RequestException he = new RequestException(responseCode,
                    "invoke server interface faild " + "[" + responseCode + "]");
            throw he;
        }
    }

    /**
     * 关闭网络连接
     *
     * @param httpClient http网络连接
     */
    private void shutdown(HttpClient httpClient) {
        if (null != httpClient) {
            httpClient.getConnectionManager().shutdown();
        }
    }


    public HashMap<String, String> getParams() {
        return params;
    }


    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }


    public ArrayList<Header> getHeaders() {
        return headers;
    }


    public boolean isGetStreamResult() {
        return isGetStreamResult;
    }


    public void setGetStreamResult(boolean isGetStreamResult) {
        this.isGetStreamResult = isGetStreamResult;
    }


    public void setHeaders(ArrayList<Header> headers) {
        this.headers = headers;
    }

    private void addHeaders(HttpUriRequest request) {
        if (headers != null && headers.size() > 0) {
            for (int i = 0; i < headers.size(); i++) {
                request.addHeader(headers.get(i));
            }
        }
    }

    /**
     * 取消本次请求
     */
    public void cancleRequest() {
        if (curRequest != null && !curRequest.isAborted()) {
            curRequest.abort();
        }
    }


    public IResponseParser getResponseParser() {
        return responseParser;
    }


    public void setResponseParser(IResponseParser responseParser) {
        this.responseParser = responseParser;
    }

}
