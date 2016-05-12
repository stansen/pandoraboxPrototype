package com.rxjavahttprequest.service;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.rxjavahttprequest.R;
import com.rxjavahttprequest.http.HttpsUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by jiudeng009 on 2016/4/18.
 */
public class ServiceFactory {

    private static ServiceFactory sInstance;
    private Retrofit mRetrofit;
    private Context mContext;
    private HashMap<String, Object> mServiceCache;

    public static ServiceFactory getInstance(Context context,String host){
        if (sInstance == null){
            synchronized (ServiceFactory.class){
                if (sInstance == null){
                    sInstance = new ServiceFactory(context,host);
                }
            }
        }
        return sInstance;
    }

    public ServiceFactory(Context context,String host){
        OkHttpClient client = new OkHttpClient();
        try {
            client = client.newBuilder()
                    .sslSocketFactory(HttpsUtils.getSslSocketFactory(context.getAssets().open("yytd.bks"), context.getResources().getString(R.string.password_bks)))
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            request = request.newBuilder()
                                    .addHeader("test", "for header")
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();
        } catch (IOException e) {
            Log.d("error", e.getMessage());
            e.printStackTrace();
        }
//        "http://10.0.0.12:20380/"
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(client)
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        mRetrofit = builder.build();
        mContext = context;
        mServiceCache = new HashMap<>();

    }

    /**
     * 获取对应的服务
     * @param service 服务
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> service){

        T t = (T) mServiceCache.get(service.getName());
        if (t == null){
            t = mRetrofit.create(service);
            mServiceCache.put(service.getName(), t);
        }
        return t;
    }



}
