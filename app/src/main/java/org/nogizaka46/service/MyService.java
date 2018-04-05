package org.nogizaka46.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.util.Log;


import org.nogizaka46.R;
import org.nogizaka46.config.Constant;
import org.nogizaka46.contract.IApiService;
import org.nogizaka46.http.HttpUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;



/**
 * Created by acer on 2016/11/30.
 */

public class MyService extends Service {
    //private Subscription subscription ;
    private NotificationCompat.Builder builder;
    private NotificationManager manager ;
    Notification notification;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       String urlstring = intent.getStringExtra(Constant.SERVICEDOWNLOAD);
        Log.e("TAG", "onHandleIntent: "+urlstring );
        IApiService retrofitInterface = HttpUtils.getInstance().getRetrofitInterface();
        Observable<ResponseBody> observable = retrofitInterface.getAPK(urlstring);
        // subscription =

                 observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {


                    @Override
                    public void onError(Throwable e) {
                        //stopSelf();
                        Log.e("TAG", "onError:service "+e );
                    }

                    @Override
                    public void onComplete() {

                    }


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "nogi.apk";
                        Log.e("TAG", "serviceonNext: "+filePath );
                        InputStream inputStream = responseBody.byteStream();
                        long totallen = responseBody.contentLength();
                        long lenplus = 0;
                        BufferedInputStream bis = new BufferedInputStream(inputStream);
                        BufferedOutputStream bos = null;

                        try {
                            OutputStream os = new FileOutputStream(filePath);
                            bos = new BufferedOutputStream(os);
                            int len = 0;
                            byte[] temp = new byte[1024 * 16];
                            while ((len = bis.read(temp)) != -1) {
                                bos.write(temp, 0, len);
                                lenplus += len;
                                Log.e("TAG", "onservice "+lenplus );
                                Log.e("TAG", "onNext: "+totallen );
                                int progress = (int) ((lenplus / (float) totallen) * 100);
                                 manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                 builder = new NotificationCompat.Builder(MyService.this);

                                Notification notification = builder.setSmallIcon(R.drawable.logo)
                                        .setProgress(100, progress, false)

                                        .setContentTitle("正在下载")
                                        .setContentText("已经下载" + progress + "%")
                                        .build();
                                manager.notify(0, notification);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (bis != null) {
                                    bis.close();
                                }
                                if (bos != null) {
                                    bos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                       Intent intent1 = new Intent();
                        intent1.setAction(Intent.ACTION_VIEW);
                        intent1.setDataAndType(Uri.fromFile(new File(filePath)),"application/vnd.android"+".package-archive");
                        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this,0,intent1,PendingIntent.FLAG_ONE_SHOT);
                        Notification notification = builder.setContentTitle("下载已完成")
                                .setContentText("点击安装")
                                .setContentIntent(pendingIntent)
                                .build();
                        manager.notify(0,notification);
                        Log.e("TAG", "onserviceaaaaaaa: finish" );

                    }
                });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (!subscription.isUnsubscribed()) {
//            subscription.unsubscribe();
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
