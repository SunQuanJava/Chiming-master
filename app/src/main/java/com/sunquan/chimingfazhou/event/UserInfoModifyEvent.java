package com.sunquan.chimingfazhou.event;

/**
 * 用户信息修改event
 * <p/>
 * Created by Administrator on 2015/6/16.
 */
public class UserInfoModifyEvent {

    public static final int TYPE_MODIFY_PHOTO = 0x1;
    public static final int TYPE_MODIFY_NICKNAME = 0x2;
    public static final int TYPE_MODIFY_FARMINTON = 0x3;
    private int modifyType;

    public UserInfoModifyEvent(int modifyType) {
        this.modifyType = modifyType;
    }

    /**
     * 是否修改头像
     *
     * @return
     */
    public boolean isPhotoModified() {
        return (modifyType & TYPE_MODIFY_PHOTO) == TYPE_MODIFY_PHOTO;
    }

    /**
     * 是否修改昵称
     *
     * @return
     */
    public boolean isNicknameModified() {
        return (modifyType & TYPE_MODIFY_NICKNAME) == TYPE_MODIFY_NICKNAME;
    }

    /**
     * 是否修改法名
     *
     * @return
     */
    public boolean isFarmintonModified() {
        return (modifyType & TYPE_MODIFY_FARMINTON) == TYPE_MODIFY_FARMINTON;
    }
}
