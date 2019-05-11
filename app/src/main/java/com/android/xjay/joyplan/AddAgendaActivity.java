package com.android.xjay.joyplan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.Utils.DateFormat;


public class AddAgendaActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDBHelper mHelper;
    private CustomTimePicker myTimePicker;
    private EditText editText_agenda_title;
    private EditText editText_agenda_address;
    private TextView tv_agenda_start_time;
    private TextView tv_agenda_end_time;
    private TextView tv_agenda_cancel;
    private TextView tv_agenda_confirm;
    private EditText editText_notation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String date=bundle.getString("date","000000");
        String nextDate=bundle.getString("nextDate","000000");
        date=date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
        nextDate=nextDate.substring(0,4)+"-"+nextDate.substring(4,6)+"-"+nextDate.substring(6,8);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);
        editText_agenda_title=findViewById(R.id.editText_agenda_title);
        editText_agenda_address=findViewById(R.id.editText_agenda_address);
        tv_agenda_start_time=findViewById(R.id.tv_select_agenda_start_time);
        tv_agenda_end_time=findViewById(R.id.tv_select_agenda_end_time);
        editText_notation=findViewById(R.id.editText_notation);
        tv_agenda_cancel=findViewById(R.id.tv_agenda_cancel);
        tv_agenda_confirm=findViewById(R.id.tv_agenda_confirm);
        initTimePicker();
        tv_agenda_confirm.setOnClickListener(this);

        tv_agenda_start_time.setOnClickListener(this);
        tv_agenda_end_time.setOnClickListener(this);
        tv_agenda_start_time.setText(date);
        tv_agenda_end_time.setText(nextDate);


        tv_agenda_cancel.setOnClickListener(this);
        mHelper = UserDBHelper.getInstance(this, 1);
    }

    private void initTimePicker() {

        long beginTime = System.currentTimeMillis();
        String endTime = "2020-04-07 18:00";


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
                String title=editText_agenda_title.getText().toString();
                String address=editText_agenda_address.getText().toString();
                String content=editText_notation.getText().toString();
                Agenda agenda=new Agenda(title,start_time,end_time,content,address);
                //mHelper.reset();
                mHelper.insert_agenda(agenda);
                SQLiteDatabase dbRead=mHelper.getReadableDatabase();
                Cursor c;
                c=dbRead.query("agenda_table",null,null,null,null,null,null);
                int length=c.getCount();
                String s=new Integer(length).toString();
                Toast toast=Toast.makeText(this,s,Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
