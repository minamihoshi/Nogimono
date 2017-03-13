package utils;


import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import org.nogizaka46.Main2Frag_Tab2;
import org.nogizaka46.R;

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
    //用ShapeDrawable画个圆
    public static ShapeDrawable buildAvatarCircleOverlay(Context context) {
        //获取半径
        int radius = 666;
        ShapeDrawable overlay = new ShapeDrawable(new RoundRectShape(null, new RectF(
                Main2Frag_Tab2.sScreenWidth / 2 - dpToPx(context,Constants.CIRCLE_RADIUS_DP * 2),
                Main2Frag_Tab2.sProfileImageHeight / 2 - dpToPx(context,Constants.CIRCLE_RADIUS_DP* 2),
                Main2Frag_Tab2.sScreenWidth / 2 - dpToPx(context,Constants.CIRCLE_RADIUS_DP * 2),
                Main2Frag_Tab2.sProfileImageHeight / 2 - dpToPx(context,Constants.CIRCLE_RADIUS_DP * 2)),
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius}));
        overlay.getPaint().setColor(context.getResources().getColor(R.color.gray));
        return overlay;
    }
    public static int dpToPx(Context context,int dp) {
        return Math.round((float) dp * context.getResources().getDisplayMetrics().density);
    }
}
