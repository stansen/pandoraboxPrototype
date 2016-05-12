package com.rxjavahttprequest.model;



import com.rxjavahttprequest.BuildConfig;

/**
 * Created by Gstansen on 2015/11/25.
 */
public class BaseRequestModel {
    public String System="Android";
    public String SystemVersion ="5.0";
    public int AppVersion = BuildConfig.VERSION_CODE;


    public void setBaseCommonModel(BaseRequestModel baseRequestModel){
        this.SystemVersion = baseRequestModel.SystemVersion;
        AppVersion = baseRequestModel.AppVersion;
    }
}
