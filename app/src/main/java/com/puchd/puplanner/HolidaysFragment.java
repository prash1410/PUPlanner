package com.puchd.puplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HolidaysFragment extends android.support.v4.app.Fragment
{
    HolidaysDatabase holidaysDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        holidaysDatabase = new HolidaysDatabase(getActivity());
        View view = inflater.inflate(R.layout.fragment_holidays, container, false);

        if(holidaysDatabase.NoHolidaysAdded())
        {
            TextView NoHolidaysTextView = (TextView)view.findViewById(R.id.NoHolidaysTextView);
            NoHolidaysTextView.setVisibility(View.VISIBLE);
            NoHolidaysTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startActivity(new Intent(getActivity(),CreateEditHoliday.class));
                }
            });
        }
        else
        {
            ArrayList<Holidays> Holidays = new ArrayList<>();
            for(String HolidayData:holidaysDatabase.FetchHolidays())
            {
                String TempHolidayData[] = HolidayData.split("_");
                String Description = "";
                try {
                    Description = TempHolidayData[4];
                }catch(ArrayIndexOutOfBoundsException exception){}
                Holidays.add(new Holidays(Integer.valueOf(TempHolidayData[0]),Integer.valueOf(TempHolidayData[1]),Integer.valueOf(TempHolidayData[2]),TempHolidayData[3],Description));
            }
            ListView HolidaysList = (ListView)view.findViewById(R.id.HolidaysList);
            HolidaysListAdapter holidaysListAdapter = new HolidaysListAdapter(Holidays,getActivity());
            HolidaysList.setAdapter(holidaysListAdapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Holidays");
    }
}
