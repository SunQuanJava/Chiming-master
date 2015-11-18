package com.sunquan.chimingfazhou.models;

import java.io.Serializable;

/**
 * 思的详情实体类
 * <p/>
 * Created by Administrator on 2015/5/19.
 */
public class SiDetailInfo implements Serializable {
    private static final long serialVersionUID = 1789793769051417225L;

    /**
     * 跳转的H5地址
     */
    private String link;

    /**
     * 文章id
     */
    private String id;

    /**
     * 扩展字段,可以为空
     */
    private String ext;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
