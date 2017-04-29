package org.nogizaka46.ui.activity;


import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.ui.WebPageActivity;
import org.nogizaka46.utils.EncryptUtils;
import org.nogizaka46.view.MyToast;
import org.nogizaka46.view.SweetAlertDialog;

public class ImageActivity extends BaseActivity {

    @InjectView(R.id.photoview)
    PhotoView photoview;

    private DownloadManager mDownloadManager = null;
    private String mFileName = "";
    private long downloadId;
    private String imagepath;
    private  Context mContext  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);

        mContext =ImageActivity.this;
        Intent intent = getIntent();
        imagepath = intent.getStringExtra("image");
        Glide.with(this).load(imagepath).thumbnail(0.1f).crossFade().placeholder(R.drawable.loading).into(photoview);

        photoview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new SweetAlertDialog(ImageActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(getResources().getString(R.string.dialog_titles))

                        .setContentText(getResources().getString(R.string.dialog_content)).setConfirmText(getResources().getString(R.string.ok)).setCancelText(getResources().getString(R.string.cancel)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        if (Build.VERSION.SDK_INT >= 23) {

                            sweetAlertDialog.dismiss();

                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                initDownloadManager(imagepath);//下载文件后的读写文件属于危险权限,因此要动态的申请权限
                            } else {
                                ActivityCompat.requestPermissions(ImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }

                        } else {
                            sweetAlertDialog.dismiss();
                            initDownloadManager(imagepath);
                        }
                    }

                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();
                    }
                }).show();

                return false;
            }
        });
    }



    private void initDownloadManager(String urlstring) {

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mFileName = urlstring;
        String path = EncryptUtils.md5(urlstring) + ".jpg";
        //File destFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.jpg");
//        if (destFile.exists()) {
//            destFile.delete();
//        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlstring))
                .setTitle("文件下载")
                .setDescription(urlstring)
                .setVisibleInDownloadsUi(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, path)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setMimeType("image/jpeg||image/png||image/gif");
        downloadId = mDownloadManager.enqueue(request);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户授予权限
                    initDownloadManager(imagepath);
                } else {
                    // 用户拒绝权限
                    finish();
                }
                return;
            }

        }
    }

}
