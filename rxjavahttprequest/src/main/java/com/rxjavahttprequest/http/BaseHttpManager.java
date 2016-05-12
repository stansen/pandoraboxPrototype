package com.rxjavahttprequest.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.rxjavahttprequest.exception.FileDownloadOutOfSpaceException;
import com.rxjavahttprequest.model.BaseRequestModel;
import com.rxjavahttprequest.model.BaseResponseModel;
import com.rxjavahttprequest.model.EncryptRequestModel;
import com.rxjavahttprequest.model.FileDownloadModel;
import com.rxjavahttprequest.model.FileDownloadStatus;
import com.rxjavahttprequest.model.FileInput;
import com.rxjavahttprequest.utils.FileDownloadDBHelper;
import com.rxjavahttprequest.utils.FileDownloadUtils;
import com.rxjavahttprequest.utils.IFileDownloadDBHelper;
import com.rxjavahttprequest.utils.Sercurity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by jiudeng009 on 2016/4/19.
 */
public class BaseHttpManager {


    protected Gson mGson;
    protected Context mContext;
    protected IFileDownloadDBHelper mHelper;
    private Handler mHandler;


    public BaseHttpManager(Context context){
        mGson = new Gson();
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        mHelper = new FileDownloadDBHelper(context);
    }

    /**
     * 不加密的请求map
     * @param request 请求
     * @return 请求map
     */
    protected HashMap<String, String> noSecure(BaseRequestModel request){
        HashMap<String, String> params = new HashMap<>();
        String json = mGson.toJson(request);
        params.put("Data", json);
        return params;
    }

    /**
     * 加密的请求map
     * @param request 请求
     * @param ak AK
     * @return 请求map
     */
    protected HashMap<String, String> secure(BaseRequestModel request, byte[] ak){
        HashMap<String, String> params = new HashMap<>();
        String json = mGson.toJson(request);
        EncryptRequestModel encryptRequestModel = Sercurity.encryptRequest(ak, json);
        params.put("Key", encryptRequestModel.Key);
        params.put("Data", encryptRequestModel.Data);
        return params;
    }

    /**
     * 发送文件的加密请求map
     * @param request 请求
     * @param files 发送的文件
     * @param ak AK
     * @return 请求map
     */
    protected HashMap<String, RequestBody> secure(BaseRequestModel request, List<FileInput> files, byte[] ak){
        HashMap<String, RequestBody> params = new HashMap<>();
        String json = mGson.toJson(request);
        EncryptRequestModel encryptRequestModel = Sercurity.encryptRequest(ak, json);
        RequestBody requestBodyKey = RequestBody.create(MediaType.parse("text/plain"), encryptRequestModel.Key);
        RequestBody requestBodyData = RequestBody.create(MediaType.parse("text/plain"), encryptRequestModel.Data);
        params.put("Key", requestBodyKey);
        params.put("Data", requestBodyData);
        for (FileInput file : files){
            RequestBody requestBody = RequestBody.create(MediaType.parse(guessMimeType(file.filename)), file.file);
            params.put(file.key + "\"; filename=\"" + file.filename, requestBody);
        }
        return params;
    }

    /**
     * 获取文件mime类型
     *
     * @param path 文件路径
     * @return 文件类型
     */
    protected String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 返回结果处理
     * @param baseResponseModel 返回结果
     * @param type 正确的返回类型
     * @return 返回的对象
     */
    protected Object check(BaseResponseModel baseResponseModel, Type type){
        Logger.t("code").d(baseResponseModel.Code + " " + baseResponseModel.Message);
        switch (baseResponseModel.Code){
            case 0:
                Log.d("json", baseResponseModel.Data);
                if ("null".equals(baseResponseModel.Data)){
                    return "没有更多数据";
                }
                else {
                    Logger.t("json").json(baseResponseModel.Data);
                    return mGson.fromJson(baseResponseModel.Data, type);
                }
            case 1:
                Logger.t("failed json").json(baseResponseModel.Data);
                return baseResponseModel;
            default:
                return baseResponseModel.Message;

        }
    }

    /**
     * 返回结果解密等处理
     * @param baseResponseModel 返回结果
     * @param ak AK
     * @param type 正确的返回类型
     * @return 返回的对象
     */
    protected Object checkSecure(BaseResponseModel baseResponseModel, byte[] ak, Type type){
        Logger.t("code").d(baseResponseModel.Code + " " + baseResponseModel.Message);
        switch (baseResponseModel.Code){
            case 0:
                if ("null".equals(baseResponseModel.Data)){
                    return "没有更多数据";
                }
                else {
                    decode(baseResponseModel, ak);
                    Logger.t("json").json(baseResponseModel.Data);
                    return mGson.fromJson(baseResponseModel.Data, type);
                }
            case 1:
                decode(baseResponseModel, ak);
                Logger.t("failed json").json(baseResponseModel.Data);
                return baseResponseModel;
            default:
                return baseResponseModel.Message;
        }
    }

