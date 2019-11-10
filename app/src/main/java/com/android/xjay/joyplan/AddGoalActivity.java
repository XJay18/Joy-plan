package com.android.xjay.joyplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.xjay.joyplan.Utils.DateFormat;

/**
 * 添加目标activity
 */
public class AddGoalActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 目标结束时间文字
     */
    private TextView tv_end_time;

    /**
     * 取消按钮
     */
    private TextView tv_cancel;

    /**
     * 确认按钮
     */
    private TextView tv_add;

    /**
     * 时间选择器
     */
    private CustomTimePicker myEndTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        tv_end_time = findViewById(R.id.tv_select_goal_end_time);
        tv_cancel = findViewById(R.id.tv_goal_cancel);
        tv_add = findViewById(R.id.tv_goal_confirm);
        tv_end_time.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        initTimePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_goal_end_time: {
                myEndTimePicker.show(tv_end_time.getText().toString());
                break;
            }
            case R.id.tv_goal_cancel: {
                finish();
                break;
            }
            case R.id.tv_goal_confirm: {
                sentBroadcast();
                finish();
                break;
            }

        }
    }

    /**
     * 发送添加目标广播
     */
    private void sentBroadcast() {
        Intent intent = new Intent();
        intent.setAction("ADD GOAL");
        sendBroadcast(intent);
    }

    /**
     * 初始化时间选择器
     */
    private void initTimePicker() {

        long start_beginTime = System.currentTimeMillis();
        String endTime = "2020-04-07 18:00";

        String str_BT = DateFormat.long2Str(start_beginTime, true);
        String str_trans_BT = str_BT.substring(5);
        tv_end_time.setText(str_trans_BT);

        long end_beginTime = System.currentTimeMillis() + 60 * 60 * 1000;
        str_BT = DateFormat.long2Str(end_beginTime, true);
        str_trans_BT = str_BT.substring(5);


        myEndTimePicker = new CustomTimePicker(AddGoalActivity.this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String raw = DateFormat.long2Str(timestamp, true);
                Log.v("raw", raw);
                // remove the first 4 chars i.e. year.
                tv_end_time.setText(DateFormat.long2Str(timestamp, true));

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
}
