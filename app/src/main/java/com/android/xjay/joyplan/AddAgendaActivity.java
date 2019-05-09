package com.android.xjay.joyplan;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.xjay.calendarview.Calendar;
import com.android.xjay.joyplan.Utils.DateFormat;


public class AddAgendaActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDBHelper mHelper;
    private CustomTimePicker myTimePicker;
    private TextView tv_agenda_title;
    private TextView tv_agenda_address;
    private TextView tv_agenda_start_time;
    private TextView tv_agenda_end_time;
    private TextView tv_agenda_cancel;
    private TextView tv_agenda_confirm;
    private EditText editText_notation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);
        tv_agenda_start_time=findViewById(R.id.tv_select_agenda_start_time);
        tv_agenda_end_time=findViewById(R.id.tv_select_agenda_end_time);
        editText_notation=findViewById(R.id.editText_notation);
        tv_agenda_cancel=findViewById(R.id.tv_agenda_cancel);
        tv_agenda_confirm=findViewById(R.id.tv_agenda_confirm);
        initTimePicker();
        tv_agenda_confirm.setOnClickListener(this);
        tv_agenda_start_time.setOnClickListener(this);
        tv_agenda_end_time.setOnClickListener(this);
        tv_agenda_cancel.setOnClickListener(this);
        mHelper = UserDBHelper.getInstance(this, 1);
    }

    private void initTimePicker() {

        long beginTime = System.currentTimeMillis();
        String endTime = "2020-04-07 18:00";
        tv_agenda_start_time.setText(endTime);
        tv_agenda_end_time.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        myTimePicker = new CustomTimePicker(AddAgendaActivity.this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_agenda_start_time.setText(DateFormat.long2Str(timestamp, true));
            }
        }, beginTime, DateFormat.str2Long(endTime, true), "请选择时间", 23);
        //允许点击屏幕或物理返回键关闭
        myTimePicker.setCancelable(true);
        // 显示时和分
        myTimePicker.setTimePickerShowMode(0);
        // 允许循环滚动
        myTimePicker.setScrollLoop(true);
        // 允许滚动动画
        myTimePicker.setCanShowAnim(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_select_agenda_start_time: {
                myTimePicker.show(tv_agenda_start_time.getText().toString());
                break;
            }
            case R.id.tv_select_agenda_end_time: {
                myTimePicker.show(tv_agenda_end_time.getText().toString());
                break;
            }
            case R.id.tv_agenda_cancel:{
                Intent intent=new Intent();
                intent.setClass(this,HomeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_agenda_confirm:{
                String start_time=tv_agenda_start_time.getText().toString();
                String end_time=tv_agenda_end_time.getText().toString();
                String title=tv_agenda_title.getText().toString();
                String address=tv_agenda_address.getText().toString();
                String content=editText_notation.getText().toString();
                Agenda agenda=new Agenda(title,start_time,end_time,content,address);
                mHelper.insert_agenda(agenda);
            }
        }
    }
}
