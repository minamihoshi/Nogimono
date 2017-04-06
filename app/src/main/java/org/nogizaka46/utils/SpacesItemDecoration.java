package org.nogizaka46.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置item之间的分割空间
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //outRect.left = space;
        //outRect.right = space;
        outRect.bottom = space;
        if(parent.getChildPosition(view) == 0 || parent.getChildPosition(view) == 1 || parent.getChildPosition(view) == 2 || parent.getChildPosition(view) == 3){
            outRect.top = space;
        }
    }
}
