package com.android.xjay.joyplan;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.Utils.DateFormat;
import com.android.xjay.joyplan.web.WebServicePost;

public class AddActivity extends AppCompatActivity {
    private UserDBHelper mHelper; //SQLite helper
    private CustomTimePicker myTimePicker;
    private TextView tv_select_start_time;
    private TextView tv_select_end_time;
    private EditText editText_title;
    private EditText editText_description;
    private EditText editText_address;
    private Context mContext;
    private String string_title;
    private String string_start_time;
    private String string_end_time;
    private String string_description;
    private String string_address;
    private Button btn_add;
    private Button btn_cancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mContext = getApplicationContext();
        //textView to select starttime&time

        tv_select_start_time = findViewById(R.id.tv_select_start_time);
        tv_select_end_time = findViewById(R.id.tv_select_end_time);
        initTimePicker();

        //On textView click open timePicker
        tv_select_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTimePicker.show(tv_select_start_time.getText().toString());
            }
        });

        tv_select_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTimePicker.show(tv_select_end_time.getText().toString());
            }
        });
        initTimePicker();
        //get single instance of DB
        mHelper = UserDBHelper.getInstance(this, 1);

        editText_title = findViewById(R.id.editText_title);
        editText_description = findViewById(R.id.editText_detail);
        editText_address = findViewById(R.id.editText_address);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new MyOnClickListener());
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel: {
                    Intent intent = new Intent();
                    intent.setClass(mContext, ScheduleActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btn_add: {
                    string_title = editText_title.getText().toString();
                    string_description = editText_description.getText().toString();
                    string_start_time = tv_select_start_time.getText().toString();
                    string_end_time = tv_select_end_time.getText().toString();
                    string_address = editText_address.getText().toString();

                    //put the information into a stuInfo

                    StudentActivityInfo info = new StudentActivityInfo(string_title, string_description, string_start_time, string_end_time, string_address);

                    //insert_studentActivity the stuInfo
                    mHelper.insert_studentActivity(info);


                    SQLiteDatabase dbRead = mHelper.getReadableDatabase();
                    Cursor c = dbRead.query("user_info", null, null, null, null, null, null);
                    Integer i = c.getCount();

                    //show the number of record in DB
                    Toast toast = Toast.makeText(getApplicationContext(), i.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    sentBroadcast();
                    new Thread(new RegThread()).start();
                    Intent intent = new Intent();
                    intent.setClass(mContext, ScheduleActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    private class RegThread implements Runnable {
        public void run() {
            //获取服务器返回数据
            System.out.println("wenti" + string_title);
            System.out.println("wenti" + string_description);
            System.out.println("wenti" + string_start_time);
            System.out.println("wenti" + string_address);
            String RegRet = WebServicePost.activityPost(string_title,
                    string_start_time, string_description,
                    string_address, "ActiLet");
            //更新UI，界面处理
            //showReq(RegRet);
        }
    }


    private void sentBroadcast() {
        Intent intent = new Intent();
        intent.setAction("ADD ACTIVITY");
        intent.putExtra("sele", "广播测试");
        sendBroadcast(intent);
    }

    protected void onResume() {
        super.onResume();
        mHelper = UserDBHelper.getInstance(mContext, 1);
        mHelper.openWriteLink();
    }

    protected void onPause() {
        super.onPause();
        mHelper.closeLink();
    }

    private void initTimePicker() {

        long beginTime = System.currentTimeMillis();
        String endTime = "2020-04-07 18:00";
        tv_select_start_time.setText(endTime);
        tv_select_end_time.setText(endTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        myTimePicker = new CustomTimePicker(AddActivity.this,
                new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_select_start_time.setText(
                        DateFormat.long2Str(timestamp,
                        true));
            }
        }, beginTime, DateFormat.str2Long(
                endTime, true), "请选择时间");
        //允许点击屏幕或物理返回键关闭
        myTimePicker.setCancelable(true);
        // 显示时和分
        myTimePicker.setTimePickerShowMode(0);
        // 允许循环滚动
        myTimePicker.setScrollLoop(true);
        // 允许滚动动画
        myTimePicker.setCanShowAnim(true);
    }
}


