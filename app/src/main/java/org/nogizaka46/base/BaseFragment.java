package org.nogizaka46.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by acer on 2016/11/15.
 */

public class BaseFragment extends Fragment {
   public Context mContext ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


//     public void  initToolBar(View rootView, @IdRes int id , String title, boolean hasDisplayHome){
//        toolbar = (Toolbar) rootView.findViewById(id);
//        setHasOptionsMenu(true);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        toolbar.setTitle(title);
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//
//        if(hasDisplayHome){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//    }
}
