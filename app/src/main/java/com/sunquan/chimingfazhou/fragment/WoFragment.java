package com.sunquan.chimingfazhou.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.baizhi.baseapp.controller.BaseHandler;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.event.BackEvent;
import com.sunquan.chimingfazhou.event.TabChangeEvent;
import com.sunquan.chimingfazhou.event.UserInfoModifyEvent;
import com.sunquan.chimingfazhou.util.IntentUtils;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;

/**
 * 我fragment
 * <p/>
 * Created by Administrator on 2015/4/25.
 */
public class WoFragment extends AppBaseFragment implements View.OnClickListener {

    private static final long INTERVAL = 2000L;

    /** 用户昵称 */
    private TextView mUser_nickname;

    /** 用户姓名 */
    private TextView mUser_name;

    /** 用户头像 */
    private ImageView mPhoto;

    private MyHandler mHandler;

    private boolean isPhotoLoaded;

    private ImageLoadingListener mImageLoadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
            isPhotoLoaded = false;
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            isPhotoLoaded = true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        mHandler = new MyHandler(this);
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        setLeftContentIcon(R.drawable.back_icon_selector);
        setCenterText(GlobalDataHolder.getInstance(getActivity()).getUserInfo().getFarmington());
        final View view = inflater.inflate(R.layout.fragment_wo_layout, container, false);
        view.findViewById(R.id.logout_btn).setOnClickListener(this);
        view.findViewById(R.id.user_info).setOnClickListener(this);
        view.findViewById(R.id.user_feedback).setOnClickListener(this);
        view.findViewById(R.id.user_update_password).setOnClickListener(this);
        view.findViewById(R.id.jingang_friend).setOnClickListener(this);
        view.findViewById(R.id.app_team).setOnClickListener(this);

        mUser_nickname = (TextView) view.findViewById(R.id.wo_user_nickname);
        mUser_name = (TextView) view.findViewById(R.id.wo_user_name);
        mPhoto = (ImageView) view.findViewById(R.id.wo_user_picture);
        mUser_name.setText(GlobalDataHolder.getInstance(getActivity()).getUserInfo().getFarmington());
        mUser_nickname.setText(GlobalDataHolder.getInstance(getActivity()).getUserInfo().getNickname());

        renderPhoto();

        return view;
    }

    private void renderPhoto() {
        final String photoUrl = GlobalDataHolder.getInstance(getActivity()).getUserInfo().getPhoto();
        if (!TextUtils.isEmpty(photoUrl)) {
            NetController.newInstance(getActivity()).renderImage(mPhoto, photoUrl, R.drawable.wo_user_picture,mImageLoadingListener);
        } else {
            mPhoto.setImageResource(R.drawable.wo_user_picture);
        }
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) EventBus.getDefault().post(new BackEvent(WoFragment.class));
    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    /**
     * 退出登录
     */
    private void logout() {
        showProgress(getString(R.string.login_out_ing));
        new Thread(new Runnable() {
            @Override
            public void run() {
                //退出登录
                NetController.newInstance(getActivity()).logout();
                mHandler.sendEmptyMessageDelayed(0, INTERVAL);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_btn://退出登录
                showLogoutPromptDialog();
                break;

            case R.id.user_info://用户信息
                goToUserCenterPage();
                break;

            case R.id.user_feedback://用户反馈
                doFeedBack();
                break;

            case R.id.user_update_password://修改密码
                IntentUtils.goToSetPasswordActivity(getActivity());
                break;

            case R.id.jingang_friend://金刚道友
                IntentUtils.goJingangFriendPage(getActivity());
                break;

            case R.id.app_team://开发团队
                IntentUtils.goTeamPage(getActivity());
                break;
            default:
                break;
        }
    }

    private void goToUserCenterPage() {
        IntentUtils.goToUserCenterPage(getActivity(), IntentUtils.ACTION_USER_CENTER_WO);
    }

    /**
     * 退出登录警告窗
     */
    private void showLogoutPromptDialog() {
        SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager())
                .setMessage("是否确认退出？")
                .setTitle("退出登录")
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    public void onPositiveButtonClicked(int requestCode) {
                        logout();
                    }
                }).setTag("是否确认退出？").showSinglton();
    }

    /**
     * 用户反馈
     */
    private void doFeedBack() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:1213244340@qq.com"));
        //接收人地址列表
        data.putExtra(Intent.EXTRA_EMAIL, new String[]{"641071223@qq.com", "641071223@qq.com"});
        //抄送人地址列表
        data.putExtra(Intent.EXTRA_CC, new String[]{"641071223@qq.com"});
        //密送人地址列表
        data.putExtra(Intent.EXTRA_BCC, new String[]{"641071223@qq.com"});
        data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
        data.putExtra(Intent.EXTRA_TEXT, "这是内容");
        startActivity(data);
    }

    /**
     * 用户信息修改
     * @param event
     */
    @SuppressWarnings("unused")
    public void onEvent(UserInfoModifyEvent event) {
        //更新用户头像
        if (event.isPhotoModified()) {
            renderPhoto();
        }
        //更新法名
        if (event.isFarmintonModified()) {
            mUser_name.setText(GlobalDataHolder.getInstance(getActivity()).getUserInfo().getFarmington());
            setCenterText(GlobalDataHolder.getInstance(getActivity()).getUserInfo().getFarmington());
        }
        //更新姓名
        if (event.isNicknameModified()) {
            mUser_nickname.setText(GlobalDataHolder.getInstance(getActivity()).getUserInfo().getNickname());
        }
    }

    public void onEvent(TabChangeEvent event) {
        final String className = event.getClassName();
        if (className.equals(((Object) this).getClass().getSimpleName()) && !isPhotoLoaded) {
            renderPhoto();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    static class MyHandler extends BaseHandler {
        private WeakReference<WoFragment> reference;

        public MyHandler(WoFragment woFragment){
            super(Looper.getMainLooper());
            reference = new WeakReference<>(woFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final WoFragment woFragment= reference.get();
            woFragment.dismessProgress();
            //关闭音乐播放器
            ((MyApplication)woFragment.getActivity().getApplication()).getService().stopMusic();
            //跳转到登陆页
            IntentUtils.goToLoginPage(woFragment.getActivity());
        }

    }
}
