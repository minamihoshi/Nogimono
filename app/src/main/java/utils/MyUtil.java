package utils;


import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyUtil {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



     //将时间戳转日期
     public  static  String  timeToDate(String times){
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
         String sd = sdf.format(new Date(Long.parseLong(times)));
         return sd;
     }

}
