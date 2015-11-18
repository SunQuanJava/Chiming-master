package com.sunquan.chimingfazhou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.baizhi.baseapp.controller.LoadController;
import com.baizhi.widget.ActionSheet;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.adapter.UserInfoListAdapter;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.event.UserInfoModifyEvent;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.models.UserInfoItem;
import com.sunquan.chimingfazhou.util.AlbumAndCaptuHelper;
import com.sunquan.chimingfazhou.util.EditInfoUtil;
import com.sunquan.chimingfazhou.util.IntentUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 个人中心页，包括图片编辑，用户个人信息编辑等
 * <p>
 * Created by sunquan1 on 2015/1/6.
 */
public class UserCenterActivity extends AppBaseActivity implements IListDialogListener, IPositiveButtonDialogListener, AdapterView.OnItemClickListener {
    private List<UserInfoItem> mUserInfoItems;
    private UserInfoListAdapter mUserInfoListAdapter;
    private File mFile;
    private File mParentFile;
    private UserInfo mTempUserInfo;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.user_center);
        mAction = getIntent().getAction();
        //从注册到个人中心，跳转到主页
        if (IntentUtils.ACTION_USER_SET_PASSWORD.equals(mAction)) {
            setCenterText(getString(R.string.user_info_perfect));
            setRightText(getString(R.string.user_save), 0);
        }
        //从我页到个人中心，直接返回
        else {
            setCenterText(getString(R.string.user_info_title));
            setLeftContentIcon(R.drawable.back_icon_selector);
            setRightText(getString(R.string.user_save), 0);
        }
        initData();
        initView();
    }

    /**
     * 初始化数据信息
     */
    private void initData() {
        mParentFile = new File(Environment.getExternalStorageDirectory(), "cmfz_temp_camera");
        final UserInfo userInfo = GlobalDataHolder.getInstance(this).getUserInfo();
        mTempUserInfo = userInfo.clone();
        //完善用户信息
        mUserInfoItems = Arrays.asList(new UserInfoItem(UserInfoItem.PLACE_HOLDER),
                new UserInfoItem(getString(R.string.user_photo), mTempUserInfo.getPhoto(), UserInfoItem.PHOTO),
                new UserInfoItem(getString(R.string.user_farming), mTempUserInfo.getFarmington(), UserInfoItem.NORMAL),
                new UserInfoItem(getString(R.string.user_nickname), mTempUserInfo.getNickname(), UserInfoItem.NORMAL),
                new UserInfoItem(UserInfoItem.PLACE_HOLDER),
                new UserInfoItem(getString(R.string.user_gender), mTempUserInfo.getGender(), UserInfoItem.NORMAL),
                new UserInfoItem(getString(R.string.user_location), mTempUserInfo.getLocation(), UserInfoItem.NORMAL),
                new UserInfoItem(getString(R.string.user_descriptiom), mTempUserInfo.getDescription(), UserInfoItem.NORMAL));

    }

    private void initView() {
        final ListView mUserList = (ListView) findViewById(R.id.user_list);
        mUserList.setVerticalScrollBarEnabled(false);
        mUserList.setOnItemClickListener(this);
        mUserInfoListAdapter = new UserInfoListAdapter(this);
        mUserInfoListAdapter.setItems(mUserInfoItems);
        mUserInfoListAdapter.setCallback(new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                dismissProgress();
                showMessage(getString(R.string.user_photo_load_fail_message));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                notifyHead();
                dismissProgress();
                showMessage(getString(R.string.user_photo_load_success_message));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
        mUserList.setAdapter(mUserInfoListAdapter);

    }

    private boolean isModified() {
        return !mTempUserInfo.equals(GlobalDataHolder.getInstance(this).getUserInfo());
    }

    /**
     * 通知其他页面更新头像
     */
    private void notifyHead() {
        EventBus.getDefault().post(new UserInfoModifyEvent(UserInfoModifyEvent.TYPE_MODIFY_PHOTO));
    }

    /**
     * 更新用户名称
     */
    private void notifyNickName() {
        EventBus.getDefault().post(new UserInfoModifyEvent(UserInfoModifyEvent.TYPE_MODIFY_NICKNAME | UserInfoModifyEvent.TYPE_MODIFY_FARMINTON));
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            onBackPressed();
        } else if (event == RIGHT_BUTTON) {
            //保存用户信息
            saveInfo();
        }
    }

    private void saveInfo() {
        if (!checkRequired()) return;
        //没有修改
        if (!isModified()) {
            back();
            showMessage(getString(R.string.user_tip_nochange));
        } else {
            //保存用户信息
            doSave();
        }
    }

    private boolean checkRequired() {
        if(TextUtils.isEmpty(mTempUserInfo.getFarmington())) {
            showMessage(getString(R.string.user_center_farmington_no_empty));
            return false;
        }
        if(TextUtils.isEmpty(mTempUserInfo.getNickname())) {
            showMessage(getString(R.string.user_center_nickname_no_empty));
            return false;
        }
        return true;
    }

    private void doSave() {
        showProgress(getString(R.string.user_tip_savinguserinfo));
        NetController.newInstance(this).modifyInfo(mTempUserInfo, new LoadController.DataCallback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo) {
                dismissProgress();
                //更新用户名
                notifyNickName();
                //从注册到个人中心，跳转到主页
                back();
                showMessage(getString(R.string.user_tip_savesuccess));
            }

            @Override
            public void fail(Object... objects) {
                dismissProgress();
                showMessage(getString(R.string.user_tip_changefailure));
            }
        });
    }

    private void showSavePromptDialog() {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(getString(R.string.user_change_userinfo))
                .setMessage(getString(R.string.user_change_userinfo_tip))
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        if (!checkRequired()) return;
                        doSave();
                    }
                })
                .setNegativeListener(new INegativeButtonDialogListener() {
                    @Override
                    public void onNegativeButtonClicked(int requestCode) {
                        back();

                    }
                })
                .setTag("user_center_save_message")
                .showSinglton();
    }

    private void back() {
        //从注册到个人中心，跳转到主页
        if (IntentUtils.ACTION_USER_SET_PASSWORD.equals(mAction)) {
            IntentUtils.goToMainPage(this);
        }
        //从我页到个人中心，直接返回
        else {
            UserCenterActivity.super.onBackPressed();
        }
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            return;
        }
        if (!isModified()) {
            back();
        } else {
            showSavePromptDialog();
        }
    }

    @Override
    protected void onDestroy() {
        //清空拍照缓存文件
        if (mParentFile.exists()) {
            for (File f : mParentFile.listFiles()) {
                f.delete();
            }
            mParentFile.delete();
        }
        super.onDestroy();
    }

    private void initFile() {
        if (!mParentFile.exists()) {
            mParentFile.mkdirs();
        }
        mFile = new File(mParentFile,
                "camera" + System.currentTimeMillis() + ".jpg");
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index, String text) {
        if (getString(R.string.user_from_album).equals(text)) {
            albumSelected();
        } else if (getString(R.string.user_photograph).equals(text)) {
            captureSelected();
        }
    }

    /**
     * 相册
     */
    public void albumSelected() {
        initFile();
        AlbumAndCaptuHelper.albumSelected(this);
    }

    /**
     * 拍照
     */
    public void captureSelected() {
        initFile();
        AlbumAndCaptuHelper.captureSeleced(this, mFile);
    }

    private void doCropPhoto(Uri uri, int requestCode) {
        AlbumAndCaptuHelper.doCropPhoto(this, mFile, uri, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AlbumAndCaptuHelper.RESULT_CODE_ALBUM:
                if (data != null && data.getData() != null) {
                    doCropPhoto(data.getData(), AlbumAndCaptuHelper.RESULT_CODE_CROP_BY_ALBUM);
                }
                break;
            case AlbumAndCaptuHelper.RESULT_CODE_CAPTURE:
                if (mFile.exists()) {
                    doCropPhoto(Uri.fromFile(mFile), AlbumAndCaptuHelper.RESULT_CODE_CROP_BY_CAPTURE);
                }
                break;
            case AlbumAndCaptuHelper.RESULT_CODE_CROP_BY_CAPTURE:
            case AlbumAndCaptuHelper.RESULT_CODE_CROP_BY_ALBUM:
                doUpload(data);
                break;
        }
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case EditActivity.RESPOND_CODE_DESCRIPTION_EDIT:
                final String description = data.getStringExtra(IntentUtils.EXTRA_EDIT_DESCRIPTION);
                mTempUserInfo.setDescription(description);
                mUserInfoItems.get(mUserInfoItems.size() - 1).setValue(description);
                mUserInfoListAdapter.notifyDataSetChanged(false);
                break;
            //修改昵称
            case EditActivity.RESPOND_CODE_NICKNAME_EDIT:
                final String nickname = data.getStringExtra(IntentUtils.EXTRA_EDIT_NICKNAME);
                mTempUserInfo.setNickname(nickname);
                mUserInfoItems.get(3).setValue(nickname);
                mUserInfoListAdapter.notifyDataSetChanged(false);
                break;
            //修改法名
            case EditActivity.RESPOND_CODE_FARMINTON_EDIT:
                final String farminton = data.getStringExtra(IntentUtils.EXTRA_EDIT_FARMINTON);
                mTempUserInfo.setFarmington(farminton);
                mUserInfoItems.get(2).setValue(farminton);
                mUserInfoListAdapter.notifyDataSetChanged(false);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doUpload(Intent data) {
        if (data == null || data.getExtras() == null) {
            return;
        }
        final Bundle extras = data.getExtras();
        final Bitmap photo = extras.getParcelable("data");
        FileOutputStream outStreamz = null;
        try {
            outStreamz = new FileOutputStream(mFile);
            photo.compress(Bitmap.CompressFormat.PNG, 30, outStreamz);
            outStreamz.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outStreamz != null) {
                try {
                    outStreamz.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (mFile.exists()) {
            uploadImage();
        }
    }

    private void uploadImage() {
        showProgress(getString(R.string.user_uploading_photo));
        NetController.newInstance(this).uploadPhoto(mFile, new LoadController.DataCallback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo) {
                dismissProgress();
                showProgress(getString(R.string.load_user_photo_message));
                mTempUserInfo.setPhoto(userInfo.getPhoto());
                mUserInfoItems.get(1).setValue(userInfo.getPhoto());
                mUserInfoListAdapter.notifyDataSetChanged(true);
            }

            @Override
            public void fail(Object... objects) {
                dismissProgress();
                showMessage(getString(R.string.user_uploadfailure));
            }
        });
    }

    @Override
    public void onListItemSelected(String value, int number, int requestCode) {
        if (requestCode == EditInfoUtil.REQUEST_EDIT_GENDER) {
            if (value.equals(getString(R.string.male))) {
                mTempUserInfo.setGender("m");
            } else if (value.equals(getString(R.string.female))) {
                mTempUserInfo.setGender("f");
            }
            final UserInfoItem item = mUserInfoListAdapter.getItem(5);
            item.setValue(mTempUserInfo.getGender());
            mUserInfoListAdapter.notifyDataSetChanged(false);
        }
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == EditInfoUtil.REQUEST_ADDRESS_DIALOG && EditInfoUtil.mCitySelectView != null) {
            EditInfoUtil.mCitySelectView.select();
            final String location = mTempUserInfo.getLocation();
            if (!TextUtils.isEmpty(location) && location.contains(getString(R.string.no_limited))) {
                mTempUserInfo.setLocation(location.substring(0, location.indexOf(getString(R.string.no_limited))).trim());
            }
            mUserInfoListAdapter.getItem(6).setValue(mTempUserInfo.getLocation());
            mUserInfoListAdapter.notifyDataSetChanged(false);
            EditInfoUtil.mCitySelectView = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final UserInfoItem userInfoItem = (UserInfoItem) parent.getItemAtPosition(position);
        if (userInfoItem == null) {
            return;
        }
        final String key = userInfoItem.getKey();
        if (key.equals(getString(R.string.user_photo))) {
            showActionSheet(getString(R.string.user_photograph), getString(R.string.user_from_album));

        } else if (key.equals(getString(R.string.user_farming))) {
            editFarminton();

        } else if (key.equals(getString(R.string.user_nickname))) {
            editNickname();

        } else if (key.equals(getString(R.string.user_gender))) {
            editGender();

        } else if (key.equals(getString(R.string.user_location))) {
            editLocation();

        } else if (key.equals(getString(R.string.user_descriptiom))) {
            editDescription();

        }
    }

    private void editLocation() {
        EditInfoUtil.editAddress(UserCenterActivity.this, mTempUserInfo);
    }

    private void editGender() {
        EditInfoUtil.editGender(UserCenterActivity.this, mTempUserInfo.getGender());
    }

    private void editNickname() {
        IntentUtils.editNicknamePage(this, mTempUserInfo);
    }

    private void editFarminton() {
        IntentUtils.editFarmintonPage(this, mTempUserInfo);
    }

    private void editDescription() {
        IntentUtils.editDescriptionPage(this, mTempUserInfo);
    }
}