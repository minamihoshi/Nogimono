package org.nogizaka46;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import utils.MyUtil;

public class MainActivity extends BaseActivity  {
    private Button[] mTabs;
    Main1Frag main1Frag;
    Main2Frag main2Frag;
    Main3Frag main3Frag;
    TextView head_title;
    LinearLayout right_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        setDefaultFragment();

    }
    private void initView() {
        mTabs = new Button[3];
        mTabs[0] = (Button) findViewById(R.id.btn_gz);
        setDrawableTop(mTabs[0],25,25);
        mTabs[1] = (Button) findViewById(R.id.btn_mainpage);
        setDrawableTop(mTabs[1],25,25);
        mTabs[2] = (Button) findViewById(R.id.btn_me);
        setDrawableTop(mTabs[2],25,25);
        mTabs[1].setSelected(true);
        head_title= (TextView)findViewById(R.id.title);
        head_title.setText(getResources().getString(R.string.tab_mainpage));
        right_layout= (LinearLayout) findViewById(R.id.right_layout);
        right_layout.setVisibility(View.VISIBLE);
    }


    //设置默认的Fragment
    private void setDefaultFragment()
    {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        main2Frag = new Main2Frag();
        transaction.replace(R.id.fragment_container, main2Frag);
        transaction.commit();
    }



    public void setDrawableTop(Button tv,int iconWidth,int iconHeight){
        Drawable drawable = tv.getCompoundDrawables()[1];
        drawable.setBounds(0, 0, MyUtil.dip2px(MainActivity.this, iconWidth), MyUtil.dip2px(MainActivity.this, iconHeight));
        tv.setCompoundDrawables(null, drawable, null, null);
    }


    public void onTabClicked(View view){
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (main1Frag != null) {
            transaction.hide(main1Frag);
        }
        if (main2Frag != null) {
            transaction.hide(main2Frag);
        }
        if (main3Frag != null) {
            transaction.hide(main3Frag);
        }
        switch (view.getId()){
            case R.id.btn_gz:
                if(main1Frag==null){
                    main1Frag=new Main1Frag();
                    transaction.add(R.id.fragment_container,main1Frag);
                }else{
                    transaction.show(main1Frag);
                }
                mTabs[0].setSelected(true);
                mTabs[1].setSelected(false);
                mTabs[2].setSelected(false);
                head_title= (TextView)findViewById(R.id.title);
                head_title.setText(getResources().getString(R.string.tab_mainpage_gz));

                break;
            case R.id.btn_mainpage:
                if(main2Frag==null){
                    main2Frag=new Main2Frag();
                    transaction.add(R.id.fragment_container,main2Frag);
                }else{
                    transaction.show(main2Frag);
                }
                mTabs[0].setSelected(false);
                mTabs[1].setSelected(true);
                mTabs[2].setSelected(false);
                head_title= (TextView)findViewById(R.id.title);
                head_title.setText(getResources().getString(R.string.tab_mainpage));
                break;
            case R.id.btn_me:
                if(main3Frag==null){
                    main3Frag=new Main3Frag();
                    transaction.add(R.id.fragment_container,main3Frag);
                }else{
                    transaction.show(main3Frag);
                }
                mTabs[0].setSelected(false);
                mTabs[1].setSelected(false);
                mTabs[2].setSelected(true);
                head_title= (TextView)findViewById(R.id.title);
                head_title.setText(getResources().getString(R.string.tab_mainpage_me));
                break;
        }
        transaction.commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);

        }
        return true;
    }
   public void back(View view){
       this.finish();
   }

    public  void doActionRight(View view){

    }
}
