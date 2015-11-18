package com.avast.android.dialogs.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.avast.android.dialogs.iface.INeutralButtonDialogListener;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.avast.android.dialogs.util.DialogTagHolder;


/**
 * Internal base builder that holds common values for all dialog fragment builders.
 *
 * @author Tomas Vondracek
 */
public abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

    public final static String ARG_REQUEST_CODE = "request_code";
    public final static String ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto";
    public final static String DEFAULT_TAG = "simple_dialog";
    public final static int DEFAULT_REQUEST_CODE = -42;

    protected final Context mContext;
    protected final FragmentManager mFragmentManager;
    protected final Class<? extends BaseDialogFragment> mClass;

    protected Fragment mTargetFragment;
    protected boolean mCancelable = true;
    protected boolean mCancelableOnTouchOutside = true;
    //默认启用blur，即默认使用xml中的值
    protected boolean mUseBlur = false;

    protected String mTag = DEFAULT_TAG;
    protected int mRequestCode = DEFAULT_REQUEST_CODE;

    protected IPositiveButtonDialogListener mPositiveButtonDialogListener;
    protected INegativeButtonDialogListener mNegativeButtonDialogListener;
    protected INeutralButtonDialogListener mNeutralButtonDialogListener;

    public BaseDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
        mFragmentManager = fragmentManager;
        mContext = context.getApplicationContext();
        mClass = clazz;
    }

    protected abstract T self();

    protected abstract Bundle prepareArguments();

    public T setUseBlur(boolean useBlur) {
        mUseBlur = useBlur;
        return self();
    }

    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return self();
    }

    public T setCancelableOnTouchOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        if (cancelable) {
            mCancelable = cancelable;
        }
        return self();
    }

    public T setTargetFragment(Fragment fragment, int requestCode) {
        mTargetFragment = fragment;
        mRequestCode = requestCode;
        return self();
    }

    public T setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return self();
    }

    public T setTag(String tag) {
        mTag = tag;
        return self();
    }

    public T setPositiveListener(IPositiveButtonDialogListener positiveButtonDialogListener) {
        mPositiveButtonDialogListener = positiveButtonDialogListener;
        return self();
    }

    public T setNegativeListener(INegativeButtonDialogListener negativeButtonDialogListener) {
        mNegativeButtonDialogListener = negativeButtonDialogListener;
        return self();
    }

    public T setNeutralListener(INeutralButtonDialogListener neutralButtonDialogListener) {
        mNeutralButtonDialogListener = neutralButtonDialogListener;
        return self();
    }

    protected BaseDialogFragment createBasic() {
        final Bundle args = prepareArguments();

        final BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(mContext, mClass.getName(), args);

        args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);

        if (mTargetFragment != null) {
            fragment.setTargetFragment(mTargetFragment, mRequestCode);
        } else {
            args.putInt(ARG_REQUEST_CODE, mRequestCode);
        }
        fragment.setCancelable(mCancelable);
        fragment.setUseBlur(mUseBlur);
        fragment.setDialogListener(mPositiveButtonDialogListener, mNegativeButtonDialogListener, mNeutralButtonDialogListener);
        return fragment;
    }

    protected BaseDialogFragment create() {
        return createBasic();
    }

    public DialogFragment createFragment() {
        BaseDialogFragment fragment = create();
        return fragment;
    }

    public void show() {
        BaseDialogFragment fragment = create();
        fragment.show(mFragmentManager, mTag);
    }

    /**
     * 必须设置了tag的才能使用showSinglton
     */
    public void showSinglton() {
        if (mTag.equals(DEFAULT_TAG)) {
            throw new RuntimeException("Tag has to be set when show singlton");
        }
        if (DialogTagHolder.checkHasTag(mTag)) {
            return;
        }
        BaseDialogFragment fragment = create();
        DialogTagHolder.putTag(mTag);
        fragment.show(mFragmentManager, mTag);
    }

    /**
     * Like show() but allows the commit to be executed after an activity's state is saved. This
     * is dangerous because the commit can be lost if the activity needs to later be restored from
     * its state, so this should only be used for cases where it is okay for the UI state to change
     * unexpectedly on the user.
     */
    public DialogFragment showAllowingStateLoss() {
        BaseDialogFragment fragment = create();
        fragment.showAllowingStateLoss(mFragmentManager, mTag);
        return fragment;
    }
}
