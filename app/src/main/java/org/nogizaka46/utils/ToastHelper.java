package org.nogizaka46.utils;

import android.content.Context;
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


    }



