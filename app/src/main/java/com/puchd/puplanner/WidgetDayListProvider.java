package com.puchd.puplanner;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WidgetDayListProvider implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<String> SubjectList = new ArrayList<String>();
    ArrayList<String> StartingTimeList = new ArrayList<String>();
    ArrayList<String> EndingTimeList = new ArrayList<String>();
    ArrayList<String> TeacherList = new ArrayList<String>();
    ArrayList<String> VenueList = new ArrayList<String>();
    ArrayList<String> TypeList = new ArrayList<String>();
    ArrayList<Integer> ColorList = new ArrayList<Integer>();
    Context context;
    int appWidgetId;
    NewScheduleDatabase newScheduleDatabase;
    SharedPreferences sharedPreferences;

    public WidgetDayListProvider(Context context, Intent intent)
    {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        newScheduleDatabase = new NewScheduleDatabase(this.context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        populateListItem();
    }

    private void populateListItem()
    {
        SubjectList.clear();
        StartingTimeList.clear();
        EndingTimeList.clear();
        ColorList.clear();
        TeacherList.clear();
        VenueList.clear();
        TypeList.clear();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String Day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_day);
        ComponentName thisWidget = new ComponentName(context, WidgetProvider_Day.class);
        remoteViews.setTextViewText(R.id.WidgetDayView, Day);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        for(String TempRecord:newScheduleDatabase.DayWidgetFeeder(sharedPreferences.getString("DefaultSchedule",""),Day))
        {
            String Temp[] = TempRecord.split("_");
            SubjectList.add(Temp[0]);
            StartingTimeList.add(Temp[1]);
            EndingTimeList.add(Temp[2]);
            ColorList.add(Integer.valueOf(Temp[3]));
            if(Temp.length>4)
            {
                TeacherList.add(Temp[4]);
                VenueList.add(Temp[5]);
                TypeList.add(Temp[6]);
            }
            else
            {
                TeacherList.add("");
                VenueList.add("");
                TypeList.add("");
            }
        }
    }


    @Override
    public void onCreate()
    {
        populateListItem();
    }

    @Override
    public void onDataSetChanged()
    {
        populateListItem();
    }

    @Override
    public void onDestroy()
    {
        SubjectList.clear();
        StartingTimeList.clear();
        EndingTimeList.clear();
        ColorList.clear();
        TeacherList.clear();
        VenueList.clear();
        TypeList.clear();
        newScheduleDatabase.close();
    }

    @Override
    public int getCount() {
        return SubjectList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_day_row);
        remoteView.setTextViewText(R.id.WidgetDayRowSubject,SubjectList.get(position));
        remoteView.setTextViewText(R.id.WidgetDayStartingTimings,StartingTimeList.get(position));
        remoteView.setTextViewText(R.id.WidgetDayEndingTimings,EndingTimeList.get(position));
        String Extras = "";
        if(!TeacherList.get(position).isEmpty())
        {
            Extras += TeacherList.get(position);
            if(!VenueList.get(position).isEmpty())
            {
                Extras += ", "+VenueList.get(position);
                if(!TypeList.get(position).isEmpty())Extras += ", "+TypeList.get(position);
            }
        }
        if(TeacherList.get(position).isEmpty() && !VenueList.get(position).isEmpty())
        {
            Extras += VenueList.get(position);
            if(!TypeList.get(position).isEmpty())Extras += ", "+TypeList.get(position);
        }
        if(TeacherList.get(position).isEmpty() && VenueList.get(position).isEmpty() && !TypeList.get(position).isEmpty())Extras += TypeList.get(position);
        remoteView.setTextViewText(R.id.WidgetDayExtras,Extras);
        if(TeacherList.get(position).isEmpty() && VenueList.get(position).isEmpty() && TypeList.get(position).isEmpty())remoteView.setViewVisibility(R.id.WidgetDayExtras, View.GONE);
        remoteView.setInt(R.id.WidgetDayRowColor, "setBackgroundColor", ColorList.get(position));
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }
}
