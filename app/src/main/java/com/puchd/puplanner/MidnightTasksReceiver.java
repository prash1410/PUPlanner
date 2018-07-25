package com.puchd.puplanner;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MidnightTasksReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context,"Task executed",Toast.LENGTH_LONG).show();
        new AlarmsManager(context);
        widgetUpdater(context);
    }

    public void widgetUpdater(Context context)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider_Day.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.WidgetDayList);
    }
}
