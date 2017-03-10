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
    //处理请求异常
    public static Boolean getRequestStutas(String RequestStutas) {
        Boolean eStutas;
        if (RequestStutas.equals("100")) {//无网络
            eStutas = false;
        } else if (RequestStutas.equals("600")) {//连接超时,请确认你的网络是否连接
            eStutas = false;
        } else if (RequestStutas.equals("300")) {
            eStutas = false;
        } else if (RequestStutas.equals("400")) {
            eStutas = false;
        } else if (RequestStutas.equals("500")) {
            eStutas = false;
        } else if (RequestStutas.equals("")) {
            eStutas = false;
        } else {
            eStutas = true;//正常登陆
        }
        return eStutas;
    }

    // dip转像素
    public static int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;
    }

    // 像素转dip
    public static float PixelsToDip(Context context, int Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float dips = Pixels / SCALE;
        return dips;
    }
     //将时间戳转日期
     public  static  String  timeToDate(String times){
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
         String sd = sdf.format(new Date(Long.parseLong(times)));
         return sd;
     }

}
