package org.nogizaka46.ui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.nogizaka46.R;
import org.nogizaka46.adapter.CommentAdapter;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.base.MyApplication;
import org.nogizaka46.bean.CommentBean;
import org.nogizaka46.bean.LzyResponse;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.bean.WithpicBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.config.UrlConfig;
import org.nogizaka46.db.PreUtils;
import org.nogizaka46.http.HttpUtils;
import org.nogizaka46.ui.activity.ImageActivity;
import org.nogizaka46.utils.DividerItemDecoration;
import org.nogizaka46.utils.EncryptUtils;
import org.nogizaka46.utils.UMShareUtil;
import org.nogizaka46.view.MyToast;
import org.nogizaka46.view.SweetAlertDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class WebPageActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    String previews;
    String url;
    Context context;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.tv_huifu)
    TextView tvHuifu;
    @BindView(R.id.lin_bottom)
    LinearLayout linBottom;
    private DownloadManager mDownloadManager = null;
    private String mFileName = "";
    private long downloadId;
    private WebView.HitTestResult hitTestResult;
    private int clickdown = 0;
    private boolean longclick;
    private NewBean newBean;
    private boolean isBlog;
    private String news_id;
    private String[] titlestrings = new String[]{};
    private String CookieStr;
    private float downX;
    private float downY;
    private WebSettings webSetting;
    private String father;
    private String touid;
    private CommentAdapter commentAdapter;
    private List<CommentBean> list;
    private InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpager);
        ButterKnife.bind(this);
        // img_left_layout.setVisibility(View.VISIBLE);
        context = WebPageActivity.this;

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                newBean = (NewBean) getIntent().getExtras().getSerializable(Constant.STARTWEB);
                if (newBean != null) {
                    previews = newBean.getView();
                    url = UrlConfig.BASE_FORMATWEB + previews;
                    Log.e("TAG", "onCreate: "+"--" +url );
                    news_id = newBean.getId();
                    isBlog = false;
                } else {
                    String blogurl = getIntent().getStringExtra(Constant.STARTWEB_BLOG);
                    url = blogurl;
                    isBlog = true;
                }
            }
        }

        initToolBar();
        initwebview();
        initRv();
        initSoft();
        initComBottom();
    }
    private void initComBottom() {
        if (isBlog) {
            linBottom.setVisibility(View.GONE);
        }else{
            linBottom.setVisibility(View.VISIBLE);
        }
    }

    private void initSoft() {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    private void initRv() {
        list = new ArrayList<>();
        commentAdapter = new CommentAdapter(list, new CommentAdapter.onItemClickLintener() {
            @Override
            public void onClick(CommentBean item, int childposition) {
                if(item.getChild().size()>4 && childposition==4){
                    Intent intent = new Intent(WebPageActivity.this ,CommentDetailActivity.class);
                    intent.putExtra(Constant.COMMENT_FATHER_ARTICEL,news_id);
                    intent.putExtra(Constant.COMMENT_FATHER_ID,item.getCid());
                    startActivity(intent);
                    return;
                }

                //点击子评论
                int cid = item.getCid(); //主评论cid
                CommentBean.ChildBean childBean = item.getChild().get(childposition);

                CommentBean.ChildBean.UserBeanX user = childBean.getUser();
                String nickname = user.getNickname();//要回复的nickname
                int id = user.getId(); //要回复的id
                father = String.valueOf(cid);
                touid = String.valueOf(id);
                tvHuifu.setVisibility(View.VISIBLE);
                SpannableString ss = new SpannableString("回复#"+item.getFloor()+"@"+childBean.getFloor() + nickname + ":");//定义hint的值
                AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
                ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                editComment.setHint(ss);
                editComment.requestFocus();

                inputMethodManager.toggleSoftInput(0, 0);

            }

            @Override
            public void onLongClick(CommentBean item, int childposition) {
                if(item.getChild().size()>4 && childposition==4){
                   return;
                }
                showCommetDelDialog(String.valueOf(item.getChild().get(childposition).getCid()));
            }
        });


        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击主评论
                CommentBean.UserBean user = list.get(position).getUser();

                String nickname = user.getNickname(); //楼主名
                int cid = list.get(position).getCid(); //评论cid
                father = String.valueOf(cid);
                touid = null;
                tvHuifu.setVisibility(View.VISIBLE);
                editComment.requestFocus();

                SpannableString ss = new SpannableString("回复#"+list.get(position).getFloor() + nickname+":" );//定义hint的值
                AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
                ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                editComment.setHint(ss);

                inputMethodManager.toggleSoftInput(0, 0);


            }
        });
        commentAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                showCommetDelDialog(String.valueOf(list.get(position).getCid()));
                return true;
            }
        });
        rvComment.setAdapter(commentAdapter);
        rvComment.setLayoutManager(new LinearLayoutManager(WebPageActivity.this, LinearLayoutManager.VERTICAL, false));
        rvComment.addItemDecoration(new DividerItemDecoration(WebPageActivity.this, DividerItemDecoration.VERTICAL_LIST));


    }


    private void showCommetDelDialog(final String cid) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText(getResources().getString(R.string.dialog_titles))
                .setContentText("是否删除这条评论").setConfirmText(getResources().getString(R.string.ok)).setCancelText(getResources().getString(R.string.cancel)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                deleteComment(cid);
                sweetAlertDialog.dismissWithAnimation();


            }
        }).show();

    }


    //删除评论
    void deleteComment(String cid) {
        String userid = PreUtils.readStrting(WebPageActivity.this, Constant.USER_ID);

        if(TextUtils.isEmpty(userid)){
            Toast.makeText(this, "您还未登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            return;
        }

        String token = PreUtils.readStrting(WebPageActivity.this, Constant.USER_TOKEN);
        HttpUtils.getInstance().getRetrofitInterface().delComment(userid, token, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<String> stringLzyResponse) {
                        if (stringLzyResponse.code == 200) {
                            Toast.makeText(WebPageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WebPageActivity.this, stringLzyResponse.message, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(WebPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        getAllComment();
                    }
                });

    }


    private void initToolBar() {
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.BLACK);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        webSetting.setJavaScriptEnabled(true);

        getAllComment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        webSetting.setJavaScriptEnabled(false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initwebview() {

        Log.e("TAG", "initwebview: " + url);
        webview.loadUrl(url);
        webSetting = webview.getSettings();
        webSetting.setSupportZoom(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setSupportMultipleWindows(true);//设置WebView是否支持多窗口。如果设置为true
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口，默认false
        webSetting.setAllowFileAccess(true);//是否允许访问文件，默认允许
        webSetting.setDisplayZoomControls(false);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);//为图片添加放大缩小功能
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings()
                    .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        DecimalFormat format = new DecimalFormat("0.00");
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        if (!isBlog) {
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
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.e("TAG", "onReceivedSslError: ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webview.getSettings()
                            .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                toolbar.setTitle(view.getTitle());

                ViewGroup.LayoutParams layoutParams = webview.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                webview.setLayoutParams(layoutParams);


                CookieManager cookieManager = CookieManager.getInstance();
                CookieStr = cookieManager.getCookie(url);
                Log.e("TAG", "onPageFinished: " + CookieStr);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText(getResources().getString(R.string.web_error)).show();

            }


        });

        if (!isBlog) {
            webview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.e("TAG", "onClick:touch ");
                    hitTestResult = webview.getHitTestResult();
                    Log.e("TAG", "onTouch: " + hitTestResult.toString());
                    Log.e("TAG", "onTouch: " + clickdown);


                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        clickdown = 1;
                        longclick = false;

                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        Log.e("TAG", "onClick:down ");
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (!longclick && clickdown == 1 && hitTestResult != null) {
                            if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE || hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                                Log.e("TAG", "onClick:goto ");
                                clickdown = 0;
                                String url = hitTestResult.getExtra();
                                Intent intent = new Intent(WebPageActivity.this, ImageActivity.class);
                                intent.putExtra("image", url);
                                startActivity(intent);
                            }
                        }
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                        float v = Math.abs(motionEvent.getX() - downX) + Math.abs(motionEvent.getY() - downY);
                        Log.e("TAG", "onClick:else " + v);
                        if (v > 10.0f) {
                            clickdown = 100;
                        }


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
        Log.e("TAG", "initDownloadManager: " + urlstring);
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
        if (isBlog) {
            request.addRequestHeader("Cookie", CookieStr);
        }

        downloadId = mDownloadManager.enqueue(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                // webview.scrollTo(0,0);
                break;
            case R.id.save:
                long savecode = ((MyApplication) getApplication()).liteOrm.cascade().insert(newBean);
                Log.e("TAG", "----------------------------------" + savecode);

                if (savecode > 0) {
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "已经收藏过了~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                if (newBean != null) {
                    String title = newBean.getTitle();
                    String summary = newBean.getSummary();
                    String url = UrlConfig.BASE_FORMATWEB + newBean.getView();
                    String image = null;
                    List<WithpicBean> withpic = newBean.getWithpic();
                    if (withpic != null) {
                        WithpicBean withpicBean = withpic.get(0);
                        image = withpicBean.getImage();
                    }
                    UMShareUtil.shareUrl(WebPageActivity.this, url, title, summary, image, umShareListener);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isBlog) {
            getMenuInflater().inflate(R.menu.menu_web, menu);
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

        if (webview.canGoBack()) {
            webview.goBack();
        } else {
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
            Toast.makeText(WebPageActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WebPageActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    @OnClick({R.id.toolbar, R.id.edit_comment, R.id.btn_send,R.id.tv_huifu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_huifu:
                tvHuifu.setVisibility(View.GONE);
                father = null;
                touid = null ;
                editComment.setHint("");
                break;
            case R.id.toolbar:
                break;
            case R.id.edit_comment:
                break;
            case R.id.btn_send:

                sendComment();
                break;
        }
    }


    private void getAllComment() {
        HttpUtils.getInstance().getRetrofitInterface().getAllComment(news_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<List<CommentBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<List<CommentBean>> commentBeanLzyResponse) {

                        list.clear();
                        list.addAll(commentBeanLzyResponse.data);
                        commentAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void sendComment() {
        String userid = PreUtils.readStrting(WebPageActivity.this, Constant.USER_ID);

        if(TextUtils.isEmpty(userid)){

            Toast.makeText(this, "评论需要登录", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;

        }
        String token = PreUtils.readStrting(WebPageActivity.this, Constant.USER_TOKEN);
        String msg = editComment.getText().toString();




        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(WebPageActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUtils.getInstance().getRetrofitInterface().sendComment(news_id, userid, token, msg, father, touid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LzyResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LzyResponse<String> stringLzyResponse) {

                        Toast.makeText(WebPageActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(WebPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        editComment.setText("");
                        tvHuifu.setVisibility(View.GONE);
                        inputMethodManager.toggleSoftInput(0, 0);
                        getAllComment();
                    }
                });
    }





}
