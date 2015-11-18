package com.sunquan.chimingfazhou.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.activity.WenDetailActivity;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.event.GoToPlayListEvent;
import com.sunquan.chimingfazhou.models.WenDetailInfo;
import com.sunquan.chimingfazhou.models.WenDetailIntroductionInfo;
import com.sunquan.chimingfazhou.widget.ScoreView;

import de.greenrobot.event.EventBus;

/**
 * 闻详情章节简介页
 * <p/>
 * Created by Administrator on 2015/5/11.
 */
public class WenDetailIntroductionFragment extends AppBaseFragment implements View.OnClickListener{

    private static final int MAX_SCORE = 5;
    private WenDetailIntroductionInfo mIntroductionInfo;

    private ImageView mThumbnail;
    private TextView mBriefView;
    private TextView mTitleView;
    private TextView mAuthorView;
    private TextView mBroadcastView;
    private TextView mSetView;
    private ScoreView mScoreView;
    private TextView mDateView;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        final View view = inflater.inflate(R.layout.fragment_wen_introduction_layout, container, false);
        final WenDetailInfo detailInfo = (WenDetailInfo) getArguments().getSerializable(WenDetailActivity.WEN_EXTRA);
        mIntroductionInfo = detailInfo.getIntroduction();
        initView(view);
        renderView();
        return view;
    }

    private void initView(View view) {
        mThumbnail = (ImageView) view.findViewById(R.id.left_icon);
        mTitleView = (TextView) view.findViewById(R.id.title);
        mAuthorView = (TextView) view.findViewById(R.id.author);
        mBroadcastView = (TextView) view.findViewById(R.id.broadcast);
        mSetView = (TextView) view.findViewById(R.id.set_number);
        mScoreView = (ScoreView) view.findViewById(R.id.ratingbar);
        mDateView = (TextView) view.findViewById(R.id.date);
        mBriefView = (TextView) view.findViewById(R.id.brief);
    }

    private void renderView() {
        NetController.newInstance(getActivity()).renderImage(mThumbnail, mIntroductionInfo.getThumbnail());
        mThumbnail.setOnClickListener(this);
        mTitleView.setText(mIntroductionInfo.getTitle());
        mAuthorView.setText(getString(R.string.introduction_author) + mIntroductionInfo.getAuthor());
        mBroadcastView.setText(getString(R.string.introduction_broadcast) + mIntroductionInfo.getBroadcast());
        mSetView.setText(getString(R.string.introduction_set_number) + mIntroductionInfo.getSet_count());
        mScoreView.setScore(Integer.valueOf(mIntroductionInfo.getScore()));
        mScoreView.setMaxScore(MAX_SCORE);
        mDateView.setText(getString(R.string.introduction_date) + mIntroductionInfo.getCreate_date());
        mBriefView.setText(getString(R.string.introduction_brief) + mIntroductionInfo.getBrief());
    }

    @Override
    protected void handleClickEvent(int event) {
    }

    @Override
    protected boolean isShowTitle() {
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.left_icon) {
            EventBus.getDefault().post(new GoToPlayListEvent());
        }
    }

}
