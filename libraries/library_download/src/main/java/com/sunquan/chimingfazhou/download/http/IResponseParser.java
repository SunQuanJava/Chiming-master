/**
 *
 */
package com.sunquan.chimingfazhou.download.http;

import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.util.TaskExecutor;

import org.apache.http.HttpResponse;


/**
 * @author sunquan
 *         用于解析服务端的响应数据
 */
public interface IResponseParser<T> {
    public Object parseResponse(HttpResponse response) throws RequestException;

    public TaskExecutor<T> getTaskRefer();
}
