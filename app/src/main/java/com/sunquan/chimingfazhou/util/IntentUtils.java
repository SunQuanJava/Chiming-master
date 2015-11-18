package com.sunquan.chimingfazhou.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;

import com.baizhi.baseapp.controller.BaseHandler;
import com.baizhi.baseapp.util.PreferencesUtil;
import com.baizhi.baseapp.widget.WToast;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.models.XiuInfo;
import com.sunquan.chimingfazhou.models.XiuSubInfo;

/**
 * 页面跳转工具类
 * <p/>
 * Created by Administrator on 2015/5/11.
 */
public final class IntentUtils {

    public static final String EXTRA_WEN_NOTIFICATION_DETAIL = "extra.wen.notification.detail";
    public static final String EXTRA_XIU_DETAIL = "extra.xiu.detail";
    public static final String EXTRA_XIU_COUNTER = "extra.xiu.counter";
    public static final String EXTRA_XIU_COUNTER_POSITION = "extra.xiu.counter.position";
    public static final String EXTRA_XIU_DETAIL_BACK = "extra.xiu.detail.back";
    public static final String EXTRA_XIU_DETAIL_BACK_POSITION = "extra.xiu.detail.back.position";
    public static final String EXTRA_EDIT_NICKNAME = "extra.edit.nickname";
    public static final String EXTRA_EDIT_FARMINTON = "extra.edit.farminton";
    public static final String EXTRA_EDIT_DESCRIPTION = "extra.edit.description";

    public static final String ACTION_FROM_FORGET = "action.from.forget";
    public static final String ACTION_FROM_REGISTER = "action.from.register";
    public static final String ACTION_XIU_DETAIL = "action.xiu.detail";
    public static final String ACTION_XIU_COUNTER = "action.xiu.counter";
    public static final String ACTION_USER_CENTER_WO = "action.user.center.wo";
    public static final String ACTION_USER_SET_PASSWORD = "action.user.set.password";
    public static final String ACTION_USER_VERIFICATION_CODE = "action.user.verification.code";

    public static final String ACTION_FROM_USER_CENTER_FOR_DESCRIPTION = "action.from.user.center.for.description";
    public static final String ACTION_FROM_USER_CENTER_FOR_NICKNAME = "action.from.user.center.for.nickname";
    public static final String ACTION_FROM_USER_CENTER_FOR_FARMINTON = "action.from.user.center.for.farminton";
    public static final int REQUEST_CODE_XIU_COUNTER = 200;

