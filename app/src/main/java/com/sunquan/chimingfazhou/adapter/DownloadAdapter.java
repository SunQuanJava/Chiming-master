package com.sunquan.chimingfazhou.adapter;

import java.io.File;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baizhi.baseapp.adapter.BasePageAdapter;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.download.http.impl.MyDownloadFileRequest;
import com.sunquan.chimingfazhou.download.module.DownloadModule;
import com.sunquan.chimingfazhou.download.util.Util;
import com.sunquan.chimingfazhou.widget.CheckedText;

/**
 * 下载播放列表的adapter
 */
public class DownloadAdapter extends BasePageAdapter<DownloadModule> {

    private int curPos;
    private OnClickListener mBtnListener;
    private OnDownloadItemClickedListener mOnItemClicked;

    public DownloadAdapter(Context context,
                           OnClickListener btnListener,OnDownloadItemClickedListener onItemClickedListener) {
        super(context);
        this.mBtnListener = btnListener;
        this.mOnItemClicked = onItemClickedListener;
    }

    public void setSelectedPos(int index) {
        if (index >= 0 && index < getCount()) {
            curPos = index;
        } else {
            curPos = -1;
        }
    }

    public int getSelectedPos() {
        return curPos;
    }

    @Override
    protected int getResource(int position) {
        return R.layout.downloadlist_item;
    }

    @Override
    protected void renderConvertView(final int position,final View convertView,final ViewGroup parent) {
        MyViewHolder holder = (MyViewHolder) convertView.getTag();
        if(holder == null) {
            // 初始化VIEW HOLDER
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }

        // 设置按钮监听，并将DownloadModule对象保存到按钮的tag中供按钮点击时使用
        final DownloadModule item = getItem(position);
        holder.download.setTag(item);
        holder.open.setTag(item);
        holder.pause.setTag(item);
        holder.continuee.setTag(item);
        holder.progress.setTag(item);

        holder.download.setOnClickListener(mBtnListener);
        holder.open.setOnClickListener(mBtnListener);
        holder.pause.setOnClickListener(mBtnListener);
        holder.continuee.setOnClickListener(mBtnListener);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClicked.onItemClicked(position,item);
            }
        });

        // 临时文件
        final String tempFileName = MyDownloadFileRequest.DOWNLOAD_PATH
                + item.getFileName()
                + MyDownloadFileRequest.TEMP_FILE_EXT_NAME;
        final File tempFile = new File(tempFileName);

        // 下载好的文件
        final  String fileName = MyDownloadFileRequest.DOWNLOAD_PATH
                + item.getFileName();
        final File file = new File(fileName);
        // 如果存在下载任务，并且下载任务正在进行，则显示“暂停”按钮
        if (item.getDownloadRequest() != null
                && !item.getDownloadRequest().hasPaused()) {
            holder.progress.setText(Util.getPercentFormat(
                    tempFile.length(), item.getTotal()));
            showSpecialBtn(holder.pause, holder);
        }
        // 如果临时文件存在，则显示“继续”按钮
        else if (tempFile.exists()) {
            holder.progress.setText(Util.getPercentFormat(
                     tempFile.length(), item.getTotal()));
            showSpecialBtn(holder.continuee, holder);
        }
        // 如果已经下载好，则显示“打开”按钮
        else if (file.exists()) {
            holder.progress.setText(
                    Util.convertFileSize(item.getTotal()));
            showSpecialBtn(holder.open, holder);
        }
        // 其他情况显示“下载”按钮
        else {
            holder.progress.setText(
                    Util.convertFileSize(item.getTotal()));
            showSpecialBtn(holder.download, holder);
        }
        holder.title.setText(item.getTitle());
        holder.title.setChecked(item.isChecked());
    }

    public void showSpecialBtn(View showBtnArea,
                                MyViewHolder holder) {
        holder.continuee.setVisibility(View.GONE);
        holder.download.setVisibility(View.GONE);
        holder.pause.setVisibility(View.GONE);
        holder.open.setVisibility(View.GONE);
        showBtnArea.setVisibility(View.VISIBLE);
    }

    /**
     * 某一项的点击的监听
     */
    public interface OnDownloadItemClickedListener {
        void onItemClicked(int position,DownloadModule item);
    }

    public static class MyViewHolder extends BasePageAdapter.ViewHolder{
        public CheckedText title;
        public TextView progress;
        public ImageView download;
        public ImageView open;
        public ImageView pause;
        public ImageView continuee;

        public MyViewHolder(View convertView) {
            super(convertView);
            title = (CheckedText) convertView.findViewById(R.id.title);
            progress = (TextView) convertView.findViewById(R.id.progress);
            download = (ImageView) convertView.findViewById(R.id.download);
            continuee = (ImageView) convertView.findViewById(R.id.continu);
            pause = (ImageView) convertView.findViewById(R.id.pause);
            open = (ImageView) convertView.findViewById(R.id.complete);
        }
    }
}
