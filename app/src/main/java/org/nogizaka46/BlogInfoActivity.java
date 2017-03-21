package org.nogizaka46;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;



public class BlogInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_info);
        ImageButton img_left_layout=(ImageButton) findViewById(R.id.top_button_back);
        img_left_layout.setVisibility(View.VISIBLE);

    }


}
