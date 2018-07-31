package com.puchd.puplanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class DayLessonsCustomAdapter extends ArrayAdapter<DayLessons>
{
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

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        DayLessons dayLessons1 = getItem(position);
        DayLessonsCustomAdapter.ViewHolder viewHolder;
        final View result;
        if(convertView==null)
        {
            viewHolder = new DayLessonsCustomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.day_lesson_row,parent,false);
            viewHolder.DayColorView = convertView.findViewById(R.id.DayColorView);
            viewHolder.DaySubjectView = convertView.findViewById(R.id.DaySubjectView);
            viewHolder.DayAbbreviationView = convertView.findViewById(R.id.DayAbbreviationView);
            viewHolder.DayTimeView = convertView.findViewById(R.id.DayTimeView);
            viewHolder.DayTypeView = convertView.findViewById(R.id.DayTypeView);
            viewHolder.DayTeacherView = convertView.findViewById(R.id.DayTeacherView);
            viewHolder.DayVenueView = convertView.findViewById(R.id.DayVenueView);
            viewHolder.Container = convertView.findViewById(R.id.Container);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (DayLessonsCustomAdapter.ViewHolder)convertView.getTag();
            result = convertView;
        }
        viewHolder.DayColorView.setBackgroundColor(Objects.requireNonNull(dayLessons1).getLessonColor());
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
