package com.sunquan.chimingfazhou.models;

import java.util.ArrayList;

/**
 * 公共配置
 *
 * Created by Administrator on 2015/6/30.
 */
public class CommonConfigBean {


    private ArrayList<TeamMember> teams;

    private String welcome_photo;

    public ArrayList<TeamMember> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<TeamMember> teams) {
        this.teams = teams;
    }

    public String getWelcome_photo() {
        return welcome_photo;
    }

    public void setWelcome_photo(String welcome_photo) {
        this.welcome_photo = welcome_photo;
    }
}
