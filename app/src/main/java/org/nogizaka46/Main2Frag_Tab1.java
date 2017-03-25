package org.nogizaka46;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.NewsListAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.Constants;
import utils.Httputil;
import utils.SpacesItemDecoration;
import view.WaveSwipeRefreshLayout;


public class Main2Frag_Tab1 extends Fragment {
    View view;
    NewsListAdapter adapter;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.swipeRefresh)
    WaveSwipeRefreshLayout swipeRefresh;
    @InjectView(R.id.main_layout)
    LinearLayout mainLayout;
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
        swipeRefresh.setWaveColor(R.color.main_bg_color);
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
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        };
    }

    private void SetListData() {
        if (adapter == null) {
            adapter = new NewsListAdapter(getActivity(), mSelfData);
            recyclerview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Map<String, ?> item = mSelfData.get(position);
                Intent intent = new Intent(getActivity(), NewsInfoActivity.class);
                intent.putExtra("id", item.get("id").toString());
                //因为共享元素是Android5.0引入的，所以需在android5.0（LOLLIPOP）以上系统下运行，低版本运行会报错，需要判断版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //实现元素共享效果前后两个界面的元素共享名字TransitionName必须相同，设置任意的字符串即可。
                    v.setTransitionName(getString( R.string.app_name));
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, v.getTransitionName());
                    startActivityForResult(intent, 1, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerview.addItemDecoration(decoration);
    }



    private void refreshList() {
        if (!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        } else {
            doAction();
        }
    }

    private void doAction() {
        Httputil.httpGet(Constants.Base_Url + "blogs/getBlogsByPage", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    JSONArray content = jsonObject.getJSONArray("content");
                    if (content != null && content.length() > 0) {
                        for (int i = 0; i < content.length(); i++) {
                            HashMap<String, Object> map1 = new HashMap<String, Object>();
                            JSONObject itemobj = new JSONObject(content.get(i).toString());
                            map1.put("id", itemobj.optString("id").toString());
                            map1.put("image_url", itemobj.optString("image_url").toString());
                            map1.put("name", itemobj.optString("name").toString());
                            map1.put("summary", itemobj.optString("summary").toString());
                            map1.put("content", itemobj.optString("content").toString());
                            map1.put("category", itemobj.optString("category").toString());
                            map1.put("created_time", itemobj.optString("created_time").toString());
                            mSelfData.add(map1);
                        }
                    }
                    msg.what = 1;
                } catch (Exception e) {
                    msg.what = 2;
                    msg.obj = getResources().getString(R.string.nodata);
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = getResources().getString(R.string.intentent_slow);
                handler.sendMessage(msg);
            }
        });
    }


}
