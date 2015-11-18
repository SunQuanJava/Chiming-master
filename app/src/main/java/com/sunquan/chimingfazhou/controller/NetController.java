package com.sunquan.chimingfazhou.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.baizhi.baseapp.controller.GsonTransformer;
import com.baizhi.baseapp.controller.LoadController;
import com.baizhi.baseapp.util.NetworkState;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.constants.Constants;
import com.sunquan.chimingfazhou.models.MainPageInfo;
import com.sunquan.chimingfazhou.models.SiDetailInfo;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.models.UserInfos;
import com.sunquan.chimingfazhou.models.WenDetailInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * 网络控制类
 * <p/>
 * Created by Administrator on 2015/4/25.
 */
public class NetController {

    private static NetController instance;
    private AQuery mAQuery;
    private Context mContext;

    private NetController(Context context) {
        mContext = context.getApplicationContext();
        mAQuery = MyApplication.getAQuery(mContext);
    }

    public static NetController newInstance(Context context) {
        if (instance == null) {
            instance = new NetController(context);
        }
        return instance;
    }


    /**
     * 从网络上获取信息数据
     * 优先从网络上获取数据，如果网络获取成功直接显示并缓存，否则从缓存中获取数据，如果存在缓存数据则直接显示，否则提示失败
     *
     * @param url
     * @param dataCallback
     */
    public void loadMainInfo(final String url, final LoadController.DataCallback<MainPageInfo> dataCallback, int type) {
        LoadController.getInstance(mContext).loadData(url, dataCallback, MainPageInfo.class, type);
    }

    /**
     * 获取会员列表
     *
     * @param url
     * @param dataCallback
     */
    public void getMembersData(final String url, final LoadController.DataCallback<UserInfos> dataCallback, final int type) {
        LoadController.getInstance(mContext).loadData(url, dataCallback, UserInfos.class, type);
    }

    /**
     * 加载思的详情页的数据
     *
     * @param url
     * @param dataCallback
     */
    public void loadSiDetailInfo(final String url, final LoadController.DataCallback<SiDetailInfo> dataCallback) {
        LoadController.getInstance(mContext).loadData(url, dataCallback, SiDetailInfo.class, LoadController.HTTP_FIRST);
    }

    /**
     * 加载闻的详情页数据
     *
     * @param url
     * @param dataCallback
     */
    public void loadWenDetailInfo(final String url, final LoadController.DataCallback<WenDetailInfo> dataCallback) {
        LoadController.getInstance(mContext).loadData(url, dataCallback, WenDetailInfo.class, LoadController.HTTP_FIRST);
    }

    /**
     * 通过密码登录
     *
     * @param phone
     * @param password
     * @param dataCallback
     */
    public void loginWithPassword(final String phone, final String password, final LoadController.DataCallback<UserInfo> dataCallback) {
        login(phone, password, null, dataCallback);
    }

    /**
     * 短信验证码登录
     *
     * @param phone
     * @param code
     * @param dataCallback
     */
    public void loginWithCode(final String phone, final String code, final LoadController.DataCallback<UserInfo> dataCallback) {
        login(phone, null, code, dataCallback);
    }

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param dataCallback
     */
    public void register(final String phone, final String password, final LoadController.DataCallback<UserInfo> dataCallback) {
        if (!NetworkState.isActiveNetworkConnected(mContext)) {
            dataCallback.fail();
        } else {
            final HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("password", password);
            final String url = Constants.HOST + Constants.REGIST_URL;
            mAQuery.transformer(new GsonTransformer()).ajax(url, map, UserInfo.class, new AjaxCallback<UserInfo>() {
                @Override
                public void callback(String url, UserInfo object, AjaxStatus status) {
                    //注册成功
                    if (status.getCode() == 200 && object != null && !TextUtils.isEmpty(object.getUid())) {
                        GlobalDataHolder.getInstance(mContext).setUserInfo(object);
                        GlobalDataHolder.getInstance(mContext).initXiuDatasFromDB();
                        dataCallback.success(object);
                    }
                    //注册失败
                    else {
                        if (object != null && !TextUtils.isEmpty(object.getErrmsg())) {
                            dataCallback.fail(object.getErrmsg());
                        } else {
                            dataCallback.fail();
                        }
                    }
                }
            });
        }
    }

    /**
     * 退出登录
     */
    public void logout() {
        //保存功课记录到数据库
        GlobalDataHolder.getInstance(mContext).saveXiuDatasToDB();
        //清空用户信息
        GlobalDataHolder.getInstance(mContext).setUserInfo(null);
    }

    /**
     * 修改密码
     *
     * @param password
     * @param dataCallback
     */
    public void modifyPassword(final String password, final LoadController.DataCallback<UserInfo> dataCallback) {
        if (!NetworkState.isActiveNetworkConnected(mContext)) {
            dataCallback.fail();
        } else {
            final HashMap<String, String> map = new HashMap<>();
            map.put("password", password);
            map.put("uid", GlobalDataHolder.getInstance(mContext).getUid());
            final String url = Constants.HOST + Constants.MODIFY_INFO_URL;
            mAQuery.transformer(new GsonTransformer()).ajax(url, map, UserInfo.class, new AjaxCallback<UserInfo>() {
                @Override
                public void callback(String url, UserInfo object, AjaxStatus status) {
                    //修改密码成功
                    if (status.getCode() == 200 && object != null && !TextUtils.isEmpty(object.getUid())) {
                        GlobalDataHolder.getInstance(mContext).setUserInfo(object);
                        dataCallback.success(object);
                    }
                    //修改密码失败
                    else {
                        dataCallback.fail();
                    }
                }
            });
        }
    }

