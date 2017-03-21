package org.nogizaka46;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import adapter.BlogListAdapter;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;
import utils.Constants;
import utils.Httputil;

public class BlogListActivity extends BaseActivity {
    PullToRefreshListView listView;
    Context context=BlogListActivity.this;
    TextView title;
    String name_alphas,member_names;
    BlogListAdapter bloglistadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_list);
        initView();
        initData();
        initHandler();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshList();
            }
        }, 500);
    }


    private void initView() {
        ImageButton img_left_layout=(ImageButton) findViewById(R.id.top_button_back);
        img_left_layout.setVisibility(View.VISIBLE);
        title= (TextView) findViewById(R.id.title);
        listView= (PullToRefreshListView) findViewById(R.id.listview);
    }
    private void initData() {
        name_alphas=getIntent().getStringExtra("name_alpha");
        member_names=getIntent().getStringExtra("name_kanji");
        title.setText(member_names+"");
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new BlogListRefreshListener());
    }

  private  class BlogListRefreshListener implements PullToRefreshBase.OnRefreshListener2{

      @Override
      public void onPullDownToRefresh(PullToRefreshBase refreshView) {
          listView.setMode(PullToRefreshBase.Mode.BOTH);
          mSelfData.clear();
          refreshList();
      }

      @Override
      public void onPullUpToRefresh(PullToRefreshBase refreshView) {
          mSelfData.clear();
          doAction();
      }
  }
    private void initHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case  1:
                        SetListData();
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


    private void refreshList(){
        if (!listView.isRefreshing()){
            listView.setRefreshing();
        }else {
            doAction();
        }
    }

    private void SetListData() {
        if (bloglistadapter==null){
            bloglistadapter=new BlogListAdapter(context,mSelfData);
            listView.setAdapter(bloglistadapter);
        }else{
            bloglistadapter.notifyDataSetChanged();
        }
    }

    private void doAction(){
        Httputil.httpGet(Constants.Base_Url + "member/blogs?nameAlpha="+name_alphas, new RequestCallBack<String>() {
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
                            map1.put("author",itemobj.optString("author").toString());
                            map1.put("time",itemobj.optString("time").toString());
                            map1.put("title",itemobj.optString("title").toString());
                            map1.put("summary",itemobj.optString("summary").toString());
                            map1.put("url",itemobj.optString("url").toString());
                            map1.put("name_alpha",itemobj.optString("name_alpha").toString());
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
    public  void  back(View view){
        this.finish();
    }
    public  void  doActionRight(View view){

    }
}
