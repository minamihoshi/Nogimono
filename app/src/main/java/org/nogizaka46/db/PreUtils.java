package org.nogizaka46.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bodhixu on 2016/10/10.
 * SharedPreference工具类
 */
public class PreUtils {

    /**
     * 获得 SharedPreferences 实例
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return  PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 写入boolean数据
     * @param context
     * @param key
     * @param value
     */
    public static void writeBoolean(Context context, String key, boolean value) {
        //获得Editor
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        //写数据
        editor.putBoolean(key, value);
        //提交
        editor.commit();
    }

    /**
     * 读取boolean数据
     * @param context
     * @param key
     * @return
     */
    public static boolean readBoolean(Context context,  String key){
        //读数据
        return getSharedPreferences(context).getBoolean(key, false);
    }

    /**
     * 写入String数据
     * @param context
     * @param key
     * @param value
     */
    public static void writeString(Context context, String key, String value) {
        //获得Editor
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        //写数据
        editor.putString(key, value);
        //提交
        editor.commit();
    }

    /**
     * 读取String数据
     * @param context
     * @param key
     * @return
     */
    public static String readStrting(Context context,  String key){
        //读数据
        return getSharedPreferences(context).getString(key, "");
    }

    /**
     * 写入int数据
     * @param context
     * @param key
     * @param value
     */
    public static void writeInt(Context context, String key, int value) {
        //获得Editor
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        //写数据
        editor.putInt(key, value);
        //提交
        editor.commit();
    }

    /**
     * 读取int数据
     * @param context
     * @param key
     * @return
     */
    public static int readInt(Context context,  String key){
        //读数据
        return getSharedPreferences(context).getInt(key, 0);
    }
}
