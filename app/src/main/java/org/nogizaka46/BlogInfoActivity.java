package org.nogizaka46;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import utils.ADFilterTool;

public class BlogInfoActivity extends BaseActivity {
      WebView webView;
      String blog_url,name_kanji;
      TextView head;
      Context context=BlogInfoActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_info);
        head = (TextView) findViewById(R.id.title);
        ImageButton img_left_layout=(ImageButton) findViewById(R.id.top_button_back);
        img_left_layout.setVisibility(View.VISIBLE);
        initWebview();

    }

    private void initWebview() {
        webView= (WebView) findViewById(R.id.webview);
        blog_url=getIntent().getStringExtra("url");
        name_kanji=getIntent().getStringExtra("name_kanji");
        webView.loadUrl(blog_url);
        head.setText(name_kanji+"");
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);//设置WebView是否允许执行JavaScript脚本，默认false，不允许。
        webSetting.setSupportMultipleWindows(true);//设置WebView是否支持多窗口。如果设置为true
        webSetting.setSupportZoom(true);//WebView是否支持使用屏幕上的缩放控件和手势进行缩放，默认值true
        webSetting.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);//为图片添加放大缩小功能
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

            }

            @Override
            public void onHideCustomView() {

            }

        });
        webView.setWebViewClient(new WebViewClient() {

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
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                //重新测量
                webView.measure(w, h);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                super.onReceivedError(view, errorCode, description, failingUrl);

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                url = url.toLowerCase();
                if (!ADFilterTool.hasAd(context, url)) {
                        return super.shouldInterceptRequest(view, url);
                    }else{
                        return new WebResourceResponse(null,null,null);
                    }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearCache(true);
            webView.stopLoading();
            //防止系统在3.x的手机，在缩放按钮未完全关闭时候退出的闪退
            webView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (webView != null) {
                        webView.destroy();
                        webView = null;
                    }
                }
            }, 3000);
        }
        super.onDestroy();
    }
    public  void back(View view){
        this.finish();
    }
    public  void doActionRight(View view){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
