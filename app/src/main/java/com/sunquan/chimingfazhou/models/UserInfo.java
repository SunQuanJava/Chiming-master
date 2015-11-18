package com.sunquan.chimingfazhou.models;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 用户信息
 * <p/>
 * Created by Administrator on 2015/6/8.
 */
public class UserInfo implements Serializable,Cloneable{
    private static final long serialVersionUID = 271399481147239782L;

    /**
     * 用户id
     */
    private String uid;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别 （m：男，f：女）
     */
    private String gender;
    /**
     * 头像
     */
    private String photo;
    /**
     * 所在地
     */
    private String location;
    /**
     * 个人签名
     */
    private String description;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
    private String password;

    private String errmsg;

    /**
     * 法名
     */
    private String farmington;

    private String province;

    private String city;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return "f".equals(gender)?"女":"男";
    }

    public String getGenderForHttp() {
        return TextUtils.isEmpty(gender)?"m":gender;
    }

    public void setGender(String gender) {
        switch (gender) {
            case "男":
                this.gender = "m";
                break;
            case "女":
                this.gender = "f";
                break;
            default:
                this.gender = gender;
                break;
        }
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getFarmington() {
        return farmington;
    }

    public void setFarmington(String farmington) {
        this.farmington = farmington;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        return !(description != null ?
                !description.equals(userInfo.description) : userInfo.description != null) && !(errmsg != null ?
                !errmsg.equals(userInfo.errmsg) : userInfo.errmsg != null) && !(farmington != null ?
                !farmington.equals(userInfo.farmington) : userInfo.farmington != null) && !(gender != null ?
                !gender.equals(userInfo.gender) : userInfo.gender != null) && !(location != null ?
                !location.equals(userInfo.location) : userInfo.location != null) && !(nickname != null ?
                !nickname.equals(userInfo.nickname) : userInfo.nickname != null) && !(password != null ?
                !password.equals(userInfo.password) : userInfo.password != null) && !(phone != null ?
                !phone.equals(userInfo.phone) : userInfo.phone != null) && !(photo != null ?
                !photo.equals(userInfo.photo) : userInfo.photo != null) && !(uid != null ?
                !uid.equals(userInfo.uid) : userInfo.uid != null);

    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (errmsg != null ? errmsg.hashCode() : 0);
        result = 31 * result + (farmington != null ? farmington.hashCode() : 0);
        return result;
    }

    @Override
    public UserInfo clone() {
        try {
            return (UserInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
