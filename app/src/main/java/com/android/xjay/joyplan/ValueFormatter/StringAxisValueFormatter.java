package com.android.xjay.joyplan.ValueFormatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class StringAxisValueFormatter implements IAxisValueFormatter {
    //区域值
    private String[] mStrs;

    /**
     *     * 对字符串类型的坐标轴标记进行格式化
     *     * @param strs
     *    
     */
    public StringAxisValueFormatter(String[] strs) {
        this.mStrs = strs;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        return mStrs[(int) v % mStrs.length];
    }
}
