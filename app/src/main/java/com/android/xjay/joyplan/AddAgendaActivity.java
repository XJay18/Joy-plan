package com.android.xjay.joyplan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
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


/**
 * 添加日程activity
 */
public class AddAgendaActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 数据库helper的引用(单例)
     */
    private UserDBHelper mHelper;

    /**
     * 时间选择器（开始时间）
     */
    private CustomTimePicker myStartTimePicker;

    /**
     * 时间选择器（结束时间）
     */
    private CustomTimePicker myEndTimePicker;

    /**
     * 活动标题输入框
     */
    private EditText editText_agenda_title;

    /**
     * 活动地点输入框
     */
    private EditText editText_agenda_address;

    /**
     * 活动开始时间文字
     */
    private TextView tv_agenda_start_time;

    /**
     * 活动结束时间文字
     */
    private TextView tv_agenda_end_time;

    /**
     * 取消按钮
     */
    private TextView tv_agenda_cancel;

    /**
     * 确认按钮
     */
    private TextView tv_agenda_confirm;

    /**
     * 备注输入框
     */
    private EditText editText_notation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 获取intent
        Intent intent = getIntent();
        // 获取bundle
        Bundle bundle = intent.getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);

        // 找到所有部件
        findAllViews();

        // 初始化时间选择器
        initTimePicker();

        // 注册按下回车监听，实现按下回车后光标跳转
        editText_agenda_title.addTextChangedListener(new JumpTextWatcher(this, editText_agenda_title, editText_agenda_address));
        editText_agenda_address.addTextChangedListener(new JumpTextWatcher(this, editText_agenda_address, editText_notation));
        editText_notation.addTextChangedListener(new JumpTextWatcher(this, editText_notation, tv_agenda_confirm));

        // 注册点击监听
        tv_agenda_confirm.setOnClickListener(this);
        tv_agenda_start_time.setOnClickListener(this);
        tv_agenda_end_time.setOnClickListener(this);
        tv_agenda_cancel.setOnClickListener(this);

        // 获取数据库操作类实例（单例）
        mHelper = UserDBHelper.getInstance(this, 1);

        // 如果bundle内容非空，从bundle中获取开始时间和结束时间，并显示出来
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

    // 找到所有部件
    private void findAllViews() {
        // 找到所有部件
        editText_agenda_title = findViewById(R.id.editText_agenda_title);
        editText_agenda_address = findViewById(R.id.editText_agenda_address);
        tv_agenda_start_time = findViewById(R.id.tv_select_agenda_start_time);
        tv_agenda_end_time = findViewById(R.id.tv_select_agenda_end_time);
        editText_notation = findViewById(R.id.editText_notation);
        tv_agenda_cancel = findViewById(R.id.tv_agenda_cancel);
        tv_agenda_confirm = findViewById(R.id.tv_agenda_confirm);
    }

    /**
     * 初始化时间选择器
     */
    private void initTimePicker() {

        // 获取当前时间
        long current_time = System.currentTimeMillis();
        //最大可选结束时间()
        long temp = 157680000000L;
        long max_time = current_time + temp;

        // 开始时间字符串
        String str_start_time = DateFormat.long2Str(current_time, true);
        // 将年份截取掉（不需要显示年份）
        String str_trans_start_time = str_start_time.substring(5);
        // 显示开始时间
        tv_agenda_start_time.setText(str_trans_start_time);

        // 结束时间时间戳
        long end_beginTime = current_time + 60 * 60 * 1000;
        // 结束时间字符串
        String str_end_time = DateFormat.long2Str(end_beginTime, true);
        // 将年份截取掉（不需要显示年份）
        String str_trans_end_time = str_end_time.substring(5);
        // 显示结束时间
        tv_agenda_end_time.setText(str_trans_end_time);

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

                tv_agenda_end_time.setText(str_trans_temp);
                myEndTimePicker.setSelectedTime(temp_beginTime, false);
//                Log.v("boolean",t+"");
            }
        }, current_time, max_time, "请选择时间");
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
        }, end_beginTime, max_time, "请选择时间");
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
            // 开始时间
            case R.id.tv_select_agenda_start_time: {
                myStartTimePicker.show(tv_agenda_start_time.getText().toString());
                break;
            }
            // 结束时间
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
            // 取消按钮
            case R.id.tv_agenda_cancel: {
                finish();
                break;
            }
            // 确认按钮
            case R.id.tv_agenda_confirm: {
                // mHelper.reset();
                String start_time = tv_agenda_start_time.getText().toString();
                start_time = "2019-" + start_time;
                String end_time = tv_agenda_end_time.getText().toString();
                end_time = "2019-" + end_time;
                //日程标题和备注
                String agenda_title = editText_agenda_title.getText().toString();
                String agenda_content = editText_notation.getText().toString();

                //创建延时通知
                Date begin_date = null;
                try {
                    begin_date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(start_time);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                /*Log.e("begin_time",start_time);
                Log.e("begin_date:before para", begin_date.toString());*/
                new NotificationTool(this).createPendingNotification(begin_date, agenda_title, agenda_content);

                String title = editText_agenda_title.getText().toString();
                String address = editText_agenda_address.getText().toString();
                String content = editText_notation.getText().toString();
                if (content == null)
                    content = "";
                Agenda agenda = new Agenda(title, start_time, end_time, content, address);
                //mHelper.reset();
                mHelper.insert_agenda(agenda);




                SQLiteDatabase dbRead = mHelper.getReadableDatabase();
                Cursor c;
                c = dbRead.query("agenda_table", null, null, null, null, null, null);

//                MODIFIED THE TOAST
//                int length = c.getCount();
//                String s = new Integer(length).toString();
//                Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);





                new Thread(new MyThread(agenda)).start();



                finish();
                break;
            }

        }
    }

    private class MyThread implements Runnable{
        Agenda agenda;
        public MyThread(Agenda agenda){
            this.agenda=agenda;
        }
        public void run() {
            // 获取服务器返回的数据
            PhoneNumber phoneNumber=PhoneNumber.getInstance();
            String stringPhoneNumber=phoneNumber.getPhone_number();
            String infoString = WebServiceGet.addAgendaGet(stringPhoneNumber, agenda.getTitle(),agenda.getStarttime(),agenda.getEndtime(),agenda.getNotation(),agenda.getAddress());
            // 更新 UI，使用 runOnUiThread()方法
            showResponse(infoString);
        }

    }

    private void showResponse(final String response){
            sendBroadcast();
            if(response.equals("true")){
                Looper.prepare();
                Toast.makeText(this, "任务添加成功，已同步到云端", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else{
                Looper.prepare();
                Toast.makeText(this, "任务添加成功，未到云端失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }


    }

    /**
     * 添加活动后发送广播
     */
    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("ADD AGENDA");
        sendBroadcast(intent);
    }





}
