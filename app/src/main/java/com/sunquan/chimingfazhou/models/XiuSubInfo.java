package com.sunquan.chimingfazhou.models;

import java.io.Serializable;

/**
 * 修二级记录页
 *
 * Created by Administrator on 2015/5/12.
 */
public class XiuSubInfo implements Serializable {
    private static final long serialVersionUID = 3029154212861449441L;

    private String uid;
    private String task_name;
    private String task_id;

    private String parent_task_id;
    private String parent_task_name;

    private String last_practice_time;
    private String count;

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

    public String getParent_task_id() {
        return parent_task_id;
    }

    public void setParent_task_id(String parent_task_id) {
        this.parent_task_id = parent_task_id;
    }

    public String getParent_task_name() {
        return parent_task_name;
    }

    public void setParent_task_name(String parent_task_name) {
        this.parent_task_name = parent_task_name;
    }

    public String getLast_practice_time() {
        return last_practice_time;
    }

    public void setLast_practice_time(String last_practice_time) {
        this.last_practice_time = last_practice_time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
