package org.nogizaka46;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Constants;
import utils.Httputil;




public class Main2Frag_Tab2 extends Fragment {
    View view;
    private RadioGroup group;
    private  LinearLayout lin1,lin2;
     List<Map<String, Object>> mSelfData;
    Handler handler;
    ShowMemberAdapter adapter;
    GridView gridView;
    RecyclerView recyclerView;
    public List<Map<String, Object>> total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
           view=inflater.inflate(R.layout.main2frag_tab2,container,false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        if (handler == null) {
        initView();
        initData();
        initHandler();

        }
    }

    private void initHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case  1:
                       SetListData();
                        break;

                }
            }
        };
    }

    private void initView() {
        lin1= (LinearLayout) view.findViewById(R.id.group_layout1);
        lin2= (LinearLayout) view.findViewById(R.id.group_layout2);
        group= (RadioGroup) view.findViewById(R.id.blog_group);
        gridView= (GridView) view.findViewById(R.id.grid);
        recyclerView= (RecyclerView) view.findViewById(R.id.member_list_recyclerview);
    }

    private void initData() {
        mSelfData = new ArrayList<Map<String, Object>>();
        total= new ArrayList<Map<String, Object>>();
        group.setOnCheckedChangeListener(new Main2Group());
        gridView.setOnItemClickListener(new GridMemberLisntener());
        doAction();
    }

    private void SetListData() {
        adapter=new ShowMemberAdapter(getActivity(),mSelfData);
        gridView.setAdapter(adapter);

    }

    private void doAction() {
        Httputil.httpGet( Constants.Base_Url+"member/getAll", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg=new Message();
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    if(obj.getString("code").equals("200")){
                            JSONArray responseData=obj.optJSONArray("responseData");
                        if (responseData!=null&& responseData.length() > 0) {
                            for (int i = 0; i <responseData.length() ; i++) {
                                HashMap<String,Object>map = new HashMap<String, Object>();
                                JSONObject itemobj = new JSONObject(responseData.opt(i).toString());
                                map.put("name_kanji",itemobj.optString("name_kanji").toString()); //成员的汉字
                                map.put("name_alpha",itemobj.optString("name_alpha").toString()); //成员的罗马音
                                map.put("avatar",itemobj.optString("avatar").toString());//成员头像
                                map.put("birthday",itemobj.optString("birthday").toString());//生日
                                map.put("url",itemobj.optString("url").toString());//blog地址
                                map.put("height",itemobj.optString("height").toString());//身高
                                map.put("period",itemobj.optString("period").toString());//
                                mSelfData.add(map);
                            }
                        }
                        msg.what=1;
                    }
                } catch (Exception e) {
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

  private  class  GridMemberLisntener implements AdapterView.OnItemClickListener{

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Map<String,?>item=mSelfData.get(position);
          Intent intent=new Intent(getActivity(),BlogListActivity.class);
          intent.putExtra("name_alpha",item.get("name_alpha").toString());
          intent.putExtra("name_kanji",item.get("name_kanji").toString());
          startActivity(intent);
      }
  }
   private  class Main2Group implements RadioGroup.OnCheckedChangeListener{
       @Override
       public void onCheckedChanged(RadioGroup group, int checkedId) {
           switch (checkedId){
               case  R.id.btn1:
                   lin1.setVisibility(View.VISIBLE);
                   lin2.setVisibility(View.GONE);
                   break;
               case  R.id.btn2:
                   lin1.setVisibility(View.GONE);
                   lin2.setVisibility(View.VISIBLE);
                   break;
           }
       }
   }

    public class ShowMemberAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, Object>> mSelfData;

        public ShowMemberAdapter(Context context, List<Map<String, Object>> data) {
            this.context = context;
            this.mSelfData = data;
        }

        @Override
        public int getCount() {
            return mSelfData.size();
        }


        @Override
        public Object getItem(int position) {
            return mSelfData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Map<String, ?> item = mSelfData.get(position);
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.member_grid_item, null);
                holder=new ViewHolder();
                holder.imageview= (ImageView) convertView.findViewById(R.id.imageview);
                holder.text= (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(position==mSelfData.size()){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_group_item, null);
                holder.peroid= (TextView) convertView.findViewById(R.id.group_name);
                holder.peroid.setText(item.get("peroid").toString());
            }else {
                Glide.with(context).load(item.get("avatar").toString()).override(290, 350).centerCrop().error(R.drawable.noimg).into(holder.imageview);
                holder.text.setText(item.get("name_kanji").toString());
            }

            return convertView;
        }


        class ViewHolder {
            TextView peroid;
            ImageView imageview;
            TextView text;

        }
    }
}
