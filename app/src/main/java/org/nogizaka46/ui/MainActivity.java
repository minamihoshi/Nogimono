package org.nogizaka46.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.nogizaka46.R;
import org.nogizaka46.base.BaseActivity;
import org.nogizaka46.ui.fragment.Main1Frag;
import org.nogizaka46.ui.fragment.Main2Frag;
import org.nogizaka46.ui.fragment.Main3Frag;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Main1Frag main1Frag;
    Main2Frag main2Frag;
    Main3Frag main3Frag;
    @InjectView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigation)
    NavigationView navigation;
    @InjectView(R.id.toolbar_main)
    Toolbar toolbar;
    private int INTERVAL_OF_TWO_CLICK_TO_QUIT = 1000;
    private long mLastPressBackTime;

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
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
    }


    private void initView() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setTitle(getResources().getString(R.string.tab_mainpage));
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Log.e("TAG", "bbbbbbbbbbbbbbbbbb: "+itemId);
                switch (itemId){
                    case R.id.nav_main :
                        Toast.makeText(MainActivity.this,"aaa",Toast.LENGTH_LONG).show();

                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.e("TAG", "onNavigationItemSelected: "+item.getItemId() );
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

                toolbar.setTitle(getResources().getString(R.string.tab_mainpage_gz));
                break;

            case R.id.menu_home:
                if (main2Frag == null) {
                    main2Frag = new Main2Frag();
                    transaction.add(R.id.fragment_container, main2Frag);
                } else {
                    transaction.show(main2Frag);
                }
                toolbar.setTitle(getResources().getString(R.string.tab_mainpage));

                break;

            case R.id.menu_me:
                if (main3Frag == null) {
                    main3Frag = new Main3Frag();
                    transaction.add(R.id.fragment_container, main3Frag);
                } else {
                    transaction.show(main3Frag);
                }

                toolbar.setTitle(getResources().getString(R.string.tab_mainpage_me));
                break;
        }

        transaction.commit();
        return true;
    }




//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mLastPressBackTime < INTERVAL_OF_TWO_CLICK_TO_QUIT) {
                finish();
            } else {
                Toast.makeText(this, R.string.msgexit, Toast.LENGTH_SHORT).show();

                mLastPressBackTime = System.currentTimeMillis();
            }
        }


    }


}

