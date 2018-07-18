package org.nogizaka46.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by acer on 2016/11/15.
 */

public class ToastHelper {

    private static Toast toast;


    public static void showToast(Context context, String text, int time) {
        if (toast == null) {
            toast = Toast.makeText(context, text, time);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
    public static void Toastshow(final Context context, final String text) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                    Looper.prepare();
//                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
//                    Looper.loop();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    }



