/**
 *
 */
package com.sunquan.chimingfazhou.download.module;


import com.sunquan.chimingfazhou.download.http.MyDownloadAbstractRequest;
import com.sunquan.chimingfazhou.download.http.impl.MyDownloadFileRequest;

import java.io.File;

/**
 * @author sunquan
 */
public class DownloadModule {
    private String fileName;
    private String title;
    private long total;
    private long progress;
    private String downloadUrl;
    private boolean isChecked;
    private String albumName;
    private String albumUrl;

    private MyDownloadAbstractRequest downloadRequest;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public MyDownloadAbstractRequest getDownloadRequest() {
        return downloadRequest;
    }

    public void setDownloadRequest(MyDownloadAbstractRequest downloadRequest) {
        this.downloadRequest = downloadRequest;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getPlayPath() {
        // 下载好的文件
        final  String fileName = MyDownloadFileRequest.DOWNLOAD_PATH
                + this.fileName;
        final File file = new File(fileName);
        if(file.exists()) {
            return file.getAbsolutePath();
        } else {
            return downloadUrl;
        }
    }

}
