package com.sunquan.chimingfazhou.controller;

import android.content.Context;
import android.text.TextUtils;

import com.baizhi.baseapp.util.IOCUtil;
import com.baizhi.baseapp.util.PreferencesUtil;
import com.baizhi.baseapp.util.UUIDGenerator;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.database.XiuSubTable;
import com.sunquan.chimingfazhou.database.XiuTable;
import com.sunquan.chimingfazhou.models.CommonConfigBean;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.models.XiuInfo;
import com.sunquan.chimingfazhou.models.XiuSubInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 共有数据封装(1，功课记录数据,2，用户个人信息数据)
 * <p/>
 * Created by Administrator on 2015/5/13.
 */
public class GlobalDataHolder {


    private static GlobalDataHolder instance;

    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_UID = "uid";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FARMINGTON = "farmington";
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_CITY = "city";

    private ArrayList<XiuInfo> mXiuInfo;
    private ArrayList<XiuInfo> mTempXiuInfo;
    private XiuSubInfoStoreMap mXiuSubInfosMap;
    private XiuSubInfoStoreMap mTempXiuSubInfosMap;
    private String uid = "001";
    private UserInfo mUserInfo;
    private Context mContext;
    private MainPageBodyInfo currentWenPageBodyInfo;
    private MainPageBodyInfo currentSiPageBodyInfo;
    private CommonConfigBean commonConfigBean;

