package org.nogizaka46.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.bean.MemberListBean;
import org.nogizaka46.bean.VersionBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.config.UrlConfig;
import org.nogizaka46.db.PreUtils;
import org.nogizaka46.http.HttpUtils;
import org.nogizaka46.http.IApiService;
import org.nogizaka46.service.MyService;
import org.nogizaka46.ui.activity.AboutActivity;
import org.nogizaka46.ui.blogactivity.BlogActivity;
import org.nogizaka46.ui.fragment.Main2Frag;
import org.nogizaka46.ui.fragment.Main3Frag;
import org.nogizaka46.ui.fragment.MemberFrag.Main1Frag;
import org.nogizaka46.utils.ClearCacheUtils;
import org.nogizaka46.utils.ToastHelper;
import org.nogizaka46.utils.UMShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    Main1Frag main1Frag;
    Main2Frag main2Frag;
    Main3Frag main3Frag;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.fragment_container)
    RelativeLayout fragmentContainer;
    private int INTERVAL_OF_TWO_CLICK_TO_QUIT = 1000;
    private long mLastPressBackTime;
    //private Subscription subscription;
    private String download, versionName, versionMsg;

    private int versionCode;
    ActionBar supportActionBar;
    CoordinatorLayout.LayoutParams params;
    private String title , subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        initView();
        setDefaultFragment();
        getNewVersionCode();
        //Toast.makeText(this, "我是热更新toast", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //设置默认的Fragment
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //if(main2Frag!=null&&main1Frag!=null&&main3Frag!=null){
//            transaction.show(main2Frag);
//            transaction.hide(main1Frag);
//            transaction.hide(main3Frag);
        //}else{
        main2Frag = new Main2Frag();
        main1Frag = new Main1Frag();
        main3Frag = new Main3Frag();

        transaction.add(R.id.fragment_container, main1Frag);
        transaction.add(R.id.fragment_container, main3Frag);
        transaction.add(R.id.fragment_container, main2Frag);

        //}

        transaction.commit();
       // bottomNavigationView.getMenu().getItem(1).setChecked(true);

        bottomNavigationView.findViewById(R.id.menu_home).performClick();

    }


    private void initView() {

        setSupportActionBar(toolbar);
         supportActionBar = getSupportActionBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);


        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        toolbar.setTitle(getResources().getString(R.string.tab_mainpage));
        collapsingToolbar.setTitleEnabled(false);
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {

                    //展开状态
                    toolbar.setTitle(title);
                    toolbar.setSubtitle(subtitle);

                }else if(state == State.COLLAPSED){

                    //折叠状态
                    toolbar.setTitle("");
                    toolbar.setSubtitle("");

                }else {
                    //中间状态

                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - mLastPressBackTime < INTERVAL_OF_TWO_CLICK_TO_QUIT) {
                    Intent intent = new Intent(MainActivity.this, BlogActivity.class);
                    Bundle bundle = new Bundle();
                    MemberListBean memberListBean = new MemberListBean();
                    memberListBean.setRome(Constant.ALLBLOGS);
                    memberListBean.setName("全部");
                    bundle.putSerializable(Constant.STARTBLOG, memberListBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // Toast.makeText(MainActivity.this, "双击有惊喜", Toast.LENGTH_SHORT).show();

                    mLastPressBackTime = System.currentTimeMillis();
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_save:
                        Intent intent = new Intent(MainActivity.this, SaveActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.nav_main:


                        break;

                    case R.id.cleaer_cache:
                        showConfirmMessageDialog();

                        // item.setTitle("清除缓存");
                        break;

                    case R.id.nav_about:
                        Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_share:
                        UMShareUtil.shareUrl(MainActivity.this, download, "我在使用乃木物", "坂道系综合资讯app 一起来用吧~", null, umShareListener);
                        break;

                    case R.id.nav_manage:
                        String userid = PreUtils.readStrting(MainActivity.this, Constant.USER_ID);
                        Intent intent2 = new Intent();
                        if (TextUtils.isEmpty(userid)) {
                            Toast.makeText(MainActivity.this, "您还没有登录", Toast.LENGTH_SHORT).show();
                            intent2.setClass(MainActivity.this, LoginActivity.class);
                        } else {
                            intent2.setClass(MainActivity.this, SettingActivity.class);
                        }
                        startActivity(intent2);
                        break;
                    default:
                        ToastHelper.showToast(MainActivity.this, "尚未开发");
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (main1Frag != null) {
            transaction.hide(main1Frag);
        }
        if (main2Frag != null) {
            transaction.hide(main2Frag);
        }
        if (main3Frag != null) {
            transaction.hide(main3Frag);
        }
        switch (item.getItemId()) {

            case R.id.menu_gz:
                if (main1Frag == null) {
                    main1Frag = new Main1Frag();
                    transaction.add(R.id.fragment_container, main1Frag);
                } else {
                    transaction.show(main1Frag);
                }
                 params =
                        (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
                params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
                fragmentContainer.requestLayout();
                appBar.setExpanded(true,true);
                title = getResources().getString(R.string.tab_mainpage_gz);
                subtitle = "双击进入全部";
                toolbar.setTitle(title);
                toolbar.setSubtitle(subtitle);

                break;

            case R.id.menu_home:
                if (main2Frag == null) {
                    main2Frag = new Main2Frag();
                    transaction.add(R.id.fragment_container, main2Frag);
                } else {
                    transaction.show(main2Frag);
                }

                 params =
                      (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
                params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
                fragmentContainer.requestLayout();

                appBar.setExpanded(true);
                title = getResources().getString(R.string.tab_mainpage);
                subtitle="";
                toolbar.setTitle(title);
                toolbar.setSubtitle("");

                break;

            case R.id.menu_me:

               // appBar.setVisibility(View.GONE);
//                params =
//                        (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
//                params.setBehavior(null);
//                fragmentContainer.requestLayout();


                if (main3Frag == null) {
                    main3Frag = new Main3Frag();
                    transaction.add(R.id.fragment_container, main3Frag);
                } else {
                    transaction.show(main3Frag);
                }
                Log.e("TAG", "onNavigationItemSelected: 11111111" );
                appBar.setExpanded(false);
                appBar.setExpanded(false);
                Log.e("TAG", "onNavigationItemSelected: 2222222" );
                toolbar.setTitle(getResources().getString(R.string.tab_mainpage_me));
                toolbar.setSubtitle("");
                break;
        }

        transaction.commit();
        return true;
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mLastPressBackTime < INTERVAL_OF_TWO_CLICK_TO_QUIT) {
                finish();
            } else {
                Toast.makeText(this, R.string.msgexit, Toast.LENGTH_SHORT).show();

                mLastPressBackTime = System.currentTimeMillis();
            }
        }


    }

    void getNewVersionCode() {
        IApiService retrofitInterface = HttpUtils.getInstance().getRetrofitInterface();
        Observable<VersionBean> observable = retrofitInterface.getVersionCheck(UrlConfig.VERSION_CHECK);

        // subscription =

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionBean>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        int localVersionCode = getVersionCode(MainActivity.this);
                        Log.e("TAG", "onCompleted: " + localVersionCode + versionCode);
                        if (versionCode > localVersionCode) {
                            // PreUtils.writeBoolean(MainActivity.this,Constant.NEADUPDATE,true);
                            // PreUtils.writeString(MainActivity.this,Constant.KEY_NEWVERSION_URL,vsersion_url);
                            showMyDialog();
                        } else {
                            //PreUtils.writeBoolean(MainActivity.this,Constant.NEADUPDATE,false);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VersionBean versionBean) {
                        download = versionBean.getDownload();
                        versionCode = versionBean.getVersionCode();
                        versionName = versionBean.getVersionName();
                        versionMsg = versionBean.getMsg();
                        Log.e("TAG", "onNext: " + download);
                        // PreUtils.writeString(MainActivity.this, Constant.KEY_NEWVERSION_URL, vsersion_url);
                        // PreUtils.writeInt(MainActivity.this, Constant.KEY_NEWVERSION_CODE, version);
                    }
                });

    }

    public static int getVersionCode(Context context) {
        int version = 0;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TAG", "Package name not found", e);
        }
        ;
        return version;
    }

    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("检测到新版本")
                .setIcon(R.drawable.logo)
                .setCancelable(false)
                .setMessage("是否更新")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= 23) {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                // startDownloadService();
                                startDownload();
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }

                        } else {
                            //startDownloadService();
                            startDownload();
                        }

                    }
                })
                .setNegativeButton("no", null)
                .create()
                .show();

    }

    private void startDownload() {
        DownloadManager mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download))
                .setTitle("文件下载")
                .setDescription("乃木物")
                .setVisibleInDownloadsUi(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "nogimono")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                .setMimeType("application/vnd.android" + ".package-archive");


        mDownloadManager.enqueue(request);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(subscription!=null&&!subscription.isUnsubscribed()){
//            subscription.unsubscribe();
//        }
        UMShareAPI.get(this).release();
        //ButterKnife.reset(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户授予权限
                    // startDownloadService();
                    startDownload();
                } else {
                    // 用户拒绝权限
                    ToastHelper.showToast(MainActivity.this, "申请权限失败");
                }
                return;
            }

        }
    }

    private void startDownloadService() {
        Intent intent = new Intent(MainActivity.this, MyService.class);
        intent.putExtra(Constant.SERVICEDOWNLOAD, download);
        startService(intent);
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

            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void showConfirmMessageDialog() {
        final QMUIDialog.CheckBoxMessageDialogBuilder builder = new QMUIDialog.CheckBoxMessageDialogBuilder(this);
        builder
                .setTitle("清除缓存?")
                .setMessage("确认清除")
                .setChecked(true)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                        dialog.cancel();
                    }
                })
                .addAction("确认", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                        if (builder.isChecked()) {
                            ClearCacheUtils.clearAllCache(MainActivity.this);
                        }
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }




}

