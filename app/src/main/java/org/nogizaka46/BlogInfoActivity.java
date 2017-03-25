package org.nogizaka46;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BlogInfoActivity extends BaseActivity {

    @InjectView(R.id.top_button_back)
    ImageButton img_left_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_info);
        ButterKnife.inject(this);
        img_left_layout.setVisibility(View.VISIBLE);

    }


}
