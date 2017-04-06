package org.nogizaka46.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        },2000);
    }
}
