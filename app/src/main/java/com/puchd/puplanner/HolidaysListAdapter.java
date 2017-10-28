package com.puchd.puplanner;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HolidaysListAdapter extends ArrayAdapter<Holidays>
{
    private ArrayList<Holidays>HolidayData;
    Context mContext;
    private static class ViewHolder
    {
        TextView Date,Title,Description;
        View Separator;
    }

    public HolidaysListAdapter(ArrayList<Holidays>HolidaysData, Context context)
    {
        super(context,R.layout.holiday_row,HolidaysData);
        this.HolidayData = HolidaysData;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holidays holidays = getItem(position);
        HolidaysListAdapter.ViewHolder viewHolder;
        View result;
        if (convertView==null)
        {
            viewHolder = new HolidaysListAdapter.ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.holiday_row,parent,false);
            viewHolder.Date = (TextView)convertView.findViewById(R.id.HolidayListDateTextView);
            viewHolder.Title = (TextView)convertView.findViewById(R.id.HolidaysListTitle);
            viewHolder.Description = (TextView)convertView.findViewById(R.id.HolidaysListDescription);
            viewHolder.Separator = convertView.findViewById(R.id.HolidaysListSeparator);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (HolidaysListAdapter.ViewHolder)convertView.getTag();
            result = convertView;
        }
        int Day=0,Month=0,Year=0;
        String Description = "",Title="";
        if (holidays != null)
        {
            Day = holidays.getintday();
            Month = holidays.getMonth();
            Year = holidays.getYear();
            Title = holidays.getTitle();
            if(!holidays.getDescription().isEmpty())Description=holidays.getDescription();
        }
        viewHolder.Title.setText(Title);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(Year, Month, Day-1);
        String day = simpledateformat.format(date);
        viewHolder.Date.setText(""+Day + Html.fromHtml(getDayNumberSuffix(Day))+" " + (new DateFormatSymbols().getMonths()[Month]) + ", " + Year+"\n"+day);
        if(Description.isEmpty())
        {
            viewHolder.Separator.setVisibility(View.GONE);
            viewHolder.Description.setVisibility(View.GONE);
        }
        else viewHolder.Description.setText(Description);
        return convertView;
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "<sup>th</sup>";
        }
        switch (day % 10) {
            case 1:
                return "<sup>st</sup>";
            case 2:
                return "<sup>nd</sup>";
            case 3:
                return "<sup>rd</sup>";
            default:
                return "<sup>th</sup>";
        }
    }
}
