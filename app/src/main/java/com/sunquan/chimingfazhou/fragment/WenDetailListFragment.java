package com.sunquan.chimingfazhou.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.baizhi.baseapp.controller.BaseHandler;
import com.baizhi.baseapp.util.NetworkState;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.activity.WenDetailActivity;
import com.sunquan.chimingfazhou.adapter.DownloadAdapter;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.http.impl.MyDownloadFileRequest;
import com.sunquan.chimingfazhou.download.manager.DownloadManager;
import com.sunquan.chimingfazhou.download.module.DownloadModule;
import com.sunquan.chimingfazhou.download.util.Constants;
import com.sunquan.chimingfazhou.download.util.MD5;
import com.sunquan.chimingfazhou.download.util.Util;
import com.sunquan.chimingfazhou.event.ChangeSongEvent;
import com.sunquan.chimingfazhou.event.CoverDownloadEvent;
import com.sunquan.chimingfazhou.models.WenDetailInfo;
import com.sunquan.chimingfazhou.models.WenDetailListItem;
import com.sunquan.chimingfazhou.service.MusicPlayService;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 闻详情章节列表页
 * <p/>
 * Created by Administrator on 2015/5/11.
 */
public class WenDetailListFragment extends AppBaseFragment implements View.OnClickListener, DownloadAdapter.OnDownloadItemClickedListener {

    private ListView mDownloadList;
    private DownloadAdapter mAdapter;
    private ArrayList<DownloadModule> mModules;
    private DownloadManager mDownloadManager;
    private int mNotification_id;
    private MusicPlayService mService;

    private View headerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        final MyApplication application = (MyApplication) getActivity().getApplication();
        mService = application.getService();
        mService.setActivityContext(getActivity());
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        final View view = inflater.inflate(R.layout.fragment_wen_list_layout, container, false);
        initData();
        initView(view);
        return view;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initData() {
        //创建下载目录
        final File appDataDir = new File(Constants.APP_DATA_PATH);
        if (!appDataDir.exists()) {
            appDataDir.mkdir();
        }
        //初始化下载管理类
        mDownloadManager = new DownloadManager(getActivity());
        mDownloadManager.addDownloadHandler(new MyHandler(this, "downloadTest"));
        final WenDetailInfo detailInfo = (WenDetailInfo) getArguments().getSerializable(WenDetailActivity.WEN_EXTRA);
        mService.setWenDetailInfo(detailInfo);
        //组装下载播放元数据
        assumeDownModules(detailInfo);
    }

    /**
     * 组装下载播放的数据
     *
     * @return
     */
    private WenDetailInfo assumeDownModules(WenDetailInfo detailInfo) {
        final ArrayList<WenDetailListItem> mListInfos = detailInfo.getList();
        mModules = new ArrayList<>();
        final DownloadModule currentSong = mService.getCurrentSong();
        for (WenDetailListItem item : mListInfos) {
            final DownloadModule dm = new DownloadModule();
            dm.setChecked(false);
            dm.setProgress(0);
            dm.setTotal(Long.valueOf(item.getSize()));
            dm.setTitle(item.getTitle());
            dm.setDownloadUrl(item.getDownload_url());
            dm.setFileName(MD5.getMD5(item.getDownload_url() + item.getTitle()) + getSuffix(item.getDownload_url()));
            dm.setAlbumName(detailInfo.getIntroduction().getTitle());
            dm.setAlbumUrl(detailInfo.getIntroduction().getThumbnail());
            if (currentSong != null && currentSong.getTitle().equals(dm.getTitle()) && currentSong.getAlbumName().equals(dm.getAlbumName())) {
                dm.setChecked(true);
            }
            mModules.add(dm);
        }
        return detailInfo;
    }

    private String getSuffix(String download_url) {
        final int dotIndex = download_url.lastIndexOf(".");
        if (dotIndex > 0 && download_url.length() < dotIndex) {
            return download_url.substring(dotIndex);
        }
        return ".amr";
    }

