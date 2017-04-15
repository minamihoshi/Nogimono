package org.nogizaka46.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.config.UrlConfig;
import org.nogizaka46.view.MyToast;

/**
 * 新闻详情界面
 */
public class NewsInfoActivity extends BaseActivity {
    String previews;
    Context context = NewsInfoActivity.this;
    View view;
    @InjectView(R.id.top_button_back)  ImageButton topButtonBack;
    @InjectView(R.id.title)  TextView title;
    @InjectView(R.id.news_layout)  LinearLayout newsLayout;
    @InjectView(R.id.webView) WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_info);
        ButterKnife.inject(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            previews = getIntent().getStringExtra("preview");
        }
        title.setText(getResources().getString(R.string.news_info));
        topButtonBack.setVisibility(View.VISIBLE);
         initWebview();

    }

    private void initWebview() {
        String url = UrlConfig.BASE_URL + previews;
        webView.loadUrl(url);
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);//设置WebView是否允许执行JavaScript脚本，默认false，不允许。
        webSetting.setSupportMultipleWindows(true);//设置WebView是否支持多窗口。如果设置为true
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口，默认false
        webSetting.setAllowFileAccess(true);//是否允许访问文件，默认允许
        webSetting.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSetting.setUseWideViewPort(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);//为图片添加放大缩小功能
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String titles) {
                super.onReceivedTitle(view, titles);
                title.setText(titles);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {

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


            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                MyToast.showText(context, getResources().getString(R.string.web_error), Toast.LENGTH_SHORT, false);
            }
        });
    }


    public void back(View view) {
        NewsInfoActivity.this.finish();

    }



    public void doActionRight(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();   //返回上一页面
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
