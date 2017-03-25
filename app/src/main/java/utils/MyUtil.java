package utils;


import java.text.SimpleDateFormat;
import java.util.Date;



public class MyUtil {


     //将时间戳转日期
     public  static  String  timeToDate(String times){
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
         String sd = sdf.format(new Date(Long.parseLong(times)));
         return sd;
     }



}
