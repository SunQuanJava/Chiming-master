package com.sunquan.chimingfazhou.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.sunquan.chimingfazhou.adapter.XiuPageAdapter;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.event.BackEvent;
import com.sunquan.chimingfazhou.event.CoverEvent;
import com.sunquan.chimingfazhou.event.SameTabClickEvent;
import com.sunquan.chimingfazhou.event.TabChangeEvent;
import com.sunquan.chimingfazhou.models.XiuInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 修fragment
 * <p/>
 * Created by Administrator on 2015/4/25.
 */
public class XiuFragment extends AppBaseFragment implements SwipeMenuListView.OnMenuItemClickListener, SwipeMenuCreator, AdapterView.OnItemClickListener, View.OnClickListener {

    private XiuPageAdapter mAdapter;
    private ArrayList<XiuInfo> mXiuInfos;
    private SwipeMenuListView mSwipeMenuListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        final View view = inflater.inflate(R.layout.fragment_xiu_layout, container, false);

        setLeftContentIcon(R.drawable.back_icon_selector);
        setCenterText(getString(R.string.tab_xiu_tip));
        mXiuInfos = GlobalDataHolder.getInstance(getActivity()).getXiuInfo();

        //set mAdapter
        mAdapter = new XiuPageAdapter(getActivity());
        mAdapter.setItems(mXiuInfos);

        mSwipeMenuListView = (SwipeMenuListView) view.findViewById(R.id.swipeListView);

        // set creator
        mSwipeMenuListView.setMenuCreator(this);
        mSwipeMenuListView.setOnMenuItemClickListener(this);
        mSwipeMenuListView.setOnItemClickListener(this);

