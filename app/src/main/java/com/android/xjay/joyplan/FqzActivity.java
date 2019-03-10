package com.android.xjay.joyplan;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import com.android.xjay.joyplan.Utils.DateFormat;
import android.view.LayoutInflater;

public class FqzActivity extends AppCompatActivity
        implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private TextView tv_hour;
    private TextView tv_minute;
    private LayoutInflater inflater;
    private TextView tv_pickertitle;
    private CustomTimePicker mTimePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dis_fqz);
        findViewById(R.id.ll_fqz_cycle).setOnClickListener(this);
        tv_hour=findViewById(R.id.tv_fqz_hour);
        tv_minute=findViewById(R.id.tv_fqz_minute);
//        View view = inflater.inflate(R.layout.time_picker_dialog,null);
//        tv_pickertitle = (TextView) view.findViewById(R.id.tv);
        initTimePicker();

    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.ll_fqz_cycle){
            mTimePicker.show();
        }
    }

    public void onTimeSet(TimePicker view,int hourOfDay,int min){
        String hour= String.valueOf(hourOfDay);
        String minute=String.valueOf(min);
        tv_hour.setText(hour);
        tv_minute.setText(minute);
    }

    private void initTimePicker() {
        long beginTimestamp = DateFormat.str2Long("2010-00-00", false);
        long endTimestamp = System.currentTimeMillis();

        mTimePicker = new CustomTimePicker(this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                // mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
                String hh_mm=DateFormat.long2Str(timestamp, 2);
                String[] hm=hh_mm.split(":");
                tv_hour.setText(hm[0]);
                tv_minute.setText(hm[1]);
            }
        }, beginTimestamp, endTimestamp);

        mTimePicker.setCancelable(false);
        mTimePicker.setCanShowPreciseTime(false);
        mTimePicker.setScrollLoop(true);
        mTimePicker.setCanShowAnim(false);
    }
}