package com.android.xjay.joyplan.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.xjay.joyplan.HomeActivity;
import com.android.xjay.joyplan.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificationReceiver extends BroadcastReceiver {

    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        if (intent.getAction().equals("com.example.notification")) {
            createNotification();
            Toast.makeText(mcontext, "received", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotification() {

        String id = "channel_ID";
        String name = "channel_Name";
        long[] pattern = {0, 1000, 0, 1000};

        NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder;

        Intent intent = new Intent(mcontext, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0, intent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel(id, name, NotificationManager.IMPORTANCE_MAX, pattern, notificationManager);
            builder = new NotificationCompat.Builder(mcontext, id);
        } else {
            builder = new NotificationCompat.Builder(mcontext);
            builder.setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                    .setLights(0x00FF00, 1000, 1000);
        }

        builder.setContentTitle("Title")
                .setContentText("Text")
                //Icon资源不可以在mipmap目录下
                .setLargeIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.logo))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                //点击通知后关闭通知
                .setAutoCancel(true);
        Notification notification = builder.build();
        notificationManager.notify(10, notification);
    }


    private void createNotificationChannel(String id, String name, int IMPORTANCE, long[] pattern, NotificationManager notificationManager) {

//        NotificationChannel必须在android8.0之后才可以使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(id, name, IMPORTANCE);

            channel.enableVibration(true);
            channel.setVibrationPattern(pattern);

            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), Notification.AUDIO_ATTRIBUTES_DEFAULT);

            channel.enableLights(true);
            channel.setLightColor(0x00FF00);
            notificationManager.createNotificationChannel(channel);
        }

    }
}
