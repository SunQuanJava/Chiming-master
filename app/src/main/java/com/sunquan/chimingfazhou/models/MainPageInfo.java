package com.sunquan.chimingfazhou.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 一级页面实体类
 *
 * Created by Administrator on 2015/5/4.
 */
public class MainPageInfo implements Serializable{


    private static final long serialVersionUID = -7768350234360910181L;

    private ArrayList<MainPageHeaderInfo> header;

    private ArrayList<MainPageBodyInfo> body;

    public ArrayList<MainPageHeaderInfo> getHeader() {
        return header;
    }

    public void setHeader(ArrayList<MainPageHeaderInfo> header) {
        this.header = header;
    }

    public ArrayList<MainPageBodyInfo> getBody() {
        return body;
    }

    public void setBody(ArrayList<MainPageBodyInfo> body) {
        this.body = body;
    }
}
