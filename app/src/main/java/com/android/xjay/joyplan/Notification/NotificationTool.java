package com.android.xjay.joyplan.Notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Date;

public class NotificationTool {

    Context mcontext;
    NotificationReceiver notificationReceiver;
    private static int pending_request_code = 0x101;

    public NotificationTool(Context context) {
        mcontext = context;
    }

    private void checkNotificationPermission() {
        if (!isNotificationEnabled()) {
            Toast.makeText(mcontext,
                    "亲爱的joyplaner，请开启通知权限", Toast.LENGTH_LONG).show();
            setupNotification();
        }
    }

    private boolean isNotificationEnabled() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return NotificationManagerCompat.from(
                    mcontext).getImportance() != NotificationManager.IMPORTANCE_NONE;
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
                localIntent.putExtra(
                        "android.provider.extra.app.APP_PACKAGE",
                        mcontext.getPackageName());
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
            localIntent.setClassName("com.android.settings",
                    "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName",
                    mcontext.getPackageName());


        } catch (Exception e) {
            e.printStackTrace();
            // FIXME
            System.out.println(" cxx   pushPermission 有问题");
        }

    }


    public void createPendingNotification(Date begin_date, String title, String content) {
        Date now_date = new Date();
        Long now_time = now_date.getTime();
        Long begin_time = begin_date.getTime();
        Long time = begin_time - now_time;
        // FIXME
        /*测试代码
        Log.e("time", time.toString());
        Log.e("begin", begin_time.toString());
        Log.e("begin",begin_date.toString());
        Log.e("nowmill", now_time.toString());*/
        createPendingNotification(time, title, content);
    }

    private void createPendingNotification(long time, String title, String content) {
        AlarmManager alarmManager = (AlarmManager) mcontext.getSystemService(
                Context.ALARM_SERVICE);

        Intent intent = new Intent();
        intent.setAction("com.example.notification");
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        intent.putExtras(bundle);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mcontext, pending_request_code++, intent, 0);

        long delaytime = time + System.currentTimeMillis();

        //对不同版本的进行定时
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    delaytime, pendingIntent);//AndAllowWhileIdle
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo =
                    new AlarmManager.AlarmClockInfo(delaytime, pendingIntent);
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        }
        // FIXME
//        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,delaytime,pendingIntent);
//        }
//        else{
//            alarmManager.set(AlarmManager.RTC_WAKEUP,delaytime,pendingIntent);
//        }
    }

}
