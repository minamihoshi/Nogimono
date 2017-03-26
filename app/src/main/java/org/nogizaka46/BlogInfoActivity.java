package org.nogizaka46;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BlogInfoActivity extends BaseActivity {

    @InjectView(R.id.top_button_back)
    ImageButton img_left_layout;
    @InjectView(R.id.webview)
    WebView webview;

    @InjectView(R.id.title)
    TextView head;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_info);
        ButterKnife.inject(this);
        img_left_layout.setVisibility(View.VISIBLE);
        initwebview();
    }

    private void initwebview() {
        url = getIntent().getStringExtra("url");
        webview.loadUrl(url);
        WebSettings webSetting = webview.getSettings();
        webSetting.setJavaScriptEnabled(true);//设置WebView是否允许执行JavaScript脚本，默认false，不允许。
        webSetting.setSupportMultipleWindows(true);//设置WebView是否支持多窗口。如果设置为true
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口，默认false
        webSetting.setAllowFileAccess(true);//是否允许访问文件，默认允许
        webSetting.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);//为图片添加放大缩小功能
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
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                //重新测量
                webview.measure(w, h);
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
