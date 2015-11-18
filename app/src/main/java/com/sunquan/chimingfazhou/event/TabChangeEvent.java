package com.sunquan.chimingfazhou.event;

/**
 * 切换底部tab的event
 *
 * Created by Administrator on 2015/5/14.
 */
public class TabChangeEvent {

    private String mClassName;
    private String mTag;

    public TabChangeEvent(String className) {
        this.mClassName = className;
    }

    public TabChangeEvent(String tag, String className) {
        this.mTag = tag;
        this.mClassName = className;
    }

    public String getTag() {
        return mTag;
    }

    public String getClassName() {
        return mClassName;
    }
}
