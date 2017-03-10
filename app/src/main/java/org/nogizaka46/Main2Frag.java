package org.nogizaka46;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;


public class Main2Frag extends Fragment {
    View view;
    ViewPager viewPager;
    Main2Frag_Tab1 main2Frag_tab1=new Main2Frag_Tab1();
    Main2Frag_Tab2 main2Frag_tab2=new Main2Frag_Tab2();
    Main2Frag_Tab3 main2Frag_tab3=new Main2Frag_Tab3();
    Main2Frag_Tab4 main2Frag_tab4=new Main2Frag_Tab4();
    Main2Frag_Tab5 main2Frag_tab5=new Main2Frag_Tab5();
    private ViewPagerAdapter mViewPagerAdapter;
    private RadioGroup main2frag_radiotype;
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
        viewPager= (ViewPager) view.findViewById(R.id.viewPager);
        main2frag_radiotype= (RadioGroup) view.findViewById(R.id.main2frag_radiotype);
        main2frag_radiotype.check(R.id.radio_manger1);
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                      switch (position){
                          case 0:
                              main2frag_radiotype.check(R.id.radio_manger1);
                              break;
                          case 1:
                              main2frag_radiotype.check(R.id.radio_manger2);
                              break;
                          case 2:
                              main2frag_radiotype.check(R.id.radio_manger3);
                              break;
                          case 3:
                              main2frag_radiotype.check(R.id.radio_manger4);
                              break;
                          case 4:
                              main2frag_radiotype.check(R.id.radio_manger5);
                              break;
                      }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        main2frag_radiotype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                   switch (checkedId){
                       case R.id.radio_manger1:
                           viewPager.setCurrentItem(0);
                           break;
                       case R.id.radio_manger2:
                               viewPager.setCurrentItem(1);
                           break;
                       case R.id.radio_manger3:
                               viewPager.setCurrentItem(2);
                           break;
                       case R.id.radio_manger4:
                               viewPager.setCurrentItem(3);
                           break;
                       case R.id.radio_manger5:
                               viewPager.setCurrentItem(4);
                           break;
                   }

            }
        });
    }



    public  class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return main2Frag_tab1;
                case 1:
                    return main2Frag_tab2;
                case 2:
                    return main2Frag_tab3;
                case 3:
                    return main2Frag_tab4;
                case 4:
                    return main2Frag_tab5;

            }
            throw new IllegalStateException("No fragment at position " + position);
        }

        @Override
        public int getCount() {
            return 5;
        }


    }


}