    /**
     * 解密
     * @param baseResponseModel 返回结果
     * @param ak AK
     * @return 解密结果
     */
    protected String decode(BaseResponseModel baseResponseModel, byte[] ak) {
        try {
            baseResponseModel.Data = Sercurity.aesDecrypt(baseResponseModel.Data, ak);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResponseModel.Data;
    }

    /**
     * 获取暂停前下载的文件大小
     * @param dirName 下载文件夹
     * @param fileName 文件名
     * @param url 下载url
     * @return
     */
    protected long getCurrentBytes(String dirName, String fileName, String url){
        long currentBytes = 0;
        File file = new File(dirName, fileName);
        int id = FileDownloadUtils.generateId(url, dirName + File.separator + fileName);
        FileDownloadModel model = mHelper.find(id);
        if (model != null && model.getStatus() == FileDownloadStatus.paused && file.exists()){
            currentBytes = model.getSoFar();
        }
        else {
            currentBytes = 0;
            if (file.exists()){
                file.delete();
               }
        }
        Logger.t("currentBytes").d(currentBytes + "");

        return currentBytes;
    }

    /**
     * 保存文件
     * @param response 请求响应
     * @param url 下载url
     *@param destFileDir 保存文件夹
     * @param destFileName 保存文件名
     * @param isContinue 是否支持断点下载
     * @param callback 回调     @return
     * @throws IOException
     */
    protected File saveFile(ResponseBody response, String url, String destFileDir, String destFileName, boolean isContinue, final HttpRequestManager.ResultCallback callback) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[1024 * 4];
        int len;
//        FileOutputStream fos = null;
        RandomAccessFile accessFile = null;
        long total = 0;
        long sum = 0;
        File file = new File(destFileDir, destFileName);

        try {
            is = response.byteStream();
            total = response.contentLength() + file.length();
            accessFile = getRandomAccessFile(file, isContinue, total);
            FileDescriptor fd = accessFile.getFD();
            sum = accessFile.length() == total ? 0 : accessFile.length();
//            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                accessFile.write(buf, 0, len);
                final long finalSum = sum;
                final long finalTotal = total;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(finalSum * 1.0f / finalTotal);
                    }
                });
            }
            fd.sync();
            Logger.t("length").d(file.length() + "");
            return file;
        }
        finally {
            int id = FileDownloadUtils.generateId(url, destFileDir + File.separator + destFileName);
            FileDownloadModel model = mHelper.find(id);
            if (model == null){
                model = new FileDownloadModel();
                model.setId(id);
                model.setPath(destFileDir + File.separator + destFileName);
                model.setUrl(url);
                model.setSoFar(sum);
                model.setTotal(total);
                model.setStatus(sum == total ? FileDownloadStatus.completed : FileDownloadStatus.paused);
                mHelper.insert(model);
            }else if (sum != total){
                mHelper.updatePause(id, sum);
            }
            if (is != null) is.close();
            if (accessFile != null) accessFile.close();
        }
    }

    private RandomAccessFile getRandomAccessFile(File file, final boolean isContinue, final long totalBytes)
            throws IOException {

        if (file.exists() && file.isDirectory()) {
            throw new RuntimeException(String.format("found invalid internal destination path[%s]," +
                    " & path is directory[%B]", file.getAbsolutePath(), file.isDirectory()));
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException(String.format("create new file error  %s", file.getAbsolutePath()));
            }
        }

        RandomAccessFile outFd = new RandomAccessFile(file, "rw");
        long breakpointBytes = 0;
        // check the available space bytes whether enough or not.
        if (totalBytes > 0) {
            breakpointBytes = outFd.length();

            final long requiredSpaceBytes = totalBytes - breakpointBytes;
            Logger.t("当前,总,还需").d(breakpointBytes + " " + totalBytes + " " + requiredSpaceBytes);
            final long freeSpaceBytes = FileDownloadUtils.getFreeSpaceBytes(file.getAbsolutePath());
            Logger.t("path").d(file.getAbsolutePath());

            if (freeSpaceBytes < requiredSpaceBytes) {
                outFd.close();
                // throw a out of space exception.
                throw new FileDownloadOutOfSpaceException(freeSpaceBytes,
                        requiredSpaceBytes, breakpointBytes);
            }
            else if (requiredSpaceBytes <= 0){
                breakpointBytes = 0;
            }
        }

        if (isContinue) {
            outFd.seek(breakpointBytes);
        }

        return outFd;
    }

}
