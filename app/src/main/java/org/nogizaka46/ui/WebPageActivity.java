package org.nogizaka46.ui;


import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.base.MyApplication;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.bean.WithpicBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.config.UrlConfig;
import org.nogizaka46.ui.activity.ImageActivity;
import org.nogizaka46.utils.EncryptUtils;
import org.nogizaka46.utils.UMShareUtil;
import org.nogizaka46.view.MyToast;
import org.nogizaka46.view.SweetAlertDialog;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WebPageActivity extends BaseActivity {

    @InjectView(R.id.webview)
    WebView webview;
    String previews;
    String url;
    Context context;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private DownloadManager mDownloadManager = null;
    private String mFileName = "";
    private long downloadId;
    private WebView.HitTestResult hitTestResult;
    private int clickdown = 0;
    private boolean longclick;
    private NewBean newBean ;
    private boolean isBlog ;
    private String []  titlestrings= new String []{};
    private  String CookieStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpager);
        ButterKnife.inject(this);
       // img_left_layout.setVisibility(View.VISIBLE);
        context = WebPageActivity.this;
        initToolBar();
        initwebview();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);

       toolbar.setTitleTextColor(Color.BLACK);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void initwebview() {
        if(getIntent() != null){
            if ( getIntent().getExtras() != null) {
                newBean = (NewBean) getIntent().getExtras().getSerializable(Constant.STARTWEB);
                if(newBean !=null){
                    previews = newBean.getView();
                    url = UrlConfig.BASE_FORMATWEB + previews;
                    isBlog =false;
                }else{
                    String blogurl = getIntent().getStringExtra(Constant.STARTWEB_BLOG);
                    url =blogurl ;
                    isBlog =true;
                }



            }
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
        if(!isBlog){
            String formatResult = format.format((float) (outMetrics.widthPixels) / (float) 668); //668为html页面的宽度
            //设置初始缩放大小  100%   屏幕宽度 / 网页设置的宽度
            webview.setInitialScale((int) (Float.valueOf(formatResult) * 100));
        }

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
                 toolbar.setTitle(view.getTitle());
                CookieManager cookieManager = CookieManager.getInstance();
                 CookieStr = cookieManager.getCookie(url);
                Log.e("TAG", "onPageFinished: "+CookieStr );
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText(getResources().getString(R.string.web_error)).show();

            }


        });

        if(!isBlog){
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("TAG", "onClick:1111111111111 ");
                hitTestResult = webview.getHitTestResult();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    clickdown = 1;
                    longclick = false;
                } else if (!longclick && clickdown == 1 && motionEvent.getAction() == MotionEvent.ACTION_UP && hitTestResult!=null) {
                    if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE || hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        Log.e("TAG", "onClick: ");
                        clickdown = 0;
                        String url = hitTestResult.getExtra();
                        Intent intent = new Intent(WebPageActivity.this, ImageActivity.class);
                        intent.putExtra("image", url);
                        startActivity(intent);
                    }
                } else {
                    clickdown = 100;
                }
                return false;
            }


        });
        }
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("TAG", "onLongClick: ");
                longclick = true;
                hitTestResult = webview.getHitTestResult();
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE || hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText(getResources().getString(R.string.dialog_titles))

                            .setContentText(getResources().getString(R.string.dialog_content)).setConfirmText(getResources().getString(R.string.ok)).setCancelText(getResources().getString(R.string.cancel)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            String url = hitTestResult.getExtra();
                            if (Build.VERSION.SDK_INT >= 23) {

                                sweetAlertDialog.dismiss();

                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    initDownloadManager(url);//下载文件后的读写文件属于危险权限,因此要动态的申请权限
                                } else {
                                    ActivityCompat.requestPermissions(WebPageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }

                            } else {
                                sweetAlertDialog.dismiss();
                                initDownloadManager(url);
                            }
                        }

                    }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismiss();
                        }
                    }).show();
                }
                return false;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
               toolbar.setTitle(title);

            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {

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
        MyToast.makeText(context, urlstring, Toast.LENGTH_LONG).show();
        Log.e("TAG", "initDownloadManager: "+urlstring );
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
        if(isBlog){
            request.addRequestHeader("Cookie",CookieStr);
        }

        downloadId = mDownloadManager.enqueue(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home :
                finish();
               // webview.scrollTo(0,0);
                break;
            case R.id.save:
                long savecode = ((MyApplication) getApplication()).liteOrm.cascade().insert(newBean);
                Log.e("TAG", "----------------------------------"+savecode);

                if(savecode>0){
                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"已经收藏过了~",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                if (newBean!=null) {
                    String title = newBean.getTitle();
                    String summary = newBean.getSummary();
                    String url = UrlConfig.BASE_FORMATWEB + newBean.getView();
                    String image = null;
                    List<WithpicBean> withpic = newBean.getWithpic();
                    if(withpic!=null){
                        WithpicBean withpicBean = withpic.get(0);
                        image = withpicBean.getImage();
                    }
                    UMShareUtil.shareUrl(WebPageActivity.this,url,title,summary,image,umShareListener);
                }

                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isBlog){
            getMenuInflater().inflate(R.menu.menu_web,menu);
        }
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onBackPressed() {

        if(webview.canGoBack()){
            webview.goBack();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(WebPageActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WebPageActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                com.umeng.socialize.utils.Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WebPageActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
