package org.nogizaka46;


import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.Constants;
import view.MyToast;
import view.SweetAlertDialog;


public class WebPageActivity extends BaseActivity {
    @InjectView(R.id.top_button_back) ImageButton img_left_layout;
    @InjectView(R.id.webview)  WebView webview;
    @InjectView(R.id.title)
    TextView head;
    String previews;
    String url;
    Context context = WebPageActivity.this;
    private DownloadManager mDownloadManager = null;
    private String mFileName = "";
    private long downloadId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpager);
        ButterKnife.inject(this);
        img_left_layout.setVisibility(View.VISIBLE);
        initwebview();
    }

    private void initwebview() {
        if(getIntent()!=null&&getIntent().getExtras()!=null){
            previews=getIntent().getStringExtra("preview");
           url = Constants.New_Base_Url1 + previews;
        }
        webview.loadUrl(url);
        WebSettings webSetting = webview.getSettings();
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setSupportMultipleWindows(true);//设置WebView是否支持多窗口。如果设置为true
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口，默认false
        webSetting.setAllowFileAccess(true);//是否允许访问文件，默认允许
        webSetting.setDisplayZoomControls(false);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);//为图片添加放大缩小功能
        webSetting.setLoadWithOverviewMode(true);
        DecimalFormat format = new DecimalFormat("0.00");
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        String formatResult = format.format((float)(outMetrics.widthPixels) / (float)668); //668为html页面的宽度
        //设置初始缩放大小  100%   屏幕宽度 / 网页设置的宽度
        webview.setInitialScale((int)(Float.valueOf(formatResult) *100));
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                MyToast.showText(context, getResources().getString(R.string.web_error), Toast.LENGTH_SHORT, false);
            }
        });
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult hitTestResult = webview.getHitTestResult();
                if(hitTestResult.getType()== WebView.HitTestResult.IMAGE_TYPE|| hitTestResult.getType()== WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE){
                    new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE).setTitleText("提示")
                            .setContentText("是否将图片保存到本地").setConfirmText("是").setCancelText("取消").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                sweetAlertDialog.dismiss();
                                String url = hitTestResult.getExtra();
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    initDownloadManager(url);//下载文件后的读写文件属于危险权限,因此要动态的申请权限
                                } else {
                                    ActivityCompat.requestPermissions(WebPageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            }
                        }
                    }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                      sweetAlertDialog.dismiss();
                        }
                    }).show();
                }
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                head.setText(title);
            }

            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

            }

            @Override
            public void onHideCustomView() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            webview.clearCache(true);
            webview.stopLoading();
            //防止系统在3.x的手机，在缩放按钮未完全关闭时候退出的闪退
            webview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (webview != null) {
                        webview.destroy();
                        webview = null;
                    }
                }
            }, 3000);
        }
        super.onDestroy();

    }
    public void back(View view) {
        this.finish();
    }

    public void doActionRight(View view) {

    }
    private void initDownloadManager(String urlstring) {
        MyToast.makeText(context,urlstring,Toast.LENGTH_LONG).show();
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mFileName = urlstring;
        File destFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.jpg");
        if (destFile.exists()) {
            destFile.delete();
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlstring))
                .setTitle("文件下载")
                .setDescription(urlstring)
                .setVisibleInDownloadsUi(true)
                 .setDestinationUri(Uri.parse("file://" + destFile.getAbsolutePath()))
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
                    initDownloadManager(url);
                } else {
                    // 用户拒绝权限
                    finish();
                }
                return;
            }

        }
    }
}
