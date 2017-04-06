package org.nogizaka46.utils;

import android.os.Environment;


import org.nogizaka46.base.MyApplication;

import java.io.File;


/**
 * Created by wanggang on 2016/11/14.
 *
 * 文件管理
 */

public class FileManger {



    public  static File getRootCacheFile(){
        if(Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)){
            return MyApplication.getInstance().getExternalCacheDir();
        }else {

            return MyApplication.getInstance().getCacheDir();
        }
    }


    public static File getImageCacheFile(){

        return new File(getRootCacheFile(),"image");
    }


    public static File getHttpCache(){
        return new File(getRootCacheFile(),"http");
    }
}
