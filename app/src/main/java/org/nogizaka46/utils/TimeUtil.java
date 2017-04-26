package org.nogizaka46.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtil {


     //将时间戳转日期
     public  static  String  timeToDate(int time){
         long lt = new Long(time);
         return millis2String(lt*1000L);
     }

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    private  static String millis2String(long millis) {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(millis));
    }

}
