package org.nogizaka46.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.view.MyToast;

public class ImageActivity extends BaseActivity {

    @InjectView(R.id.photoview)
    PhotoView photoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        Glide.with(this).load(image).into(photoview);
    }
}
