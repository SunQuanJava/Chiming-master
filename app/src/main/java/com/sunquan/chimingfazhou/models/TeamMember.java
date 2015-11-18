package com.sunquan.chimingfazhou.models;

/**
 * 开发团队bean
 *
 * Created by Administrator on 2015/6/17.
 */
public class TeamMember {
    private String photo;
    private String name;
    private String job;

    public TeamMember() {
    }

    public TeamMember(String photo, String name, String job) {
        this.photo = photo;
        this.name = name;
        this.job = job;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
