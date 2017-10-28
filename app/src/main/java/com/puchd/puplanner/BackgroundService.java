package com.puchd.puplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class BackgroundService extends Service
{
    public Context context=this;
    public Handler handler = null;
    public Runnable runnable = null;
    public NewScheduleDatabase newScheduleDatabase;
    public AttendanceDatabase attendanceDatabase;
    public SharedPreferences sharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return Service.START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onCreate()
    {
        //MidnightTasks();
        newScheduleDatabase = new NewScheduleDatabase(context);
        if(newScheduleDatabase.GetTableCount()>=1)
        {
            attendanceDatabase = new AttendanceDatabase(context);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String Day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
            final ArrayList<String> StartingTime = newScheduleDatabase.NotificationFeeder(sharedPreferences.getString("DefaultSchedule",""),Day);
            if(!StartingTime.isEmpty())
            {
                final ArrayList<Integer> Notified = new ArrayList<>();
                for(int i=0;i<StartingTime.size();i++)
                {
                    Notified.add(0);
                }
                handler = new Handler();
                runnable = new Runnable()
                {
                    public void run()
                    {
                        Calendar CurrentTime = Calendar.getInstance();
                        int CurrentHour = CurrentTime.get(Calendar.HOUR_OF_DAY);
                        int CurrentMinute = CurrentTime.get(Calendar.MINUTE);
                        for(int i=0;i<StartingTime.size();i++)
                        {
                            int StartingHour = getHour(StartingTime.get(i));
                            int StartingMinute = getMinute(StartingTime.get(i));
                            if(CurrentHour == StartingHour && CurrentMinute == StartingMinute && Notified.get(i)==0)
                            {
                                Random random = new Random(10000);
                                Calendar NotificationTimer = Calendar.getInstance();
                                Intent NotificationIntent = new Intent(context,PendingIntentHandler.class);
                                NotificationIntent.putExtra("StartingTime",StartingTime.get(i));
                                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,random.nextInt(),NotificationIntent, PendingIntent.FLAG_ONE_SHOT);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,NotificationTimer.getTimeInMillis(),pendingIntent);
                                else alarmManager.setExact(AlarmManager.RTC_WAKEUP,NotificationTimer.getTimeInMillis(),pendingIntent);
                                Notified.set(i,1);
                            }
                        }
                        handler.postDelayed(runnable, 5000);
                    }
                };
                handler.postDelayed(runnable, 0);
            }
        }

    }
    @Override
    public void onDestroy()
    {
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
    }

    public void MidnightTasks()
    {
        Calendar NotificationTimer = Calendar.getInstance();
        //NotificationTimer.setTimeInMillis(System.currentTimeMillis());
        NotificationTimer.set(Calendar.HOUR_OF_DAY, 24);
        NotificationTimer.set(Calendar.MINUTE,0);
        NotificationTimer.set(Calendar.SECOND,0);
        Intent NotificationIntent = new Intent(context,PendingIntentHandler.class);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,NotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,NotificationTimer.getTimeInMillis(),pendingIntent);
        else alarmManager.setExact(AlarmManager.RTC_WAKEUP,NotificationTimer.getTimeInMillis(),pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public int getHour(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        if(timeDiv[1].equals("PM") && HourSplit[0].equals("12"))return 12;
        if(timeDiv[1].equals("PM"))return Integer.valueOf(HourSplit[0])+12;
        if(timeDiv[1].equals("AM") && HourSplit[0].equals("12"))return 0;
        return Integer.valueOf(HourSplit[0]);
    }

    public int getMinute(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        return Integer.valueOf(HourSplit[1]);
    }
}