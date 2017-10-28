package com.puchd.puplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class DataBaseErrorCustomAdapter extends ArrayAdapter<DatabaseError> {

    private ArrayList<DatabaseError> databaseErrors;
    Context mcontext;

    private static class ViewHolder
    {
        TextView ErrorColorView;
        TextView ErrorSubjectView;
        TextView ErrorAbbreviationView;
        TextView ErrorDayTimeView;
    }

    public DataBaseErrorCustomAdapter(ArrayList<DatabaseError>data, Context context)
    {
        super(context,R.layout.database_error_row_item,data);
        this.databaseErrors = data;
        this.mcontext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        DatabaseError databaseError = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if(convertView==null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.database_error_row_item,parent,false);
            viewHolder.ErrorColorView = (TextView)convertView.findViewById(R.id.ErrorColorView);
            viewHolder.ErrorSubjectView = (TextView)convertView.findViewById(R.id.ErrorSubjectView);
            viewHolder.ErrorAbbreviationView = (TextView)convertView.findViewById(R.id.ErrorAbbreviationView);
            viewHolder.ErrorDayTimeView = (TextView)convertView.findViewById(R.id.ErrorDayTimeView);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
            result = convertView;
        }
        viewHolder.ErrorColorView.setBackgroundColor(databaseError.getLessonColor());
        viewHolder.ErrorSubjectView.setText(databaseError.getLessonName());
        viewHolder.ErrorAbbreviationView.setText(databaseError.getLessonAbbreviation());
        viewHolder.ErrorDayTimeView.setText(databaseError.getLessonDayTime());
        return convertView;
    }

}
