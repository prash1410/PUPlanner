package com.puchd.puplanner;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Objects;

public class WidgetProvider_Day extends AppWidgetProvider
{
    public static final String WIDGET_IDS_KEY ="widgetprovider_daywidgetids";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        update(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.hasExtra(WIDGET_IDS_KEY))
        {
            int[] ids = Objects.requireNonNull(intent.getExtras()).getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        }
        else super.onReceive(context, intent);
    }

    public void update(Context context, AppWidgetManager manager, int[] ids) {

        for (int appWidgetId : ids)
        {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_day);
            Intent intent = new Intent(context, WidgetDayService.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            rv.setRemoteAdapter(R.id.WidgetDayList, intent);
            manager.updateAppWidget(appWidgetId, rv);
        }
    }

}
