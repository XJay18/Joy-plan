package com.android.xjay.joyplan;

import android.content.Intent;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.xjay.calendarview.Calendar;
import com.android.xjay.joyplan.Utils.DateFormat;


public class AddAgendaActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDBHelper mHelper;
    private CustomTimePicker myStartTimePicker;
    private CustomTimePicker myEndTimePicker;
    private TextView tv_agenda_start_time;
    private TextView tv_agenda_end_time;
    private TextView tv_agenda_cancel;
    private TextView tv_agenda_confirm;
    private EditText editText_notation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);
        tv_agenda_start_time = findViewById(R.id.tv_select_agenda_start_time);
        tv_agenda_end_time = findViewById(R.id.tv_select_agenda_end_time);
        editText_notation = findViewById(R.id.editText_notation);
        tv_agenda_cancel = findViewById(R.id.tv_agenda_cancel);
        tv_agenda_confirm = findViewById(R.id.tv_agenda_confirm);
        initTimePicker();
        tv_agenda_confirm.setOnClickListener(this);
        tv_agenda_start_time.setOnClickListener(this);
        tv_agenda_end_time.setOnClickListener(this);
        tv_agenda_cancel.setOnClickListener(this);
        mHelper = UserDBHelper.getInstance(this, 1);
    }

    private void initTimePicker() {

        long start_beginTime = System.currentTimeMillis();
        String endTime = "2020-04-07 18:00";
        String str_BT = DateFormat.long2Str(start_beginTime, true);
        String str_trans_BT = str_BT.substring(5);
        tv_agenda_start_time.setText(str_trans_BT);

        long end_beginTime = System.currentTimeMillis() + 60 * 60 * 1000;
        str_BT = DateFormat.long2Str(end_beginTime, true);
        str_trans_BT = str_BT.substring(5);
        tv_agenda_end_time.setText(str_trans_BT);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        myStartTimePicker = new CustomTimePicker(AddAgendaActivity.this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String raw = DateFormat.long2Str(timestamp, true);
//                Log.v("timestamp", timestamp + "");
////                Log.v("raw", raw);
                // remove the first 4 chars i.e. year.
                String modified = raw.substring(5);
                Log.v("modified", modified);
                tv_agenda_start_time.setText(modified);
                long temp_beginTime = timestamp + 60 * 60 * 1000;
                String str_temp = DateFormat.long2Str(temp_beginTime, true);
                String str_trans_temp = str_temp.substring(5);
//                Log.v("str_trans_temp",str_trans_temp);
                tv_agenda_end_time.setText(str_trans_temp);
                myEndTimePicker.setSelectedTime(temp_beginTime,false);
//                Log.v("boolean",t+"");
            }
        }, start_beginTime, DateFormat.str2Long(endTime, true), "请选择时间");
        //允许点击屏幕或物理返回键关闭
        myStartTimePicker.setCancelable(true);
        // 显示日期和时、分
        myStartTimePicker.setTimePickerShowMode(2);
        // 允许循环滚动
        myStartTimePicker.setScrollLoop(true);
        // 允许滚动动画
        myStartTimePicker.setCanShowAnim(false);

        myEndTimePicker = new CustomTimePicker(AddAgendaActivity.this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String raw = DateFormat.long2Str(timestamp, true);
                Log.v("raw", raw);
                // remove the first 4 chars i.e. year.
                String modified = raw.substring(5);
                Log.v("modified", modified);
                tv_agenda_end_time.setText(modified);
            }
        }, end_beginTime, DateFormat.str2Long(endTime, true), "请选择时间");
        //允许点击屏幕或物理返回键关闭
        myEndTimePicker.setCancelable(true);
        // 显示日期和时、分
        myEndTimePicker.setTimePickerShowMode(2);
        // 允许循环滚动
        myEndTimePicker.setScrollLoop(true);
        // 允许滚动动画
        myEndTimePicker.setCanShowAnim(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_agenda_start_time: {
                myStartTimePicker.show(tv_agenda_start_time.getText().toString());
                break;
            }
            case R.id.tv_select_agenda_end_time: {
//                myEndTimePicker.setSelectedTime(tv_agenda_end_time.getText().toString(),false);
//                Log.v("传入",tv_agenda_end_time.getText().toString());
//                Log.v("传入",DateFormat.str2Long(tv_agenda_end_time.getText().toString(),true)+"");
//                myEndTimePicker.setTime(DateFormat.str2Long(tv_agenda_end_time.getText().toString(),true));
//
//                myEndTimePicker.getTime();
                myEndTimePicker.show(tv_agenda_end_time.getText().toString());
                break;
            }
            case R.id.tv_agenda_cancel: {
                Intent intent = new Intent();
                intent.setClass(this, HomeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_agenda_confirm: {
                Calendar calendar = new Calendar();
                String start_time = tv_agenda_start_time.getText().toString();
                String end_time = tv_agenda_end_time.getText().toString();
                String content = editText_notation.getText().toString();
                String yearstr = start_time.substring(0, 4);
                String monthstr = start_time.substring(5, 7);
                String daystr = start_time.substring(8, 10);
                int year = Integer.parseInt(yearstr);
                int month = Integer.parseInt(monthstr);
                int day = Integer.parseInt(daystr);
                calendar.setDay(day);
                calendar.setMonth(month);
                calendar.setYear(year);
                Agenda agenda = new Agenda(calendar, content);
                mHelper.insert_agenda(agenda);
            }
        }
    }
}
