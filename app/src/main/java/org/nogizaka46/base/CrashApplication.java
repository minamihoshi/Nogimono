package org.nogizaka46.base;

import android.app.Application;

/**
 * Created by xyz on 2017/6/19.
 */

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(getApplicationContext());
    }

}
