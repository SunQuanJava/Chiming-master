package com.sunquan.chimingfazhou.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/11.
 */
public class WenDetailListItem implements Serializable {


    private static final long serialVersionUID = 6216406254672157932L;

    /**
     * 集数名称
     */
    private String title;

    /**
     * 下载地址
     */
    private String download_url;

    /**
     * 大小
     */
    private String size;

    /**
     * 音频时长
     */
    private String duration;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
