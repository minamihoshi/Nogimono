package org.nogizaka46.adapter;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.nogizaka46.R;
import org.nogizaka46.bean.ComFloorBean;
import org.nogizaka46.utils.ImageLoader;

import java.util.List;

/**
 * Created by lnx on 2018/4/8.
 */

public class ComDetailAdapter extends BaseQuickAdapter<ComFloorBean.FloorBean.ChildBean,BaseViewHolder> {


    public ComDetailAdapter(@Nullable List<ComFloorBean.FloorBean.ChildBean> data) {
        super(R.layout.item_com_child, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComFloorBean.FloorBean.ChildBean item) {
        helper.setText(R.id.tv_nicknama_comchild,item.getUser().getNickname())
                .setText(R.id.tv_time_comchild ,item.getTime())
                .setText(R.id.tv_floor_comchild ,"#"+item.getFloor());

        String nickname=null;
        String s = null ;
        if(item.getTouser()!=null){
            int id = item.getTouser().getId();
            nickname = item.getTouser().getNickname();
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append("回复" +nickname +":" );
            s = builder.toString();
            helper .setText(R.id.tv_content_comchild ,s+item.getMsg());
        }else{
            helper .setText(R.id.tv_content_comchild ,item.getMsg());
        }

        new ImageLoader.Builder(mContext).setImageUrl(item.getUser().getAvatar()).setImageView((ImageView) helper.getView(R.id.iv_avatar_comchild));

    }
}
