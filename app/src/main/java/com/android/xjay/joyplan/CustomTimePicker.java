package com.android.xjay.joyplan;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.xjay.joyplan.Utils.DateFormat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 自制时间选择器。
 */
public class CustomTimePicker implements View.OnClickListener,
        PickerView.OnSelectedListener {

    /**
     * 当前活动的内容。
     */
    private Context mContext;
    /**
     * 回调属性。
     */
    private Callback mCallback;
    /**
     * 时间选择器的可选时间范围，选择的时间。
     */
    private Calendar mBeginTime, mEndTime, mSelectedTime;
    /**
     * 是否显示对话框。
     */
    private boolean mCanDialogShow;

    /**
     * 时间选择器对话框。
     */
    private Dialog mPickerDialog;
    /**
     * 时间选择器的年选项、月选项、日选项、时选项以及分选项。
     */
    private PickerView mDpvYear, mDpvMonth, mDpvDay, mDpvHour, mDpvMinute;
    /**
     * 时间选择器的各个时间单位。
     */
    private TextView mTvYearUnit, mTvMonthUnit, mTvDayUnit, mTvHourUnit, mTvMinuteUnit;

    /**
     * 时间选择器的起始时间。
     */
    private int mBeginYear, mBeginMonth, mBeginDay, mBeginHour, mBeginMinute,
            mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
    /**
     * 记录时间选择器的单位。
     */
    private List<String> mYearUnits = new ArrayList<>(),
            mMonthUnits = new ArrayList<>(),
            mDayUnits = new ArrayList<>(),
            mHourUnits = new ArrayList<>(),
            mMinuteUnits = new ArrayList<>();

    private DecimalFormat mDecimalFormat = new DecimalFormat("00");

    /**
     * 时间选择器的展示模式。
     */
    private int mTimePickerShowMode;
    private int mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE;

    /**
     * 时间选择器的单位
     */
    private static final int SCROLL_UNIT_HOUR = 0b1;
    private static final int SCROLL_UNIT_MINUTE = 0b10;

    /**
     * 时间单位的最大值
     */
    private static final int MAX_MINUTE_UNIT = 59;
    private int MAX_HOUR_UNIT = 23;
    private static final int MAX_MONTH_UNIT = 12;

    /**
     * The max of hour unit may be changed when choosing the time span
     *
     * @param max maximum of hour
     */
    private void setMaxHourUnit(int max) {
        MAX_HOUR_UNIT = max;
    }

    /**
     * 联动时延.
     */
    private static final long LINKAGE_DELAY_DEFAULT = 100L;

    /**
     * 时间选择接口，用于结果回调.
     */
    public interface Callback {
        void onTimeSelected(long timestamp);
    }

    /**
     * Initialize the timepicker through datestr
     *
     * @param context      Activity Context
     * @param callback     results callback
     * @param beginDateStr starttime str yyyy-MM-dd HH:mm
     * @param endDateStr   starttime str yyyy-MM-dd HH:mm
     */
    CustomTimePicker(Context context, Callback callback,
                     String beginDateStr, String endDateStr,
                     String title) {
        this(context, callback, DateFormat.str2Long(beginDateStr, true),
                DateFormat.str2Long(endDateStr, true), title, 23, new int[]{0, 0, 0, 0, 0});
    }

    /**
     * Initialize the timepicker through timestamp
     *
     * @param context        Activity Context
     * @param callback       results callback
     * @param beginTimestamp in millisecond
     * @param endTimestamp   in millisecond
     * @param title          the title of the picker
     */
    CustomTimePicker(Context context, Callback callback,
                     long beginTimestamp, long endTimestamp,
                     String title) {
        if (context == null || callback == null || beginTimestamp <= 0 || beginTimestamp >= endTimestamp) {
            mCanDialogShow = false;
            return;
        }

        mContext = context;
        mCallback = callback;
        mBeginTime = Calendar.getInstance();
        mBeginTime.setTimeInMillis(beginTimestamp);
        mEndTime = Calendar.getInstance();
        mEndTime.setTimeInMillis(endTimestamp);
        mSelectedTime = Calendar.getInstance();
        initView(title);
        initData();
        mCanDialogShow = true;
    }

    /**
     * Dedicated Constructor for fqz activity
     *
     * @param context        Activity Context
     * @param callback       results callback
     * @param beginTimestamp in millisecond
     * @param endTimestamp   in millisecond
     * @param title          the title of the picker
     * @param maxOfHour      maximum of hour, may not be exactly 23 in
     *                       different scenes.
     */
    CustomTimePicker(Context context, Callback callback,
                     long beginTimestamp, long endTimestamp,
                     String title, int maxOfHour, int[] initValues) {
        if (context == null || callback == null || beginTimestamp <= 0 || beginTimestamp >= endTimestamp) {
            mCanDialogShow = false;
            return;
        }

        mContext = context;
        mCallback = callback;
        mBeginTime = Calendar.getInstance();
        mBeginTime.setTimeInMillis(beginTimestamp);
        mEndTime = Calendar.getInstance();
        mEndTime.setTimeInMillis(endTimestamp);
        mSelectedTime = Calendar.getInstance();

        initView(title);
        initData(maxOfHour, initValues);
        mCanDialogShow = true;
    }

    /**
     * 初始化视图，需要在最初调用。
     */
    private void initView(String title) {
        mPickerDialog = new Dialog(mContext, R.style.time_picker_dialog);
        mPickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPickerDialog.setContentView(R.layout.time_picker_dialog);
        TextView tp_title = mPickerDialog.findViewById(R.id.tv_tp_title);
        tp_title.setText(title);


        Window window = mPickerDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        mPickerDialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
        mPickerDialog.findViewById(R.id.tv_confirm).setOnClickListener(this);
        mTvYearUnit = mPickerDialog.findViewById(R.id.tv_year_unit);
        mTvMonthUnit = mPickerDialog.findViewById(R.id.tv_month_unit);
        mTvDayUnit = mPickerDialog.findViewById(R.id.tv_day_unit);
        mTvHourUnit = mPickerDialog.findViewById(R.id.tv_hour_unit);
        mTvMinuteUnit = mPickerDialog.findViewById(R.id.tv_minute_unit);

        mDpvYear = mPickerDialog.findViewById(R.id.dpv_year);
        mDpvYear.setOnSelectListener(this);
        mDpvMonth = mPickerDialog.findViewById(R.id.dpv_month);
        mDpvMonth.setOnSelectListener(this);
        mDpvDay = mPickerDialog.findViewById(R.id.dpv_day);
        mDpvDay.setOnSelectListener(this);
        mDpvHour = mPickerDialog.findViewById(R.id.dpv_hour);
        mDpvHour.setOnSelectListener(this);
        mDpvMinute = mPickerDialog.findViewById(R.id.dpv_minute);
        mDpvMinute.setOnSelectListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                break;

            case R.id.tv_confirm:
                if (mCallback != null) {
                    mCallback.onTimeSelected(mSelectedTime.getTimeInMillis());
                }
                break;
        }

        if (mPickerDialog != null && mPickerDialog.isShowing()) {
            mPickerDialog.dismiss();
        }
    }

    @Override
    public void onSelect(View view, String selected) {
        if (view == null || TextUtils.isEmpty(selected)) return;

        int timeUnit;
        try {
            timeUnit = Integer.parseInt(selected);
        } catch (Throwable ignored) {
            return;
        }

        switch (view.getId()) {
            case R.id.dpv_year:
                mSelectedTime.set(Calendar.YEAR, timeUnit);
                linkageMonthUnit(true, LINKAGE_DELAY_DEFAULT);
                break;

            case R.id.dpv_month:
                int lastSelectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
                mSelectedTime.add(Calendar.MONTH, timeUnit - lastSelectedMonth);
                linkageDayUnit(true, LINKAGE_DELAY_DEFAULT);
                break;

            case R.id.dpv_day:
                mSelectedTime.set(Calendar.DAY_OF_MONTH, timeUnit);
                linkageHourUnit(true, LINKAGE_DELAY_DEFAULT);
                break;

            case R.id.dpv_hour:
                mSelectedTime.set(Calendar.HOUR_OF_DAY, timeUnit);
                linkageMinuteUnit(true);
                break;

            case R.id.dpv_minute:
                mSelectedTime.set(Calendar.MINUTE, timeUnit);
                break;
        }
    }

    /**
     * 初始化时间选择器中的数据。
     */
    private void initData() {
        mSelectedTime.setTimeInMillis(mBeginTime.getTimeInMillis());

        mBeginYear = mBeginTime.get(Calendar.YEAR);
        /* 日历月份范围：0~11 */
        mBeginMonth = mBeginTime.get(Calendar.MONTH) + 1;
        mBeginDay = mBeginTime.get(Calendar.DAY_OF_MONTH);
        mBeginHour = mBeginTime.get(Calendar.HOUR_OF_DAY);
        mBeginMinute = mBeginTime.get(Calendar.MINUTE);

        mEndYear = mEndTime.get(Calendar.YEAR);
        mEndMonth = mEndTime.get(Calendar.MONTH) + 1;
        mEndDay = mEndTime.get(Calendar.DAY_OF_MONTH);
        mEndHour = mEndTime.get(Calendar.HOUR_OF_DAY);
        mEndMinute = mEndTime.get(Calendar.MINUTE);

        boolean canSpanYear = mBeginYear != mEndYear;
        boolean canSpanMon = !canSpanYear && mBeginMonth != mEndMonth;
        boolean canSpanDay = !canSpanMon && mBeginDay != mEndDay;
        boolean canSpanHour = !canSpanDay && mBeginHour != mEndHour;
        boolean canSpanMinute = !canSpanHour && mBeginMinute != mEndMinute;
        if (canSpanYear) {
            initDateUnits(MAX_MONTH_UNIT, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH), MAX_HOUR_UNIT, MAX_MINUTE_UNIT);
        } else if (canSpanMon) {
            initDateUnits(mEndMonth, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH), MAX_HOUR_UNIT, MAX_MINUTE_UNIT);
        } else if (canSpanDay) {
            initDateUnits(mEndMonth, mEndDay, MAX_HOUR_UNIT, MAX_MINUTE_UNIT);
        } else if (canSpanHour) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, MAX_MINUTE_UNIT);
        } else if (canSpanMinute) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, mEndMinute);
        }
    }

    /**
     * 初始化时间选择器中的数据，其中maxOfHour标识最大可选小时数。
     */
    private void initData(int maxOfHour, int[] initValues) {
        setMaxHourUnit(maxOfHour);
        mSelectedTime.setTimeInMillis(mBeginTime.getTimeInMillis());

        mBeginYear = mBeginTime.get(Calendar.YEAR);

        /* 日历月份由0开始计算 */
        mBeginMonth = mBeginTime.get(Calendar.MONTH) + 1;
        mBeginDay = mBeginTime.get(Calendar.DAY_OF_MONTH);
        mBeginHour = mBeginTime.get(Calendar.HOUR_OF_DAY);
        mBeginMinute = mBeginTime.get(Calendar.MINUTE);

        mEndYear = mEndTime.get(Calendar.YEAR);
        mEndMonth = mEndTime.get(Calendar.MONTH) + 1;
        mEndDay = mEndTime.get(Calendar.DAY_OF_MONTH);
        mEndHour = mEndTime.get(Calendar.HOUR_OF_DAY);
        mEndMinute = mEndTime.get(Calendar.MINUTE);

        boolean canSpanYear = mBeginYear != mEndYear;
        boolean canSpanMon = !canSpanYear && mBeginMonth != mEndMonth;
        boolean canSpanDay = !canSpanMon && mBeginDay != mEndDay;
        boolean canSpanHour = !canSpanDay && mBeginHour != mEndHour;
        boolean canSpanMinute = !canSpanHour && mBeginMinute != mEndMinute;
        if (canSpanYear) {
            initDateUnits(MAX_MONTH_UNIT, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH), MAX_HOUR_UNIT, MAX_MINUTE_UNIT, initValues);
        } else if (canSpanMon) {
            initDateUnits(mEndMonth, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH), MAX_HOUR_UNIT, MAX_MINUTE_UNIT, initValues);
        } else if (canSpanDay) {
            initDateUnits(mEndMonth, mEndDay, MAX_HOUR_UNIT, MAX_MINUTE_UNIT, initValues);
        } else if (canSpanHour) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, MAX_MINUTE_UNIT, initValues);
        } else if (canSpanMinute) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, mEndMinute, initValues);
        }
    }

    /**
     * 初始化每个时间单位的范围。
     */
    private void initDateUnits(int endMonth, int endDay, int endHour, int endMinute) {
        for (int i = mBeginYear; i <= mEndYear; i++) {
            mYearUnits.add(String.valueOf(i));
        }

        for (int i = mBeginMonth; i <= endMonth; i++) {
            mMonthUnits.add(mDecimalFormat.format(i));
        }

        for (int i = mBeginDay; i <= endDay; i++) {
            mDayUnits.add(mDecimalFormat.format(i));
        }

        if ((mScrollUnits & SCROLL_UNIT_HOUR) != SCROLL_UNIT_HOUR) {
            mHourUnits.add(mDecimalFormat.format(mBeginHour));
        } else {
            for (int i = 0; i <= endHour; i++) {
                mHourUnits.add(mDecimalFormat.format(i));
            }
        }

        if ((mScrollUnits & SCROLL_UNIT_MINUTE) != SCROLL_UNIT_MINUTE) {
            mMinuteUnits.add(mDecimalFormat.format(mBeginMinute));
        } else {
            for (int i = 0; i <= endMinute; i++) {
                mMinuteUnits.add(mDecimalFormat.format(i));
            }
        }

        mDpvYear.setDataList(mYearUnits);
        mDpvYear.setSelected(0);
        mDpvMonth.setDataList(mMonthUnits);
        mDpvMonth.setSelected(0);
        mDpvDay.setDataList(mDayUnits);
        mDpvDay.setSelected(0);
        mDpvHour.setDataList(mHourUnits);
        mDpvHour.setSelected(0);
        mDpvMinute.setDataList(mMinuteUnits);
        mDpvMinute.setSelected(0);
        setCanScroll();
    }

    /***
     * 通过initValues列表初始化时间列表。
     */
    private void initDateUnits(int endMonth, int endDay, int endHour, int endMinute, int[] initValues) {
        for (int i = mBeginYear; i <= mEndYear; i++) {
            mYearUnits.add(String.valueOf(i));
        }

        for (int i = mBeginMonth; i <= endMonth; i++) {
            mMonthUnits.add(mDecimalFormat.format(i));
        }

        for (int i = mBeginDay; i <= endDay; i++) {
            mDayUnits.add(mDecimalFormat.format(i));
        }

        if ((mScrollUnits & SCROLL_UNIT_HOUR) != SCROLL_UNIT_HOUR) {
            mHourUnits.add(mDecimalFormat.format(mBeginHour));
        } else {
            for (int i = 0; i <= endHour; i++) {
                mHourUnits.add(mDecimalFormat.format(i));
            }
        }

        if ((mScrollUnits & SCROLL_UNIT_MINUTE) != SCROLL_UNIT_MINUTE) {
            mMinuteUnits.add(mDecimalFormat.format(mBeginMinute));
        } else {
            for (int i = 0; i <= endMinute; i++) {
                mMinuteUnits.add(mDecimalFormat.format(i));
            }
        }

        mDpvYear.setDataList(mYearUnits);
        mDpvYear.setSelected(initValues[0]);
        mDpvMonth.setDataList(mMonthUnits);
        mDpvMonth.setSelected(initValues[1]);
        mDpvDay.setDataList(mDayUnits);
        mDpvDay.setSelected(initValues[2]);
        mDpvHour.setDataList(mHourUnits);
        mDpvHour.setSelected(initValues[3]);
        mDpvMinute.setDataList(mMinuteUnits);
        mDpvMinute.setSelected(initValues[4]);

        setCanScroll();
    }

    private void setCanScroll() {
        mDpvYear.setCanScroll(mYearUnits.size() > 1);
        mDpvMonth.setCanScroll(mMonthUnits.size() > 1);
        mDpvDay.setCanScroll(mDayUnits.size() > 1);
        mDpvHour.setCanScroll(mHourUnits.size() > 1 && (mScrollUnits & SCROLL_UNIT_HOUR) == SCROLL_UNIT_HOUR);
        mDpvMinute.setCanScroll(mMinuteUnits.size() > 1 && (mScrollUnits & SCROLL_UNIT_MINUTE) == SCROLL_UNIT_MINUTE);
    }

    /**
     * Linkage: Month
     *
     * @param showAnim whether to show animation
     * @param delay    linkage next level delay time
     */
    private void linkageMonthUnit(final boolean showAnim, final long delay) {
        int minMonth;
        int maxMonth;
        minMonth = 1;
        maxMonth = MAX_MONTH_UNIT;

        /* 初始化时间选择器容器 */
        mMonthUnits.clear();
        for (int i = minMonth; i <= maxMonth; i++) {
            mMonthUnits.add(mDecimalFormat.format(i));
        }
        mDpvMonth.setDataList(mMonthUnits);

        /* 确保联动检查值没有溢出或在联动时更改 */
        int selectedMonth = getValueInRange(mSelectedTime.get(Calendar.MONTH) + 1, minMonth, maxMonth);
        mSelectedTime.set(Calendar.MONTH, selectedMonth - 1);
        mDpvMonth.setSelected(selectedMonth - minMonth);
        if (showAnim) {
            mDpvMonth.startAnim();
        }

        // Linkage: Day
        mDpvMonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                linkageDayUnit(showAnim, delay);
            }
        }, delay);
    }

    /**
     * Linkage: Day
     *
     * @param showAnim whether to show animation
     * @param delay    linkage next level delay time
     */
    private void linkageDayUnit(final boolean showAnim, final long delay) {
        int minDay;
        int maxDay;
        minDay = 1;
        maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayUnits.clear();
        for (int i = minDay; i <= maxDay; i++) {
            mDayUnits.add(mDecimalFormat.format(i));
        }
        mDpvDay.setDataList(mDayUnits);

        int selectedDay = getValueInRange(mSelectedTime.get(Calendar.DAY_OF_MONTH), minDay, maxDay);
        mSelectedTime.set(Calendar.DAY_OF_MONTH, selectedDay);
        mDpvDay.setSelected(selectedDay - minDay);
        if (showAnim) {
            mDpvDay.startAnim();
        }

        mDpvDay.postDelayed(new Runnable() {
            @Override
            public void run() {
                linkageHourUnit(showAnim, delay);
            }
        }, delay);
    }

    /**
     * Linkage: Hour
     *
     * @param showAnim whether to show animation
     * @param delay    linkage next level delay time
     */
    private void linkageHourUnit(final boolean showAnim, final long delay) {
        if ((mScrollUnits & SCROLL_UNIT_HOUR) == SCROLL_UNIT_HOUR) {
            int minHour;
            int maxHour;
            minHour = 0;
            maxHour = MAX_HOUR_UNIT;
            mHourUnits.clear();
            for (int i = minHour; i <= maxHour; i++) {
                mHourUnits.add(mDecimalFormat.format(i));
            }
            mDpvHour.setDataList(mHourUnits);

            int selectedHour = getValueInRange(mSelectedTime.get(
                    Calendar.HOUR_OF_DAY), minHour, maxHour);
            mSelectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            mDpvHour.setSelected(selectedHour - minHour);
            if (showAnim) {
                mDpvHour.startAnim();
            }
        }

        mDpvHour.postDelayed(new Runnable() {
            @Override
            public void run() {
                linkageMinuteUnit(showAnim);
            }
        }, delay);
    }

    /**
     * Linkage: Minute
     *
     * @param showAnim whether to show animation
     */
    private void linkageMinuteUnit(final boolean showAnim) {
        if ((mScrollUnits & SCROLL_UNIT_MINUTE) == SCROLL_UNIT_MINUTE) {
            int minMinute;
            int maxMinute;
            minMinute = 0;
            maxMinute = MAX_MINUTE_UNIT;
            mMinuteUnits.clear();
            for (int i = minMinute; i <= maxMinute; i++) {
                mMinuteUnits.add(mDecimalFormat.format(i));
            }
            mDpvMinute.setDataList(mMinuteUnits);

            int selectedMinute = getValueInRange(mSelectedTime.get(Calendar.MINUTE), minMinute, maxMinute);
            mSelectedTime.set(Calendar.MINUTE, selectedMinute);
            mDpvMinute.setSelected(selectedMinute - minMinute);
            if (showAnim) {
                mDpvMinute.startAnim();
            }
        }

        setCanScroll();
    }

    /**
     * 如果value在给定范围中，则返回value，否则返回给定value的最大值或最小值。
     */
    private int getValueInRange(int value, int minValue, int maxValue) {
        if (value < minValue) {
            return minValue;
        } else if (value > maxValue) {
            return maxValue;
        } else {
            return value;
        }
    }

    /**
     * Display the timepicker
     *
     * @param dateStr String of the starttime, yyyy-MM-dd or yyyy-MM-dd HH:mm
     */
    public void show(String dateStr) {
        boolean canShow=canShow();
        boolean isEmpty=TextUtils.isEmpty(dateStr);
        if (!canShow|| isEmpty)
            return;

        /* 不展示动画效果 */
        if (setSelectedTime(dateStr, false)) {
            mPickerDialog.show();
        }
    }

    public void show(long timestamp) {
        if (!canShow()) return;

        if (setSelectedTime(timestamp, false)) {
            mPickerDialog.show();
        }
    }

    public void show() {
        if (!canShow()) return;
        mPickerDialog.show();
    }

    private boolean canShow() {
        return mCanDialogShow && mPickerDialog != null;
    }

    /**
     * Set the chosen time for the timepicker
     *
     * @param dateStr  String of starttime
     * @param showAnim whether to show animation
     * @return whether setting is successful
     */
    public boolean setSelectedTime(String dateStr, boolean showAnim) {
        return canShow() && !TextUtils.isEmpty(dateStr)
                && setSelectedTime(DateFormat.str2Long(
                dateStr, mTimePickerShowMode), showAnim);
    }

    /**
     * Set the chosen time for the timepicker
     *
     * @param timestamp String of starttime
     * @param showAnim  whether to show animation
     * @return whether setting is successful
     */
    public boolean setSelectedTime(long timestamp, boolean showAnim) {
        if (!canShow()) return false;

        if (timestamp < mBeginTime.getTimeInMillis()) {
            timestamp = mBeginTime.getTimeInMillis();
        } else if (timestamp > mEndTime.getTimeInMillis()) {
            timestamp = mEndTime.getTimeInMillis();
        }
        mSelectedTime.setTimeInMillis(timestamp);

        mYearUnits.clear();
        for (int i = mBeginYear; i <= mEndYear; i++) {
            mYearUnits.add(String.valueOf(i));
        }
        mDpvYear.setDataList(mYearUnits);
        mDpvYear.setSelected(mSelectedTime.get(Calendar.YEAR) - mBeginYear);
        linkageMonthUnit(showAnim, showAnim ? LINKAGE_DELAY_DEFAULT : 0);
        return true;
    }

    /**
     * 设置是否允许单击屏幕或返回键以关闭
     */
    void setCancelable(boolean cancelable) {
        if (!canShow()) return;

        mPickerDialog.setCancelable(cancelable);
    }

    /**
     * Sets the Time Picker Show Mode
     *
     * @param timePickerShowMode 0: show YEAR MONTH DAY HOUR MINUTE
     *                           1: show HOUR MINUTE
     */
    public void setTimePickerShowMode(int timePickerShowMode) {
        if (!canShow()) return;

        /* 展示 YEAR MONTH DAY HOUR MINUTE */
        if (timePickerShowMode == 0) {
            initScrollUnit();
            mDpvHour.setVisibility(View.VISIBLE);
            mTvHourUnit.setVisibility(View.VISIBLE);
            mDpvMinute.setVisibility(View.VISIBLE);
            mTvMinuteUnit.setVisibility(View.VISIBLE);

        }
        /* 展示 HOUR MINUTE */
        else if (timePickerShowMode == 1) {
            initScrollUnit();
            mDpvYear.setVisibility(View.GONE);
            mTvYearUnit.setVisibility(View.GONE);
            mDpvMonth.setVisibility(View.GONE);
            mTvMonthUnit.setVisibility(View.GONE);
            mDpvDay.setVisibility(View.GONE);
            mTvDayUnit.setVisibility(View.GONE);

            mDpvHour.setVisibility(View.VISIBLE);
            mTvHourUnit.setVisibility(View.VISIBLE);
            mDpvMinute.setVisibility(View.VISIBLE);
            mTvMinuteUnit.setVisibility(View.VISIBLE);
        }
        /* 展示 DAY HOUR MINUTE */
        else if (timePickerShowMode == 2) {
            initScrollUnit();
            mDpvYear.setVisibility(View.GONE);
            mTvYearUnit.setVisibility(View.GONE);

            mDpvMonth.setVisibility(View.VISIBLE);
            mTvMonthUnit.setVisibility(View.VISIBLE);
            mDpvDay.setVisibility(View.VISIBLE);
            mTvDayUnit.setVisibility(View.VISIBLE);
            mDpvHour.setVisibility(View.VISIBLE);
            mTvHourUnit.setVisibility(View.VISIBLE);
            mDpvMinute.setVisibility(View.VISIBLE);
            mTvMinuteUnit.setVisibility(View.VISIBLE);
        }
        mTimePickerShowMode = timePickerShowMode;
    }

    private void initScrollUnit(Integer... units) {
        if (units == null || units.length == 0) {
            mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE;
        } else {
            for (int unit : units) {
                mScrollUnits ^= unit;
            }
        }
    }

    /**
     * Set whether the starttime control can be rotated in circulation
     */
    public void setScrollLoop(boolean canLoop) {
        if (!canShow()) return;

        mDpvYear.setCanScrollLoop(canLoop);
        mDpvMonth.setCanScrollLoop(canLoop);
        mDpvDay.setCanScrollLoop(canLoop);
        mDpvHour.setCanScrollLoop(canLoop);
        mDpvMinute.setCanScrollLoop(canLoop);
    }

    /**
     * Set whether the starttime control displays a scrolling animation
     */
    public void setCanShowAnim(boolean canShowAnim) {
        if (!canShow()) return;

        mDpvYear.setCanShowAnim(canShowAnim);
        mDpvMonth.setCanShowAnim(canShowAnim);
        mDpvDay.setCanShowAnim(canShowAnim);
        mDpvHour.setCanShowAnim(canShowAnim);
        mDpvMinute.setCanShowAnim(canShowAnim);
    }

    /**
     * 销毁对话框。
     */
    public void onDestroy() {
        if (mPickerDialog != null) {
            mPickerDialog.dismiss();
            mPickerDialog = null;

            mDpvYear.onDestroy();
            mDpvMonth.onDestroy();
            mDpvDay.onDestroy();
            mDpvHour.onDestroy();
            mDpvMinute.onDestroy();
        }
    }
}
