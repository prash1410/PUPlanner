package com.puchd.puplanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class HolidaysRecyclerViewAdapter extends RecyclerView.Adapter<HolidaysRecyclerViewAdapter.MyViewHolder>
{
    private Context context;
    private List<Holidays> holidaysList;

    public HolidaysRecyclerViewAdapter(Context context, List<Holidays> holidaysList)
    {
        this.context = context;
        this.holidaysList = holidaysList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView Date,Title,Description,NumDays;
        RelativeLayout viewBackground, viewForeground;
        View Separator;

        public MyViewHolder(View view)
        {
            super(view);
            Date = view.findViewById(R.id.HolidayListDateTextView);
            Description = view.findViewById(R.id.HolidaysListDescription);
            NumDays = view.findViewById(R.id.numDays);
            Separator = view.findViewById(R.id.HolidaysListSeparator);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }
}
