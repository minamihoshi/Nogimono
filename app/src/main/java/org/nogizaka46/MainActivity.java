package org.nogizaka46;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Main1Frag main1Frag;
    Main2Frag main2Frag;
    Main3Frag main3Frag;
    @InjectView(R.id.title)
    TextView head_title;
    @InjectView(R.id.right_layout)
    LinearLayout right_layout;
    @InjectView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigation) NavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);
        initView();
        setDefaultFragment();
    }

    //设置默认的Fragment
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        main2Frag = new Main2Frag();
        transaction.replace(R.id.fragment_container, main2Frag);
        transaction.commit();
    }

    private void initView() {
        head_title.setText(getResources().getString(R.string.tab_mainpage));
        right_layout.setVisibility(View.VISIBLE);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
        switch (item.getItemId()) {
            case R.id.menu_gz:
                if (main1Frag == null) {
                    main1Frag = new Main1Frag();
                    transaction.add(R.id.fragment_container, main1Frag);
                } else {
                    transaction.show(main1Frag);
                }
                head_title = (TextView) findViewById(R.id.title);
                head_title.setText(getResources().getString(R.string.tab_mainpage_gz));
                break;

            case R.id.menu_home:
                if (main2Frag == null) {
                    main2Frag = new Main2Frag();
                    transaction.add(R.id.fragment_container, main2Frag);
                } else {
                    transaction.show(main2Frag);
                }
                head_title = (TextView) findViewById(R.id.title);
                head_title.setText(getResources().getString(R.string.tab_mainpage));
                break;

            case R.id.menu_me:
                if (main3Frag == null) {
                    main3Frag = new Main3Frag();
                    transaction.add(R.id.fragment_container, main3Frag);
                } else {
                    transaction.show(main3Frag);
                }
                head_title = (TextView) findViewById(R.id.title);
                head_title.setText(getResources().getString(R.string.tab_mainpage_me));
                break;
        }
        transaction.commit();
        return true;
    }

    @Override
    public void onClick(View v) {

    }


    public void back(View view) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);

        }
        return true;
    }

    public void doActionRight(View view) {

    }


}

