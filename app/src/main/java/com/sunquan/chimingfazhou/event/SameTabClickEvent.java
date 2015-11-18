package com.sunquan.chimingfazhou.event;

/**
 * 切换底部tab的event
 *
 * Created by Administrator on 2015/5/14.
 */
public class SameTabClickEvent {

    private String mClassName;
    private Object mExt;

    public SameTabClickEvent(String className) {
        this.mClassName = className;
    }

    public SameTabClickEvent(String className, Object ext) {
        this.mClassName = className;
        this.mExt = ext;
    }

    public Object getExt() {
        return mExt;
    }

    public String getClassName() {
        return mClassName;
    }
}
