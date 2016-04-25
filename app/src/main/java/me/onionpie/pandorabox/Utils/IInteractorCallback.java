package me.onionpie.pandorabox.Utils;

/**
 * Created by jiudeng009 on 2016/3/14.
 */
public abstract class IInteractorCallback<T>{

    public abstract void success(T t);

    public abstract void ServiceError(String msg);

    public void failed(String dataJson){

    }
}
