package org.nogizaka46.utils;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.nogizaka46.R;

/**
 * Created by acer on 2017/4/26.
 */

public class UMShareUtil {



 public  static void shareUrl(Activity activity, String url, String title, String summary, @Nullable String thumb, UMShareListener umShareListener){
        UMImage image =null;
       // if(thumb !=null){
       //  image = new UMImage(activity, thumb);
        //}else{
             image = new UMImage(activity, R.drawable.logo);
        //}

        new ShareAction((Activity) activity).setPlatform(SHARE_MEDIA.QQ)
                .withMedia(getUmengWeb(url,title,summary,image))
                .setDisplayList(SHARE_MEDIA.QQ)
              .setCallback(umShareListener).open();
//                       .setCallback(umShareListener)
//                      .share();

    }

    private static UMWeb getUmengWeb(String url,String title ,String des,UMImage image){
        UMWeb  web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(des);//描述
        return  web ;
    }



}
