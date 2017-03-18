package org.nogizaka46;

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
import utils.Constants;
import utils.Httputil;
import view.MyToast;
import view.WaveSwipeRefreshLayout;


public class Main2Frag_Tab1 extends Fragment {
      View view;
     RecyclerView recyclerView;
    WaveSwipeRefreshLayout swipeRefreshLayout;
     private Handler handler;
     private List<Map<String, Object>> mSelfData;
     NewsListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view=inflater.inflate(R.layout.main2frag_tab1, container, false);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        if (handler==null){
           initView();
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

    private void initView() {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout= (WaveSwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    private void initData() {
        mSelfData = new ArrayList<Map<String, Object>>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());
    }

    private class SwipeRefreshListener implements  WaveSwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            mSelfData.clear();
            refreshList();
        }
    }

    private void initHandler() {
         handler=new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 switch (msg.what){
                     case 1:
                         SetListData();
                         swipeRefreshLayout.setRefreshing(false);
                         break;
                     case 2:
                         MyToast.showText(getActivity(),msg.obj.toString(),false);
                         swipeRefreshLayout.setRefreshing(false);
                         break;
                     case 3:
                         MyToast.showText(getActivity(),msg.obj.toString(),false);
                         swipeRefreshLayout.setRefreshing(false);
                         break;
                 }
             }
         };
    }
    private  void SetListData(){
       if (adapter==null){
           adapter=new NewsListAdapter(getActivity(),mSelfData);
           recyclerView.setAdapter(adapter);
       }else {
           adapter.notifyDataSetChanged();
       }
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Map<String,?>item=mSelfData.get(position);
                Intent intent=new Intent(getActivity(),NewsInfoActivity.class);
                intent.putExtra("id",item.get("id").toString());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
     }

    private void refreshList() {
      if(!swipeRefreshLayout.isRefreshing()){
          swipeRefreshLayout.setRefreshing(true);
      }else {
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
                    JSONArray content=jsonObject.getJSONArray("content");
                    if (content!=null&& content.length() > 0) {
                        for (int i = 0; i <content.length() ; i++) {
                            HashMap<String, Object> map1 = new HashMap<String, Object>();
                            JSONObject itemobj = new JSONObject(content.get(i).toString());
                            map1.put("id",itemobj.optString("id").toString());
                            map1.put("image_url",itemobj.optString("image_url").toString());
                            map1.put("name",itemobj.optString("name").toString());
                            map1.put("summary",itemobj.optString("summary").toString());
                            map1.put("content",itemobj.optString("content").toString());
                            map1.put("category",itemobj.optString("category").toString());
                            map1.put("created_time",itemobj.optString("created_time").toString());
                            mSelfData.add(map1);
                        }
                    }
                    msg.what=1;
                } catch (Exception e) {
                    msg.what=2;
                    msg.obj=getResources().getString(R.string.nodata);
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message msg=new Message();
                msg.what=3;
                msg.obj=getResources().getString(R.string.intentent_slow);
                handler.sendMessage(msg);
            }
        });
    }

}
