package com.puchd.puplanner;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        AttendanceDatabase attendanceDatabase = new AttendanceDatabase(context);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String Action = intent.getStringExtra("Action");
        String Subject = intent.getStringExtra("Subject");
        Integer ID = intent.getIntExtra("ID",0);
        if(Action.equals("Attend"))
        {
            attendanceDatabase.ExecuteRandomQuery("UPDATE "+sharedPreferences.getString("DefaultSchedule","")+" SET TotalLectures=TotalLectures+1 WHERE Subject='"+Subject+"'");
            attendanceDatabase.ExecuteRandomQuery("UPDATE "+sharedPreferences.getString("DefaultSchedule","")+" SET Attended=Attended+1 WHERE Subject='"+Subject+"'");
        }
        if(Action.equals("Bunk"))
        {
            attendanceDatabase.ExecuteRandomQuery("UPDATE "+sharedPreferences.getString("DefaultSchedule","")+" SET TotalLectures=TotalLectures+1 WHERE Subject='"+Subject+"'");
        }

        if(Action.equals("DoNothing"))
        {
            Toast.makeText(context,"Please pick an option",Toast.LENGTH_SHORT).show();
        }
        if(!Action.equals("DoNothing"))
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Objects.requireNonNull(notificationManager).cancel(ID);
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }
    }
}
