package me.onionpie.pandorabox.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/10/29.
 */
public class DateFormat {
    /**
     * 将时间戳格式化为指定格式日期
     *
     * @param dataFormat 指定格式  eg:yyyy-MM-dd
     * @param timeStamp  时间戳
     * @return
     */
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        String length = timeStamp + "";
        if (length.length() < 11)
            timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat, Locale.CHINA);
        result = format.format(new Date(timeStamp));
        return result;
    }

    public static String getDefaultFormatData(long timeStamp) {
        //将php时间戳转换为java时间戳
        String length = timeStamp + "";
        if (length.length() < 11)
            timeStamp = timeStamp * 1000;
        String result = "";
        if (timeStamp == 0) {
            return "";
        }
        result = SimpleDateFormat.getDateInstance().format(timeStamp);
        return result;
    }


}
