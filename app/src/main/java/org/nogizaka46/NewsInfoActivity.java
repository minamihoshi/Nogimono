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
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.Constants;
import utils.Httputil;
import utils.MyUtil;
import view.MyToast;

/**
 * 新闻详情界面
 */
public class NewsInfoActivity extends BaseActivity {
    String id;
    HashMap<String, Object> map;
    Context context = NewsInfoActivity.this;
    View view;
    @InjectView(R.id.top_button_back)
    ImageButton topButtonBack;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.title_texts)
    TextView titleTexts;
    @InjectView(R.id.type)
    TextView type;
    @InjectView(R.id.time)
    TextView time;
    @InjectView(R.id.images)
    ImageView images;
    @InjectView(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_info);
        ButterKnife.inject(this);
        topButtonBack.setVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("id");
        title.setText(R.string.news_info);
        initHandler();
        doAction();
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        titleTexts.setText(map.get("name").toString());
                        content.setText(map.get("content").toString());
                        type.setText(getResources().getString(R.string.news_type) + " " + map.get("category").toString());
                        time.setText(getResources().getString(R.string.news_times) + " " + MyUtil.timeToDate(map.get("created_time").toString()));
                        Glide.with(getApplicationContext()).load(map.get("image_url").toString()).error(R.drawable.noimg).into(images);
                        break;
                    case 2:
                        MyToast.showText(context, msg.obj.toString(), Toast.LENGTH_SHORT, false);
                        break;
                }
            }
        };
    }

    private void doAction() {
        Httputil.httpGet(Constants.Base_Url + "/blogs/getBlogDetailById?id=" + id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = new Message();
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    if (obj.getString("code").equals("200")) {
                        if (obj != null) {
                            JSONObject responseData = obj.optJSONObject("responseData");
                            map = new HashMap<String, Object>();
                            map.put("name", responseData.optString("name"));
                            map.put("summary", responseData.optString("summary"));
                            map.put("content", responseData.optString("content"));
                            map.put("category", responseData.optString("category"));
                            map.put("image_url", responseData.optString("image_url"));
                            map.put("created_time", responseData.optString("created_time"));
                            mSelfData.add(map);
                            msg.what = 1;
                        }
                    }
                } catch (Exception e) {

                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = getResources().getString(R.string.nodata);
                handler.sendMessage(msg);
            }
        });
    }

    public void back(View view) {
        this.finish();
    }

    public void doActionRight(View view) {

    }
}
