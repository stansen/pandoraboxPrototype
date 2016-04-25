package me.onionpie.pandorabox.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

/**
 * Created by stansen on 2015/4/12.
 */
public class NetWorkCheck {
    /**
     * 对网络连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            NetworkInfo[] info = connManager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
    //获取本机wifi的ip地址
    public static String getIpAddress(WifiManager wifiManager){
        if(!wifiManager.isWifiEnabled()){
            return "wifi isn't enable";
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }
    //获取便携式热点开启状态
    public static boolean isWifiApOpened(WifiManager wifiManager){
        try{
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            return (Integer)method.invoke(wifiManager) == 13;
        }catch (Exception e){
            return false;
        }
    }

    public static String getApAddress(){
        return "192.168.43.1";
    }

    private static String intToIp(int i){
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
}
