package com.android.xjay.joyplan.Notification;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.xjay.joyplan.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

public class NotificationTool {

    Context mcontext;
    NotificationReceiver notificationReceiver;
    private static int pending_request_code=0x101;

    public NotificationTool(Context context) {
        mcontext = context;
    }

    private void checkNotificationPermission() {
        if (!isNotificationEnabled()) {
            Toast.makeText(mcontext, "亲爱的joyplaner，请开启通知权限", Toast.LENGTH_LONG).show();
            setupNotification();
        }
    }

    private boolean isNotificationEnabled() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return NotificationManagerCompat.from(mcontext).getImportance() != NotificationManager.IMPORTANCE_NONE;
        }
        return NotificationManagerCompat.from(mcontext).areNotificationsEnabled();

    }

    private void setupNotification() {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //直接跳转到应用通知设置的代码：
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                localIntent.putExtra("android.provider.extra.app.APP_PACKAGE", mcontext.getPackageName());
                mcontext.startActivity(localIntent);
                return;
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                localIntent.putExtra("app_package", mcontext.getPackageName());
                localIntent.putExtra("app_uid", mcontext.getApplicationInfo().uid);
                mcontext.startActivity(localIntent);
                Toast.makeText(mcontext, "Lollipop", Toast.LENGTH_LONG).show();
                return;
            }
            if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                localIntent.setData(Uri.parse("package:" + mcontext.getPackageName()));
                mcontext.startActivity(localIntent);
                Toast.makeText(mcontext, "kitkat", Toast.LENGTH_LONG).show();

                return;
            }

            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mcontext.getPackageName());


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" cxx   pushPermission 有问题");
        }

    }


    public void createPendingNotification(Date begin_date) {
        Date now_date = new Date();
        Long now_time=now_date.getTime();
        Long begin_time=begin_date.getTime();
        Long time = begin_time-now_time;
        /*测试代码
        Log.e("time", time.toString());
        Log.e("begin", begin_time.toString());
        Log.e("begin",begin_date.toString());
        Log.e("nowmill", now_time.toString());*/
        createPendingNotification(time);
    }

    private void createPendingNotification(long time) {
        AlarmManager alarmManager = (AlarmManager) mcontext.getSystemService(mcontext.ALARM_SERVICE);

        Intent intent = new Intent();
        intent.setAction("com.example.notification");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mcontext, pending_request_code++, intent, 0);

        long delaytime = time + System.currentTimeMillis();

        //对不同版本的进行定时
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, delaytime, pendingIntent);//AndAllowWhileIdle
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(delaytime, pendingIntent);
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        }

//        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,delaytime,pendingIntent);
//        }
//        else{
//            alarmManager.set(AlarmManager.RTC_WAKEUP,delaytime,pendingIntent);
//        }
    }

}
