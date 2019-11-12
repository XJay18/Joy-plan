package com.android.xjay.joyplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * HomeActivity
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * 视图页面，加载四个fragment。
     */
    private ViewPager viewPager;
    /**
     * 底部导航栏的菜单项。
     */
    private MenuItem menuItem;
    /**
     * 底部导航栏。
     */
    private BottomNavigationView bottomNavigationView;
    /**
     * 顶部文字标识，指明当前页面是四个fragment中的某一个。
     */
    private TextView tv_tb;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = this.getIntent().getExtras();
        user_name = bundle == null ? null : bundle.getString("user_name");

        viewPager = findViewById(R.id.vp_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        tv_tb = findViewById(R.id.tv_toolbar);
        /* 初始化页面，默认页面为日程页面 */
        tv_tb.setText("日程");

        UserDBHelper userDBHelper = UserDBHelper.getInstance(this, 1);
        userDBHelper.resetCourseTable();

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

            // Triggered when scrolling the pages
            // position: current page index
            // position offset: value from 0~1,
            //                  getting larger when the page is scrolled to the right
            //                  and vice versa.
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            // Triggered when finishing the scrolling action
            // position: current page index (after scrolling)
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
                Log.v("SELECT: ", position + ". ");
                switch (position) {
                    case 0: {
                        findViewById(R.id.activity_main_toolbar).setVisibility(View.VISIBLE);
                        tv_tb.setText("日程");
                        break;
                    }
                    case 1: {
                        findViewById(R.id.activity_main_toolbar).setVisibility(View.VISIBLE);
                        tv_tb.setText("活动");
                        break;
                    }
                    case 2: {
                        findViewById(R.id.activity_main_toolbar).setVisibility(View.VISIBLE);
                        tv_tb.setText("发现");
                        break;
                    }
                    case 3: {
                        findViewById(R.id.activity_main_toolbar).setVisibility(View.GONE);
                        tv_tb.setText("设置");
                        break;
                    }
                    default: {
                        Toast.makeText(HomeActivity.this, "错误！", Toast.LENGTH_SHORT).show();
                        Log.e("NoExistError", "Page not exist.");
                    }
                }
            }


            // Triggered when changing the state of the scrolling action
            // state: 0 stop
            //        1 scrolling
            //        2 scrolling done
            // usually 1->2->0
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);
    }

    /**
     * 为适配器设置视图页面。
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(HomeFragment.newInstance("日程"));
        adapter.addFragment(HomeFragment.newInstance("活动"));
        adapter.addFragment(HomeFragment.newInstance("发现"));
        adapter.addFragment(HomeFragment.newInstance("设置", user_name));
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

