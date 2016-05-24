package me.onionpie.pandorabox.Service;

import me.onionpie.pandorabox.Model.ResponseModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jiudeng007 on 2016/5/11.
 */
public interface UserService {
    @GET("jfinal_demo/user/register")
    Observable<ResponseModel> register(@Query("user_name") String name, @Query("user_pwd") String password);

    @GET("jfinal_demo/user/login")
    Observable<ResponseModel> login(@Query("user_name") String name, @Query("user_pwd") String password);
}
