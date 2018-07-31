package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AlarmsManager
{

    public AlarmsManager(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LastAlarmsCount", Context.MODE_PRIVATE);
        int lastAlarmsCount = sharedPreferences.getInt("Count",-1);
        if(lastAlarmsCount == -1) createAlarms(context);

        else
        {
            cancelAlarms(context);
            createAlarms(context);
        }
        createMidnightAlarm(context);
    }

    private void createAlarms(Context context)
    {
        NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(context);
        if(newScheduleDatabase.GetTableCount()>=1)
        {
            Calendar calendar1 = Calendar.getInstance();
            Date date = calendar1.getTime();
            String Day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            ArrayList<String> lessonData = newScheduleDatabase.AlarmsFeeder(sharedPreferences.getString("DefaultSchedule",""),Day);

            int alarmCounter = 0;
            Calendar CurrentTime = Calendar.getInstance();
            int CurrentHour = CurrentTime.get(Calendar.HOUR_OF_DAY);
            int CurrentMinute = CurrentTime.get(Calendar.MINUTE);

            for(String lesson:lessonData)
            {
                String NotificationData[] = lesson.split("_");
                String Subject = NotificationData[0];
                String StartingTime = NotificationData[1];
                int hour = getHour(StartingTime);
                int minute = getMinute(StartingTime);

                if(checktimings(CurrentHour+":"+CurrentMinute,hour+":"+minute))
                {
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

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND,0);

                    Intent intent = new Intent(context, PendingIntentReceiver.class);
                    intent.putExtra("Subject",Subject);
                    intent.putExtra("StartingTime",StartingTime);
                    intent.putExtra("EndingTime",EndingTime);
                    intent.putExtra("CellColor",CellColor);
                    intent.putExtra("BigText",BigText);

                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmCounter, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    alarmCounter++;
                }
                SharedPreferences numberOfAlarms = context.getSharedPreferences("LastAlarmsCount", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = numberOfAlarms.edit();
                editor.putInt("Count",alarmCounter);
                editor.apply();
            }
        }
    }

    private void cancelAlarms(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LastAlarmsCount", Context.MODE_PRIVATE);
        int numberOfAlarms = sharedPreferences.getInt("Count",-1);
        if(numberOfAlarms != -1)
        {
            for(int i = 0; i < numberOfAlarms; i++)
            {
                Intent intent = new Intent(context, PendingIntentReceiver.class);
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                Objects.requireNonNull(alarmManager).cancel(pendingIntent);
            }
        }
    }

    private void createMidnightAlarm(Context context)
    {
        Intent intent = new Intent(context, MidnightTasksReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1410, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        if(Calendar.getInstance().after(calendar)) calendar.add(Calendar.DATE, 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 *60 *24, pendingIntent);
    }

    private int getHour(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        if(timeDiv[1].equals("PM") && HourSplit[0].equals("12"))return 12;
        if(timeDiv[1].equals("PM"))return Integer.valueOf(HourSplit[0])+12;
        if(timeDiv[1].equals("AM") && HourSplit[0].equals("12"))return 0;
        return Integer.valueOf(HourSplit[0]);
    }

    private int getMinute(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        return Integer.valueOf(HourSplit[1]);
    }

    private boolean checktimings(String time, String endtime)
    {
        String pattern = "HH:mm";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try
        {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);
            return date1.before(date2);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
