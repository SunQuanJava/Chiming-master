/**
 *
 */
package com.sunquan.chimingfazhou.download.http.impl;

import android.content.Context;
import android.text.TextUtils;

import com.sunquan.chimingfazhou.download.exception.RequestException;
import com.sunquan.chimingfazhou.download.http.MyDownloadAbstractRequest;
import com.sunquan.chimingfazhou.download.util.Constants;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author sunquan
 */
public class MyDownloadFileRequest extends MyDownloadAbstractRequest<String> {
    private static final String REQUEST_HEADER_KEY_RANGE = "Range";
    public static final String DOWNLOAD_PATH = Constants.APP_DATA_PATH
            + "/downloadTest/";
    public static final int DOWNLOAD_PAUSE_CODE = 201;
    /**
     * 临时文件扩展名
     */
    public static final String TEMP_FILE_EXT_NAME = ".tmp";
    private static final int BUFFER_SIZE = 1024;
    private static final String RESPONSE_HEADER_KEY_FILE_LENGTH = "Content-Length";
    /**
     * 单个文件下载进度最大更新次数
     */
    private static final int PROGRESS_MAX_UPDATE_COUNT = 25;
    private static final String TAG = MyDownloadFileRequest.class.getSimpleName();
    int curDownloadedLength = 0;
    private BufferedInputStream bins = null;
    private InputStream is = null;
    private File curFile;
    private RandomAccessFile raf;
    private String mFileSavePath;
    private boolean mIsRedownload;

    //代码仅供交流学习，请勿盗作他用，转载请注明出处。创造良好的共同学习环境，大家才会有动力去分享。
    public MyDownloadFileRequest(Context context, String fileName, boolean isRedownload) {
        super(context);
        if (!TextUtils.isEmpty(fileName)) {
            // 把文件变为临时文件
            this.mFileSavePath = DOWNLOAD_PATH + fileName + TEMP_FILE_EXT_NAME;
            File f = new File(mFileSavePath);
            // 如果文件已经存在则从文件末尾写入,此处记录已下载的长度
            if (f.exists() && f.isFile()) {
                curDownloadedLength = (int) (f.length());
            }
        }
        this.mIsRedownload = isRedownload;
        this.isTest = Constants.isTest;
    }


    public MyDownloadFileRequest(Context context, String id, String fileName,
                                 String updateTime, boolean isRedownload, boolean isWorkingGuide) {
        super(context);
        if (isWorkingGuide) {
            if (!TextUtils.isEmpty(fileName)) {
                // 把文件变为临时文件
                this.mFileSavePath = DOWNLOAD_PATH + fileName
                        + TEMP_FILE_EXT_NAME;
                File f = new File(mFileSavePath);
                // 如果文件已经存在则从文件末尾写入,此处记录已下载的长度
                if (f.exists() && f.isFile()) {
                    curDownloadedLength = (int) (f.length());
                }
            }
        } else {
            if (!TextUtils.isEmpty(fileName)) {
                // 把文件变为临时文件
                this.mFileSavePath = DOWNLOAD_PATH + fileName
                        + TEMP_FILE_EXT_NAME;
                File f = new File(mFileSavePath);
                // 如果文件已经存在则从文件末尾写入,此处记录已下载的长度
                if (f.exists() && f.isFile()) {
                    curDownloadedLength = (int) (f.length());
                }
            }
        }
        this.mIsRedownload = isRedownload;
        this.isTest = Constants.isTest;

    }


