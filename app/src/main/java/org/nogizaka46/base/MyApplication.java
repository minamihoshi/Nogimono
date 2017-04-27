package org.nogizaka46.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.nogizaka46.config.Constant;


/**
 * Created by acer on 2016/11/15.
 */

public class MyApplication extends Application{

    public static MyApplication app;
    public static LiteOrm liteOrm;

    {
        Config.DEBUG =true;
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    public MyApplication() {
         app =this;
    }

    public static MyApplication getInstance() {
        if (app == null) {
            app = new MyApplication();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Config.isJumptoAppStore = true;
        UMShareAPI.get(this);
        if (liteOrm == null) {
            // 使用级联操作
            DataBaseConfig config = new DataBaseConfig(this, Constant.DB_NAME);
            config.debugged = true; // open the log
            config.dbVersion = 1; // set database version
            config.onUpdateListener = null; // set database update listener
            liteOrm = LiteOrm.newCascadeInstance(config);// cascade
        }
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //mPushAgent.setNoDisturbMode(0, 0, 0, 0);
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("TAG", "onSuccess: "+deviceToken );
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("TAG", "onFailure: "+s+s1 );
            }
        });

    }
}
