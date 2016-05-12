package com.rxjavahttprequest.model;

/**
 * Created by Gstansen on 2015/11/25.
 */
public class BaseResponseModel {
    /**
     * 成功则返回0
     */
    public int Code;
    /**
     * 成功的时候该字段为空字符串,失败时则为错误信息
     */
    public String Message;
    /**
     * 返回的原始数据，有可能是加密的,请求失败时为空
     */
    public String Data;
}
