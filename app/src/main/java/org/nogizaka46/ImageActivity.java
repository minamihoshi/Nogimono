package org.nogizaka46;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import view.MyToast;

public class ImageActivity extends AppCompatActivity {

    @InjectView(R.id.photoview)
    PhotoView photoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        MyToast.showText(this,image, Toast.LENGTH_LONG);
        photoview.setImageResource(R.mipmap.nogi17th);
        Glide.with(this).load(image).into(photoview);
    }
}
