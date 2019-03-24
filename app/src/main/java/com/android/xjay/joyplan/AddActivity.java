package com.android.xjay.joyplan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.Utils.DateFormat;

public class AddActivity extends AppCompatActivity {
    private UserDBHelper_schedule mHelper;//SQLite helper
    private CustomTimePicker myTimePicker;
    private TextView tv_select_date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //textView to select date&time
        tv_select_date=findViewById(R.id.tv_select_date);

        //On textView click open timePicker
        tv_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTimePicker.show(tv_select_date.getText().toString());
            }
        });
        initTimePicker();
        //get single instance of DB
        mHelper = UserDBHelper_schedule.getInstance(this, 1);


        final EditText editText_title = (EditText) findViewById(R.id.editText_title);
        final EditText editText_info = (EditText) findViewById(R.id.editText_info);
        final EditText editText_address = (EditText) findViewById(R.id.editText_address);
        Button button = (Button) findViewById(R.id.addButton);

        //On addButton click add a new studentActivity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mHelper.clean();
                //mHelper.reset();

                //get the information of the studentActivity
                String s1=editText_title.getText().toString();
                String s2=editText_info.getText().toString();
                String s3=tv_select_date.getText().toString();
                String s4=editText_address.getText().toString();

                //put the information into a stuInfo
                StudentActivityInfo info = new StudentActivityInfo(s1,s2,s3,s4);

                //insert the stuInfo
                mHelper.insert(info);


                SQLiteDatabase dbRead = mHelper.getReadableDatabase();
                Cursor c = dbRead.query("user_info", null, null, null, null, null, null);
                Integer i = c.getCount();

                //show the number of record in DB
                Toast toast = Toast.makeText(getApplicationContext(),i.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //btn to listActivity
        Button changeButton = findViewById(R.id.btn_activity_added);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddActivity.this, ScheduleActivity.class);
                startActivity(intent);
                AddActivity.this.finish();
            }
        });

    }

    protected void onResume () {
        super.onResume();
        mHelper = UserDBHelper_schedule.getInstance(this, 1);
        mHelper.openWriteLink();
    }

    protected void onPause () {
        super.onPause();
        mHelper.closeLink();
    }

    private void initTimePicker() {

        long beginTime = DateFormat.str2Long("2010-00-00 18:00", true);
        long endTime = System.currentTimeMillis();
        tv_select_date.setText(DateFormat.long2Str(endTime,true));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
       myTimePicker = new CustomTimePicker(AddActivity.this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_select_date.setText(DateFormat.long2Str(timestamp, true));
            }
        }, beginTime, endTime,"请选择时间",23);
        //允许点击屏幕或物理返回键关闭
        myTimePicker.setCancelable(true);
        // 显示时和分
        myTimePicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        myTimePicker.setScrollLoop(true);
        // 允许滚动动画
        myTimePicker.setCanShowAnim(true);
    }

}
