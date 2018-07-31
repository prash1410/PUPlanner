package com.puchd.puplanner;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class DayFragment extends Fragment
{
    ListView DaySchedule;
    ArrayList<DayLessons> dayLessons = new ArrayList<>();
    ArrayList<Integer> LessonColor = new ArrayList<>();
    ArrayList<String> LessonSubject = new ArrayList<>();
    ArrayList<String> LessonAbbreviation = new ArrayList<>();
    ArrayList<String> LessonStartingTime = new ArrayList<>();
    ArrayList<String> LessonEndingTime = new ArrayList<>();
    ArrayList<String> LessonType = new ArrayList<>();
    ArrayList<String> LessonTeacher = new ArrayList<>();
    ArrayList<String> LessonVenue = new ArrayList<>();
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static DayFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPage = Objects.requireNonNull(getArguments()).getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(getActivity());
        String Day = "";
        if(mPage==1)Day = "Monday";
        if(mPage==2)Day = "Tuesday";
        if(mPage==3)Day = "Wednesday";
        if(mPage==4)Day = "Thursday";
        if(mPage==5)Day = "Friday";
        if(mPage==6)Day = "Saturday";
        if(mPage==7)Day = "Sunday";
        LessonColor = newScheduleDatabase.DayLessonsColor(Day);
        LessonSubject = newScheduleDatabase.DayLessons("Subject",Day);
        LessonAbbreviation = newScheduleDatabase.DayLessons("Abbreviation",Day);
        LessonStartingTime = newScheduleDatabase.DayLessons("StartingTime",Day);
        LessonEndingTime = newScheduleDatabase.DayLessons("EndingTime",Day);
        LessonType = newScheduleDatabase.DayLessons("Type",Day);
        LessonTeacher = newScheduleDatabase.DayLessons("Teacher",Day);
        LessonVenue = newScheduleDatabase.DayLessons("Venue",Day);

        dayLessons.clear();
        for(int i=0;i<LessonSubject.size();i++)
        {
            dayLessons.add(new DayLessons(LessonColor.get(i),LessonSubject.get(i),LessonAbbreviation.get(i),"Starts at "+LessonStartingTime.get(i)+" Ends at "+LessonEndingTime.get(i),LessonType.get(i),LessonTeacher.get(i),LessonVenue.get(i)));
        }
        DaySchedule = view.findViewById(R.id.DaySchedule);
        DayLessonsCustomAdapter dayLessonsCustomAdapter = new DayLessonsCustomAdapter(dayLessons,getActivity());
        DaySchedule.setAdapter(dayLessonsCustomAdapter);
        DaySchedule.setDividerHeight(0);
        return view;
    }
}
