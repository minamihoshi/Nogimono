package org.nogizaka46.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.nogizaka46.R;
import org.nogizaka46.adapter.MyNewsAdapter;

/**
 * 2018/4/3
 *
 * @author wangjf
 */

public class ImageLoader {
    public static class Builder{
        private Context context;
        private String imageUrl;
        private ImageView imageView;
        private Integer loadResourceId;
        private Float sizeMultiplie;

        private boolean isCrossFade;
        private boolean isCircleCrop;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setImageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder setLoadResourceId(Integer loadResourceId) {
            this.loadResourceId = loadResourceId;
            return this;
        }




        public Builder setCircleCrop(boolean circleCrop) {
            isCircleCrop = circleCrop;
            return this;
        }

        public Builder setSizeMultiplie(Float sizeMultiplie) {
            this.sizeMultiplie = sizeMultiplie;
            return this;
        }

        public Builder setCrossFade(boolean crossFade) {
            isCrossFade = crossFade;
            return this;
        }

        public void show(){
            RequestBuilder builder =  Glide.with(context).load(imageUrl);

            RequestOptions requestOptions = null;
            if(isCrossFade){
                builder = builder.transition(DrawableTransitionOptions.withCrossFade());
            }

            if(isCircleCrop){
                if(requestOptions == null){
                    requestOptions = new RequestOptions();
                }
                requestOptions = requestOptions.circleCrop();
            }
            if(loadResourceId != null){
                if(requestOptions == null){
                    requestOptions = new RequestOptions();
                }
                requestOptions = requestOptions.placeholder(loadResourceId);
            }
            if(sizeMultiplie != null){
                builder = builder.thumbnail(sizeMultiplie);
            }
            if(requestOptions != null){
                builder = builder.apply(requestOptions);
            }
            builder.into(imageView);
        }
    }
}
