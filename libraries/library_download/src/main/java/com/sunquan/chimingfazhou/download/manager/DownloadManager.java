package com.sunquan.chimingfazhou.download.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.http.impl.MyDownloadFileRequest;
import com.sunquan.chimingfazhou.download.module.DownloadModule;
import com.sunquan.chimingfazhou.download.util.DownloadCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class DownloadManager {
    public static final int QUERY_ERROR = -1;
    public static final int QUERY_SUCCESS = 1;
    public static final int DOWNLOAD_ERROR = -2;
    public static final int DOWNLOAD_SUCCESS = 2;
    public static final int DOWNLOAD_PROGRESS_CHANGE = 3;
    private ArrayList<Handler> downloadHandlers;
    private Context mContext;


    public DownloadManager(Context context) {
        this.mContext = context;
    }


    public void addDownloadHandler(Handler handler) {
        if (downloadHandlers == null) {
            downloadHandlers = new ArrayList<>();
        }
        if (!downloadHandlers.contains(handler)) {
            downloadHandlers.add(handler);
        }
    }


    public MyDownloadFileRequest download(DownloadModule dm,
                                          boolean isRedownload) {
        final HashMap<String, Object> callbackObjs = new HashMap<String, Object>();
        callbackObjs.put("DownloadModule", dm);
        if (dm == null) {
            callbackObjs.put("exception", new RequestException("DownloadModule info is null."));
            sendDownloadMsg(callbackObjs, DOWNLOAD_ERROR, -1, -1);
            return null;
        }
        File f = new File(MyDownloadFileRequest.DOWNLOAD_PATH);
        boolean createPathSuccess = true;
        if (!f.exists()) {
            createPathSuccess = false;
            if (f.mkdirs()) {
                createPathSuccess = true;
            }
        }
        if (!createPathSuccess) {
            callbackObjs.put("exception", new RequestException("create file path failed."));
            sendDownloadMsg(callbackObjs, DOWNLOAD_ERROR, -1, -1);
            return null;
        }
        MyDownloadFileRequest dfr = new MyDownloadFileRequest(mContext, dm.getFileName(), isRedownload);
        dfr.setUrl(dm.getDownloadUrl());
        dfr.sendDownloadRequestInBg(new DownloadCallback<String>() {

            @Override
            public void onDownloadProgressChanged(int progress, int total) {
                sendDownloadMsg(callbackObjs, DOWNLOAD_PROGRESS_CHANGE,
                        progress, total);
            }


            @Override
            public void internalDone(String returnValue,
                                     RequestException excetion) {
                if (excetion != null) {
                    callbackObjs.put("exception", excetion);
                    sendDownloadMsg(callbackObjs, DOWNLOAD_ERROR, -1, -1);
                } else {
                    sendDownloadMsg(callbackObjs, DOWNLOAD_SUCCESS, -1, -1);
                }
            }
        });
        return dfr;
    }

    private void sendDownloadMsg(HashMap<String, Object> callbackObjs,
                                 int what, int progress, int total) {
        if (downloadHandlers != null && downloadHandlers.size() > 0) {
            for (Handler handler : downloadHandlers) {
                switch (what) {
                    case DOWNLOAD_ERROR:
                        if (handler != null) {
                            Message msg = handler.obtainMessage(DOWNLOAD_ERROR);
                            msg.obj = callbackObjs;
                            msg.sendToTarget();
                        }
                        break;
                    case DOWNLOAD_PROGRESS_CHANGE:
                        if (handler != null) {
                            Message msg = handler.obtainMessage(DOWNLOAD_PROGRESS_CHANGE);
                            msg.arg1 = progress;
                            msg.arg2 = total;
                            msg.obj = callbackObjs;
                            msg.sendToTarget();
                        }
                        break;
                    case DOWNLOAD_SUCCESS:
                        if (handler != null) {
                            Message msg = handler.obtainMessage(DOWNLOAD_SUCCESS);
                            msg.obj = callbackObjs;
                            msg.sendToTarget();
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    }
}
