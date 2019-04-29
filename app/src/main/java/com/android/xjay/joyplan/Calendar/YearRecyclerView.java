package com.android.xjay.joyplan.Calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 年份布局选择View
 */
public final class YearRecyclerView extends RecyclerView {
    private CalendarViewDelegate mDelegate;
    private YearViewAdapter mAdapter;
    private OnMonthSelectedListener mListener;

    public YearRecyclerView(Context context) {
        this(context, null);
    }

    public YearRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new YearViewAdapter(context);
        setLayoutManager(new GridLayoutManager(context, 3));
        setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                if (mListener != null && mDelegate != null) {
                    Month month = mAdapter.getItem(position);
                    if (month == null) {
                        return;
                    }
                    if (!CalendarUtil.isMonthInRange(month.getYear(), month.getMonth(),
                            mDelegate.getMinYear(), mDelegate.getMinYearMonth(),
                            mDelegate.getMaxYear(), mDelegate.getMaxYearMonth())) {
                        return;
                    }
                    mListener.onMonthSelected(month.getYear(), month.getMonth());
                    if (mDelegate.mYearViewChangeListener != null) {
                        mDelegate.mYearViewChangeListener.onYearViewChange(true);
                    }
                }
            }
        });
    }

    /**
     * 设置
     *
     * @param delegate delegate
     */
    final void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        this.mAdapter.setup(delegate);
    }

    /**
     * 初始化年视图
     *
     * @param year year
     */
    final void init(int year) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        for (int i = 1; i <= 12; i++) {
            date.set(year, i - 1, 1);
            int mDaysCount = CalendarUtil.getMonthDaysCount(year, i);
            Month month = new Month();
            month.setDiff(CalendarUtil.getMonthViewStartDiff(year, i, mDelegate.getWeekStart()));
            month.setCount(mDaysCount);
            month.setMonth(i);
            month.setYear(year);
            mAdapter.addItem(month);
        }
    }

    /**
     * 更新周起始
     */
    final void updateWeekStart() {
        for (Month month : mAdapter.getItems()) {
            month.setDiff(CalendarUtil.getMonthViewStartDiff(month.getYear(), month.getMonth(), mDelegate.getWeekStart()));
        }
    }

    /**
     * 月份选择事件
     *
     * @param listener listener
     */
    final void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mListener = listener;
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int height = MeasureSpec.getSize(heightSpec);
        int width = MeasureSpec.getSize(widthSpec);
        mAdapter.setYearViewSize(width / 3, height / 4);
    }

    interface OnMonthSelectedListener {
        void onMonthSelected(int year, int month);
    }
}