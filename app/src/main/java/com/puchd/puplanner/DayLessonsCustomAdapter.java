package com.puchd.puplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DayLessonsCustomAdapter extends ArrayAdapter<DayLessons> {
    private ArrayList<DayLessons> dayLessons;
    Context mcontext;

    private static class ViewHolder
    {
        TextView DayColorView;
        TextView DaySubjectView;
        TextView DayAbbreviationView;
        TextView DayTimeView;
        TextView DayTypeView;
        TextView DayTeacherView;
        TextView DayVenueView;
        RelativeLayout Container;
    }

    public DayLessonsCustomAdapter(ArrayList<DayLessons>data, Context context)
    {
        super(context,R.layout.day_lesson_row,data);
        this.dayLessons = data;
        this.mcontext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        DayLessons dayLessons1 = getItem(position);
        DayLessonsCustomAdapter.ViewHolder viewHolder;
        final View result;
        if(convertView==null)
        {
            viewHolder = new DayLessonsCustomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.day_lesson_row,parent,false);
            viewHolder.DayColorView = (TextView)convertView.findViewById(R.id.DayColorView);
            viewHolder.DaySubjectView = (TextView)convertView.findViewById(R.id.DaySubjectView);
            viewHolder.DayAbbreviationView = (TextView)convertView.findViewById(R.id.DayAbbreviationView);
            viewHolder.DayTimeView = (TextView)convertView.findViewById(R.id.DayTimeView);
            viewHolder.DayTypeView = (TextView)convertView.findViewById(R.id.DayTypeView);
            viewHolder.DayTeacherView = (TextView)convertView.findViewById(R.id.DayTeacherView);
            viewHolder.DayVenueView = (TextView)convertView.findViewById(R.id.DayVenueView);
            viewHolder.Container = (RelativeLayout)convertView.findViewById(R.id.Container);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (DayLessonsCustomAdapter.ViewHolder)convertView.getTag();
            result = convertView;
        }
        viewHolder.DayColorView.setBackgroundColor(dayLessons1.getLessonColor());
        viewHolder.DaySubjectView.setText(dayLessons1.getLessonName());
        viewHolder.DayAbbreviationView.setText(dayLessons1.getLessonAbbreviation());
        viewHolder.DayTimeView.setText(dayLessons1.getLessonDayTime());

        if(!dayLessons1.getLessonType().isEmpty()) viewHolder.DayTypeView.setText(dayLessons1.getLessonType());
        else viewHolder.DayTypeView.setVisibility(View.GONE);

        if(!dayLessons1.getLessonTeacher().isEmpty())viewHolder.DayTeacherView.setText(dayLessons1.getLessonTeacher());
        else viewHolder.DayTeacherView.setVisibility(View.GONE);

        if (!dayLessons1.getLessonVenue().isEmpty())viewHolder.DayVenueView.setText(dayLessons1.getLessonVenue());
        else viewHolder.DayVenueView.setVisibility(View.GONE);

        if(dayLessons1.getLessonType().isEmpty() && dayLessons1.getLessonTeacher().isEmpty() && dayLessons1.getLessonVenue().isEmpty()) viewHolder.Container.setVisibility(View.GONE);

        return convertView;
    }
}
