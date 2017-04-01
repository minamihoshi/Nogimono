package org.nogizaka46;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ShowMemberAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;
import utils.Constants;
import utils.Httputil;


public class Main2Frag_Tab2 extends Fragment implements  AdapterView.OnItemClickListener {
    View view;
    List<Map<String, Object>> mSelfData;
    Handler handler;
    @InjectView(R.id.listview)
    PullToRefreshListView listview;
    ShowMemberAdapter showMemberAdapter;
    @InjectView(R.id.net_error_layout)  RelativeLayout netErrorMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main2frag_tab2, container, false);
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
            initData();
            initHandler();
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
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new BlogListRefreshListener());
        listview.setOnItemClickListener(this);
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        SetListData();
                        netErrorMain.setVisibility(View.GONE);//网络设置隐藏
                        listview.onRefreshComplete();
                        break;
                    case 2:
                        netErrorMain.setVisibility(View.VISIBLE);
                        netErrorMain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        listview.onRefreshComplete();
                        break;
                }
            }
        };
    }

    private void SetListData() {
        if (showMemberAdapter == null) {
            showMemberAdapter = new ShowMemberAdapter(getActivity(), mSelfData);
            listview.setAdapter(showMemberAdapter);
        } else {
            showMemberAdapter.notifyDataSetChanged();
        }

    }

    private void refreshList() {
        if (!listview.isRefreshing()) {
            listview.setRefreshing();
        } else {
            doAction();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,?>item=mSelfData.get(position-1);
        Intent intent=new Intent(getActivity(),BlogInfoActivity.class);
        intent.putExtra("preview",item.get("preview").toString());
        startActivity(intent);
    }

    private class BlogListRefreshListener implements PullToRefreshBase.OnRefreshListener2 {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            listview.setMode(PullToRefreshBase.Mode.BOTH);
            mSelfData.clear();
            refreshList();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            mSelfData.clear();
            doAction();
        }
    }

    private void doAction() {
        Httputil.httpGet(Constants.New_Base_Url + "data/getblog", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    JSONArray content = jsonObject.optJSONArray("content");
                    if (content != null && content.length() > 0) {
                        for (int i = 0; i < content.length(); i++) {
                            HashMap<String, Object> map1 = new HashMap<String, Object>();
                            JSONObject itemobj = new JSONObject(content.opt(i).toString());
                            map1.put("id", itemobj.optString("id").toString());
                            map1.put("title", itemobj.optString("title").toString());
                            map1.put("summary", itemobj.optString("summary").toString());
                            map1.put("delivery", itemobj.optString("delivery").toString());
                            map1.put("type", itemobj.optString("type").toString());
                            map1.put("detail", itemobj.optString("detail").toString());
                            map1.put("preview", itemobj.optString("preview").toString());
                            mSelfData.add(map1);
                        }
                    }
                    msg.what = 1;
                } catch (Exception e) {
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