        //add headerview and footerview
        final RelativeLayout headerView = new RelativeLayout(getActivity());
        headerView.setLayoutParams(new SwipeMenuListView.LayoutParams(SwipeMenuListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.xiu_list_header)));
        headerView.setBackgroundColor(0xffefeff4);
        mSwipeMenuListView.addHeaderView(headerView);

        final View footerView = inflater.inflate(R.layout.xiu_footer_view, container, false);
        footerView.setLayoutParams(new SwipeMenuListView.LayoutParams(SwipeMenuListView.LayoutParams.MATCH_PARENT, SwipeMenuListView.LayoutParams.WRAP_CONTENT));
        mSwipeMenuListView.addFooterView(footerView);
        footerView.findViewById(R.id.add_wrapper).setOnClickListener(this);

        mSwipeMenuListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) EventBus.getDefault().post(new BackEvent(XiuFragment.class));
    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    @Override
    public boolean onMenuItemClick(final int position, final SwipeMenu menu, final int index) {
        switch (menu.getViewType()) {
            case XiuInfo.TYPE_DEFAULT:
                break;
            case XiuInfo.TYPE_MODIFIABLE:
                modifiableMenuClick(position, index);
                break;
            case 2:
                topMenuClick(position);
                break;
        }
        return false;
    }

    private void topMenuClick(final int position) {
        getActivity().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                showPromptDialog(position);
            }
        });
    }

    private void modifiableMenuClick(final int position, int index) {
        if (index == 0 && position > 2) {
            getActivity().getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    topItem(position);
                }
            });
        } else if (index == 1) {
            getActivity().getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    showPromptDialog(position);
                }
            });
        }
    }

    private void topItem(int position) {
        final XiuInfo xiuInfo = mAdapter.getItem(position);
        mAdapter.remove(position);
        mAdapter.insert(xiuInfo, 2);
        mAdapter.notifyDataSetChanged();
    }

    private void showPromptDialog(final int position) {
        SimpleDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                .setTitle(getString(R.string.delete_xiu_galary))
                .setMessage(getString(R.string.delete_xiu_message_prompt))
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        final XiuInfo xiuInfo = mAdapter.getItem(position);
                        mXiuInfos.remove(xiuInfo);
                        GlobalDataHolder.getInstance(getActivity()).removeXiuSubInfosByXiuInfo(xiuInfo);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setTag("xiu_delete_prompt")
                .showSinglton();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final XiuInfo xiuInfo = (XiuInfo) parent.getItemAtPosition(position);
        if (xiuInfo != null && xiuInfo.getShowType() == XiuInfo.TYPE_MODIFIABLE) {
            IntentUtils.goToXiuSubPage(getActivity(), xiuInfo);
        }
    }

    @Override
    public void create(SwipeMenu menu) {
        switch (menu.getViewType()) {
            case XiuInfo.TYPE_DEFAULT:
                break;
            case XiuInfo.TYPE_MODIFIABLE:
                createModifiableMenu(menu);
                break;
            case 2:
                createModifiableTopMenu(menu);
                break;
        }
    }

    private void createModifiableTopMenu(SwipeMenu menu) {
        final SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
        deleteItem.setBackground(new ColorDrawable(0xffff3b30));
        deleteItem.setTitle(getString(R.string.delete_message));
        deleteItem.setTitleColor(0xffffffff);
        deleteItem.setTitleSize(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_text_size));
        deleteItem.setTypedValue(TypedValue.COMPLEX_UNIT_PX);
        deleteItem.setWidth(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_width));
        menu.addMenuItem(deleteItem);
    }

    private void createModifiableMenu(SwipeMenu menu) {
        final SwipeMenuItem topItem = new SwipeMenuItem(getActivity().getApplicationContext());
        topItem.setBackground(new ColorDrawable(0xffc7c7cc));
        topItem.setTitle(getString(R.string.xiu_top));
        topItem.setTitleColor(0xff313131);
        topItem.setTitleSize(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_text_size));
        topItem.setTypedValue(TypedValue.COMPLEX_UNIT_PX);
        topItem.setWidth(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_width));
        menu.addMenuItem(topItem);

        final SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
        deleteItem.setBackground(new ColorDrawable(0xffff3b30));
        deleteItem.setTitle(getString(R.string.delete_message));
        deleteItem.setTitleColor(0xffffffff);
        deleteItem.setTitleSize(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_text_size));
        deleteItem.setTypedValue(TypedValue.COMPLEX_UNIT_PX);
        deleteItem.setWidth(getResources().getDimensionPixelSize(R.dimen.swipe_menu_item_width));
        menu.addMenuItem(deleteItem);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_wrapper) {
            showAddDialog();
        }
    }

    private void showAddDialog() {
        final View customView = LayoutInflater.from(getActivity()).inflate(R.layout.galary_content, null);
        final EditText editText = (EditText) customView.findViewById(R.id.xiu_add_edittext);
        editText.setHint(getString(R.string.input_galary_tip));
        editText.requestFocus();
        BillboardDialogFragment.createBuilder(getActivity())
                .setTitle(getString(R.string.add_galary_message))
                .setCustomView(customView)
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        final String inputText = editText.getText().toString();
                        if (TextUtils.isEmpty(inputText)) {
                            showMessage(getString(R.string.empty_xiu_galary_message));
                        } else if (isDuplicateName(inputText)) {
                            showMessage(getString(R.string.exist_xiu_galary_message));
                        } else {
                            final XiuInfo xiuInfo = getXiuInfoByInputText(inputText);
                            mXiuInfos.add(xiuInfo);
                            mAdapter.notifyDataSetChanged();

                            EventBus.getDefault().post(new CoverEvent());
                        }
                    }
                })
                .setTag(getString(R.string.add_galary_tag))
                .showSinglton();
    }

    private XiuInfo getXiuInfoByInputText(String inputText) {
        final XiuInfo xiuInfo = new XiuInfo();
        xiuInfo.setTask_name(inputText);
        xiuInfo.setCreate_time(String.valueOf(System.currentTimeMillis()));
        xiuInfo.setTask_id(UUIDGenerator.getUUID());
        xiuInfo.setShowType(XiuInfo.TYPE_MODIFIABLE);
        xiuInfo.setUid(GlobalDataHolder.getInstance(getActivity()).getUid());
        return xiuInfo;
    }

    private boolean isDuplicateName(String inputText) {
        for (XiuInfo xiuInfo : mXiuInfos) {
            if (xiuInfo.getTask_name().equals(inputText)) {
                return true;
            }
        }
        return false;
    }

    public void onEvent(TabChangeEvent event) {
        final String className = event.getClassName();
        if (className.equals(((Object) this).getClass().getSimpleName()) && mSwipeMenuListView != null && mSwipeMenuListView.isOpen()) {
            mSwipeMenuListView.closeMenu();
        }
    }

    public void onEvent(SameTabClickEvent event) {
        final String className = event.getClassName();
        if (className.equals(((Object) this).getClass().getSimpleName()) && mSwipeMenuListView != null && mSwipeMenuListView.getFirstVisiblePosition() != 0) {
            mSwipeMenuListView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
