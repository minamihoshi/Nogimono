package org.nogizaka46.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nogizaka46.R;
import org.nogizaka46.adapter.AllListAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;

import org.nogizaka46.ui.WebPageActivity;
import org.nogizaka46.view.SweetAlertDialog;
import org.nogizaka46.view.WaveSwipeRefreshLayout;


public class Main2Frag_Tab1 extends Fragment {
    View view;
    AllListAdapter adapter;
    @InjectView(R.id.recyclerview) RecyclerView recyclerview;
    @InjectView(R.id.swipeRefresh) WaveSwipeRefreshLayout swipeRefresh;
    private Handler handler;
    private List<Map<String, Object>> mSelfData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main2frag_tab1, container, false);
        }
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        if (handler == null) {
            initHandler();
            initData();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshList();
                }
            }, 500);
        }

    }

    private void initData() {
        mSelfData = new ArrayList<Map<String, Object>>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshListener());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private class SwipeRefreshListener implements WaveSwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            mSelfData.clear();
            refreshList();
        }

    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        SetListData();
                        swipeRefresh.setRefreshing(false);
                        break;
                    case 2://当网络没有的时候就显示空的list,要不然没了网络数据还在
                        SetListData();
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE).setTitleText(msg.obj.toString()).show();
                        swipeRefresh.setRefreshing(false);
                        break;
                }
            }
        };
    }

    private void SetListData() {
        if (adapter == null) {
            adapter = new AllListAdapter(getActivity(), mSelfData);
            recyclerview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setOnItemClickListener(new AllListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Map<String, ?> item = mSelfData.get(position);
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("preview", item.get("preview").toString());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void refreshList() {
        if (!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        } else {
            doAction();
        }
    }

    private void doAction() {

    }
}
