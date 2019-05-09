package com.android.xjay.joyplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity implements OnChartValueSelectedListener,View.OnClickListener {
    private PieChart mPieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statistics_bar);

        initview();
    }

    private void initview(){
        mPieChart.findViewById(R.id.mPieChart);
        //饼状图
        mPieChart.setOnChartValueSelectedListener(this);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5,10,5,5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //中间文件
        mPieChart.setDrawHoleEnabled(false);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        setData();
    }
    private void setData(){
        ArrayList<PieEntry> entries=new ArrayList<>();
        entries.add(new PieEntry(40, "学习"));
        entries.add(new PieEntry(20, "运动"));
        entries.add(new PieEntry(30, "娱乐"));
        entries.add(new PieEntry(10, "其他"));


    }

    @Override
    public void onValueSelected(Entry e, Highlight h) { }
    @Override
    public void onNothingSelected() { }
    @Override
    public  void onClick(View v){

    }
}
