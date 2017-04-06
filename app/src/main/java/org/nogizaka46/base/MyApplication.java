package org.nogizaka46.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


/**
 * Created by acer on 2016/11/15.
 */

public class MyApplication extends Application{

    public static MyApplication app;

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

    }
}
