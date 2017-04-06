package org.nogizaka46.utils;


import java.text.SimpleDateFormat;
import java.util.Date;



public class MyUtil {


     //将时间戳转日期
     public  static  String  timeToDate(String time){
         String res;
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
         long lt = new Long(time);
         Date date = new Date(lt);
         res = simpleDateFormat.format(date);
         return res;
     }



}
