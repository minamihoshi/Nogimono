package org.nogizaka46.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.nogizaka46.R;
import org.nogizaka46.bean.UnreadComBean;
import org.nogizaka46.utils.ImageLoader;

import java.util.List;

/**
 * Created by lnx on 2018/4/6.
 */

public class UnreadAdapter extends BaseQuickAdapter<UnreadComBean ,BaseViewHolder> {


    public UnreadAdapter(@Nullable List<UnreadComBean> data) {
        super(R.layout.item_unread, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnreadComBean item) {

        helper.setText(R.id.tv_nickname_unread,item.getFromuser().getNickname())
                .setText(R.id.tv_msg_unread ,item.getMessage())
                .setText(R.id.tv_time_unread ,item.getTime());


        new ImageLoader.Builder(mContext).setImageUrl(item.getFromuser().getAvatar()).setImageView((ImageView)helper.getView(R.id.iv_unread));

    }
}