    private static boolean isExit;
    private static final int EXIT_MESSAGE = 0;
    private static BaseHandler exitHandler = new BaseHandler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            isExit = false;
        }
    };


    /**
     * 跳转到闻的详情页
     *
     * @param context
     * @param bodyInfo
     */
    public static void goToDetailPageByBodyInfo(final Context context, final MainPageBodyInfo bodyInfo) {
        Intent intent = null;
        final String type = bodyInfo.getType();
        if (MainPageBodyInfo.WEN.equals(type)) {
            GlobalDataHolder.getInstance(context).setCurrentWenPageBodyInfo(bodyInfo);
            intent = initIntent(context.getString(R.string.scheme_wenDetail));
        } else if (MainPageBodyInfo.SI.equals(type)) {
            GlobalDataHolder.getInstance(context).setCurrentSiPageBodyInfo(bodyInfo);
            intent = initIntent(context.getString(R.string.scheme_siDetail));
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转到修的详情页
     *
     * @param context
     * @param xiuInfo
     */
    public static void goToXiuSubPage(final Context context, final XiuInfo xiuInfo) {
        final Intent intent = initIntent(context.getString(R.string.scheme_xiuDetail));
        intent.putExtra(EXTRA_XIU_DETAIL, xiuInfo);
        intent.setAction(ACTION_XIU_DETAIL);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转到修的计数页
     *
     * @param context
     * @param xiuSubInfo
     */
    public static void goToXiuCounterPage(final Context context, final XiuSubInfo xiuSubInfo, final int position) {
        final Intent intent = initIntent(context.getString(R.string.scheme_xiuCounter));
        intent.putExtra(EXTRA_XIU_COUNTER, xiuSubInfo);
        intent.putExtra(EXTRA_XIU_COUNTER_POSITION, position);
        intent.setAction(ACTION_XIU_COUNTER);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_XIU_COUNTER);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转到首页
     *
     * @param context
     */
    public static void goToMainPage(final Context context) {
        final boolean isFirstStart = PreferencesUtil.getBooleanByName(context, "isShowWelcome", true);
        final Intent intent;
        //判断是否是第一次使用
        if (isFirstStart) {
            intent = initIntent(context.getString(R.string.scheme_welcome));
        } else {
            intent = initIntent(context.getString(R.string.scheme_main));
        }
        context.startActivity(intent);
        ((MyApplication) ((Activity) context).getApplication()).finishAllActivity();
        ((Activity) context).overridePendingTransition(R.anim.scale_in, R.anim.scale_hold);
    }

    /**
     * 跳转到登陆页
     *
     * @param context
     */
    public static void goToLoginPage(final Context context) {
        final Intent intent = initIntent(context.getString(R.string.scheme_login));
        context.startActivity(intent);
        ((MyApplication) ((Activity) context).getApplication()).finishAllActivity();
        ((Activity) context).overridePendingTransition(R.anim.scale_in, R.anim.scale_hold);
    }

    /**
     * 跳转到金刚道友页
     *
     * @param context
     */
    public static void goJingangFriendPage(final Context context) {
        final Intent intent = initIntent(context.getString(R.string.scheme_jingangFriend));
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转到开发团队页
     *
     * @param context
     */
    public static void goTeamPage(final Context context) {
        final Intent intent = initIntent(context.getString(R.string.scheme_team));
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转个人信息页
     *
     * @param context
     */
    public static void goToUserCenterPage(final Context context, final String action) {
        final Intent intent = initIntent(context.getString(R.string.scheme_userCenter));
        intent.setAction(action);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转设置密码页
     */
    public static void goToSetPasswordActivity(final Context context) {
        final Intent intent = initIntent(context.getString(R.string.scheme_setPassword));
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    public static void goToSetPasswordActivity(final Context context, String mPhoneStr) {
        final Intent intent = initIntent(context.getString(R.string.scheme_setPassword));
        intent.putExtra("phone", mPhoneStr);
        intent.setAction(IntentUtils.ACTION_USER_VERIFICATION_CODE);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转到验证码页
     */
    public static void goToVerificationCodeActivity(final Context context, final String mPhoneStr, final String action) {
        final Intent intent = initIntent(context.getString(R.string.scheme_verificationCode));
        intent.putExtra("phone", mPhoneStr);
        intent.setAction(action);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转到注册页
     *
     * @param context
     */
    public static void goToRegisterPage(final Context context) {
        final Intent intent = initIntent(context.getString(R.string.scheme_register));
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 跳转到找回密码页面
     *
     * @param context
     */
    public static void goToUpdatePasswordPage(final Context context) {
        final Intent intent = initIntent(context.getString(R.string.scheme_forgetPassword));
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 退到后台
     */
    public static void goToHome(final Context context) {
        if (!isExit) {
            isExit = true;
            new WToast(context).showMessage(R.string.double_click_message);
            exitHandler.sendEmptyMessageDelayed(EXIT_MESSAGE, 2000);
        } else {
            isExit = false;
            exitHandler.removeMessages(EXIT_MESSAGE);
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(intent);
        }
    }

    /**
     * 返回到修的详情页
     *
     * @param context
     * @param xiuSubInfo
     */
    public static void backToXiuDetailPage(final Context context, final XiuSubInfo xiuSubInfo, final int position) {
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_XIU_DETAIL_BACK, xiuSubInfo);
        intent.putExtra(EXTRA_XIU_DETAIL_BACK_POSITION, position);
        ((Activity) context).setResult(Activity.RESULT_OK, intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.scale_hold, R.anim.out_to_right);
    }

    /**
     * 编辑名称
     *
     * @param context
     * @param mTempUserInfo
     */
    public static void editNicknamePage(final Context context, final UserInfo mTempUserInfo) {
        final Intent intent = initIntent(context.getString(R.string.scheme_edit));
        intent.putExtra(IntentUtils.EXTRA_EDIT_NICKNAME, mTempUserInfo.getNickname());
        intent.setAction(IntentUtils.ACTION_FROM_USER_CENTER_FOR_NICKNAME);
        ((Activity) context).startActivityForResult(intent, 200);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 编辑法名
     *
     * @param context
     * @param mTempUserInfo
     */
    public static void editFarmintonPage(final Context context, final UserInfo mTempUserInfo) {
        final Intent intent = initIntent(context.getString(R.string.scheme_edit));
        intent.putExtra(IntentUtils.EXTRA_EDIT_FARMINTON, mTempUserInfo.getFarmington());
        intent.setAction(IntentUtils.ACTION_FROM_USER_CENTER_FOR_FARMINTON);
        ((Activity) context).startActivityForResult(intent, 200);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }

    /**
     * 编辑个人签名
     *
     * @param context
     * @param mTempUserInfo
     */
    public static void editDescriptionPage(final Context context, final UserInfo mTempUserInfo) {
        final Intent intent = initIntent(context.getString(R.string.scheme_edit));
        intent.putExtra(IntentUtils.EXTRA_EDIT_DESCRIPTION, mTempUserInfo.getDescription());
        intent.setAction(IntentUtils.ACTION_FROM_USER_CENTER_FOR_DESCRIPTION);
        ((Activity) context).startActivityForResult(intent, 200);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }


    private static Intent initIntent(final String uriString) {
        final Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uriString));
        return intent;
    }

}
