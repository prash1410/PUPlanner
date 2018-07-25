package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Objects;

public class DayTimeListAdapter extends BaseExpandableListAdapter
{
    private Context context;
    private ArrayList<DayInfo> dayList;
    public DayTimeListAdapter(Context context,ArrayList<DayInfo>dayList)
    {
        this.context = context;
        this.dayList = dayList;
    }
    @Override
    public int getGroupCount()
    {
        return dayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<TimeInfo> timeList = dayList.get(groupPosition).gettimeList();
        return timeList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        ArrayList<TimeInfo> timeList = dayList.get(groupPosition).gettimeList();
        return timeList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        DayInfo dayInfo = (DayInfo)getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.day_row,null);
        }
        final TextView dayName = convertView.findViewById(R.id.DayName);
        dayName.setText(dayInfo.getDayName());
        ImageButton CreateNewLesson = convertView.findViewById(R.id.CreateNewLesson);
        CreateNewLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TimeInfo timeInfo = new TimeInfo();
                timeInfo.setStartingTime("8:00 AM");
                timeInfo.setEndingTime("9:00 AM");
                dayList.get(groupPosition).gettimeList().add(timeInfo);
                DayTimeListAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final TimeInfo timeInfo = (TimeInfo)getChild(groupPosition,childPosition);
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.time_row,null);
        }
        final TextView startingTime = convertView.findViewById(R.id.StartingTime);
        startingTime.setText(timeInfo.getStartingTime());
        startingTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final TimePickerDialog StartingTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = " AM";
                        } else {
                            if(hourOfDay>12)hourOfDay = hourOfDay-12;
                            AM_PM = " PM";
                        }
                        if(hourOfDay<1)hourOfDay=12;
                        String adjustedMinute = ":";
                        if(minute<10) adjustedMinute+="0";
                        timeInfo.setStartingTime(hourOfDay + adjustedMinute + minute+AM_PM);
                        notifyDataSetChanged();
                    }
                },getHour(startingTime.getText().toString()),getMinute(startingTime.getText().toString()),false);
                StartingTimePicker.show();
            }
        });
        final TextView endingTime = convertView.findViewById(R.id.EndingTime);
        endingTime.setText(timeInfo.getEndingTime());
        endingTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final TimePickerDialog EndingTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        String AM_PM;
                        if(hourOfDay < 12) AM_PM = " AM";
                        else
                        {
                            if(hourOfDay>12)hourOfDay = hourOfDay-12;
                            AM_PM = " PM";
                        }
                        if(hourOfDay<1)hourOfDay=12;
                        String adjustedMinute = ":";
                        if(minute<10) adjustedMinute+="0";
                        timeInfo.setEndingTime(hourOfDay + adjustedMinute + minute+AM_PM);
                        notifyDataSetChanged();
                    }
                },getHour(endingTime.getText().toString()),getMinute(endingTime.getText().toString()),false);
                EndingTimePicker.show();
            }
        });
        TextView RemoveLesson = convertView.findViewById(R.id.DeleteLesson);
        RemoveLesson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayList.get(groupPosition).gettimeList().remove(childPosition);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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
