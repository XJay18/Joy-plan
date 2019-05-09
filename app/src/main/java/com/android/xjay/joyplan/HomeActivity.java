package com.android.xjay.joyplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * HomeActivity
 */

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private TextView tv_tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.vp_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        tv_tb=findViewById(R.id.tv_toolbar);
        // Initialize the page, set the toolbar title as agenda.
        tv_tb.setText("日程");

        // To disable the shift mode, we can simply add
        // 'app:itemHorizontalTranslationEnabled="false"'
        // to BottomNavigationView in the activity_home.xml instead using the function below.
        // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_agenda:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_planing:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_discovery:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.item_setup:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            // Triggered when scrolling the pages
            // position: current page index
            // position offset: value from 0~1,
            //                  getting larger when the page is scrolled to the right
            //                  and vice versa.
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                findViewById(R.)
            }

            @Override
            // Triggered when finishing the scrolling action
            // position: current page index (after scrolling)
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
                Log.v("SELECT: ",position+". ");
                switch (position){
                    case 0:{
                        tv_tb.setText("日程");
                        break;
                    }
                    case 1:{
                        tv_tb.setText("活动");
                        break;
                    }
                    case 2:{
                        tv_tb.setText("发现");
                        break;
                    }
                    case 3:{
                        tv_tb.setText("设置");
                        break;
                    }
                    default:{
                        Toast.makeText(HomeActivity.this, "错误！", Toast.LENGTH_SHORT).show();
                        Log.e("NoExistError","Page not exist.");
                    }
                }
            }

            @Override
            // Triggered when changing the state of the scrolling action
            // state: 0 stop
            //        1 scrolling
            //        2 scrolling done
            // usually 1->2->0
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(HomeFragment.newInstance("日程"));
        adapter.addFragment(HomeFragment.newInstance("活动"));
        adapter.addFragment(HomeFragment.newInstance("发现"));
        adapter.addFragment(HomeFragment.newInstance("设置"));
        viewPager.setAdapter(adapter);
    }

}

class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}

