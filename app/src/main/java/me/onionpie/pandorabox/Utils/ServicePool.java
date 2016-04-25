package me.onionpie.pandorabox.Utils;

import java.util.HashMap;


/**
 * Created by jiudeng007 on 2016/1/12.
 */
public class ServicePool {
//    public static ServicePool mInstance;
//    private HashMap<String, IInteractor> mServices;
//
//    private ServicePool() {
//        mServices = new HashMap<>();
//    }
//
//    public static ServicePool getInstance() {
//        if (mInstance == null) {
//            mInstance = new ServicePool();
//        }
//        return mInstance;
//    }
//
//    public <T extends IInteractor> T getService(Class<T> key) {
//        if (!mServices.containsKey(key.getName())) {
//            try {
//                Object o = key.newInstance();
//                IInteractor interactor = (IInteractor) o;
//                mServices.put(key.getName(), interactor);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return (T) mServices.get(key.getName());
//    }
}
