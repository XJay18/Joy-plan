package com.android.xjay.joyplan.StatisticsFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjay.joyplan.R;
import com.android.xjay.joyplan.ValueFormatter.MyValueFormatter;
import com.android.xjay.joyplan.ValueFormatter.MyYAxisValueFormatter;
import com.android.xjay.joyplan.ValueFormatter.StringAxisValueFormatter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FqzStatistic extends AppCompatActivity
        implements OnChartValueSelectedListener, View.OnClickListener {
    /**
     * 柱状图
     */
    private BarChart mBarChart;
    /**
     * 显示上一周数据的按钮
     */
    private Button btn_last_week;
    /**
     * 显示下一周数据的按钮
     */
    private Button btn_next_week;
    /**
     * 返回按钮
     */
    private LinearLayout btn_back;
    /**
     * 数据格式
     */
    private SimpleDateFormat dateFormat;
    /**
     * 当前日期
     */
    private String currentDate;
    /**
     * 显示周
     */
    private TextView show_week;
    /**
     * 显示分钟
     */
    private TextView show_minutes;
    /**
     * 设置日期格式
     */
    private Date date;
    /**
     * 设置日期格式
     */
    private Calendar cal;
    /**
     * 柱状图的横坐标数组
     */
    private String[] value = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_fqz);
        initUi();
    }

    /**
     * 初始化页面属性
     */
    private void initUi() {
        dateFormat = new SimpleDateFormat("MM月dd日");
        date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        currentDate = getTimeInterval("now");

        show_week = findViewById(R.id.show_week_fqz);
        show_week.setTextSize(22.5f);
        show_week.setTextColor(Color.WHITE);
        show_minutes = findViewById(R.id.minutes);
        show_minutes.setTextSize(28.5f);
        show_minutes.setTextColor(Color.WHITE);

        btn_last_week = findViewById(R.id.btn_last_week_fqz);
        btn_last_week.setOnClickListener(this);
        btn_next_week = findViewById(R.id.btn_next_week_fqz);
        btn_next_week.setOnClickListener(this);
        btn_back = findViewById(R.id.bt_statistics_fqz_back);
        btn_back.setOnClickListener(this);
        //堆叠条形图
        mBarChart = findViewById(R.id.mBarChart_fqz);
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
        //动画
        mBarChart.animateXY(1000, 1000);
        //设置是否可以触摸
        mBarChart.setTouchEnabled(false);
        // 改变y标签的位置
        YAxis yAxis = mBarChart.getAxisLeft();
        yAxis.setValueFormatter(new MyYAxisValueFormatter());
        yAxis.setAxisMaximum(150f);
        //设置坐标数值的字体大小
        yAxis.setTextSize(15f);
        yAxis.setAxisMinimum(0f);
        yAxis.setGridColor(Color.WHITE);
        yAxis.setAxisLineColor(Color.WHITE);
        yAxis.setTextColor(Color.WHITE);
        mBarChart.getAxisRight().setEnabled(false);

        StringAxisValueFormatter formatter = new StringAxisValueFormatter(value);
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置坐标数值的字体大小
        xAxis.setTextSize(12f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(formatter);
        mBarChart.getAxisLeft().setDrawAxisLine(false);

        Legend l = mBarChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(10f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
        l.setTextColor(Color.WHITE);

        setData();
    }

    /**
     * 初始化柱状图数据
     */
    private void setData() {
        show_week.setText(currentDate);
        int total = 0;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < 7; i++) {
            float mult = (1000);
            int val1 = (int) (Math.random() * mult) % 150;
            total += val1;
            yVals1.add(new BarEntry(i, new float[]{val1}));
        }
        show_minutes.setText(total + "分钟");

        BarDataSet set1;

        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "一天使用番茄钟总数");
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"数量"});
            //设置顶点值字体大小
            set1.setValueTextSize(15f);
            //设置图例字体大小
            set1.setFormSize(15f);
            //设置是否显示定点数值
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            //设置条形图宽度
            data.setBarWidth(0.3f);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);
            mBarChart.setData(data);
        }
        mBarChart.setFitBars(true);
        mBarChart.invalidate();
    }

    /**
     * 设置颜色
     */
    private int[] getColors() {
        //有尽可能多的颜色每项堆栈值
        //int[] colors = new int[]{Color.rgb(255,201,12),Color.rgb(248,223,114),Color.rgb(238,162,164),Color.rgb(240,124,130)};
        //int[] colors=new int[]{Color.rgb(255,201,12)};
        int[] colors = new int[]{Color.WHITE};
        return colors;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last_week_fqz:
                mBarChart.animateXY(1000, 1000);
                currentDate = getTimeInterval("last");
                setData();
                break;
            case R.id.btn_next_week_fqz:
                mBarChart.animateXY(1000, 1000);
                currentDate = getTimeInterval("next");
                setData();
                break;
            case R.id.bt_statistics_fqz_back:
                finish();
                break;
        }
    }

    //TODO 获取当前一周日期，后期可以调用Calendar View的函数减少代码量

    /**
     * 获取当前日周的日期
     */
    private String getTimeInterval(String judge) {
        switch (judge) {
            case "last":
                cal.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case "now":
                break;
            case "next":
                cal.add(Calendar.DAY_OF_YEAR, 7);
                break;
            default:
        }
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = dateFormat.format(cal.getTime());
        imptimeBegin = cutFirstChart(imptimeBegin, '0');
        imptimeEnd = cutFirstChart(imptimeEnd, '0');
        return cal.get(Calendar.YEAR) + "年" + imptimeBegin + "至" + imptimeEnd;
    }

    //去掉字符串首字符
    private String cutFirstChart(String str, char element) {
        int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
        str = str.substring(beginIndex);
        return str;
    }
}
