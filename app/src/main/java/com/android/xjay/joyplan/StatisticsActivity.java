package com.android.xjay.joyplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.xjay.joyplan.StatisticsFragment.BarFragment;
import com.android.xjay.joyplan.StatisticsFragment.PieFragment;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 切换栏
     */
    private TabLayout mTabLayout;
    /**
     * 屏幕间的切换
     */
    private ViewPager mViewPager;
    /**
     * 字符串数组列表，储存标题名称
     */
    private ArrayList<String> TitleList = new ArrayList<>();
    /**
     * 页框数组列表，储存页面
     */
    private ArrayList<Fragment> ViewList = new ArrayList<>();
    /**
     * 柱状图页框
     */
    private Fragment BarFragment;
    /**
     * 饼状图页框
     */
    private Fragment PieFragment;
    /**
     * 返回按钮
     */
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.mytab);
        btn_back = findViewById(R.id.bt_statistics_back);

        btn_back.setOnClickListener(this);

        BarFragment = new BarFragment();
        PieFragment = new PieFragment();
        //添加页卡视图
        ViewList.add(BarFragment);
        ViewList.add(PieFragment);
        //添加页卡标题
        TitleList.add("日程完成时间");
        TitleList.add("日程完成次数");
        //设置tab模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(TitleList.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(TitleList.get(1)));


        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            //获取每个页卡
            @Override
            public Fragment getItem(int position) {
                return ViewList.get(position);
            }

            //获取页卡数
            @Override
            public int getCount() {
                return ViewList.size();
            }

            @Nullable
            //获取页卡标题
            @Override
            public CharSequence getPageTitle(int position) {
                return TitleList.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
        //tab与viewpager绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_statistics_back:
                finish();
                break;
        }
    }
    //获取本周日期

}
