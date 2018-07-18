package org.nogizaka46.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;


import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.Config;
//import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.UMShareAPI;


import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.nogizaka46.config.Constant;


/**
 * Created by acer on 2016/11/15.
 */

public class MyApplication extends CrashApplication{

    public static MyApplication app;
    public static LiteOrm liteOrm;

    {
//       // Config.DEBUG =true;
//        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
//        PlatformConfig.setQQZone("1106775372", "UJGmoo8s6KmidsOm");
        PlatformConfig.setWeixin("wxce1e4e4621e4df08", "0e872fd849bcfc7ba176320da2e62752");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3406898728", "a50a3887f12a605f40615bfae3782f2f","http://sns.whalecloud.com");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        // 安装tinker
        Beta.installTinker();
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
        UMConfigure.init(this,"58f2565aaed1794325000617","umeng",UMConfigure.DEVICE_TYPE_PHONE,"0bea6cba16ac1f7de972208856cca21f");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

       //UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
     //   UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"Av2gEb6UkgFhIlapicypgtNQQZBOklu40chu13yJQX3s");
        Bugly.init(getApplicationContext(), "497ee12d1d", false);

//        Config.isJumptoAppStore = true;
//        UMShareAPI.get(this);
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
        mPushAgent.setPushCheck(true);
        HuaWeiRegister.register(this);
        MiPushRegistar.register(this,"2882303761517835298","5601783574298");
        MeizuRegister.register(this, "1001072", "327ca30195784d659abc9908f7920034");

       //友盟分享注册
     //   UMConfigure.init(this,"5a12384aa40fa3551f0001d1","umeng",UMConfigure.DEVICE_TYPE_PHONE,"58edcfeb310c93091c000be2 5965ee00734be40b580001a0");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        initAccessTokenWithAkSk();

    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
               // hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                //alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(),  "sp7EL79u595XGfWuyRTZ1yM2", "WRsq72PgIcAE4aYIghGvBSfQeTXMLmIM");
    }

}
