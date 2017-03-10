package org.nogizaka46;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 杂志
 */
public class Main2Frag_Tab4 extends Fragment{
    View view;
    private int[] data = {R.drawable.back,R.drawable.back,R.drawable.back,R.drawable.back,R.drawable.back,R.drawable.back,R.drawable.back,R.drawable.back,R.drawable.back};
    private String[] name={"1","2","3","4","1","2","3","4","5"};
    GridView gridView;
   private  ShlefAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view=inflater.inflate(R.layout.main2frag_tab4, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        gridView= (GridView) view.findViewById(R.id.book_gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter=new ShlefAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSeclection(position);
                adapter.notifyDataSetChanged();

            }
        });

    }
    class ShlefAdapter extends BaseAdapter {
        private int clickTemp = -1;
        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }
        public void setSeclection(int position) {
            clickTemp = position;
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup parnet) {

            contentView=getActivity().getLayoutInflater().inflate(R.layout.magize_item,null);

            TextView text1=(TextView) contentView.findViewById(R.id.text);
            text1.setText(name[position]);

            ImageView img=(ImageView) contentView.findViewById(R.id.images);
            img.setBackgroundResource(data[position]);
            LinearLayout lin1= (LinearLayout) contentView.findViewById(R.id.layout1);
            if (clickTemp == position) {
                lin1.setBackgroundResource(R.drawable.spotlight);
            }else{
                lin1.setBackgroundColor(Color.TRANSPARENT);
            }
            return contentView;
        }

    }
}
