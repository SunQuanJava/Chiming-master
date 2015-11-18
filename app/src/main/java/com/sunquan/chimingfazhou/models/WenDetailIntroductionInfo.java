package com.sunquan.chimingfazhou.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/11.
 */
public class WenDetailIntroductionInfo implements Serializable {
    private static final long serialVersionUID = -4691061286150797982L;

    /**
     * 图片url
     */
    private String thumbnail;
    /**
     * 章节名
     */
    private String title;
    /**
     * 分数
     */
    private String score;
    /**
     * 作者
     */
    private String author;
    /**
     * 播音
     */
    private String broadcast;
    /**
     * 集数
     */
    private String set_count;
    /**
     * 简介
     */
    private String brief;
    /**
     * 发布日期
     */
    private String create_date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public String getSet_count() {
        return set_count;
    }

    public void setSet_count(String set_count) {
        this.set_count = set_count;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
