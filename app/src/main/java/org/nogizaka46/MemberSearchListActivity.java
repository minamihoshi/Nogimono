package org.nogizaka46;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SearchView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import adapter.MemberSearchAdapter;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;
import utils.Constants;
import utils.Httputil;

public class MemberSearchListActivity extends BaseActivity{
    SearchView searchView;
    PullToRefreshListView listView;
    Context context=MemberSearchListActivity.this;
    MemberSearchAdapter memberSearchAdapter;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_member_list);
        initView();
        initData();
        initHandler();

    }

    private void initView() {
        searchView= (SearchView) findViewById(R.id.search_view);
        listView= (PullToRefreshListView) findViewById(R.id.listview);
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                result=query.toString();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                result=newText.toString();
                return true;
            }
        });
    }

    private void initHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case  1:
                        //SetListData();
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
