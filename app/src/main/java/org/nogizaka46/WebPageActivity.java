package org.nogizaka46;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.Constants;
import view.MyToast;


public class WebPageActivity extends BaseActivity {
    @InjectView(R.id.top_button_back) ImageButton img_left_layout;
    @InjectView(R.id.webview)  WebView webview;
    @InjectView(R.id.title)
    TextView head;
    String previews;
    String url;
    Context context = WebPageActivity.this;

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
        webSetting.setJavaScriptEnabled(true);//设置WebView是否允许执行JavaScript脚本，默认false，不允许。
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setSupportMultipleWindows(true);//设置WebView是否支持多窗口。如果设置为true
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口，默认false
        webSetting.setAllowFileAccess(true);//是否允许访问文件，默认允许
        webSetting.setDisplayZoomControls(false);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);//为图片添加放大缩小功能
        webSetting.setLoadWithOverviewMode(true);
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
}
