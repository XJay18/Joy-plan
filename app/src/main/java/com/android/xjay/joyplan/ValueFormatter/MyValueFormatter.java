package com.android.xjay.joyplan.ValueFormatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyValueFormatter implements IValueFormatter {
    /**
     * 数据格式
     */
    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value) + ""; // e.g. append a dollar-sign
    }
}