package com.android.xjay.joyplan.StatisticsFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjay.joyplan.R;
import com.android.xjay.joyplan.ValueFormatter.MyValueFormatter;
import com.android.xjay.joyplan.ValueFormatter.MyYAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BarFragment extends Fragment implements OnChartValueSelectedListener {
        private View rootView;
        private BarChart mBarChart;
        @Override
        public void onAttach(Context context){
            System.out.println("问题1");
            super.onAttach(context);
        }


        @Override
        public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            rootView = inflater.inflate(R.layout.fragment_statistics_bar,container,false);
            System.out.println("问题2");
            initUi();
            System.out.println("问题3");
            return rootView;
        }

        private void initUi(){
                //堆叠条形图
            System.out.println("问题6");
            mBarChart = (BarChart) rootView.findViewById(R.id.mBarChart);
                //这里写加载布局的代码
            mBarChart.setOnChartValueSelectedListener(this);
            mBarChart.getDescription().setEnabled(false);
            mBarChart.setMaxVisibleValueCount(40);
            // 扩展现在只能分别在x轴和y轴
            mBarChart.setPinchZoom(false);
            mBarChart.setDrawGridBackground(false);
            mBarChart.setDrawBarShadow(false);
            mBarChart.setDrawValueAboveBar(false);
            mBarChart.setHighlightFullBarEnabled(false);

            //设置是否可以触摸
            mBarChart.setTouchEnabled(false);
            // 改变y标签的位置
            YAxis yAxis = mBarChart.getAxisLeft();
            yAxis.setValueFormatter(new MyYAxisValueFormatter());
            //设置坐标数值的字体大小
            yAxis.setTextSize(15f);

            yAxis.setAxisMinimum(0f);
            mBarChart.getAxisRight().setEnabled(false);

            XAxis xAxis = mBarChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //设置坐标数值的字体大小
            xAxis.setTextSize(15f);

            Legend l = mBarChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setFormSize(8f);
            l.setFormToTextSpace(4f);
            l.setXEntrySpace(6f);

            setData();
            System.out.println("问题5");
        }
        private void setData() {
                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

                for (int i = 1; i < 7 + 1; i++) {
                        float mult = (50 + 1);
                        int val1 = (int) ((Math.random() * mult) + mult)%10+1 ;
                        int val2 = (int) ((Math.random() * mult) + mult)%10+1 ;
                        int val3 = (int) ((Math.random() * mult) + mult)%10+1 ;
                        int val4 = (int) ((Math.random() * mult) + mult)%10+1 ;
                        yVals1.add(new BarEntry(i, new float[]{val1, val2, val3,val4}));
                }

                BarDataSet set1;

                if (mBarChart.getData() != null &&
                        mBarChart.getData().getDataSetCount() > 0) {
                        set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
                        set1.setValues(yVals1);
                        mBarChart.getData().notifyDataChanged();
                        mBarChart.notifyDataSetChanged();
                } else {
                        set1 = new BarDataSet(yVals1, "一周活动总结");
                        set1.setColors(getColors());
                        set1.setStackLabels(new String[]{"学习", "运动", "娱乐","其他"});
                        //设置顶点值字体大小
                        set1.setValueTextSize(15f);
                        //设置图例字体大小
                        set1.setFormSize(15f);

                        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                        dataSets.add(set1);

                        BarData data = new BarData(dataSets);
                        data.setValueFormatter(new MyValueFormatter());
                        data.setValueTextColor(Color.WHITE);

                        mBarChart.setData(data);
                }
                mBarChart.setFitBars(true);
                mBarChart.invalidate();
        }
        /*
         *设置颜色
         */
        private int[] getColors() {
                int stacksize = 4;
                //有尽可能多的颜色每项堆栈值
                int[] colors = new int[stacksize];
                for (int i = 0; i < colors.length; i++) {
                        colors[i] = ColorTemplate.MATERIAL_COLORS[i];
                }
                return colors;
        }
        @Override
        public void onValueSelected(Entry e, Highlight h) { }
        @Override
        public void onNothingSelected() { }

        /*@Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);
            //这里写逻辑代码
            System.out.println("问题7");
            System.out.println("问题8");
        }*/
}
