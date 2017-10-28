package com.puchd.puplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class PendingIntentHandler extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        if(intent.getStringExtra("StartingTime")!=null)
        {
            String StartingTime = intent.getStringExtra("StartingTime");
            Intent wakefulServiceIntent = new Intent(context, NotificationCreator.class);
            wakefulServiceIntent.putExtra("StartingTime",StartingTime);
            WakefulBroadcastReceiver.startWakefulService(context,wakefulServiceIntent);
        }
        else
        {
            Toast.makeText(context,"MidnightTasks",Toast.LENGTH_SHORT).show();
            context.startService(new Intent(context,BackgroundService.class));
        }
    }
}

