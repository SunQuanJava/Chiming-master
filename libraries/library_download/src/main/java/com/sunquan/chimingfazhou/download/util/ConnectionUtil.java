package com.sunquan.chimingfazhou.download.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.sunquan.chimingfazhou.download.module.ProxyInfo;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;


/**
 * 作 者: sunquan
 * 文件描述:联网工具类
 */
public class ConnectionUtil {
    /**
     * 字符编码格式
     */
    public static final String CHARSET_ENCODING = "UTF-8";

    /**
     * 联网请求超时时间
     */
    private static int CONN_TIME_OUT = 20000;

    /**
     * 等待响应数据超时时间
     */
    private static int SOCKET_TIME_OUT = 20000;

    /**
     * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
     */
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        // 自定义的恢复策略
        @Override
        public boolean retryRequest(IOException exception, int executionCount,
                                    HttpContext context) {
            // 设置恢复策略，在发生异常时候将自动重试3次
            if (executionCount >= 3) {
                // 如果连接次数超过了最大值则停止重试
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // 如果服务器连接失败重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                // 不要重试ssl连接异常
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                // 重试，如果请求是考虑幂等
                return true;
            }
            return false;
        }
    };


    /**
     * 获取DefaultHttpClient对象
     *
     * @return DefaultHttpClient对象
     */
    public static DefaultHttpClient getHttpClient() {
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http",
                PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https",
                SSLSocketFactory.getSocketFactory(), 443));
        // 使用线程安全的连接管理来创建HttpClient
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                getHttpParams(), schReg);

        DefaultHttpClient httpclient = new DefaultHttpClient(conMgr,
                getHttpParams());
        // 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题
        httpclient.getParams().setParameter(
                CoreProtocolPNames.USER_AGENT,
                "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                        + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        httpclient.getParams().setParameter(
                CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
        httpclient.getParams().setParameter(
                CoreProtocolPNames.HTTP_CONTENT_CHARSET, CHARSET_ENCODING);

        // 浏览器兼容性
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
                CookiePolicy.BROWSER_COMPATIBILITY);
        // 定义重试策略
        httpclient.setHttpRequestRetryHandler(requestRetryHandler);

        return httpclient;
    }


    private static HttpParams getHttpParams() {

        HttpParams htpp = new BasicHttpParams();
        HttpProtocolParams.setVersion(htpp, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(htpp, CHARSET_ENCODING);
        HttpProtocolParams.setUseExpectContinue(htpp, true);
        HttpProtocolParams.setUserAgent(
                htpp,
                "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                        + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        ConnManagerParams.setTimeout(htpp, 2000);
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIME_OUT);//
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIME_OUT);//
        return htpp;
    }


    /**
     * 根据apn获取代理信息
     *
     * @param context 上下文对象
     * @return ProxyInfo 代理信息对象
     */
    public static ProxyInfo getProxyConfig(Context context) {
        if (context == null) {
            return null;
        }
        String proxyHost = null;
        int proxyPort = -1;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        // 如果网络环境不是WIFI
        if (!(info != null && info.getType() == ConnectivityManager.TYPE_WIFI)) {
            String host = android.net.Proxy.getHost(context);
            if (!TextUtils.isEmpty(host)) {
                proxyHost = host;
                proxyPort = android.net.Proxy.getPort(context);
            }
        }
        if (!TextUtils.isEmpty(proxyHost) && proxyPort >= 0) {
            ProxyInfo proxyInfo = new ProxyInfo();
            proxyInfo.setProxyHost(proxyHost);
            proxyInfo.setProxyPort(proxyPort);
            return proxyInfo;
        }
        return null;
    }


    /**
     * 设置超时参数
     *
     * @param connectTimeOut 连网超时时间
     * @param socketTimeOut  请求超时时间
     */
    public static void setTimeOutParams(int connectTimeOut, int socketTimeOut) {
        CONN_TIME_OUT = connectTimeOut;
        SOCKET_TIME_OUT = socketTimeOut;
    }
}
