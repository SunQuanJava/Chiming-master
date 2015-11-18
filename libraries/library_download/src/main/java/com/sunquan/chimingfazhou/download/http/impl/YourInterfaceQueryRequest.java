package com.sunquan.chimingfazhou.download.http.impl;

import android.content.Context;

import com.sunquan.chimingfazhou.download.http.MyAbstractRequest;
import com.sunquan.chimingfazhou.download.util.Constants;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author sunquan
 *         <br>与服务端通信的非文件下载请求类
 */
public class YourInterfaceQueryRequest extends MyAbstractRequest<ArrayList<JSONObject>> {

    public YourInterfaceQueryRequest(Context context) {
        super(context);
        this.isTest = Constants.isTest;
    }

    @Override
    protected ArrayList<JSONObject> buildTestData() {

        return null;
    }

    @Override
    public int getRequestMethod() {
        // TODO Auto-generated method stub
        return HTTP_GET;
    }

    @Override
    public HashMap<String, String> getRequestParams() {
        return null;
    }

    @Override
    protected ArrayList<JSONObject> parseResult(Object result) throws Exception {
        ArrayList<JSONObject> infoList = new ArrayList<JSONObject>();
        try {
            JSONObject jo = new JSONObject((String) result);
            boolean state = jo.optBoolean("state");
            if (state) {
                //TODO 取服务端返回的数据，并转换为客户端对应的模型对象
//                infoList.add(null);
            } else {
                String msg = jo.optString("msg");
                //获取失败
                throw new HttpException(msg);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new HttpException("数据格式错误");
        }

        return infoList;
    }

}
