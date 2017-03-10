package org.nogizaka46;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class MemberInfoActivity extends BaseActivity {
    ImageView img;
    TextView texts;
    String name,imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_info);
        initWidget();
        initData();

    }

    private void initWidget() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(name);
    }



    private void initData() {
        name=getIntent().getStringExtra("texts");
        imgs=getIntent().getStringExtra("img");
        img=(ImageView)findViewById(R.id.images);
        texts= (TextView) findViewById(R.id.text);
        Glide.with(MemberInfoActivity.this).load(R.drawable.noimg).into(img);
        texts.setText(name);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
