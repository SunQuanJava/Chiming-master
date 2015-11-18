package com.sunquan.chimingfazhou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.baizhi.baseapp.controller.LoadController;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.SiDetailInfo;
import com.sunquan.chimingfazhou.util.ParamsUtil;

/**
 * 思的详情页
 * <p/>
 * Created by Administrator on 2015/5/11.
 */
public class SiDetailActivity extends BaseLoadingWithSwipeBackActivity implements LoadController.DataCallback<SiDetailInfo> {

    public static final String SI_INTRODUCTION = "SiDetailIntroduction";

    /**
     * H5页面的过期时间
     */
    private static final long EXPIRE_TIME = 3600000;
    private MainPageBodyInfo mBodyInfo;
    private SiDetailInfo mSiDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoadingContent(R.layout.activity_si_detail_layout);
        mBodyInfo = GlobalDataHolder.getInstance(this).getCurrentSiPageBodyInfo();
        setCenterText(mBodyInfo.getTitle());
        setLeftContentIcon(R.drawable.back_icon_selector);
        setRightContentIcon(R.drawable.share_icon_selector);
        loadData();
    }

    private void loadData() {
        final String url = ParamsUtil.getSiDetailUrl(this,mBodyInfo.getId());
        NetController.newInstance(this).loadSiDetailInfo(url, this);
        checkNewGuidePage(SI_INTRODUCTION,R.layout.wen_cover_pager,R.id.cover_cancel);
    }

    /**
     * 加载H5页面
     */
    private void loadH5Page() {
        final String url = mSiDetailInfo.getLink();
        final long expire = EXPIRE_TIME;
        final AQuery aQuery = new AQuery(this);
        aQuery.ajax(url, String.class, expire, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String html, AjaxStatus status) {
                if (status.getCode() == 200 && !TextUtils.isEmpty(html)) {
                    //成功
                    WebView wv = aQuery.id(R.id.web).getWebView();
                    wv.setHorizontalScrollBarEnabled(false);
                    wv.setWebViewClient(new MyWebViewClient());
                    wv.loadDataWithBaseURL(url, html, "text/html", "utf-8", null);
                    showContentState();
                } else {
                    showErrorState();
                }
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onRetry() {
        if (mSiDetailInfo == null) {
            loadData();
        } else {
            loadH5Page();
        }
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            super.onBackPressed();
        }
    }

    @Override
    public void success(SiDetailInfo siDetailInfo) {
        mSiDetailInfo = siDetailInfo;
        loadH5Page();
    }

    @Override
    public void fail(Object... objects) {
        showErrorState();
    }

}
