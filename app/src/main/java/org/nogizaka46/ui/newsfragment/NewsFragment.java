package org.nogizaka46.ui.newsfragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nogizaka46.R;
import org.nogizaka46.adapter.MyNewsAdapter;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.config.Constant;
import org.nogizaka46.contract.Contract;
import org.nogizaka46.ui.WebPageActivity;
import org.nogizaka46.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements Contract.INewsView, MyNewsAdapter.onNewsClickListener {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private List<NewBean> list;
    private Context mContext;
    private NewsPresenter presenter;
    private MyNewsAdapter adapter;
    private NewBean bean;
    private String category;
    private int page = 1;
    private int size = 10;
    private int mLastVisibleItem;
    private LinearLayoutManager mLayoutManager ;
    private boolean NeadClear;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        category = bundle.getString(Constant.NEWS_CATEGORY);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NeadClear =true;
        list = new ArrayList<>();
        adapter = new MyNewsAdapter(mContext, list, this);
        presenter = new NewsPresenter(this);
        presenter.getData(category, page, size);
        mLayoutManager =new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断是否该加载更多数据（1.屏幕处于停止状态；2.屏幕已经滑动到了item的最底端）
                if (mLastVisibleItem == adapter.getItemCount() - 1 && newState == RecyclerView
                        .SCROLL_STATE_IDLE) {
                    NeadClear =false;
                    ++page;
                    presenter.getData(category,page,size);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        //swipeRefresh.setProgressViewOffset(true,0,0);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_purple);
      //  swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.color_red);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 page =1 ;
                 NeadClear =true;
                  presenter.getData(category,page,size);
            }
        });
    }


    @Override
    public void getData(List<NewBean> beanList) {


        adapter.reloadRecyclerView(beanList, NeadClear);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaded() {
        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoadFailed(String errormsg) {
        Toast.makeText(mContext, errormsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNewsClick(int position) {
        bean = list.get(position);
        String preview = bean.getView();
        Intent intent = new Intent(getActivity(), WebPageActivity.class);
        intent.putExtra("preview", preview);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
