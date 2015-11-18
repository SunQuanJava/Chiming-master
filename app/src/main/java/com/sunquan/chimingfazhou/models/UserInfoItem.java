package com.sunquan.chimingfazhou.models;

/**
 * Created by sunquan1 on 2014/12/29.
 */
public class UserInfoItem {

    public static final int PLACE_HOLDER = 0;
    public static final int NORMAL = 1;
    public static final int PHOTO = 2;

    private String key;
    private String value;
    private boolean isEditable;
    private boolean isCheckable;
    /** 类型：占位，普通，头像 默认为普通*/
    private int type = NORMAL;

    public UserInfoItem(String key, String value,int type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public UserInfoItem(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public boolean isCheckable() {
        return isCheckable;
    }

    public void setCheckable(boolean isCheckable) {
        this.isCheckable = isCheckable;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
