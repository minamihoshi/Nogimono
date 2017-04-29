package org.nogizaka46.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.umeng.message.inapp.InAppMessageManager;
import com.umeng.message.inapp.UmengSplashMessageActivity;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;

public class SplashActivity extends UmengSplashMessageActivity {
    @Override
    public boolean onCustomPretreatment() {
        InAppMessageManager mInAppMessageManager = InAppMessageManager.getInstance(this);
        //设置应用内消息为Debug模式
        mInAppMessageManager.setInAppMsgDebugMode(true);
        //参数为Activity的完整包路径，下面仅是示例代码，请按实际需求填写
        mInAppMessageManager.setMainActivityPath("org.nogizaka46.ui.MainActivity");
        return super.onCustomPretreatment();
    }

    //        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                SplashActivity.this.finish();
//            }
//        },2000);
}
