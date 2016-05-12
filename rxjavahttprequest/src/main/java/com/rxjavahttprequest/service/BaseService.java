package com.rxjavahttprequest.service;



import com.rxjavahttprequest.model.BaseResponseModel;
import com.rxjavahttprequest.model.Response;
import com.rxjavahttprequest.model.UserInfo;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by jiudeng009 on 2016/4/18.
 */
public interface BaseService {

    @POST
    Observable<Response<UserInfo>> login(@Url String url, @QueryMap Map<String, String> params);

    @Multipart
    @POST("{path}")
    Observable<BaseResponseModel> postFile(@Path("path") String path, @PartMap Map<String, RequestBody> params);

    @POST("{path}")
    Observable<BaseResponseModel> postSecure(@Path("path") String path, @QueryMap Map<String, String> params);

    @POST("{path}")
    Observable<BaseResponseModel> post(@Path("path") String path, @Query("Data") String dataJson);

    @GET("{path}")
    Observable<BaseResponseModel> getSecure(@Path("path") String path, @QueryMap Map<String, String> params);

    @GET("{path}")
    Observable<BaseResponseModel> get(@Path("path") String path, @Query("Data") String dataJson);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String downloadUrl, @Header("Range") String startBytes);

}
