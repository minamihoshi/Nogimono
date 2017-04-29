package org.nogizaka46.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.nogizaka46.R;
import org.nogizaka46.bean.NewBean;
import org.nogizaka46.bean.WithpicBean;
import org.nogizaka46.utils.RecyclerViewAdapterHelper;
import org.nogizaka46.utils.TimeUtil;

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
        List<WithpicBean> withpics = newBean.getWithpic();
        if (withpics == null) {
            return 0;
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
                view = mInflater.inflate(R.layout.item_recycleview_no_pic, parent, false);
                return new NoPicHolder(view);
            case 1:
                view = mInflater.inflate(R.layout.item_recycleview_one_pic, parent, false);
                return new OnePicHolder(view);
            case 3:
                view = mInflater.inflate(R.layout.item_recycleview_more_pic, parent, false);
                return new ThreePicHolder(view);

        }
        return null;
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NewBean newBean = mList.get(position);
        String title = newBean.getTitle();
        int delivery = newBean.getDelivery();
        String provider = newBean.getProvider();
        final String time = TimeUtil.timeToDate(delivery);
        String subtitle = newBean.getSubtitle();
        String summary = newBean.getSummary();

        if (holder instanceof NoPicHolder) {

            ((NoPicHolder) holder).authorTvItemNopic.setText(provider);
            // ((NoPicHolder) holder).sourceTvItemNopic.setText(subtitle);
            ((NoPicHolder) holder).summaryTvItemNopic.setText(summary);
            ((NoPicHolder) holder).timeTvNopic.setText(time);
            ((NoPicHolder) holder).titleTvItemNopic.setText(title);


        } else if (holder instanceof OnePicHolder) {
            List<WithpicBean> withpic = newBean.getWithpic();
            WithpicBean withpicBean = withpic.get(0);
            String image = withpicBean.getImage();


            ((OnePicHolder) holder).titleTvItemOnepic.setText(title);
            ((OnePicHolder) holder).timeTvItemOnepic.setText(time);
            ((OnePicHolder) holder).authorTvItemOnepic.setText(provider);
            ((OnePicHolder) holder).tvSummary.setText(summary);

            //((OnePicHolder) holder).sourceTvItemOnepic.setText(subtitle);
            ImageView imageView = ((OnePicHolder) holder).imageItemRecycleviewHotOnepic;

            Glide.with(mContext).load(image).placeholder(R.drawable.loading).crossFade().into(imageView);

        } else {
            List<WithpicBean> withpic = newBean.getWithpic();


            String image1 = withpic.get(0).getImage();
            String image2 = withpic.get(1).getImage();
            String image3 = withpic.get(2).getImage();


            ((ThreePicHolder) holder).titleTextviewItemRecycleviewHotMorepic.setText(title);
            Glide.with(mContext).load(image1).placeholder(R.drawable.loading).crossFade().into(((ThreePicHolder) holder).image1ItemRecycleviewHotMorepic);
            Glide.with(mContext).load(image2).placeholder(R.drawable.loading).crossFade().into(((ThreePicHolder) holder).image2ItemRecycleviewHotMorepic);
            Glide.with(mContext).load(image3).placeholder(R.drawable.loading).crossFade().into(((ThreePicHolder) holder).image3ItemRecycleviewHotMorepic);
            ((ThreePicHolder) holder).timeTvNopic.setText(time);
            ((ThreePicHolder) holder).authorTvItemNopic.setText(provider);
            // ((ThreePicHolder) holder).sourceTvItemNopic.setText(subtitle);


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
                return true;
            }
        });

    }

    static class NoPicHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title_tv_item_nopic)
        TextView titleTvItemNopic;
        @InjectView(R.id.summary_tv_item_nopic)
        TextView summaryTvItemNopic;
        @InjectView(R.id.time_tv_nopic)
        TextView timeTvNopic;
        @InjectView(R.id.author_tv_item_nopic)
        TextView authorTvItemNopic;
        @InjectView(R.id.source_tv_item_nopic)
        TextView sourceTvItemNopic;

        public NoPicHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    static class OnePicHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title_tv_item_onepic)
        TextView titleTvItemOnepic;
        @InjectView(R.id.time_tv_item_onepic)
        TextView timeTvItemOnepic;
        @InjectView(R.id.author_tv_item_onepic)
        TextView authorTvItemOnepic;
        @InjectView(R.id.source_tv_item_onepic)
        TextView sourceTvItemOnepic;
        @InjectView(R.id.image_item_recycleview_hot_onepic)
        ImageView imageItemRecycleviewHotOnepic;
        @InjectView(R.id.tv_summary)
        TextView tvSummary;

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
        @InjectView(R.id.time_tv_nopic)
        TextView timeTvNopic;
        @InjectView(R.id.author_tv_item_nopic)
        TextView authorTvItemNopic;
        @InjectView(R.id.source_tv_item_nopic)
        TextView sourceTvItemNopic;

        public ThreePicHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


}


