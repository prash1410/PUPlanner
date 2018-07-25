package com.puchd.puplanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import java.util.Objects;
import java.util.Random;

public class PendingIntentReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationLightColor = intent.getIntExtra("CellColor",0);
        createNotificationChannel(notificationManager, notificationLightColor);
        createNotification(context, notificationManager, intent);
    }

    public void createNotificationChannel(NotificationManager notificationManager, int notificationLightColor)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelID = "PUPlanner";
            CharSequence channelName = "PUPlanner notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, importance);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(notificationLightColor);
            notificationChannel.setShowBadge(true);
            notificationChannel.setDescription("Notification channel for PUPlanner App which delivers notifications for your daily events");
            Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);
        }
    }

    public void createNotification(Context context, NotificationManager notificationManager, Intent passedIntent)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // To create notification on devices running Android O and above
        {
            Random randomGenerator = new Random();
            Random IDGenerator = new Random();
            int ID = IDGenerator.nextInt(10000);

            String Subject = passedIntent.getStringExtra("Subject");
            int CellColor = passedIntent.getIntExtra("CellColor",0);
            String StartingTime = passedIntent.getStringExtra("StartingTime");
            String EndingTime = passedIntent.getStringExtra("EndingTime");
            String BigText = passedIntent.getStringExtra("BigText");

            Intent NotificationReceiverIntent = new Intent(context,NotificationReceiver.class);
            NotificationReceiverIntent.putExtra("Action","DoNothing");
            NotificationReceiverIntent.putExtra("Subject",Subject);
            NotificationReceiverIntent.putExtra("ID",ID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,randomGenerator.nextInt(10000),passedIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            Intent AttendIntent = new Intent(context,NotificationReceiver.class);
            AttendIntent.putExtra("Action","Attend");
            AttendIntent.putExtra("Subject",Subject);
            AttendIntent.putExtra("ID",ID);
            PendingIntent AttendPendingIntent = PendingIntent.getBroadcast(context,randomGenerator.nextInt(10000),AttendIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            Intent BunkIntent = new Intent(context,NotificationReceiver.class);
            BunkIntent.putExtra("Action","Bunk");
            BunkIntent.putExtra("Subject",Subject);
            BunkIntent.putExtra("ID",ID);
            PendingIntent BunkPendingIntent = PendingIntent.getBroadcast(context,randomGenerator.nextInt(10000),BunkIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            Intent NoClassIntent = new Intent(context,NotificationReceiver.class);
            NoClassIntent.putExtra("Action","NoClass");
            NoClassIntent.putExtra("Subject",Subject);
            NoClassIntent.putExtra("ID",ID);
            PendingIntent NoClassPendingIntent = PendingIntent.getBroadcast(context,randomGenerator.nextInt(10000),NoClassIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Action AttendAction = new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.attend),"Attend",AttendPendingIntent).build();
            Notification.Action BunkAction = new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.bunk),"Bunk",BunkPendingIntent).build();
            Notification.Action NoClassAction = new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.noclass),"No class",NoClassPendingIntent).build();
            Notification notification  = new Notification.Builder(context, "PUPlanner")
                    .setTicker("PUPlanner - " + Subject)
                    .setContentTitle(Subject)
                    .setSmallIcon(R.drawable.notificationicon)
                    .setColor(CellColor)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setContentText(StartingTime+" - "+EndingTime)
                    .setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle().bigText(StartingTime+" to "+EndingTime+BigText))
                    .addAction(AttendAction)
                    .addAction(BunkAction)
                    .addAction(NoClassAction)
                    .setAutoCancel(false).build();
            notificationManager.notify(ID, notification);
        }
    }
}
