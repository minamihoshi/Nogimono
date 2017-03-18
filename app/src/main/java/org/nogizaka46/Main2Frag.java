package org.nogizaka46;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;



public class Main2Frag extends Fragment {
    View view;
     Main2Frag_Tab1 main2Frag_tab1=new Main2Frag_Tab1();
    Main2Frag_Tab2 main2Frag_tab2=new Main2Frag_Tab2();
    Main2Frag_Tab3 main2Frag_tab3=new Main2Frag_Tab3();
    Main2Frag_Tab4 main2Frag_tab4=new Main2Frag_Tab4();
    Main2Frag_Tab5 main2Frag_tab5=new Main2Frag_Tab5();
    TabLayout tabLayout;
    ViewPager viewPager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

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
    }
    private void initData() {
        mTitleList.add(getResources().getString(R.string.tab_1_txt));
        mTitleList.add(getResources().getString(R.string.tab_2_txt));
        mTitleList.add(getResources().getString(R.string.tab_3_txt));
        mTitleList.add(getResources().getString(R.string.tab_4_txt));
        mTitleList.add(getResources().getString(R.string.tab_5_txt));
        mViewList.add(main2Frag_tab1);
        mViewList.add(main2Frag_tab2);
        mViewList.add(main2Frag_tab3);
        mViewList.add(main2Frag_tab4);
        mViewList.add(main2Frag_tab5);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(4)));
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
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
