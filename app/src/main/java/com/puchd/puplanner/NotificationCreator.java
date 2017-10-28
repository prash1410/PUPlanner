package com.puchd.puplanner;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.WakefulBroadcastReceiver;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class NotificationCreator extends IntentService
{
    private static final String name = "com.puchd.puplanner.NotificationCreator";
    public NotificationCreator()
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if(intent!=null)
        {
            String StartingTime = intent.getStringExtra("StartingTime");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(this);
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String Day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
            String NotificationData[] = newScheduleDatabase.FinalNotificationFeeder(sharedPreferences.getString("DefaultSchedule",""),Day,StartingTime).split("_");
            String Subject = NotificationData[0];
            String EndingTime = NotificationData[2];
            Integer CellColor = Integer.valueOf(NotificationData[7]);
            String Teacher = "";
            String Type = "";
            String Venue = "";
            if(!NotificationData[4].isEmpty())Teacher = NotificationData[4];
            if(!NotificationData[3].isEmpty())Type = NotificationData[3];
            if(!NotificationData[5].isEmpty())Venue = NotificationData[5];

            String BigText = "";
            if(!Teacher.isEmpty())BigText += "\n"+Teacher;
            if(!Venue.isEmpty())BigText += "\n"+Venue;
            if(!Type.isEmpty())BigText += "\n"+Type;


            Random randomGenerator = new Random();
            Random IDGenerator = new Random();
            int ID = IDGenerator.nextInt(10000);

            Intent NotificationReceiverIntent = new Intent(this,NotificationReceiver.class);
            NotificationReceiverIntent.putExtra("Action","DoNothing");
            NotificationReceiverIntent.putExtra("Subject",Subject);
            NotificationReceiverIntent.putExtra("ID",ID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,randomGenerator.nextInt(10000),intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Intent AttendIntent = new Intent(this,NotificationReceiver.class);
            AttendIntent.putExtra("Action","Attend");
            AttendIntent.putExtra("Subject",Subject);
            AttendIntent.putExtra("ID",ID);
            PendingIntent AttendPendingIntent = PendingIntent.getBroadcast(this,randomGenerator.nextInt(10000),AttendIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            Intent BunkIntent = new Intent(this,NotificationReceiver.class);
            BunkIntent.putExtra("Action","Bunk");
            BunkIntent.putExtra("Subject",Subject);
            BunkIntent.putExtra("ID",ID);
            PendingIntent BunkPendingIntent = PendingIntent.getBroadcast(this,randomGenerator.nextInt(10000),BunkIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            Intent NoClassIntent = new Intent(this,NotificationReceiver.class);
            NoClassIntent.putExtra("Action","NoClass");
            NoClassIntent.putExtra("Subject",Subject);
            NoClassIntent.putExtra("ID",ID);
            PendingIntent NoClassPendingIntent = PendingIntent.getBroadcast(this,randomGenerator.nextInt(10000),NoClassIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Action AttendAction = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.attend),"Attend",AttendPendingIntent).build();
            Notification.Action BunkAction = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.bunk),"Bunk",BunkPendingIntent).build();
            Notification.Action NoClassAction = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.noclass),"No class",NoClassPendingIntent).build();
            Notification n  = new Notification.Builder(this)
                    .setContentTitle(Subject)
                    .setSmallIcon(R.drawable.notificationicon)
                    .setColor(CellColor)
                    .setWhen(System.currentTimeMillis())
                    .setLights(CellColor,1500,2000)
                    .setShowWhen(true)
                    .setContentText(StartingTime+" - "+EndingTime)
                    .setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(StartingTime+" to "+EndingTime+BigText))
                    .addAction(AttendAction)
                    .addAction(BunkAction)
                    .addAction(NoClassAction)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(false).build();
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            n.defaults |= Notification.DEFAULT_SOUND;
            n.defaults |= Notification.DEFAULT_VIBRATE;
            //n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(ID, n);
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }
}
