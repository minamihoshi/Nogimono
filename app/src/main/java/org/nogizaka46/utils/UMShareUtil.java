package org.nogizaka46.utils;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.Toast;

//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.nogizaka46.R;
import org.nogizaka46.ui.MainActivity;

/**
 * Created by acer on 2017/4/26.
 */

public class UMShareUtil {


    public static void shareUrl(Activity activity, String url, String title, String summary, @Nullable String thumb) {


        UMImage image = null;
        // if(thumb !=null){
        //  image = new UMImage(activity, thumb);
        //}else{
        image = new UMImage(activity, R.drawable.logo);
        //}

//        new ShareAction((Activity) activity).setPlatform(SHARE_MEDIA.QQ)
//                .withMedia(getUmengWeb(url,title,summary,image))
//                .setDisplayList(SHARE_MEDIA.QQ)
//              .setCallback(umShareListener).open();
//                       .setCallback(umShareListener)
//                      .share();UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
        new ShareAction(activity).withMedia(getUmengWeb(url, title, summary, image))
                .setDisplayList(SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener(activity)).open();

    }

    private static UMWeb getUmengWeb(String url, String title, String des, UMImage image) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(des);//描述
        return web;
    }

    // public static  void  ShareUrl
    public static UMShareListener shareListener(final Activity activity) {
        UMShareListener shareListener = new UMShareListener() {
            /**
             * @descrption 分享开始的回调
             * @param platform 平台类型
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            /**
             * @descrption 分享成功的回调
             * @param platform 平台类型
             */
            @Override
            public void onResult(SHARE_MEDIA platform) {
                //Toast.makeText(activity, "分享成功！", Toast.LENGTH_LONG).show();
                ToastHelper.Toastshow(activity,"分享成功！");
            }

            /**
             * @descrption 分享失败的回调
             * @param platform 平台类型
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                ToastHelper.Toastshow(activity,"分享失败！");
               // Toast.makeText(activity, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

            /**
             * @descrption 分享取消的回调
             * @param platform 平台类型
             */
            @Override
            public void onCancel(SHARE_MEDIA platform) {
               // Toast.makeText(activity, "分享取消！", Toast.LENGTH_LONG).show();
        ToastHelper.Toastshow(activity,"分享取消！");
            }
        };

return  shareListener;
    }
}