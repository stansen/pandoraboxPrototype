package me.onionpie.pandorabox.Service;

import me.onionpie.pandorabox.Model.ResponseModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jiudeng007 on 2016/5/11.
 */
public interface UserService {
    @GET("user/register")
    Observable<ResponseModel> register(@Query("user_name") String name, @Query("user_pwd") String password);
}
