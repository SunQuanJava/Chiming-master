package com.sunquan.chimingfazhou.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.avast.android.dialogs.fragment.BillboardDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.baizhi.baseapp.util.UUIDGenerator;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.adapter.XiuDetailPageAdapter;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.XiuInfo;
import com.sunquan.chimingfazhou.models.XiuSubInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;

import java.util.ArrayList;

/**
 * 修的二级页面
 * <p/>
 * Created by Administrator on 2015/5/14.
 */
public class XiuDetailActivity extends BaseSwipeBackActivity implements SwipeMenuListView.OnMenuItemClickListener, SwipeMenuCreator, AdapterView.OnItemClickListener {

    private ArrayList<XiuSubInfo> mXiuSubInfos;
    private XiuDetailPageAdapter mAdapter;
    private XiuInfo mXiuInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_xiu_layout);
        mXiuInfo = (XiuInfo) getIntent().getSerializableExtra(IntentUtils.EXTRA_XIU_DETAIL);
        setCenterText(mXiuInfo.getTask_name());
        setLeftContentIcon(R.drawable.back_icon_selector);
        setRightContentIcon(R.drawable.xiu_add_icon_selector);

        mXiuSubInfos = GlobalDataHolder.getInstance(this).getXiuSubInfosByXiuInfo(mXiuInfo);
        final SwipeMenuListView mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipeListView);

        mAdapter = new XiuDetailPageAdapter(this);
        mAdapter.setItems(mXiuSubInfos);

        //add headerview and footerview
        final RelativeLayout headerView = new RelativeLayout(this);
        headerView.setLayoutParams(new SwipeMenuListView.LayoutParams(SwipeMenuListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.name_xiu_sub_list_header)));
        headerView.setBackgroundColor(getResources().getColor(R.color.white));
        mSwipeMenuListView.addHeaderView(headerView);

        final RelativeLayout footerView = new RelativeLayout(this);
        footerView.setLayoutParams(new SwipeMenuListView.LayoutParams(SwipeMenuListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.name_xiu_sub_list_header)));
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        mSwipeMenuListView.addFooterView(footerView);

        // set creator
        mSwipeMenuListView.setMenuCreator(this);
        mSwipeMenuListView.setOnMenuItemClickListener(this);
        mSwipeMenuListView.setOnItemClickListener(this);

        //set empty view
        final ViewGroup parentView = (ViewGroup) mSwipeMenuListView.getParent();
        final View emptyView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.xiu_empty_layout, parentView, false);
        parentView.addView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSwipeMenuListView.setEmptyView(emptyView);

        mSwipeMenuListView.setAdapter(mAdapter);
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            super.onBackPressed();
        } else if (event == RIGHT_BUTTON) {
            showAddDialog();
        }
    }

    private void showAddDialog() {
        final View customView = LayoutInflater.from(this).inflate(R.layout.galary_content, null);
        final EditText editText = (EditText) customView.findViewById(R.id.xiu_add_edittext);
        editText.setHint(getString(R.string.input_galary_input_hint));
        editText.requestFocus();
        BillboardDialogFragment.createBuilder(this)
                .setTitle(getString(R.string.add_new_counter_message))
                .setCustomView(customView)
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        final String inputText = editText.getText().toString();
                        if (TextUtils.isEmpty(inputText)) {
                            showMessage(getString(R.string.empty_xiu_galary_count_message));
                        } else if (isDuplicateName(inputText)) {
                            showMessage(getString(R.string.exist_xiu_galary_count_message));
                        } else {
                            final XiuSubInfo xiuSubInfo = getXiuSubInfoByInputText(inputText);
                            mXiuSubInfos.add(xiuSubInfo);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setTag(getString(R.string.add_galary_tag))
                .showSinglton();
    }

    private void showModifyDialog(final int position) {
        final View customView = LayoutInflater.from(this).inflate(R.layout.galary_modify_content, null);
        final EditText editText = (EditText) customView.findViewById(R.id.xiu_add_edittext);
        final RadioGroup radioGroup = (RadioGroup) customView.findViewById(R.id.radioGroup);
        editText.setHint(getString(R.string.modify_hint_message));
        editText.requestFocus();
        BillboardDialogFragment.createBuilder(this)
                .setTitle(getString(R.string.modify_count_message))
                .setCustomView(customView)
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        final String inputText = editText.getText().toString();
                        if (TextUtils.isEmpty(inputText)) {
                            showMessage(getString(R.string.count_unmodify_message));
                            return;
                        }
                        modifyCount(inputText, radioGroup, position);
                    }
                })
                .setTag(getString(R.string.add_galary_tag))
                .showSinglton();
    }

    private void modifyCount(String inputText, RadioGroup radioGroup, int position) {
        final int id = radioGroup.getCheckedRadioButtonId();
        final boolean isAdd = id == R.id.radioAdd;
        final XiuSubInfo xiuSubInfo = mAdapter.getItem(position);
        final int currentCount = Integer.valueOf(xiuSubInfo.getCount());
        final int inputCount = Integer.valueOf(inputText);
        int resultCount;
        //增加计数
        if (isAdd) {
            resultCount = currentCount + inputCount;
            //超过最大限
            if (resultCount > XiuCounterActivity.MAX_COUNT) {
                resultCount = XiuCounterActivity.MAX_COUNT;
            }
        }
        //减少计数
        else {
            resultCount = currentCount - inputCount;
            if (resultCount < 0) {
                resultCount = 0;
            }
        }
        xiuSubInfo.setCount(String.valueOf(resultCount));
        xiuSubInfo.setLast_practice_time(String.valueOf(System.currentTimeMillis()));
        mAdapter.notifyDataSetChanged();
    }

    private XiuSubInfo getXiuSubInfoByInputText(String inputText) {
        final XiuSubInfo xiuSubInfo = new XiuSubInfo();
        xiuSubInfo.setParent_task_name(mXiuInfo.getTask_name());
        xiuSubInfo.setParent_task_id(mXiuInfo.getTask_id());
        xiuSubInfo.setTask_name(inputText);
        xiuSubInfo.setTask_id(UUIDGenerator.getUUID());
        xiuSubInfo.setUid(GlobalDataHolder.getInstance(this).getUid());
        xiuSubInfo.setCount(String.valueOf(0));
        xiuSubInfo.setLast_practice_time(String.valueOf(System.currentTimeMillis()));
        return xiuSubInfo;
    }


    @Override
    public boolean onMenuItemClick(final int position, final SwipeMenu menu, final int index) {
        if (index == 0) {
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    showModifyDialog(position);
                }
            });
        } else if (index == 1) {
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    showPromptDialog(position);
                }
            });
        }
        return false;
    }

    private void showPromptDialog(final int position) {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(getString(R.string.delete_xiu_sub_message))
                .setMessage(getString(R.string.delete_xiu_sub_message_prompt))
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        mXiuSubInfos.remove(mAdapter.getItem(position));
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setTag("xiu_sub_delete_prompt")
                .showSinglton();
    }

    @Override
    public void create(SwipeMenu menu) {
        final SwipeMenuItem addItem = new SwipeMenuItem(this.getApplicationContext());
        addItem.setBackground(new ColorDrawable(0xff0000ff));
        addItem.setTitle(getString(R.string.modify_count_message));
        addItem.setTypedValue(TypedValue.COMPLEX_UNIT_PX);
        addItem.setTitleSize(getResources().getDimensionPixelSize(R.dimen.sub_swipe_menu_item_text_size));
        addItem.setTitleTopIcon(R.drawable.ic_add);
        addItem.setTitleColor(0xffffffff);
        addItem.setWidth(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_width));
        menu.addMenuItem(addItem);

        final SwipeMenuItem deleteItem = new SwipeMenuItem(this.getApplicationContext());
        deleteItem.setBackground(new ColorDrawable(0xff0000ff));
        deleteItem.setTitle(getString(R.string.delete_count_message));
        deleteItem.setTypedValue(TypedValue.COMPLEX_UNIT_PX);
        deleteItem.setTitleSize(getResources().getDimensionPixelSize(R.dimen.sub_swipe_menu_item_text_size));
        deleteItem.setTitleTopIcon(R.drawable.ic_delete);
        deleteItem.setTitleColor(0xffffffff);
        deleteItem.setWidth(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_width));
        menu.addMenuItem(deleteItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentUtils.REQUEST_CODE_XIU_COUNTER && resultCode == RESULT_OK) {
            final XiuSubInfo xiuSubInfo = (XiuSubInfo) data.getSerializableExtra(IntentUtils.EXTRA_XIU_DETAIL_BACK);
            final int position = data.getIntExtra(IntentUtils.EXTRA_XIU_DETAIL_BACK_POSITION, -1);
            if (xiuSubInfo != null && position != -1) {
                mXiuSubInfos.remove(position);
                mXiuSubInfos.add(position, xiuSubInfo);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private boolean isDuplicateName(String inputText) {
        for (XiuSubInfo xiuSubInfo : mXiuSubInfos) {
            if (xiuSubInfo.getTask_name().equals(inputText)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final XiuSubInfo item = (XiuSubInfo) parent.getItemAtPosition(position);
        IntentUtils.goToXiuCounterPage(XiuDetailActivity.this, item, mXiuSubInfos.indexOf(item));
    }
}
