package org.nogizaka46.ui.blogactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.nogizaka46.R;
import org.nogizaka46.adapter.BlogAdapter;
import org.nogizaka46.bean.BlogBean;
import org.nogizaka46.bean.MemberListBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.contract.Contract;
import org.nogizaka46.ui.WebPageActivity;
import org.nogizaka46.utils.DividerItemDecoration;
import org.nogizaka46.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BlogActivity extends AppCompatActivity implements BlogAdapter.onBlogClicklistener ,Contract.IBlogView{


    @InjectView(R.id.toolbar_blogs)
    Toolbar toolbar;
    @InjectView(R.id.recyclerview_blogs)
    RecyclerView recyclerview;
    @InjectView(R.id.swipeRefresh_blog)
    SwipeRefreshLayout swipeRefreshBlog;
    private List<BlogBean> list;
    private BlogAdapter adpter;
    private LinearLayoutManager manager;
    private BlogBean bean;
    private BlogPresenter presenter ;
    int page = 1 ;
    int size =10 ;
    private String title_toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        ButterKnife.inject(this);


        Intent intent = getIntent();
        MemberListBean bean = (MemberListBean) intent.getSerializableExtra(Constant.STARTBLOG);
        title_toolbar =bean.getName();
        String rome = bean.getRome();

        list = new ArrayList<>();
        initToolBar();
        initRecycler();
        presenter = new BlogPresenter(this);
        presenter.getData(rome,page,size);

    }

    private void initRecycler() {
        adpter = new BlogAdapter(this, list, this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adpter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onBlogClick(int position) {
        String url = list.get(position).getUrl();
        Intent intent = new Intent(BlogActivity.this, WebPageActivity.class);
        intent.putExtra(Constant.STARTWEB_BLOG,url);
        startActivity(intent);

    }

    @Override
    public void onBlogLongClick(int position) {

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void getData(List<BlogBean> memberBeanList) {
        list.addAll(memberBeanList);
        adpter.notifyDataSetChanged();
    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoadFailed(String errormsg) {
        ToastHelper.showToast(BlogActivity.this,errormsg);
    }
}
