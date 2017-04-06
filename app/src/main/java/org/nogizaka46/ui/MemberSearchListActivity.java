package org.nogizaka46.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

public class MemberSearchListActivity extends BaseActivity {
    Context context = MemberSearchListActivity.this;
    @InjectView(R.id.listview)
    PullToRefreshListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_member_list);
        ButterKnife.inject(this);
        initData();
        initHandler();

    }


    private void initData() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                mSelfData.clear();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mSelfData.clear();

            }
        });

    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        listView.onRefreshComplete();
                        break;
                    case 2:
                        listView.onRefreshComplete();
                        break;
                    case 3:
                        listView.onRefreshComplete();
                        break;
                }

            }
        };
    }


}
