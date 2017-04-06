package org.nogizaka46.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.nogizaka46.R;
import org.nogizaka46.bean.NewsBean;
import org.nogizaka46.utils.RecyclerViewAdapterHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by acer on 2017/4/5.
 */

public class NewsAdapter extends RecyclerViewAdapterHelper<NewsBean.ContentBean>{

    private onNewsClickListener listener ;
    public NewsAdapter(Context context, List list,onNewsClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    public interface  onNewsClickListener{
        void onNewsClick(int position);

    }

    @Override
    public RecyclerView.ViewHolder onCreateMyViewHolder(ViewGroup parent, int viewType) {
        View view =mInflater.inflate(R.layout.new_list_item,null,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){
            String title = mList.get(position).getTitle();
            String summary = mList.get(position).getSummary();
            ((ItemViewHolder) holder).name.setText(title);
            ((ItemViewHolder) holder).summary.setText(summary);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNewsClick(position);
                }
            });
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.news_images)
        ImageView images;
        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.summary)
        TextView summary;
        @InjectView(R.id.created_time)
        TextView created_time;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
    }
}
