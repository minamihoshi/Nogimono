package org.nogizaka46.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.nogizaka46.R;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllListAdapter extends Adapter<ViewHolder>{
    private Context context;
    private List<Map<String, Object>> mData;

    public AllListAdapter(Context context, List<Map<String, Object>> data) {
        this.context=context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_list_item, null, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Map<String,?>item=mData.get(position);
            itemViewHolder.name.setText(item.get("title").toString());
            itemViewHolder.summary.setText(item.get("summary").toString());
            //itemViewHolder.created_time.setText(TimeUtil.timeToDate(item.get("delivery").toString()));
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);

                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }

    }
   
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    static class ItemViewHolder extends ViewHolder {
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
