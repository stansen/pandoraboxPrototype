package com.rxjavahttprequest.model;

public class Response<T> {
//    响应是否成功
    boolean success;
//    失败信息
    String info;
    String jsessonid;
//    返回的响应数据
    T data;
//    页面请求响应

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getJsessonid() {
        return jsessonid;
    }

    public void setJsessonid(String jsessonid) {
        this.jsessonid = jsessonid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "BaseResponse{" +
                "success=" + success +
                ", info='" + info + '\'' +
                ", jsessonid='" + jsessonid + '\'' +
                ", data=" + data +
                '}';
    }

}
