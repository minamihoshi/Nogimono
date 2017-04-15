package org.nogizaka46.ui.fragment;


import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nogizaka46.R;
import org.nogizaka46.config.Constant;
import org.nogizaka46.config.UrlConfig;
import org.nogizaka46.ui.newsfragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;



public class Main2Frag extends Fragment {
    View view;
     //Main2Frag_Tab1 main2Frag_tab1=new Main2Frag_Tab1();//所有

    TabLayout tabLayout;
    ViewPager viewPager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    private List<String> mPathList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.main2_frag, container, false);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            return;
        initView();
        initData();

    }

    private void initView() {
        tabLayout= (TabLayout) view.findViewById(R.id.tabs);
        viewPager= (ViewPager) view.findViewById(R.id.tab_pager);
        viewPager.setOffscreenPageLimit(3);
        //viewPager.setPageTransformer();
    }
    private void initData() {
        mTitleList.add(getResources().getString(R.string.tab_1_txt));
        mTitleList.add(getResources().getString(R.string.tab_2_txt));
        mTitleList.add(getResources().getString(R.string.tab_3_txt));
        mTitleList.add(getResources().getString(R.string.tab_4_txt));
        mPathList.add(Constant.TYPE_ALL);
        mPathList.add(Constant.TYPE_BLOG);
        mPathList.add(Constant.TYPE_NEWS);
        mPathList.add(Constant.TYPE_MAGAZINE);
        initFrag();
        tabLayout.setTabMode(TabLayout. MODE_FIXED);//设置tab模式，当前为系统默认模式
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }

    private void initFrag() {
        for (int i = 0;  i <mTitleList.size() ; i++) {
              NewsFragment fragment = new NewsFragment();
             Bundle bundle = new Bundle();
             bundle.putString(Constant.NEWS_CATEGORY,mPathList.get(i));
              fragment.setArguments(bundle);
              mViewList.add(fragment);
        }
    }

    class  MyPagerAdapter extends  FragmentPagerAdapter{

         public MyPagerAdapter(FragmentManager fm) {
             super(fm);
         }

         @Override
         public Fragment getItem(int position) {
             return mViewList.get(position);
         }

         @Override
         public int getCount() {
             return mViewList.size();
         }
         @Override
         public CharSequence getPageTitle(int position) {
             return mTitleList.get(position);
         }
     }

}
