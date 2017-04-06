package org.nogizaka46.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by StevenWang on 16/8/2.
 */
public abstract class RecyclerViewAdapterHelper<T> extends RecyclerView.Adapter<RecyclerView
        .ViewHolder> {
    public Context mContext = null;
    public List<T> mList = null;
    public LayoutInflater mInflater = null;

    public RecyclerViewAdapterHelper(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateMyViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindMyViewHolder(holder, position);
    }

    public abstract RecyclerView.ViewHolder onCreateMyViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindMyViewHolder(RecyclerView.ViewHolder holder, int position);


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void reloadRecyclerView(List<T> _list, boolean isClear) {
        if (isClear) {
            mList.clear();
        }
        mList.addAll(_list);
        notifyDataSetChanged();
    }

    /**
     * 适配器中添加单条数据，刷新UI
     *
     * @param position 要添加的数据所在适配器中的位置
     * @param data     要添加的数据
     */
    public void addItem(int position, T data) {
        mList.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * 适配器中批量添加数据，刷新UI
     *
     * @param _list         批量添加的集合
     * @param positionStart 添加到适配器中的起始位置
     */
    public void addItems(List<T> _list, int positionStart) {
        mList.addAll(_list);
        int itemCount = mList.size();
        notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * 适配器中删除单条数据，刷新UI
     *
     * @param position 要删除的数据所在适配器中的位置
     */
    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 适配器中批量删除多条数据，刷新UI
     *
     * @param _list         要删除的数据集合
     * @param positionStart 删除的数据在适配器中的起始位置
     */
    public void removeItems(List<T> _list, int positionStart) {
        mList.removeAll(_list);
        int itemCount = mList.size();
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * 修改适配器中单条数据，刷新UI
     *
     * @param position 更新的数据所在适配器中的位置
     * @param data     更新后的数据集合
     */
    public void updateItem(int position, T data) {
        mList.remove(position);
        mList.add(position, data);
        notifyItemChanged(position);
        // notifyItemRangeChanged(positionStart, itemCount);
    }
}
