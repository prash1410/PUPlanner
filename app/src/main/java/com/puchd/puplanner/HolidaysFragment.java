package com.puchd.puplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class HolidaysFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{
    FloatingActionButton fabAddHoliday;
    HolidaysDatabase holidaysDatabase;
    Button NoHolidaysButton;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        holidaysDatabase = new HolidaysDatabase(getActivity());
        View view = inflater.inflate(R.layout.fragment_holidays, container, false);
        NoHolidaysButton = view.findViewById(R.id.NoHolidaysButton);
        NoHolidaysButton.setOnClickListener(this);
        fabAddHoliday = view.findViewById(R.id.fabAddHoliday);
        fabAddHoliday.setOnClickListener(this);
        if(holidaysDatabase.NoHolidaysAdded())
        {
            NoHolidaysButton.setVisibility(View.VISIBLE);
            fabAddHoliday.hide(false);
        }
        else
        {
            NoHolidaysButton.setVisibility(View.GONE);
            fabAddHoliday.show(true);
            ArrayList<Holidays> Holidays = new ArrayList<>();
            for(String HolidayData:holidaysDatabase.FetchHolidays())
            {
                String TempHolidayData[] = HolidayData.split("_");
                String Description = "";
                try {
                    Description = TempHolidayData[5];
                }catch(ArrayIndexOutOfBoundsException exception)
                {
                    System.out.print("");
                }
                Holidays.add(new Holidays(Integer.valueOf(TempHolidayData[0]),Integer.valueOf(TempHolidayData[1]),Integer.valueOf(TempHolidayData[2]),TempHolidayData[3],Integer.valueOf(TempHolidayData[4]),Description));
            }
            ListView HolidaysList = view.findViewById(R.id.HolidaysList);
            HolidaysListAdapter holidaysListAdapter = new HolidaysListAdapter(Holidays,getActivity());
            HolidaysList.setAdapter(holidaysListAdapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Holidays");
    }

    @Override
    public void onClick(View v)
    {
        int ID = v.getId();
        switch (ID)
        {
            case R.id.NoHolidaysButton:
                startActivityForResult(new Intent(getActivity(),CreateEditHoliday.class),1);
                break;
            case R.id.fabAddHoliday:
                startActivityForResult(new Intent(getActivity(),CreateEditHoliday.class),1);
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                ft.detach(this).attach(this).commit();
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                //Write your code if there's no result
            }
        }
    }
}
