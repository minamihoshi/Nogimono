package org.nogizaka46.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


import org.nogizaka46.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BaseActivity extends AppCompatActivity {
    public Handler handler;
    public List<Map<String, Object>> mSelfData;
    ImmersionBar immersionBar;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mSelfData = new ArrayList<Map<String, Object>>();

         immersionBar = ImmersionBar.with(this);
         immersionBar
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true,0.2f)
                .init();
    }
    protected void setTranslucentStatus() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }
    @TargetApi(Build.VERSION_CODES.M)
    protected void requestRuntimePermission() {
        int hasPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
            }, 1);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void setBackgroundAlpha(float bgalpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgalpha;
        this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
         immersionBar.destroy();
    }
}
