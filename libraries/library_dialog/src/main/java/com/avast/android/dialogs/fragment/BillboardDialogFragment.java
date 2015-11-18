package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;

import java.util.List;

/**
 * Dialog with specific layout
 * <p/>
 * Created by Will on 2015/2/26.
 */
public class BillboardDialogFragment extends BaseDialogFragment {

    protected final static String ARG_TITLE = "title";
    protected final static String ARG_CUSTOM_VIEW = "custom_view";
    protected static final String ARG_POSITIVE_BUTTON = "positive_button";
    protected static final String ARG_NEGATIVE_BUTTON = "negative_button";
    protected static final int DEFAULT_VIEW_ID = -1;

    public static SimpleBillboardDialogBuilder createBuilder(FragmentActivity fragmentActivity) {
        return new SimpleBillboardDialogBuilder(fragmentActivity, fragmentActivity.getSupportFragmentManager(), BillboardDialogFragment.class);
    }

    public static SimpleBillboardDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleBillboardDialogBuilder(context, fragmentManager, BillboardDialogFragment.class);
    }

    @Override
    protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        final String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        final String positiveButtonText = getPositiveButtonText();
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (IPositiveButtonDialogListener listener : getPositiveButtonDialogListeners()) {
                        listener.onPositiveButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        final String negativeButtonText = getNegativeButtonText();
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (INegativeButtonDialogListener listener : getNegativeButtonDialogListeners()) {
                        listener.onNegativeButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }

        try {
            final int layoutId = getLayoutId();
            View mCustomView;
            if (layoutId == DEFAULT_VIEW_ID) {
                mCustomView = getCustomView();
            } else {
                mCustomView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
            }
            if (mCustomView != null) {
                builder.setView(mCustomView);
            } else {
                throw new Exception("Invalid customView");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Fail to inflate this layout");
        }

        return builder;
    }

    protected String getTitle() {
        return getArguments().getString(ARG_TITLE);
    }

    protected int getLayoutId() {
        return getArguments().getInt(ARG_CUSTOM_VIEW);
    }

    protected String getPositiveButtonText() {
        return getArguments().getString(ARG_POSITIVE_BUTTON);
    }

    protected String getNegativeButtonText() {
        return getArguments().getString(ARG_NEGATIVE_BUTTON);
    }

    protected List<IPositiveButtonDialogListener> getPositiveButtonDialogListeners() {
        return getDialogListeners(IPositiveButtonDialogListener.class);
    }

    protected List<INegativeButtonDialogListener> getNegativeButtonDialogListeners() {
        return getDialogListeners(INegativeButtonDialogListener.class);
    }

    public static class SimpleBillboardDialogBuilder extends BaseDialogBuilder<SimpleBillboardDialogBuilder> {

        private String mTitle;
        private int mCustomViewId = DEFAULT_VIEW_ID;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private View mCustomView;

        protected SimpleBillboardDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BillboardDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        public SimpleBillboardDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }

        public SimpleBillboardDialogBuilder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public SimpleBillboardDialogBuilder setPositiveButtonText(int textResourceId) {
            mPositiveButtonText = mContext.getString(textResourceId);
            return this;
        }

        public SimpleBillboardDialogBuilder setPositiveButtonText(String text) {
            mPositiveButtonText = text;
            return this;
        }

        public SimpleBillboardDialogBuilder setNegativeButtonText(int textResourceId) {
            mNegativeButtonText = mContext.getString(textResourceId);
            return this;
        }

        public SimpleBillboardDialogBuilder setNegativeButtonText(String text) {
            mNegativeButtonText = text;
            return this;
        }

        public SimpleBillboardDialogBuilder setCustomView(@LayoutRes int customViewId) {
            mCustomViewId = customViewId;
            return this;
        }

        public SimpleBillboardDialogBuilder setCustomView(View customView) {
            mCustomView = customView;
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putString(BillboardDialogFragment.ARG_TITLE, mTitle);
            args.putInt(BillboardDialogFragment.ARG_CUSTOM_VIEW, mCustomViewId);
            args.putString(BillboardDialogFragment.ARG_POSITIVE_BUTTON, mPositiveButtonText);
            args.putString(BillboardDialogFragment.ARG_NEGATIVE_BUTTON, mNegativeButtonText);
            return args;
        }

        @Override
        protected SimpleBillboardDialogBuilder self() {
            return this;
        }

        @Override
        protected BaseDialogFragment create() {
            final BaseDialogFragment fragment = createBasic();
            fragment.setCustomView(mCustomView);
            return fragment;
        }
    }
}