    @Override
    protected String buildTestData() {
        int count = 0;
        while (count < 100) {
            if (paused) {
                break;
            }
            count++;
            if (this.getTaskRefer() != null) {
                this.getTaskRefer().updateProgress(count, 100);
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.buildTestData();
    }

    @Override
    public Object parseResponse(HttpResponse response) throws RequestException {
        if (TextUtils.isEmpty(getFileSavePath())) {
            throw new RequestException("file path is empty");
        }
        JSONObject result = new JSONObject();
        this.curFile = new File(getFileSavePath());
        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == HttpStatus.SC_OK
                || responseCode == HttpStatus.SC_PARTIAL_CONTENT) {
            try {
                is = response.getEntity().getContent();

                try {
                    int fl = getFileSize(response);
                    saveFileContent(fl);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    String errorMsg = "can not obtain file size";
                    throw new Exception(errorMsg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String exMsg = "parse response faild:" + e.getMessage();
                if (paused) {
                    exMsg = "下载已停止";
                }
                throw new RequestException(DOWNLOAD_PAUSE_CODE,
                        exMsg);
            } finally {
                closeStream();
            }
        } else {
            RequestException he = new RequestException(responseCode,
                    "invoke server interface faild " + "[" + responseCode + "]");
            throw he;
        }
        try {
            result.putOpt("state", "true");
            result.putOpt("msg", "文件下载成功");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 读取服务端文件，并将其保存到本地
     */
    private void saveFileContent(int fileSize) throws Exception {
        if (TextUtils.isEmpty(getFileSavePath())) {
            throw new Exception("file path is empty");
        }
        if (mIsRedownload) {
            if (curFile.exists()) {
                // 由于服务端在此种情况下会返回完整文件内容，因此这里只要删除文件即可，不需要重新发送请求
                curFile.delete();
                curDownloadedLength = 0;
            }
        }
        // 文件更新进度次数的计数器
        int updateCount = 1;
        this.bins = new BufferedInputStream(is);
        try {
            if (!curFile.exists()) {
                curFile.createNewFile();
            }
            // 初始化当前进度,告知调用者文件总长度
            // 异步回调
            if (this.getTaskRefer() != null) {
                this.getTaskRefer().updateProgress(curDownloadedLength,
                        fileSize);
            }
        } catch (IOException ep) {
            ep.printStackTrace();
        }
        raf = new RandomAccessFile(curFile, "rw");

        // 将文件指针移到文件末尾
        raf.seek(curDownloadedLength);

        byte[] nextContentBytes = new byte[BUFFER_SIZE];
        int bytesRead = 0;

        if (fileSize > BUFFER_SIZE)// 如果文件长度大于BUFFER，要分段读取，以免内存溢出
        {
            while (curDownloadedLength < fileSize
                    && (bytesRead = readByBytes(nextContentBytes, bins,
                    fileSize - curDownloadedLength)) != -1) {
                if (paused) {
                    throw new InterruptedException();
                }
                raf.write(nextContentBytes, 0, bytesRead);

                curDownloadedLength += bytesRead;
                int unitLength = fileSize / PROGRESS_MAX_UPDATE_COUNT;
                // 这里控制单个文件进度更新次数不超过PROGRESS_MAX_UPDATE_COUNT次
                if ((curDownloadedLength >= unitLength * updateCount)
                        || bytesRead < BUFFER_SIZE) {
                    // 异步回调mListener.onProgressChanged
                    if (this.getTaskRefer() != null) {
                        this.getTaskRefer().updateProgress(curDownloadedLength,
                                fileSize);
                    }
                    updateCount++;
                }
            }

        } else {
            bytesRead = readByBytes(nextContentBytes, bins, -1);
            if (bytesRead == -1) {
                throw new RequestException("stream is empty");
            }
            raf.write(nextContentBytes, 0, bytesRead);
            curDownloadedLength += bytesRead;
            // 异步回调mListener.onProgressChanged
            if (this.getTaskRefer() != null) {
                this.getTaskRefer().updateProgress(curDownloadedLength,
                        fileSize);
            }
        }
        // 下载完毕后，更名
        if (curFile != null && getFileSavePath().endsWith(TEMP_FILE_EXT_NAME)) {
            String newName = getFileSavePath().substring(0,
                    getFileSavePath().lastIndexOf(TEMP_FILE_EXT_NAME));
            File newFile = new File(newName);
            // TODO 考虑 是否需要删除已存在的文件?
            curFile.renameTo(newFile);
        }
    }


    @Override
    public int getRequestMethod() {
        return HTTP_GET;
    }


    @Override
    public HashMap<String, String> getRequestParams() {
        return null;
    }


    @Override
    protected String parseResult(Object result) throws Exception {
        JSONObject jo = (JSONObject) result;
        boolean state = Boolean.valueOf(jo.optString("state"));
        if (state) {
            return jo.optString("msg");
        } else {
            int code = jo.optInt("code");
            throw new RequestException(code, jo.optString("msg"));
        }
    }


    private String getHeaderValue(HttpResponse res, String headerName) {
        Header[] headers = res.getHeaders(headerName);
        if (headers != null && headers.length > 0) {
            return headers[0].getValue();
        } else {
            return null;
        }

    }

    private int getFileSize(HttpResponse res) throws NumberFormatException {
        String fileLengthStr = "1";
        String range = getHeaderValue(res,
                "Content-Range");
        if (!TextUtils.isEmpty(range)) {
//        Content-Range: bytes 4942848-11030731/11030732
            fileLengthStr = range.substring(range.lastIndexOf("/") + 1);
        } else {
            fileLengthStr = getHeaderValue(res,
                    RESPONSE_HEADER_KEY_FILE_LENGTH);
        }
        int fl = Integer.valueOf(fileLengthStr);
        return fl;
    }

    /**
     * 除非流已经结束，否则至少读取Math.min(bytes.length,remainSize)个字节
     *
     * @param bytes
     * @param is
     * @param remainSize 当前文件的剩余量，如果值大于0，表示当前文件还未读完的大小
     * @return 实际读取的字节数，除了当前文件已经读完外，一般都返回bytes.length
     * @throws java.io.IOException
     */
    private int readByBytes(byte[] bytes, InputStream is, int remainSize)
            throws IOException {
        int b = -1;
        int count = 0;
        int temp = remainSize > 0 ? Math.min(bytes.length, remainSize)
                : bytes.length;
        //逐字读取，避免由于网络原因而导致read(buffer)返回-1。
        while (count < temp && (b = is.read()) != -1) {
            bytes[count] = (byte) b;
            count++;
        }
        if (b == -1) {
            return -1;
        } else {
            return count;
        }
    }


    private void closeStream() {
        try {
            if (raf != null) {
                raf.close();
            }
            if (is != null) {
                is.close();
            }
            if (bins != null) {
                bins.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public String getFileSavePath() {
        return mFileSavePath;
    }


    @Override
    public ArrayList<Header> getHeaders() {
        ArrayList<Header> tempH = super.getHeaders();
        if (tempH == null) {
            tempH = new ArrayList<Header>();
        }
        tempH.add(getBreakPointHeader());
        return tempH;
    }


    private BasicHeader getBreakPointHeader() {
        StringBuffer breakPointStr = new StringBuffer("bytes=");
        breakPointStr.append(curDownloadedLength);
        breakPointStr.append("-");
        return new BasicHeader(REQUEST_HEADER_KEY_RANGE,
                breakPointStr.toString());
    }
}