    /**
     * 修改用户信息
     *
     * @param userInfo
     * @param dataCallback
     */
    public void modifyInfo(final UserInfo userInfo, final LoadController.DataCallback<UserInfo> dataCallback) {
        if (!NetworkState.isActiveNetworkConnected(mContext)) {
            dataCallback.fail();
        } else {
            final HashMap<String, String> map = new HashMap<>();
            map.put("uid", userInfo.getUid());
            map.put("gender", userInfo.getGenderForHttp());
            map.put("phone", userInfo.getPhone());
            map.put("nickname", userInfo.getNickname());
            map.put("description", userInfo.getDescription());
            map.put("province", userInfo.getProvince());
            map.put("city", userInfo.getCity());
            map.put("farmington", userInfo.getFarmington());
            map.put("location", userInfo.getLocation());
            final String url = Constants.HOST + Constants.MODIFY_INFO_URL;
            mAQuery.transformer(new GsonTransformer()).ajax(url, map, UserInfo.class, new AjaxCallback<UserInfo>() {
                @Override
                public void callback(String url, UserInfo object, AjaxStatus status) {
                    //修改用户信息成功
                    if (status.getCode() == 200 && object != null && !TextUtils.isEmpty(object.getUid())) {
                        GlobalDataHolder.getInstance(mContext).setUserInfo(object);
                        dataCallback.success(object);
                    }
                    //修改用户信息失败
                    else {
                        dataCallback.fail();
                    }
                }
            });
        }
    }

    /**
     * 上传用户头像
     *
     * @param file
     * @param dataCallback
     */
    public void uploadPhoto(final File file, final LoadController.DataCallback<UserInfo> dataCallback) {
        if (!NetworkState.isActiveNetworkConnected(mContext)) {
            dataCallback.fail();
        } else {
            final HashMap<String, Object> map = new HashMap<>();
            map.put("uid", GlobalDataHolder.getInstance(mContext).getUid());
            map.put("photo", file);
            final String url = Constants.HOST + Constants.MODIFY_PIC_URL;
            mAQuery.transformer(new GsonTransformer()).ajax(url, map, UserInfo.class, new AjaxCallback<UserInfo>() {
                @Override
                public void callback(String url, UserInfo object, AjaxStatus status) {
                    //上传用户头像成功
                    if (status.getCode() == 200 && object != null && !TextUtils.isEmpty(object.getUid())) {
                        GlobalDataHolder.getInstance(mContext).setUserInfo(object);
                        dataCallback.success(object);
                    }
                    //上传用户头像失败
                    else {
                        dataCallback.fail();
                    }
                }
            });
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     */
    public void sendIdentifyCode(final String phone) {
        final String url = Constants.HOST + Constants.IDENTIFY_OBTAIN_RUL + "?phone=" + phone;
        mAQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>());
    }

    /**
     * 短信验证码校验
     *
     * @param phone
     * @param code
     * @param dataCallback
     */
    public void checkIdentifyCode(final String phone, final String code, final LoadController.DataCallback<JSONObject> dataCallback) {
        if (!NetworkState.isActiveNetworkConnected(mContext)) {
            dataCallback.fail();
        } else {
            final String url = Constants.HOST + Constants.IDENTIFY_CHECK_URL + "?phone=" + phone + "&code=" + code;
            mAQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    try {
                        if (status.getCode() == 200 && object != null && "success".equals(object.getString("result"))) {
                            dataCallback.success(object);
                        } else {
                            dataCallback.fail();
                        }
                    } catch (JSONException e) {
                        dataCallback.fail();
                    }
                }
            });
        }
    }

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @param code
     * @param dataCallback
     */
    private void login(final String phone, final String password, final String code, final LoadController.DataCallback<UserInfo> dataCallback) {
        if (!NetworkState.isActiveNetworkConnected(mContext)) {
            dataCallback.fail();
        } else {
            final HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            if (!TextUtils.isEmpty(password)) {
                map.put("password", password);
            }
            if (!TextUtils.isEmpty(code)) {
                map.put("code", code);
            }
            final String url = Constants.HOST + Constants.LOGIN_URL;
            mAQuery.transformer(new GsonTransformer()).ajax(url, map, UserInfo.class, new AjaxCallback<UserInfo>() {
                @Override
                public void callback(String url, UserInfo object, AjaxStatus status) {
                    //登录成功
                    if (status.getCode() == 200 && object != null && !TextUtils.isEmpty(object.getUid())) {
                        GlobalDataHolder.getInstance(mContext).setUserInfo(object);
                        GlobalDataHolder.getInstance(mContext).initXiuDatasFromDB();
                        dataCallback.success(object);
                    }
                    //登录失败
                    else {
                        if (object != null && !TextUtils.isEmpty(object.getErrmsg())) {
                            dataCallback.fail(object.getErrmsg());
                        } else {
                            dataCallback.fail();
                        }
                    }
                }
            });
        }
    }

    /**
     * 渲染图片
     *
     * @param imageView
     * @param url
     */
    public void renderImage(final ImageView imageView, final String url, int resId,final ImageLoadingListener callback) {
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(resId)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resId)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .build();//构建完成
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    ImageLoader.getInstance().displayImage(url, imageView, options, callback);
                } else {
                    ImageLoader.getInstance().displayImage(url, imageView, options);
                }
            }
        });
    }


    /**
     * 渲染图片
     *
     * @param view
     * @param url
     */
    public void renderImage(final ImageView view, final String url, int resId) {
        renderImage(view, url, resId, null);

    }

    /**
     * 渲染图片
     *
     * @param view
     * @param url
     */
    public void renderImage(final ImageView view, final String url) {
        renderImage(view, url, R.drawable.avatar_default, null);
    }

}
