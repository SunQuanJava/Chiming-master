package com.sunquan.chimingfazhou.models;

import com.sunquan.chimingfazhou.R;

import java.io.Serializable;

/**
 * 修一级记录页
 *
 * Created by Administrator on 2015/5/12.
 */
public class XiuInfo implements Serializable {
    private static final long serialVersionUID = 3029154212861449441L;

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_MODIFIABLE = 1;
    private String task_name;
    private String task_id;
    private String uid;
    private String create_time;
    /** 默认是可修改的 */
    private int showType = TYPE_MODIFIABLE;

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

}
