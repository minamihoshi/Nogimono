package org.nogizaka46.ui.blogactivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BlogActivity extends BaseActivity implements BlogAdapter.onBlogClicklistener, Contract.IBlogView {


    @BindView(R.id.toolbar_blogs)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_blogs)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefresh_blog)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.iv_areaselect)
    ImageView ivAreaselect;
    @BindView(R.id.activity_blog)
    LinearLayout activityBlog;
    @BindView(R.id.rela_sel)
    RelativeLayout relaSel;
    private List<BlogBean> list;
    private BlogAdapter adapter;
    private LinearLayoutManager manager;
    private BlogBean bean;
    private BlogPresenter presenter;
    private String rome;
    private int page = 1;
    private int size = 10;
    private String title_toolbar;
    private int mLastVisibleItem;
    private boolean NeadClear;
    private PopupWindow popupWindow;
    private boolean isShowSelect;
    private String group = "all";
    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
         bind = ButterKnife.bind(this);

        NeadClear = true;
        Intent intent = getIntent();
        MemberListBean bean = (MemberListBean) intent.getSerializableExtra(Constant.STARTBLOG);
        title_toolbar = bean.getName();
        rome = bean.getRome();
        if (Constant.ALLBLOGS.equals(rome)) {
          relaSel.setVisibility(View.VISIBLE);
        }else{
            relaSel.setVisibility(View.GONE);
        }

        list = new ArrayList<>();
        initToolBar();
        initRecycler();
        initRefresh();
        initPopup();
        presenter = new BlogPresenter(this);
        presenter.getData(rome, page, size, group);
       swipeRefresh.setRefreshing(true);
    }

    private void initRefresh() {
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_purple);
        //  swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.color_red);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onUserRefresh();
            }
        });
    }

    void onUserRefresh() {
        page = 1;
        NeadClear = true;
        presenter.getData(rome, page, size, group);
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
                    NeadClear = false;
                    ++page;
                    presenter.getData(rome, page, size, group);

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
        bind.unbind();
    }

    @Override
    public void onBlogClick(int position) {
        String url = list.get(position).getUrl();
        Intent intent = new Intent(BlogActivity.this, WebPageActivity.class);
        intent.putExtra(Constant.STARTWEB_BLOG, url);
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
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoadFailed(String errormsg) {
        ToastHelper.showToast(BlogActivity.this, errormsg);
    }


    void initPopup() {

        // areaRecyclerView  = new RecyclerView(getActivity());
        // areaRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupWindow = new PopupWindow(BlogActivity.this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(BlogActivity.this).inflate(R.layout.item_area, null);

        view.findViewById(R.id.tv_nogi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                group = "nogizaka";
                onUserRefresh();


            }
        });

        view.findViewById(R.id.tv_keya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                group = "keyakizaka";
                onUserRefresh();
            }
        });

        view.findViewById(R.id.tv_sdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                group = "all";
                onUserRefresh();
            }
        });


        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        //popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);   // 设置窗口显示的动画效果
        popupWindow.setFocusable(false);                                        // 点击其他地方隐藏键盘 popupWindow
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvArea.setSelected(false);
                ivAreaselect.setImageResource(R.drawable.area1);
                isShowSelect = true;
            }
        });
    }

    @OnClick({R.id.tv_area, R.id.iv_areaselect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_area:
                if (popupWindow != null) {
                    if (!isShowSelect) {
                        // tvArea.setTextColor(getResources().getColor(R.color.colorAccent));
                        tvArea.setSelected(true);
                        ivAreaselect.setImageResource(R.drawable.area2);
                        isShowSelect = true;
                        Log.e("TAG", "onViewClicked: show");
                        popupWindow.showAsDropDown(tvArea);
                        //  areaAdapter.notifyDataSetChanged();

                    } else {
                        tvArea.setSelected(false);
                        ivAreaselect.setImageResource(R.drawable.area1);
                        isShowSelect = false;
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        Log.e("TAG", "onViewClicked: dismissssssss");

                    }

                }

                break;
            case R.id.iv_areaselect:
                break;
        }
    }
}
