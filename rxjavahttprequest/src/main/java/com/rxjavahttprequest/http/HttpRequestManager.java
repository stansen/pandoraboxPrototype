package com.rxjavahttprequest.http;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.rxjavahttprequest.model.BaseRequestModel;
import com.rxjavahttprequest.model.BaseResponseModel;
import com.rxjavahttprequest.model.FileInput;
import com.rxjavahttprequest.service.BaseService;
import com.rxjavahttprequest.service.ServiceFactory;
import com.rxjavahttprequest.utils.FileDownloadUtils;
import com.rxjavahttprequest.utils.Sercurity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jiudeng009 on 2016/4/19.
 */
public class HttpRequestManager extends BaseHttpManager {

    private static HttpRequestManager sInstance;
    private BaseService mService;



    public static HttpRequestManager getInstance(Context context,String host){
        if (sInstance == null){
            synchronized (HttpRequestManager.class){
                if (sInstance == null){
                    sInstance = new HttpRequestManager(context,host);
                }
            }
        }
        return sInstance;
    }

    private HttpRequestManager(Context context,String host){
        super(context);
        mService = ServiceFactory.getInstance(context,host).getService(BaseService.class);
    }

    /**
     * 不加密的get请求
     * @param request 请求
     * @param path 请求url
     * @param type 请求成功返回的model类型
     * @param callback 请求回调
     * @param <T> 成功的model类型
     * @return
     */
    public <T> Subscription getExecute(final BaseRequestModel request, final String path, final Type type, final ResultCallback<T> callback){
        return Observable.just(request)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseRequestModel, Observable<BaseResponseModel>>() {
                    @Override
                    public Observable<BaseResponseModel> call(BaseRequestModel baseRequestModel) {
                        return mService.get(path, mGson.toJson(baseRequestModel));
                    }
                })
                .map(new Func1<BaseResponseModel, Object>() {
                    @Override
                    public Object call(BaseResponseModel baseResponseModel) {
                        return check(baseResponseModel, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            callback.error(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o == null){
                            Log.e("error", "is null");
                            return;
                        }
                        if (o instanceof String){
                            callback.error((String) o);
                        }
                        else if (o instanceof BaseResponseModel){
                            callback.failedData(((BaseResponseModel) o).Data);
                        }
                        else {
                            callback.success((T) o);
                        }
                    }
                });
    }

    /**
     * 加密的get请求
     * @param request 请求
     * @param path 请求url
     * @param type 请求成功返回的model类型
     * @param callback 请求回调
     * @param <T> 成功的model类型
     * @return
     */
    public <T> Subscription getSecureExecute(final BaseRequestModel request, final String path, final Type type, final ResultCallback<T> callback){
        final byte[] ak = Sercurity.generateKey();
        return Observable.just(request)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseRequestModel, Observable<BaseResponseModel>>() {
                    @Override
                    public Observable<BaseResponseModel> call(BaseRequestModel baseRequestModel) {
                        return mService.getSecure(path, secure(baseRequestModel, ak));
                    }
                })
                .map(new Func1<BaseResponseModel, Object>() {
                    @Override
                    public Object call(BaseResponseModel baseResponseModel) {
                        return checkSecure(baseResponseModel, ak, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            callback.error(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o == null){
                            Log.e("test error", "is null");
                            return;
                        }
                        if (o instanceof String){
                            Log.e("test error", (String) o);
                            callback.error((String) o);
                        }
                        else if (o instanceof BaseResponseModel){
                            callback.failedData(((BaseResponseModel) o).Data);
                        }
                        else {
                            callback.success((T) o);
                        }
                    }
                });
    }

    /**
     * 不加密的post请求
     * @param request 请求
     * @param path 请求url
     * @param type 请求成功返回的model类型
     * @param callback 请求回调
     * @param <T> 成功的model类型
     * @return
     */
    public <T> Subscription postExecute(final BaseRequestModel request, final String path, final Type type, final ResultCallback<T> callback){

        return Observable.just(request)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseRequestModel, Observable<BaseResponseModel>>() {
                    @Override
                    public Observable<BaseResponseModel> call(BaseRequestModel baseRequestModel) {
                        return mService.post(path, mGson.toJson(baseRequestModel));
                    }
                })
                .map(new Func1<BaseResponseModel, Object>() {
                    @Override
                    public Object call(BaseResponseModel baseResponseModel) {
                        Log.d("test json", baseResponseModel.Code + "  " + baseResponseModel.Data);
                        return check(baseResponseModel, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            callback.error(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o == null){
                            Log.e("error", "is null");
                            return;
                        }
                        if (o instanceof String){
                            callback.error((String) o);
                        }
                        else if (o instanceof BaseResponseModel){
                            callback.failedData(((BaseResponseModel) o).Data);
                        }
                        else {
                            callback.success((T) o);
                        }
                    }
                });
    }

