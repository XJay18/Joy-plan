package com.android.xjay.joyplan;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.xjay.joyplan.StatisticsFragment.FqzStatistic;
import com.android.xjay.joyplan.Utils.DateFormat;

import org.apache.poi.ss.usermodel.DataFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FqzActivity extends AppCompatActivity
        implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener {

    private Calendar calendar = Calendar.getInstance();
    /**
     * text view 小时。
     */
    private TextView tv_hour;
    /**
     * text view 分钟。
     */
    private TextView tv_minute;
    /**
     * 时间选择器。
     */
    private CustomTimePicker mTimePicker;
    /**
     * 番茄钟重复次数。
     */
    private String[] sizeArray = {"1", "2", "3", "4"};
    /**
     * 两次番茄钟间隔时间。
     */
    private String[] breakArray = {"5", "10", "15", "20", "30"};
    /**
     * 确认设定番茄钟的动画。
     */
    private LottieAnimationView confirmAnimationView;

    /**
     * 番茄钟默认时、分。
     */
    private int fqz_hour = 0, fqz_min = 25;
    /**
     * 番茄钟默认重复次数与间隔时间。
     */
    private int fqz_size = 1, fqz_break = 5;
    /**
     * 设置日期格式
     */
    private Date date;
    /**
     * 设置日期格式
     */
    private Calendar cal;
    /**
     * 获取当前周首日的日期
     */
    private String currentTime;
    /**
     * 数据库helper的引用(单例)
     */
    private UserDBHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        currentTime=getTimeInterval();
        mHelper = UserDBHelper.getInstance(this,1);

        //mHelper.reset();
        Fqz fqz=new Fqz(currentTime);
        boolean exist=mHelper.FqzExist(currentTime);
        if(!exist){
            mHelper.insert_fqz(fqz);
        }

        setContentView(R.layout.dis_fqz);
        findViewById(R.id.ll_fqz_cycle).setOnClickListener(this);
        findViewById(R.id.tv_fqz_confirm).setOnClickListener(this);
        findViewById(R.id.tv_fqz_setup).setOnClickListener(this);
        findViewById(R.id.btn_fqz_back).setOnClickListener(this);
        findViewById(R.id.ll_fqz_stat).setOnClickListener(this);
        findViewById(R.id.ll_fqz_help).setOnClickListener(this);
        tv_hour = findViewById(R.id.tv_fqz_hour);
        tv_minute = findViewById(R.id.tv_fqz_minute);
        initTimePicker();

        ArrayAdapter<String> mSizeAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, sizeArray);
        mSizeAdapter.setDropDownViewResource(R.layout.item_dropdown);

        Spinner sp_size = findViewById(R.id.sp_fqz_size);
        sp_size.setAdapter(mSizeAdapter);
        sp_size.setSelection(0);
        sp_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fqz_size = Integer.parseInt(sizeArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ArrayAdapter<String> mBreakAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, breakArray);
        mBreakAdapter.setDropDownViewResource(R.layout.item_dropdown);

        Spinner sp_break = findViewById(R.id.sp_fqz_break);
        sp_break.setAdapter(mBreakAdapter);
        sp_break.setSelection(0);
        sp_break.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fqz_break = Integer.parseInt(breakArray[position]);
                Log.d("fqz_break: ", String.valueOf(fqz_break));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        confirmAnimationView = findViewById(R.id.fqz_anim_okay_blue);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_fqz_cycle) {
            mTimePicker.show();
        } else if (v.getId() == R.id.tv_fqz_confirm) {
            ArrayList<Intent> intents = new ArrayList<>();
            // current hour
            int chour = calendar.get(Calendar.HOUR_OF_DAY);
            // current minute
            int cmin = calendar.get(Calendar.MINUTE);
            int[] time = {chour, cmin};
            for (int t = 0; t < 2 * fqz_size - 1; t++) {
                Log.d(
                        "curr time " + t + " ",
                        chour + "  " + cmin);
                Log.d(
                        "time " + t + " ",
                        time[0] + "  " + time[1]);
                if (t % 2 == 0)
                    time = computeTime(time[0], time[1], fqz_hour, fqz_min);
                else
                    time = computeTime(time[0], time[1], 0, fqz_break);

                intents.add(new Intent(AlarmClock.ACTION_SET_ALARM));
                intents.get(t).putExtra(AlarmClock.EXTRA_HOUR, time[0]);
                intents.get(t).putExtra(AlarmClock.EXTRA_MINUTES, time[1]);
                if (t % 2 == 0)
                    intents.get(t).putExtra(
                            AlarmClock.EXTRA_MESSAGE, "番茄钟结束");
                else
                    intents.get(t).putExtra(AlarmClock.EXTRA_MESSAGE, "番茄钟开始");
                intents.get(t).putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                startActivity(intents.get(t));
            }

            confirmAnimationView.setAnimation("okay_blue.json");
            confirmAnimationView.loop(false);
            Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    confirmAnimationView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    confirmAnimationView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
            confirmAnimationView.addAnimatorListener(mAnimationListener);
            confirmAnimationView.playAnimation();
            Toast.makeText(this, "番茄钟设置成功", Toast.LENGTH_SHORT).show();
            //插入数据库
            int totalTime=(fqz_hour*60+fqz_min)*fqz_size;
            mHelper.updateFqz(currentTime,totalTime,getTimeNow());

        } else if (v.getId() == R.id.tv_fqz_setup) {
            Intent deleteAlarm = new Intent(AlarmClock.ACTION_DISMISS_ALARM);
            startActivity(deleteAlarm);
        } else if (v.getId() == R.id.btn_fqz_back) {
            this.finish();
        } else if (v.getId() == R.id.ll_fqz_stat) {
            //Toast.makeText(this, "你点击了番茄钟的统计信息", Toast.LENGTH_SHORT).show();
            Intent fqz_sts = new Intent(FqzActivity.this, FqzStatistic.class);
            startActivity(fqz_sts);
        } else if (v.getId() == R.id.ll_fqz_help) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("番茄钟使用简介");
            mBuilder.setMessage(R.string.info_fqz);
            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        }


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int min) {

    }

    /**
     * 初始化时间选择器
     */
    private void initTimePicker() {
        // default values
        tv_hour.setText("0");
        tv_minute.setText("25");
        long beginTimestamp = DateFormat.str2Long(
                "2010-00-00 00:25", true);
        long endTimestamp = System.currentTimeMillis();

        mTimePicker = new CustomTimePicker(
                this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String hh_mm = DateFormat.long2Str(timestamp, 2);
                String[] hm = hh_mm.split(":");
                tv_hour.setText(hm[0].substring(1));
                tv_minute.setText(hm[1]);
                fqz_hour = Integer.parseInt(hm[0].substring(1));
                fqz_min = Integer.parseInt(hm[1]);
            }
        }, beginTimestamp, endTimestamp, "请选择番茄周期",
                2, new int[]{0, 0, 0, 0, 25});

        mTimePicker.setCancelable(true);
        mTimePicker.setTimePickerShowMode(1);
        mTimePicker.setScrollLoop(true);
        mTimePicker.setCanShowAnim(false);
    }

    /**
     * 通过当前时间以及设定的番茄钟计算结束时间。
     */
    public int[] computeTime(int hour, int minute, int ahour, int amin) {
        if (minute + amin >= 60) {
            hour++;
            minute += +amin - 60;
        } else
            minute += amin;
        if (hour + ahour >= 24) {
            hour += ahour - 24;
        } else
            hour += ahour;
        return new int[]{hour, minute};
    }

    /**
     * 获取当前周首日的日期
     */
    private String getTimeInterval() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYYMMdd");
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = dateFormat.format(cal.getTime());
        return imptimeBegin;
    }

    /**
     * 获取今天是周几
     */
    private int getTimeNow() {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("这里是周几？"+weekday);
        return weekday-1;
    }
}