    private void initView(View view) {
        mDownloadList = (ListView) view.findViewById(R.id.download_list);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.download_list_header, mDownloadList, false);
        ((TextView) headerView.findViewById(R.id.headerText)).setText(String.format(getString(R.string.download_header_message), mModules.size()));
        mDownloadList.addHeaderView(headerView);
        mAdapter = new DownloadAdapter(getActivity(), this, this);
        mAdapter.setItems(mModules);
        mDownloadList.setAdapter(mAdapter);
    }

    /**
     * 下载，包含网络检查
     *
     * @param dm
     * @param holder
     */
    private void executeDownloadWithCheckNet(final DownloadModule dm, final DownloadAdapter.MyViewHolder holder) {
        if (!NetworkState.isActiveNetworkConnected(getActivity())) {
            showMessage(R.string.no_network);
        } else if (!NetworkState.isWifiNetworkConnected(getActivity())) {
            SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager())
                    .setTitle(getString(R.string.warm_tip))
                    .setMessage(getString(R.string.download_tip_message))
                    .setPositiveButtonText(android.R.string.ok)
                    .setNegativeButtonText(android.R.string.cancel)
                    .setPositiveListener(new IPositiveButtonDialogListener() {
                        @Override
                        public void onPositiveButtonClicked(int requestCode) {
                            executeDownload(holder, dm);
                        }
                    })
                    .setTag("download_prompt")
                    .showSinglton();
        } else {
            executeDownload(holder, dm);
        }
    }

    /**
     * 下载
     *
     * @param holder
     * @param dm
     */
    private void executeDownload(DownloadAdapter.MyViewHolder holder, DownloadModule dm) {
        if (holder != null) {
            holder.progress.setText(Util.getPercentFormat(
                    dm.getProgress(), dm.getTotal()));
            if (holder.pause.getVisibility() != View.VISIBLE) {
                mAdapter.showSpecialBtn(holder.pause, holder);
            }
        }
        final MyDownloadFileRequest dfr = mDownloadManager.download(dm, false);
        dm.setDownloadRequest(dfr);
        showMessage(dm.getTitle() + getString(R.string.begin_download_message));
    }

    /**
     * 删除
     *
     * @param dm
     * @param holder
     */
    private void executeDelete(DownloadModule dm, DownloadAdapter.MyViewHolder holder) {
        if (holder != null) {
            if (holder.download.getVisibility() != View.VISIBLE) {
                mAdapter.showSpecialBtn(holder.download, holder);
            }
            holder.progress.setText(Util.convertFileSize(dm.getTotal()));
        }
        // 删除下载好的文件
        final String fileName = MyDownloadFileRequest.DOWNLOAD_PATH
                + dm.getFileName();
        final File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        dm.setProgress(0);
        dm.setDownloadRequest(null);
        showMessage(dm.getTitle() + getString(R.string.download_delete_message));
    }

    /**
     * 暂停
     *
     * @param dm
     * @param holder
     */
    private void executePause(DownloadModule dm, DownloadAdapter.MyViewHolder holder) {
        if (holder != null) {
            if (holder.continuee.getVisibility() != View.VISIBLE) {
                mAdapter.showSpecialBtn(holder.continuee, holder);
            }
        }
        if (dm.getDownloadRequest() != null) {
            dm.getDownloadRequest().abortRequest();
        }
        showMessage(dm.getTitle() + getString(R.string.download_pause_message));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (DownloadModule dm : mModules) {
            if (dm.getDownloadRequest() != null) {
                if (!dm.getDownloadRequest().hasPaused()) {
                    dm.getDownloadRequest().abortRequest();
                }
                dm.setDownloadRequest(null);
            }
        }
        EventBus.getDefault().unregister(this);
        mService.setActivityContext(null);
    }

    @SuppressWarnings("unused")
    public void onEvent(ChangeSongEvent event) {
        final DownloadModule downloadModule = event.getDownloadModule();
        if (downloadModule != null) {
            for (DownloadModule item : mModules) {
                item.setChecked(false);
                if (item.getAlbumName().equals(downloadModule.getAlbumName()) && item.getTitle().equals(downloadModule.getTitle())) {
                    item.setChecked(true);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void onEvent(CoverDownloadEvent coverEvent) {
        final TextView tv = (TextView) headerView.findViewById(R.id.headerTextDownload);
        if(coverEvent.isFirst){
            tv.setVisibility(View.GONE);
        }else{
            tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 很关键的一个方法，根据模型对象（或者位置）找到对应的progress相关的VIEW
     *
     * @param dm
     * @return
     */
    private DownloadAdapter.MyViewHolder getViewHolderByDM(DownloadModule dm) {
        DownloadAdapter.MyViewHolder holder = null;
        if (dm != null) {
            int index = mModules.indexOf(dm);
            if (mDownloadList.getHeaderViewsCount() > 0) {
                index += mDownloadList.getHeaderViewsCount();
            }
            int firstVisible = mDownloadList.getFirstVisiblePosition();
            int lastVisible = mDownloadList.getLastVisiblePosition();
            // 判断是否可见，若可见，才更新进度
            if (index <= lastVisible && index >= firstVisible) {
                if (index - firstVisible >= 0) {
                    View tmp = mDownloadList.getChildAt(index - firstVisible);
                    Object tag = tmp.getTag();
                    if (tag instanceof DownloadAdapter.MyViewHolder) {
                        holder = (DownloadAdapter.MyViewHolder) tag;
                    }
                }
            }
        }
        return holder;
    }

    @Override
    public void onClick(View v) {
        final DownloadModule dm = (DownloadModule) v.getTag();
        final DownloadAdapter.MyViewHolder holder;
        if (dm == null) {
            return;
        }
        holder = getViewHolderByDM(dm);
        switch (v.getId()) {
            case R.id.download:
            case R.id.continu:
                executeDownloadWithCheckNet(dm, holder);
                break;
            case R.id.pause:
                executePause(dm, holder);
                break;
            case R.id.complete:
                executeDelete(dm, holder);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClicked(final int position, final DownloadModule item) {
        if (item.isChecked()) {
            return;
        }
        mService.setCurrentListItem(position);
        mService.setSongs(mModules);
        mService.playMusicWithNetCheck(position, mModules.get(position).getPlayPath());
    }

    private void showNotification(DownloadModule dm) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(dm.getAlbumName() + "-" + dm.getTitle() + getString(R.string.download_success_message))
                .setContentTitle(dm.getAlbumName() + "-" + dm.getTitle() + getString(R.string.download_success_message))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis());

        final Notification notification = builder.build();
        notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        final NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // 发送通知
        manager.notify(getActivity().getPackageName(), mNotification_id, notification);
        mNotification_id++;
    }

    static class MyHandler extends BaseHandler {
        private SoftReference<WenDetailListFragment> fragmentRf;

        public MyHandler(WenDetailListFragment fragment, String id) {
            this.fragmentRf = new SoftReference<>(fragment);
            this.setId(id);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
                HashMap<String, Object> callbackObjs = null;
                DownloadModule dm = null;
                if (msg.obj instanceof HashMap) {
                    callbackObjs = (HashMap<String, Object>) msg.obj;
                }
                if (callbackObjs != null) {
                    dm = (DownloadModule) callbackObjs.get("DownloadModule");
                }
                if (fragmentRf.get() == null || fragmentRf.get().getActivity()==null) {
                    return;
                }
                switch (msg.what) {
                    case DownloadManager.DOWNLOAD_ERROR:
                        downloadFailOpt(callbackObjs, dm);
                        break;
                    case DownloadManager.DOWNLOAD_PROGRESS_CHANGE:
                        downloadProgressChangedOpt(msg, dm);
                        break;
                    case DownloadManager.DOWNLOAD_SUCCESS:
                        downloadSuccessOpt(dm);
                        fragmentRf.get().showNotification(dm);
                        break;
                    default:
                        break;
                }

        }

        /**
         * 下载成功
         *
         * @param dm
         */
        private void downloadSuccessOpt(DownloadModule dm) {
            if (dm == null) {
                return;
            }
            if (dm.getDownloadRequest() != null) {
                dm.setDownloadRequest(null);
            }
            final DownloadAdapter.MyViewHolder holder = fragmentRf.get().getViewHolderByDM(
                    dm);
            if (holder != null) {
                fragmentRf.get().mAdapter.showSpecialBtn(holder.open, holder);
                holder.progress.setText(Util.convertFileSize(dm.getTotal()));
            }
        }


        /**
         * 下载中
         *
         * @param msg
         * @param dm
         */
        private void downloadProgressChangedOpt(Message msg,
                                                DownloadModule dm) {
            if (dm == null) {
                return;
            }
            int progress = msg.arg1;
            int total = msg.arg2;
            final DownloadAdapter.MyViewHolder holder = fragmentRf.get().getViewHolderByDM(
                    dm);
            if (holder != null) {
                dm.setTotal(total);
                dm.setProgress(progress);
                holder.progress.setText(Util.getPercentFormat(progress,
                        total));
                if (holder.pause.getVisibility() != View.VISIBLE) {
                    fragmentRf.get().mAdapter.showSpecialBtn(holder.pause,
                            holder);
                }
            }
        }

        /**
         * 下载失败
         *
         * @param callbackObjs
         * @param dm
         */
        private void downloadFailOpt(HashMap<String, Object> callbackObjs, DownloadModule dm) {
            if (callbackObjs == null) {
                return;
            }
            final String errorMsg;
            final RequestException exception = (RequestException) callbackObjs.get("exception");
            // 如果是暂停
            if (exception.getErrorCode() == MyDownloadFileRequest.DOWNLOAD_PAUSE_CODE) {
                errorMsg = null;
            } else if (dm != null) {
                errorMsg = dm.getTitle() + fragmentRf.get().getString(R.string.download_error);
            } else {
                errorMsg = fragmentRf.get().getString(R.string.download_error);
            }
            if (!TextUtils.isEmpty(errorMsg)) {
                fragmentRf.get().showMessage(errorMsg);
            }
            final DownloadAdapter.MyViewHolder holder = fragmentRf.get().getViewHolderByDM(
                    dm);
            if (TextUtils.isEmpty(errorMsg)) {
                //暂停下载
                if (holder != null && holder.continuee.getVisibility() != View.VISIBLE) {
                    fragmentRf.get().mAdapter.showSpecialBtn(
                            holder.continuee, holder);
                }
            }
            else {
                //下载失败
                if (holder != null && holder.download.getVisibility() != View.VISIBLE) {
                    fragmentRf.get().mAdapter.showSpecialBtn(
                            holder.download, holder);
                    if(dm!=null) {
                        holder.progress.setText(Util.convertFileSize(dm.getTotal()));
                    }
                }
            }

            if (dm != null && dm.getDownloadRequest() != null) {
                dm.setDownloadRequest(null);
            }
        }
    }

    @Override
    protected void handleClickEvent(int event) {
    }

    @Override
    protected boolean isShowTitle() {
        return false;
    }

}
