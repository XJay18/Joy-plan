package com.android.xjay.joyplan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.xjay.joyplan.StatisticsFragment.BarFragment;
import com.android.xjay.joyplan.StatisticsFragment.PieFragment;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;private ArrayList<String> TitleList = new ArrayList<>();
    private ArrayList<Fragment> ViewList = new ArrayList<>();

    private Fragment BarFragment;
    private Fragment PieFragment;

    private Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initView();
    }
    private void initView(){
        mViewPager=findViewById(R.id.viewpager);
        mTabLayout=findViewById(R.id.mytab);
        btn_back=findViewById(R.id.bt_statistics_back);

        btn_back.setOnClickListener(this);

        BarFragment=new BarFragment();
        PieFragment=new PieFragment();
        //添加页卡视图
        ViewList.add(BarFragment);
        ViewList.add(PieFragment);
        //添加页卡标题
        TitleList.add("任务汇总信息");
        TitleList.add("目标进度信息");
        //设置tab模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(TitleList.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(TitleList.get(1)));


        FragmentPagerAdapter mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()){

            //获取每个页卡
            @Override
            public Fragment getItem(int position){
                return ViewList.get(position);
            }

            //获取页卡数
            @Override
            public int getCount(){
                return  ViewList.size();
            }
            @Nullable
            //获取页卡标题
            @Override
            public CharSequence getPageTitle(int position){
                return TitleList.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
        //tab与viewpager绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_statistics_back:
                finish();
                break;
        }
    }
    //获取本周日期

}
