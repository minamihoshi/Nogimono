package org.nogizaka46.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nogizaka46.R;
import org.nogizaka46.bean.BlogBean;
import org.nogizaka46.utils.RecyclerViewAdapterHelper;
import org.nogizaka46.utils.TimeUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by acer on 2017/4/19.
 */

public class BlogAdapter extends RecyclerViewAdapterHelper<BlogBean> {

    private onBlogClicklistener listener;

    public BlogAdapter(Context context, List<BlogBean> list, onBlogClicklistener listener) {
        super(context, list);
        this.listener = listener;
    }


    public interface onBlogClicklistener {
        void onBlogClick(int position);

        void onBlogLongClick(int position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateMyViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BlogBean blogBean = mList.get(position);
        String title = blogBean.getTitle();
        int post = blogBean.getPost();
        String author = blogBean.getAuthor();
        String time = TimeUtil.timeToDate(post);

        ((ViewHolder)holder).tvName.setText(author);
        ((ViewHolder)holder).tvTime.setText(time);
        ((ViewHolder)holder).tvTitle.setText(title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBlogClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onBlogLongClick(position);
                return true;
            }
        });



    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
