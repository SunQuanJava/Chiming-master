package com.sunquan.chimingfazhou.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/4.
 */
public class MainPageHeaderInfo implements Serializable {

    private static final long serialVersionUID = -8210416629288004579L;

    private String thumbnail;

    private String desc;

    private String id;

    public MainPageHeaderInfo() {
    }

    public MainPageHeaderInfo(String thumbnail, String desc) {
        this.thumbnail = thumbnail;
        this.desc = desc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object)this).getClass() != o.getClass()) return false;

        MainPageHeaderInfo that = (MainPageHeaderInfo) o;

        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (thumbnail != null ? !thumbnail.equals(that.thumbnail) : that.thumbnail != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = thumbnail != null ? thumbnail.hashCode() : 0;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
