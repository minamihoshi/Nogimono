package org.nogizaka46.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.nogizaka46.R;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.utils.TimeUtil;
import org.nogizaka46.utils.RecyclerViewAdapterHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by acer on 2017/4/12.
 */

public class MyNewsAdapter extends RecyclerViewAdapterHelper<NewBean> {
    private onNewsClickListener listener;

    public MyNewsAdapter(Context context, List<NewBean> list, onNewsClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    public interface onNewsClickListener {
        void onNewsClick(int position);

        void onNewsLongClick(int position);

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {

        NewBean newBean = mList.get(position);
        List<NewBean.WithpicBean> withpics = newBean.getWithpic();
        if(withpics == null){
            return  0;
        }
        int size = withpics.size();
        switch (size) {
            case 1:
                return 1;
            case 3:
                return 3;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateMyViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = mInflater.inflate(R.layout.item_recycleview_hotfragment_no_pic, parent, false);
                return new NoPicHolder(view);
            case 1:
                view = mInflater.inflate(R.layout.item_recycleview_hotfragment_one_pic, parent, false);
                return new OnePicHolder(view);
            case 3:
                view = mInflater.inflate(R.layout.item_recycleview_hotfragment_more_pic, parent, false);
                return new ThreePicHolder(view);

        }
        return null;
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NewBean newBean = mList.get(position);
        String title = newBean.getTitle();
        int delivery = newBean.getDelivery();
        Log.e("TAG", "onBindMyViewHolder: "+delivery );
        String provider = newBean.getProvider();
        final String time = TimeUtil.timeToDate(delivery);
        Log.e("TAG", "onBindMyViewHolder: "+time );
        if (holder instanceof NoPicHolder){

            ((NoPicHolder) holder).titleTextviewItemRecycleviewHotNopic.setText(title);
            ((NoPicHolder) holder).timeTextviewItemRecycleviewHotNopic.setText(time);
            ((NoPicHolder) holder).authorTextviewItemRecycleviewHotNopic.setText(provider);


        }else if(holder instanceof  OnePicHolder){
            List<NewBean.WithpicBean> withpic = newBean.getWithpic();
            NewBean.WithpicBean withpicBean = withpic.get(0);
            String image = withpicBean.getImage();


            ((OnePicHolder) holder).titleTextviewItemRecycleviewHotOnepic.setText(title);
            ((OnePicHolder) holder).timeTextviewItemRecycleviewHotOnepic.setText(time);
            ((OnePicHolder) holder).authorTextviewItemRecycleviewHotOnepic.setText(provider);
            ImageView imageView = ((OnePicHolder) holder).imageItemRecycleviewHotOnepic;

            Glide.with(mContext).load(image).placeholder(R.mipmap.ic_launcher).into(imageView);

        }else{
            List<NewBean.WithpicBean> withpic = newBean.getWithpic();
            String image1 = withpic.get(0).getImage();
            String image2 = withpic.get(1).getImage();
            String image3= withpic.get(2).getImage();


            ((ThreePicHolder) holder).titleTextviewItemRecycleviewHotMorepic.setText(title);
            Glide.with(mContext).load(image1).into( ((ThreePicHolder) holder).image1ItemRecycleviewHotMorepic);
            Glide.with(mContext).load(image1).into( ((ThreePicHolder) holder).image2ItemRecycleviewHotMorepic);
            Glide.with(mContext).load(image1).into( ((ThreePicHolder) holder).image3ItemRecycleviewHotMorepic);


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewsClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onNewsLongClick(position);
                return  true;
            }
        });

    }

    static class NoPicHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title_textview_item_recycleview_hot_nopic)
        TextView titleTextviewItemRecycleviewHotNopic;
        @InjectView(R.id.author_textview_item_recycleview_hot_nopic)
        TextView authorTextviewItemRecycleviewHotNopic;
        @InjectView(R.id.time_textview_item_recycleview_hot_nopic)
        TextView timeTextviewItemRecycleviewHotNopic;

        public NoPicHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    static class OnePicHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title_textview_item_recycleview_hot_onepic)
        TextView titleTextviewItemRecycleviewHotOnepic;
        @InjectView(R.id.author_textview_item_recycleview_hot_onepic)
        TextView authorTextviewItemRecycleviewHotOnepic;
        @InjectView(R.id.time_textview_item_recycleview_hot_onepic)
        TextView timeTextviewItemRecycleviewHotOnepic;
        @InjectView(R.id.image_item_recycleview_hot_onepic)
        ImageView imageItemRecycleviewHotOnepic;
        public OnePicHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    static class ThreePicHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title_textview_item_recycleview_hot_morepic)
        TextView titleTextviewItemRecycleviewHotMorepic;
        @InjectView(R.id.image1_item_recycleview_hot_morepic)
        ImageView image1ItemRecycleviewHotMorepic;
        @InjectView(R.id.image2_item_recycleview_hot_morepic)
        ImageView image2ItemRecycleviewHotMorepic;
        @InjectView(R.id.image3_item_recycleview_hot_morepic)
        ImageView image3ItemRecycleviewHotMorepic;
        public ThreePicHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }



}
