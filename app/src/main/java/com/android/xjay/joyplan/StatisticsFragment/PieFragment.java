package com.android.xjay.joyplan.StatisticsFragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.xjay.joyplan.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PieFragment extends Fragment
        implements OnChartValueSelectedListener, View.OnClickListener {
    private View rootView;
    private PieChart mPieChart1;
    private PieChart mPieChart2;
    private PieChart mPieChart3;
    private PieChart mPieChart4;
    private SimpleDateFormat dateFormat;
    private String currentDate;
    private TextView show_week;
    private Date date;
    private Calendar cal;
    private Button btn_last_week;
    private Button btn_next_week;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_statistics_pie, container, false);
        mPieChart1 = rootView.findViewById(R.id.mPieChart1);
        mPieChart2 = rootView.findViewById(R.id.mPieChart2);
        mPieChart3 = rootView.findViewById(R.id.mPieChart3);
        mPieChart4 = rootView.findViewById(R.id.mPieChart4);

        btn_last_week = rootView.findViewById(R.id.btn_last_week_p);
        btn_next_week = rootView.findViewById(R.id.btn_next_week_p);
        btn_last_week.setOnClickListener(this);
        btn_next_week.setOnClickListener(this);

        initTime();
        initUi(mPieChart1);
        initUi(mPieChart2);
        initUi(mPieChart3);
        initUi(mPieChart4);
        return rootView;
    }

    private void initTime() {
        dateFormat = new SimpleDateFormat("MM月dd日");
        date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        currentDate = getTimeInterval("now");

        show_week = rootView.findViewById(R.id.show_week_pie);
        show_week.setTextSize(17f);
    }

    private void initUi(PieChart mPieChart) {
        //这里写加载布局的代码
        //饼状图
        show_week.setText(currentDate);
        mPieChart.setOnChartValueSelectedListener(this);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setRotationEnabled(false);
        mPieChart.setHighlightPerTapEnabled(false);
        mPieChart.setExtraOffsets(25, 25, 25, 20);
        mPieChart.animateXY(1000, 1000);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //中间文件
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleRadius(82f);
        mPieChart.setHoleColor(Color.rgb(248, 223, 114));
        if (mPieChart == mPieChart1)
            mPieChart.setCenterText("学习目标");
        if (mPieChart == mPieChart2)
            mPieChart.setCenterText("锻炼目标");
        if (mPieChart == mPieChart3)
            mPieChart.setCenterText("科研目标");
        if (mPieChart == mPieChart4)
            mPieChart.setCenterText("其他目标");
        mPieChart.setCenterTextColor(Color.rgb(74, 64, 53));
        mPieChart.setCenterTextSize(15f);
        //设置文本你字体大小
        mPieChart.setDrawEntryLabels(false);
        mPieChart.setEntryLabelTextSize(20f);

        Legend l = mPieChart.getLegend();
        //不显示图例
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(20f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        setData(mPieChart);
    }

    private void setData(PieChart mPieChart) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        int x = (int) (Math.random() * 50) + 10;
        int y = 100 - x;
        entries.add(new PieEntry(x, "完成"));
        entries.add(new PieEntry(y, "未完成"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(50f);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.2f);
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //数据和颜色
        int[] colors1 = new int[]{Color.rgb(242, 123, 31), Color.rgb(204, 204, 214)};
        dataSet.setColors(colors1);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
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
            case R.id.btn_last_week_p:
                mPieChart1.animateXY(1000, 1000);
                mPieChart2.animateXY(1000, 1000);
                mPieChart3.animateXY(1000, 1000);
                mPieChart4.animateXY(1000, 1000);
                setData(mPieChart1);
                setData(mPieChart2);
                setData(mPieChart3);
                setData(mPieChart4);
                currentDate = getTimeInterval("last");
                show_week.setText(currentDate);
                break;
            case R.id.btn_next_week_p:
                mPieChart1.animateXY(1000, 1000);
                mPieChart2.animateXY(1000, 1000);
                mPieChart3.animateXY(1000, 1000);
                mPieChart4.animateXY(1000, 1000);
                setData(mPieChart1);
                setData(mPieChart2);
                setData(mPieChart3);
                setData(mPieChart4);
                currentDate = getTimeInterval("next");
                show_week.setText(currentDate);
                break;
            default:
                break;
        }
    }

    //TODO 获取当前一周日期，后期可以调用Calendar View的函数减少代码量
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
