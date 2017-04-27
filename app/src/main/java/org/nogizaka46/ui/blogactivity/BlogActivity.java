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
import android.util.Log;
import android.view.MenuItem;

import org.nogizaka46.R;
import org.nogizaka46.adapter.BlogAdapter;
import org.nogizaka46.base.BaseActivity;
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

public class BlogActivity extends BaseActivity implements BlogAdapter.onBlogClicklistener ,Contract.IBlogView{


    @InjectView(R.id.toolbar_blogs)
    Toolbar toolbar;
    @InjectView(R.id.recyclerview_blogs)
    RecyclerView recyclerview;
    @InjectView(R.id.swipeRefresh_blog)
    SwipeRefreshLayout swipeRefresh;
    private List<BlogBean> list;
    private BlogAdapter adapter;
    private LinearLayoutManager manager;
    private BlogBean bean;
    private BlogPresenter presenter ;
    private String rome;
    private int page = 1 ;
    private int size =10 ;
    private String title_toolbar ;
    private int mLastVisibleItem;
    private boolean NeadClear ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        ButterKnife.inject(this);

        NeadClear =true;
        Intent intent = getIntent();
        MemberListBean bean = (MemberListBean) intent.getSerializableExtra(Constant.STARTBLOG);
        title_toolbar =bean.getName();
         rome = bean.getRome();


        list = new ArrayList<>();
        initToolBar();
        initRecycler();
        initRefresh();
        presenter = new BlogPresenter(this);
        presenter.getData(rome,page,size);

    }

    private void initRefresh() {
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_purple);
        //  swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.color_red);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page =1 ;
                NeadClear =true;
                presenter.getData(rome,page,size);
            }
        });
    }


    private void initRecycler() {
        adapter = new BlogAdapter(this, list, this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断是否该加载更多数据（1.屏幕处于停止状态；2.屏幕已经滑动到了item的最底端）
                if (mLastVisibleItem == adapter.getItemCount() - 1 && newState == RecyclerView
                        .SCROLL_STATE_IDLE) {
                    NeadClear =false;
                    ++page;
                    presenter.getData(rome,page,size);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
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
        adapter.reloadRecyclerView(memberBeanList, NeadClear);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaded() {
        if(swipeRefresh !=null &&swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoadFailed(String errormsg) {
        ToastHelper.showToast(BlogActivity.this,errormsg);
    }
}
