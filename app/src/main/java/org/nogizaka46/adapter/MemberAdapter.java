package org.nogizaka46.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.nogizaka46.R;
import org.nogizaka46.bean.MemberListBean;
import org.nogizaka46.utils.BitmapCircleTransformation;
import org.nogizaka46.utils.RecyclerViewAdapterHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by acer on 2017/4/18.
 */

public class MemberAdapter extends RecyclerViewAdapterHelper<MemberListBean> {

    private onMemberClickListener listener;

    public MemberAdapter(Context context, List<MemberListBean> list, onMemberClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    public interface onMemberClickListener {
        void onMemberClick(int position);

        void onMemberLongClick(int position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateMyViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclermember, parent, false);
        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MemberListBean memberBean = mList.get(position);
        String name = memberBean.getName();
        String portrait = memberBean.getPortrait();
        ImageView iv_member = ((MyViewHolder) holder).iv_member;
        ((MyViewHolder)holder).membername.setText(name);
        Glide.with(mContext).load(portrait).transform(new BitmapCircleTransformation(mContext)).into(iv_member);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMemberClick(position);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onMemberLongClick(position);
                return true;
            }
        });
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.membername)
        TextView membername;
        @InjectView(R.id.iv_member)
        ImageView iv_member;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
