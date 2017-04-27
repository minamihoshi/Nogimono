package org.nogizaka46.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.nogizaka46.R;
import org.nogizaka46.adapter.MyNewsAdapter;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.base.MyApplication;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.bean.WithpicBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.config.UrlConfig;
import org.nogizaka46.utils.DividerItemDecoration;
import org.nogizaka46.utils.ToastHelper;
import org.nogizaka46.utils.UMShareUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SaveActivity extends BaseActivity implements MyNewsAdapter.onNewsClickListener {

    @InjectView(R.id.toolbar_saveactivity)
    Toolbar toolbar;
    @InjectView(R.id.recyclerview_saveactivity)
    RecyclerView recyclerview;
    @InjectView(R.id.tv_saveactivity)
    TextView tvSave;

    private List<NewBean> list ;
    private MyNewsAdapter adpter ;
    private LinearLayoutManager manager ;
    private NewBean bean ;
    private PopupWindow popupWindow ;
    private Button btn_del ,btn_openweb, btn_share ;
    private int itemposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.inject(this);

        initToolBar();
        initData();
        adpter = new MyNewsAdapter(this,list,this);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        manager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        manager.setReverseLayout(true);//列表翻转
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adpter);

        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initData() {



        list = ((MyApplication) getApplication()).liteOrm.cascade().query(NewBean.class);
        if (list ==null || list.size() == 0){
            tvSave.setVisibility(View.VISIBLE);

        }

//        for (int i = 0; i <list.size() ; i++) {
//
//            List<WithpicBean> withpic = list.get(i).getWithpic();
//            if(withpic !=null){
//                Log.e("TAG", "initData: " +withpic.size()  );
//            }else{
//                Log.e("TAG", "initData: nulllllllllllllll"  );
//            }
//
//        }

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_saveactivity);
        toolbar.setTitleTextColor(Color.BLACK);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onNewsClick(int position) {
        openweb(position);
    }

    @Override
    public void onNewsLongClick(int position) {
         itemposition =position ;
        bean =list.get(position);
        if(popupWindow==null){
            initPopup();
        }

        popupWindow.showAtLocation(recyclerview, Gravity.CENTER, 0, 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home :
                 finish();

                break;


        }
        return true;
    }

    private void openweb(int position){

        bean = list.get(position);
        Intent intent = new Intent(SaveActivity.this, WebPageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STARTWEB,bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initPopup() {
        View view = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        btn_openweb = (Button) view.findViewById(R.id.btn_openweb);
        btn_openweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                openweb(itemposition);
            }
        });


        btn_share = (Button) view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            String url = UrlConfig.BASE_FORMATWEB + bean.getView();
            String title = bean.getTitle();
            String summary = bean.getSummary();
            String image = null;
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                List<WithpicBean> withpic = bean.getWithpic();
                if(withpic!=null){
                    WithpicBean withpicBean = withpic.get(0);
                    image = withpicBean.getImage();
                }
                UMShareUtil.shareUrl(SaveActivity.this,url,title,summary,image,umShareListener);
//                new ShareAction(SaveActivity.this).setPlatform(SHARE_MEDIA.QQ)
//                        .withMedia(getUmengWeb(url,title,summary))
//                        .setDisplayList(SHARE_MEDIA.QQ)
//                        .setCallback(umShareListener).open();
            }
        });

        btn_del = (Button) view.findViewById(R.id.btn_del);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean =list.get(itemposition);
                popupWindow.dismiss();
                int delete = ((MyApplication) getApplication()).liteOrm.cascade().delete(bean);
                list.remove(itemposition);
                if(list!=null&&list.size()==0){
                    tvSave.setVisibility(View.VISIBLE);
                }
                adpter.notifyDataSetChanged();
                Log.e("TAG", "onClick: " );


            }
        });


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

            Toast.makeText(SaveActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SaveActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                com.umeng.socialize.utils.Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SaveActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


//    UMWeb getUmengWeb(String url, String title , String des){
//        UMWeb  web = new UMWeb(url);
//        web.setTitle(title);//标题
//        //web.setThumb();  //缩略图
//        web.setDescription(des);//描述
//        return  web ;
//    }
//
//    UMImage getUMimage(){
//
//        UMImage image = new UMImage(SaveActivity.this,R.mipmap.ic_launcher);
//        UMImage thumb =  new UMImage(SaveActivity.this, R.mipmap.ic_launcher);
//        image.setThumb(thumb);
//        return image ;
//    }

}
