package org.nogizaka46;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;

import utils.Constants;
import utils.Httputil;
import view.MyToast;
import utils.MyUtil;

/**
 *新闻详情界面
 * /blogs/getBlogDetailById
 */
public class NewsInfoActivity extends  BaseActivity{
     String id;
     TextView title;
     HashMap<String,Object>map;
     Context context=NewsInfoActivity.this;
     TextView  txt1,txt2,txt3,txt4;
      ImageView images;
      View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_info);
        initView();
        initHandler();
        doAction();
    }

    private void initView() {
        ImageButton left_img=(ImageButton) findViewById(R.id.top_button_back);
        left_img.setVisibility(View.VISIBLE);
        id=getIntent().getStringExtra("id");
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.news_info);
        txt1= (TextView) findViewById(R.id.title_texts);
        txt2= (TextView) findViewById(R.id.content);
        txt3= (TextView) findViewById(R.id.type);
        txt4= (TextView) findViewById(R.id.time);
        images=(ImageView) findViewById(R.id.images);
    }
    private void initHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case  1:
                        txt1.setText(map.get("name").toString());
                        txt2.setText(map.get("content").toString());
                        txt3.setText(getResources().getString(R.string.news_type)+" "+map.get("category").toString());
                        txt4.setText(getResources().getString(R.string.news_times)+" "+ MyUtil.timeToDate(map.get("created_time").toString()));
                        Glide.with(getApplicationContext()).load(map.get("image_url").toString()).error(R.drawable.noimg).into(images);
                        break;
                    case  2:
                        MyToast.showText(context, msg.obj.toString(), Toast.LENGTH_SHORT, false);
                        break;
                }
            }
        };
    }
    private void doAction() {
        Httputil.httpGet( Constants.Base_Url+"/blogs/getBlogDetailById?id="+ id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg=new Message();
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    if(obj.getString("code").equals("200")){
                        if(obj!=null){
                            JSONObject responseData=obj.getJSONObject("responseData");
                            map=new HashMap<String, Object>();
                            map.put("name",responseData.optString("name"));
                            map.put("summary",responseData.optString("summary"));
                            map.put("content",responseData.optString("content"));
                            map.put("category",responseData.optString("category"));
                            map.put("image_url",responseData.optString("image_url"));
                            map.put("created_time",responseData.optString("created_time"));
                            mSelfData.add(map);
                            msg.what=1;
                        }
                    }
                } catch (Exception e) {

                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message msg=new Message();
                msg.what=2;
                msg.obj=getResources().getString(R.string.nodata);
                handler.sendMessage(msg);
            }
        });
    }

    public  void back(View view){
        this.finish();
    }

    public  void doActionRight(View view){

    }
}
