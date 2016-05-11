package me.onionpie.pandorabox.Model;

/**
 * Created by jiudeng007 on 2016/5/11.
 */
public class ResponseModel {
    public boolean error;
    public int errorCode;
    public String errorMsg;
    public Object result;

    @Override
    public String toString() {
        return "ResponseModel{" +
                "error=" + error +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", result=" + result +
                '}';
    }
}
