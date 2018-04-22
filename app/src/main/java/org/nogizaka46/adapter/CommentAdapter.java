package org.nogizaka46.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.nogizaka46.R;
import org.nogizaka46.bean.CommentBean;
import org.nogizaka46.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lnx on 2018/4/5.
 */

public class CommentAdapter extends BaseQuickAdapter<CommentBean ,BaseViewHolder> {

    private  onItemClickLintener lintener;
    public interface  onItemClickLintener{

        void onClick(CommentBean item ,int childposition);

        void onLongClick(CommentBean item ,int childposition);
    }


    public CommentAdapter(@Nullable List<CommentBean> data,onItemClickLintener lintener) {
        super(R.layout.layout_comment_father, data);
        this.lintener = lintener ;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommentBean item) {
           helper.setText(R.id.tv_nickname_comfather,item.getUser().getNickname())
                   .setText(R.id.tv_msg_comfather,item.getMsg())
                   .setText(R.id.tv_time_comfather,item.getTime())
                   .setText(R.id.tv_floor_comfather,"#"+item.getFloor());
        new ImageLoader.Builder(mContext).setImageUrl(item.getUser().getAvatar()).setImageView((ImageView) helper.getView(R.id.iv_avatar_comfather))
                .setLoadResourceId(R.drawable.morenhead)
                .show();




        RecyclerView rv_child = helper.getView(R.id.rv_com_child);


        List<CommentBean.ChildBean> child = item.getChild();
        List<CommentBean.ChildBean> childBeans = new ArrayList<>();
        ComChildAdapter childAdapter;
        if(child.size()>4){

            childBeans.addAll(child.subList(0, 4));
            CommentBean.ChildBean childBean = new CommentBean.ChildBean();
            childBean.setGoMore(true);
            childBeans.add(childBean);
            childAdapter = new ComChildAdapter(childBeans);
        }else{
            childAdapter = new ComChildAdapter(child);
        }

        rv_child.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL ,false));
        rv_child.setAdapter(childAdapter);



        if(child!=null){

            childAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    //int oldPosition = helper.getOldPosition();
                    //int adapterPosition = helper.getAdapterPosition();
                    int layoutPosition = helper.getLayoutPosition();
                    String msg = item.getMsg();
                    String msg1 = item.getChild().get(position).getMsg();
                   // Log.e(TAG, "onItemClick: " +msg  + "----" +msg1 );

                    lintener.onClick(item ,position);


                }
            });


            childAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                    lintener.onLongClick(item,position);
                    return true;
                }
            });
        }


    }
}