    /**
     * 加密的post请求
     * @param request 请求
     * @param path 请求url
     * @param type 请求成功返回的model类型
     * @param callback 请求回调
     * @param <T> 成功的model类型
     * @return
     */
    public <T> Subscription postSecureExecute(final BaseRequestModel request, final String path, final Type type, final ResultCallback<T> callback){
        final byte[] ak = Sercurity.generateKey();
        return Observable.just(request)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseRequestModel, Observable<BaseResponseModel>>() {
                    @Override
                    public Observable<BaseResponseModel> call(BaseRequestModel baseRequestModel) {
                        return mService.postSecure(path, secure(baseRequestModel, ak));
                    }
                })
                .map(new Func1<BaseResponseModel, Object>() {
                    @Override
                    public Object call(BaseResponseModel baseResponseModel) {
                        Log.d("test json", baseResponseModel.Code + "  " + baseResponseModel.Data);
                        return checkSecure(baseResponseModel, ak, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            callback.error(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o == null){
                            Log.e("error", "is null");
                            return;
                        }
                        if (o instanceof String){
                            callback.error((String) o);
                        }
                        else if (o instanceof BaseResponseModel){
                            callback.failedData(((BaseResponseModel) o).Data);
                        }
                        else {
                            callback.success((T) o);
                        }
                    }
                });
    }

    /**
     * 加密的postFile请求
     * @param request 请求
     * @param files 发送的文件
     * @param path 请求url
     * @param type 请求成功返回的model类型
     * @param callback 请求回调
     * @param <T> 成功的model类型
     * @return
     */
    public <T> Subscription postFile(BaseRequestModel request, final List<FileInput> files, final String path, final Type type, final ResultCallback<T> callback){
        final byte[] ak = Sercurity.generateKey();
        return Observable.just(request)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseRequestModel, Observable<BaseResponseModel>>() {
                    @Override
                    public Observable<BaseResponseModel> call(BaseRequestModel baseRequestModel) {
                        return mService.postFile(path, secure(baseRequestModel, files, ak));
                    }
                })
                .map(new Func1<BaseResponseModel, Object>() {
                    @Override
                    public Object call(BaseResponseModel baseResponseModel) {
                        return checkSecure(baseResponseModel, ak, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException){
                            callback.error(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (o == null){
                            Log.e("error", "is null");
                            return;
                        }
                        if (o instanceof String){
                            callback.error((String) o);
                        }
                        else if (o instanceof BaseResponseModel){
                            callback.failedData(((BaseResponseModel) o).Data);
                        }
                        else {
                            callback.success((T) o);
                        }
                    }
                });
    }

    /**
     * 下载文件（支持断点下载）
     * @param url 下载文件url
     * @param dirName 下载文件夹
     * @param fileName 下载文件名
     * @param isContinue 是否支持断点下载
     * @param callback 下载回调
     * @return
     */
    public Subscription download(final String url, final String dirName, final String fileName, final boolean isContinue, final ResultCallback<File> callback){
        return Observable.just(url)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(String s) {
                        long currentBytes = 0;
                        File dir = new File(dirName);
                        if (!dir.exists()){
                            dir.mkdirs();
                        }
                        if (isContinue){
                           currentBytes = getCurrentBytes(dirName, fileName, url);
                        }
                        return mService.download(s, String.format("bytes=%d-", currentBytes));
                    }
                })
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody response) {
                        try {
                            return saveFile(response, url, dirName, fileName, isContinue, callback);
                        } catch (IOException e) {
                            Logger.e(e.getMessage());
                            throw Exceptions.propagate(e);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        e.printStackTrace();
                        if (e instanceof IOException) {
                            callback.error(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(File file) {
                        int id = FileDownloadUtils.generateId(url, dirName + File.separator + fileName);
                        if (file == null) {
                            Logger.e("下载失败");
                            callback.error("下载失败");
                        } else {
                            Logger.d("success");
                            mHelper.remove(id);
                            callback.success(file);
                        }
                    }
                });
    }


    public static abstract class ResultCallback<T>{
        /**
         * 请求失败回调
         * @param error 失败信息
         */
        public abstract void error(String error);
        /**
         * 请求失败回调
         * @param jsonData 失败的json数据
         */
        public void failedData(String jsonData){

        }

        /**
         * 请求成功回调
         * @param t 成功的model
         */
        public abstract void success(T t);

        /**
         * 进度回调
         * @param progress 当前进度
         */
        public void inProgress(float progress){

        };
    }

}