    private GlobalDataHolder(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized static GlobalDataHolder getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalDataHolder(context);
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void setXiuInfo(ArrayList<XiuInfo> xiuInfo) {
        if (this.mXiuInfo != null) {
            this.mXiuInfo.clear();
            this.mXiuInfo = null;
        }
        if (this.mTempXiuInfo != null) {
            this.mTempXiuInfo.clear();
            this.mTempXiuInfo = null;
        }
        this.mXiuInfo = xiuInfo;
        this.mTempXiuInfo = (ArrayList<XiuInfo>) xiuInfo.clone();
    }

    public ArrayList<XiuInfo> getXiuInfo() {
        return mXiuInfo;
    }

    public void setXiuSubInfosMap(XiuSubInfoStoreMap mXiuSubInfosMap) {
        if (this.mXiuSubInfosMap != null) {
            this.mXiuSubInfosMap.clear();
            this.mXiuSubInfosMap = null;
        }
        if (this.mTempXiuSubInfosMap != null) {
            mTempXiuSubInfosMap.clear();
            mTempXiuSubInfosMap = null;
        }
        this.mXiuSubInfosMap = mXiuSubInfosMap;
        mTempXiuSubInfosMap = mXiuSubInfosMap.clone();
    }

    public ArrayList<XiuSubInfo> getXiuSubInfosByXiuInfo(XiuInfo xiuInfo) {
        ArrayList<XiuSubInfo> xiuSubInfos = mXiuSubInfosMap.get(xiuInfo.getTask_id());
        if (xiuSubInfos == null) {
            xiuSubInfos = new ArrayList<>();
            mXiuSubInfosMap.put(xiuInfo.getTask_id(), xiuSubInfos);
        }
        return xiuSubInfos;
    }

    public void removeXiuSubInfosByXiuInfo(XiuInfo xiuInfo) {
        if (mXiuSubInfosMap.containsKey(xiuInfo.getTask_id())) {
            mXiuSubInfosMap.remove(xiuInfo.getTask_id());
        }
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<XiuSubInfo> getAllXiuSubInfos() {
        final ArrayList<XiuSubInfo> xiuSubInfos = new ArrayList<>();
        for (Map.Entry<String, ArrayList<XiuSubInfo>> entry : mXiuSubInfosMap.entrySet()) {
            xiuSubInfos.addAll(entry.getValue());
        }
        return xiuSubInfos;
    }

    public boolean isXiuInfoDataChanged() {
        return mXiuInfo != null && !mXiuInfo.equals(mTempXiuInfo);
    }

    public boolean isXiuSubInfoDataChanged() {
        return mXiuSubInfosMap != null && !mXiuSubInfosMap.equals(mTempXiuSubInfosMap);
    }

    public MainPageBodyInfo getCurrentWenPageBodyInfo() {
        return currentWenPageBodyInfo;
    }

    public void setCurrentWenPageBodyInfo(MainPageBodyInfo currentPageBodyInfo) {
        this.currentWenPageBodyInfo = currentPageBodyInfo;
    }

    public MainPageBodyInfo getCurrentSiPageBodyInfo() {
        return currentSiPageBodyInfo;
    }

    public void setCurrentSiPageBodyInfo(MainPageBodyInfo currentSiPageBodyInfo) {
        this.currentSiPageBodyInfo = currentSiPageBodyInfo;
    }

    public CommonConfigBean getCommonConfigBean() {
        if(commonConfigBean == null) {
            commonConfigBean = IOCUtil.getDataFromAssert(mContext, "commonConfig_json", CommonConfigBean.class);
        }
        return commonConfigBean;
    }

    /**
     * 设置用户信息
     *
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        if (userInfo == null) {
            PreferencesUtil.setStringByName(mContext, KEY_PASSWORD, "");
            PreferencesUtil.setStringByName(mContext, KEY_NICKNAME, "");
            PreferencesUtil.setStringByName(mContext, KEY_GENDER, "m");
            PreferencesUtil.setStringByName(mContext, KEY_DESCRIPTION, "");
            PreferencesUtil.setStringByName(mContext, KEY_LOCATION, "");
            PreferencesUtil.setStringByName(mContext, KEY_PHOTO, "");
            PreferencesUtil.setStringByName(mContext, KEY_UID, "");
            PreferencesUtil.setStringByName(mContext, KEY_PHONE, "");
            PreferencesUtil.setStringByName(mContext, KEY_FARMINGTON, "");
            PreferencesUtil.setStringByName(mContext, KEY_PROVINCE, "");
            PreferencesUtil.setStringByName(mContext, KEY_CITY, "");
            this.uid = null;
        } else {
            PreferencesUtil.setStringByName(mContext, KEY_PASSWORD, userInfo.getPassword());
            PreferencesUtil.setStringByName(mContext, KEY_NICKNAME, userInfo.getNickname());
            PreferencesUtil.setStringByName(mContext, KEY_GENDER, userInfo.getGender());
            PreferencesUtil.setStringByName(mContext, KEY_DESCRIPTION, userInfo.getDescription());
            PreferencesUtil.setStringByName(mContext, KEY_LOCATION, userInfo.getLocation());
            PreferencesUtil.setStringByName(mContext, KEY_PHOTO, userInfo.getPhoto());
            PreferencesUtil.setStringByName(mContext, KEY_UID, userInfo.getUid());
            PreferencesUtil.setStringByName(mContext, KEY_PHONE, userInfo.getPhone());
            PreferencesUtil.setStringByName(mContext, KEY_FARMINGTON, userInfo.getFarmington());
            PreferencesUtil.setStringByName(mContext, KEY_PROVINCE, userInfo.getProvince());
            PreferencesUtil.setStringByName(mContext, KEY_CITY, userInfo.getCity());
            this.uid = userInfo.getUid();
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        //如果用户信息已经存在，直接返回
        if (mUserInfo != null) {
            return mUserInfo;
        }
        //没有登录的话直接返回空
        if (!isLogin()) {
            mUserInfo = null;
            return null;
        }
        //已经登录过，则初始化用户信息
        final String nickName = PreferencesUtil.getStringByName(mContext, KEY_NICKNAME, "");
        final String gender = PreferencesUtil.getStringByName(mContext, KEY_GENDER, "m");
        final String description = PreferencesUtil.getStringByName(mContext, KEY_DESCRIPTION, "");
        final String location = PreferencesUtil.getStringByName(mContext, KEY_LOCATION, "");
        final String phone = PreferencesUtil.getStringByName(mContext, KEY_PHONE, "");
        final String photo = PreferencesUtil.getStringByName(mContext, KEY_PHOTO, "");
        final String uid = PreferencesUtil.getStringByName(mContext, KEY_UID, "");
        final String password = PreferencesUtil.getStringByName(mContext, KEY_PASSWORD, "");
        final String farmington = PreferencesUtil.getStringByName(mContext,KEY_FARMINGTON,"");
        final String province = PreferencesUtil.getStringByName(mContext,KEY_PROVINCE,"");
        final String city = PreferencesUtil.getStringByName(mContext,KEY_CITY,"");

        final UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickName);
        userInfo.setGender(gender);
        userInfo.setDescription(description);
        userInfo.setLocation(location);
        userInfo.setPhoto(photo);
        userInfo.setUid(uid);
        userInfo.setPhone(phone);
        userInfo.setPassword(password);
        userInfo.setFarmington(farmington);
        userInfo.setProvince(province);
        userInfo.setCity(city);
        this.mUserInfo = userInfo;
        this.uid = uid;

        return mUserInfo;
    }

    /**
     * 保存功课记录数据到数据库 （使用场景：1，已经登录的用户将应用置于后台，2，用户退出登录）
     */
    public synchronized void saveXiuDatasToDB() {
        if (TextUtils.isEmpty(this.uid) || this.mUserInfo == null) {
            return;
        }
        //保存到数据库
        if (GlobalDataHolder.getInstance(mContext).isXiuInfoDataChanged())
            XiuTable.getInstance().insertXiuInfosWithDeleteFirst(GlobalDataHolder.getInstance(mContext).getXiuInfo());
        if (GlobalDataHolder.getInstance(mContext).isXiuSubInfoDataChanged())
            XiuSubTable.getInstance().insertXiuInfosWithDeleteFirst(GlobalDataHolder.getInstance(mContext).getAllXiuSubInfos());
    }

    /**
     * 从数据库中初始化功课记录数据 使用场景（1，用户登录之后 2，已经登录的用户首次启动应用）
     */
    public synchronized void initXiuDatasFromDB() {
        if (TextUtils.isEmpty(this.uid) || this.mUserInfo == null) {
            throw new IllegalStateException("The account is not initialized");
        }
        ArrayList<XiuInfo> xiuInfos = XiuTable.getInstance().queryAllXiuInfos();
        if (xiuInfos == null || xiuInfos.isEmpty()) {
            final XiuInfo xiuInfo1 = new XiuInfo();
            xiuInfo1.setCreate_time(String.valueOf(System.currentTimeMillis()));
            xiuInfo1.setTask_id(UUIDGenerator.getUUID());
            xiuInfo1.setTask_name(mContext.getString(R.string.chizhousongjing));
            xiuInfo1.setShowType(XiuInfo.TYPE_DEFAULT);
            xiuInfo1.setUid(GlobalDataHolder.getInstance(mContext).getUid());

            final XiuInfo xiuInfo2 = new XiuInfo();
            xiuInfo2.setCreate_time(String.valueOf(System.currentTimeMillis()));
            xiuInfo2.setTask_id(UUIDGenerator.getUUID());
            xiuInfo2.setTask_name(mContext.getString(R.string.chengxinlifo));
            xiuInfo2.setShowType(XiuInfo.TYPE_DEFAULT);
            xiuInfo2.setUid(GlobalDataHolder.getInstance(mContext).getUid());

            xiuInfos = new ArrayList<>();
            xiuInfos.add(xiuInfo1);
            xiuInfos.add(xiuInfo2);
            XiuTable.getInstance().insertXiuInfos(xiuInfos);
        }

        ArrayList<XiuSubInfo> xiuSubInfos = XiuSubTable.getInstance().queryAllXiuSubInfos();
        if (xiuSubInfos == null) {
            xiuSubInfos = new ArrayList<>();
        }
        final GlobalDataHolder.XiuSubInfoStoreMap map = new GlobalDataHolder.XiuSubInfoStoreMap();
        if (!xiuInfos.isEmpty() && !xiuSubInfos.isEmpty())
            for (XiuSubInfo xiuSubInfo : xiuSubInfos) {
                ArrayList<XiuSubInfo> xiuSubValue = map.get(xiuSubInfo.getParent_task_id());
                if (xiuSubValue == null) {
                    xiuSubValue = new ArrayList<>();
                    map.put(xiuSubInfo.getParent_task_id(), xiuSubValue);
                }
                xiuSubValue.add(xiuSubInfo);
            }
        this.setXiuInfo(xiuInfos);
        this.setXiuSubInfosMap(map);
    }


    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return !TextUtils.isEmpty(PreferencesUtil.getStringByName(mContext, KEY_UID, ""));
    }

    /**
     * 自定义HashMap，重写clone函数
     */
    public static class XiuSubInfoStoreMap extends HashMap<String, ArrayList<XiuSubInfo>> {

        private static final long serialVersionUID = 3710627064450728852L;


        @SuppressWarnings("unchecked")
        @Override
        public XiuSubInfoStoreMap clone() {
            XiuSubInfoStoreMap target = new XiuSubInfoStoreMap();
            for (String key : this.keySet()) {
                target.put(key, (ArrayList<XiuSubInfo>) this.get(key).clone());
            }
            return target;
        }
    }


}
