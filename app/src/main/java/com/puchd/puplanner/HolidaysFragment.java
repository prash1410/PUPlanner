package com.puchd.puplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Holidays");
    }
}
