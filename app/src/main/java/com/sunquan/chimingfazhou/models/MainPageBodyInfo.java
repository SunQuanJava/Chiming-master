package com.sunquan.chimingfazhou.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/4.
 */
public class MainPageBodyInfo implements Serializable, Comparable<MainPageBodyInfo> {
    private static final long serialVersionUID = -1719713162694096747L;
    public static final String WEN = "0";
    public static final String SI = "1";
    public static final String PLASE_HOLDER = "-1";

    /** 类型 */
    private String type;

    /** 缩略图 */
    private String thumbnail;

    /** 标题 */
    private String title;

    /** 描述 */
    private String author;

    /** id */
    private String id;

    /** 集数 */
    private String set_count;

    /** 发布日期 */
    private String create_date;

    private int placeHolderResId;

    private ArrayList<MainPageBodyInfo> items;

    public ArrayList<MainPageBodyInfo> getItems() {
        return items;
    }

    public void setItems(ArrayList<MainPageBodyInfo> items) {
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public void setPlaceHolderResId(int placeHolderResId) {
        this.placeHolderResId = placeHolderResId;
    }

    public String getSet_count() {
        return set_count;
    }

    public void setSet_count(String set_count) {
        this.set_count = set_count;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    @Override
    public int compareTo(MainPageBodyInfo another) {
        if(this.getType().equals(MainPageBodyInfo.WEN)) {
            if(!another.getType().equals(MainPageBodyInfo.WEN)) {
                return -1;
            }else {
                return 0;
            }
        }
        else {
            if(another.getType().equals(MainPageBodyInfo.WEN)) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
