package utils;


import java.text.SimpleDateFormat;
import java.util.Date;



public class MyUtil {


     //将时间戳转日期
     public  static  String  timeToDate(String time){
         SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日");
         long lcc = Long.valueOf(time);
         int i = Integer.parseInt(time);
         String times = sdr.format(new Date(i * 1000L));
         return times;
     }



}
