package com.android.xjay.joyplan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.Notification.NotificationTool;
import com.android.xjay.joyplan.Utils.DateFormat;
import com.android.xjay.joyplan.Utils.JumpTextWatcher;
import com.android.xjay.joyplan.web.WebServiceGet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddAgendaActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDBHelper mHelper;
    private CustomTimePicker myStartTimePicker;
    private CustomTimePicker myEndTimePicker;

    private EditText editText_agenda_title;
    private EditText editText_agenda_address;
    private TextView tv_agenda_start_time;
    private TextView tv_agenda_end_time;
    private TextView tv_agenda_cancel;
    private TextView tv_agenda_confirm;
    private EditText editText_notation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);
        editText_agenda_title = findViewById(R.id.editText_agenda_title);
        editText_agenda_address = findViewById(R.id.editText_agenda_address);
        tv_agenda_start_time = findViewById(R.id.tv_select_agenda_start_time);
        tv_agenda_end_time = findViewById(R.id.tv_select_agenda_end_time);
        editText_notation = findViewById(R.id.editText_notation);
        tv_agenda_cancel = findViewById(R.id.tv_agenda_cancel);
        tv_agenda_confirm = findViewById(R.id.tv_agenda_confirm);
        initTimePicker();

        editText_agenda_title.addTextChangedListener(new JumpTextWatcher(this, editText_agenda_title, editText_agenda_address));
        editText_agenda_address.addTextChangedListener(new JumpTextWatcher(this, editText_agenda_address, editText_notation));
        editText_notation.addTextChangedListener(new JumpTextWatcher(this, editText_notation, tv_agenda_confirm));
        tv_agenda_confirm.setOnClickListener(this);

        tv_agenda_start_time.setOnClickListener(this);
        tv_agenda_end_time.setOnClickListener(this);

        tv_agenda_cancel.setOnClickListener(this);
        mHelper = UserDBHelper.getInstance(this, 1);
        if (bundle != null) {
            String date = bundle.getString("date", "000000");
            String nextDate = bundle.getString("nextDate", "000000");
            String strNextHour;
            String starttime;
            String endtime;
            int nextHour = Integer.parseInt(date.substring(4, 6)) + 1;
            strNextHour = new Integer(nextHour).toString();
            if (strNextHour.length() == 1) {
                strNextHour = "0" + strNextHour;
            }
            starttime = date.substring(0, 2) + "-" + date.substring(2, 4) + " " + date.substring(4, 6) + ":" + date.substring(6, 8);

            endtime = date.substring(0, 2) + "-" + date.substring(2, 4) + " " + strNextHour + ":" + date.substring(6, 8);
            nextDate = nextDate.substring(0, 2) + "-" + nextDate.substring(2, 4) + " " + nextDate.substring(4, 6) + ":" + nextDate.substring(6, 8);
            tv_agenda_start_time.setText(starttime);
            tv_agenda_end_time.setText(endtime);
        }

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

                // remove the first 4 chars i.e. year.
                String modified = raw.substring(5);
                tv_agenda_start_time.setText(modified);
                long temp_beginTime = timestamp + 60 * 60 * 1000;
                String str_temp = DateFormat.long2Str(temp_beginTime, true);
                String str_trans_temp = str_temp.substring(5);
                Log.v("str_trans_temp", str_trans_temp);
                tv_agenda_end_time.setText(str_trans_temp);
                myEndTimePicker.setSelectedTime(temp_beginTime, false);
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
                finish();
                break;
            }

            case R.id.tv_agenda_confirm: {
                // mHelper.reset();
                String start_time = tv_agenda_start_time.getText().toString();
                start_time = "2019-" + start_time;
                String end_time = tv_agenda_end_time.getText().toString();
                end_time = "2019-" + end_time;

                //创建延时通知
                Date begin_date = null;
                try {
                    begin_date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(start_time);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                /*Log.e("begin_time",start_time);
                Log.e("begin_date:before para", begin_date.toString());*/
                new NotificationTool(this).createPendingNotification(begin_date);

                String title = editText_agenda_title.getText().toString();
                String address = editText_agenda_address.getText().toString();
                String content = editText_notation.getText().toString();
                if(content==null)
                content="";
                Agenda agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);
                SQLiteDatabase dbRead = mHelper.getReadableDatabase();
                Cursor c;
                c = dbRead.query("agenda_table", null, null, null, null, null, null);

//                MODIFIED THE TOAST
//                int length = c.getCount();
//                String s = new Integer(length).toString();
//                Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
                Toast toast = Toast.makeText(this, "任务添加成功", Toast.LENGTH_SHORT);
                Intent intent = new Intent();
                intent.setAction("ADD AGENDA");
                sendBroadcast(intent);
                toast.show();

                finish();
                break;
            }

        }
    }

}
