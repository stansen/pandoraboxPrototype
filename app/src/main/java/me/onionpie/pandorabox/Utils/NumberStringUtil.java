package me.onionpie.pandorabox.Utils;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by jiudeng007 on 2015/12/7.
 */
public class NumberStringUtil {
    public static String getJdPrice(double servicePrice) {
        if (servicePrice == 0) {
            String price = "¥ 0";
            return price;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String price = "¥ "+ decimalFormat.format(servicePrice / 1000);
        String temp = String.valueOf(servicePrice);
//        Log.d("number_util",price);
        return price;
    }
}
