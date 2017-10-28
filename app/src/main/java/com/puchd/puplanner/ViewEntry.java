package com.puchd.puplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class ViewEntry extends AppCompatActivity
{
    Boolean CalledByMainActivity = false;
    Boolean CalledByImport = false;
    String Abbreviation;
    Integer CellIndex;
    ArrayList<String>Data;
    TextView ColorView,SubjectView,AbbreviationView,DayView,StartingTimeView,EndingTimeView,TeacherView,TeacherLabel,VenueView,VenueLabel,TypeView,TypeLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewentry);
        getSupportActionBar().setTitle("Lesson");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Abbreviation = getIntent().getStringExtra("Abbreviation");
        CellIndex = getIntent().getIntExtra("CellIndex",0);
        if(getIntent().getStringExtra("Caller")!=null)
        {
            if(getIntent().getStringExtra("Caller").equals("MainActivity"))CalledByMainActivity=true;
            if(getIntent().getStringExtra("Caller").equals("Import"))CalledByImport=true;
        }
        ColorView = (TextView)findViewById(R.id.ColorView);
        SubjectView = (TextView)findViewById(R.id.SubjectView);
        AbbreviationView = (TextView)findViewById(R.id.AbbreviationView);
        DayView = (TextView)findViewById(R.id.DayView);
        StartingTimeView = (TextView)findViewById(R.id.StartingTimeView);
        EndingTimeView = (TextView)findViewById(R.id.EndingTimeView);
        TeacherView = (TextView)findViewById(R.id.TeacherView);
        TeacherLabel = (TextView)findViewById(R.id.TeacherLabel);
        VenueView = (TextView)findViewById(R.id.VenueView);
        VenueLabel = (TextView)findViewById(R.id.VenueLabel);
        TypeView = (TextView)findViewById(R.id.TypeView);
        TypeLabel = (TextView)findViewById(R.id.TypeLabel);

        NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(this);
        Data = newScheduleDatabase.FetchLesson(CellIndex,Abbreviation,"","");

        DayView.setText(Data.get(1));
        StartingTimeView.setText(Data.get(2));
        EndingTimeView.setText(Data.get(3));
        SubjectView.setText(Data.get(4));
        AbbreviationView.setText(Data.get(5));
        if(!Data.get(6).isEmpty())
        {
            TeacherLabel.setVisibility(View.VISIBLE);
            TeacherView.setText(Data.get(6));
            TeacherView.setVisibility(View.VISIBLE);
        }
        if(!Data.get(7).isEmpty())
        {
            VenueLabel.setVisibility(View.VISIBLE);
            VenueView.setText(Data.get(7));
            VenueView.setVisibility(View.VISIBLE);
        }
        if(!Data.get(8).isEmpty())
        {
            TypeLabel.setVisibility(View.VISIBLE);
            TypeView.setText(Data.get(8));
            TypeView.setVisibility(View.VISIBLE);
        }
        ColorView.setBackgroundColor(Integer.valueOf(Data.get(9)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deleteeditbuttons, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ViewEntry.this,NewSchedule.class);
                if(CalledByMainActivity)
                {
                    intent.putExtra("Caller","MainActivity");
                    intent.putExtra("Caller2","CreateEditEntry");
                }
                if(CalledByImport) intent.putExtra("Caller","Import");
                startActivity(intent);
                finish();
                break;
            case R.id.action_delete:
                NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(this);
                newScheduleDatabase.DeleteLesson("DELETE FROM NewScheduleTable WHERE CellIndex="+CellIndex+" AND Abbreviation='"+Abbreviation+"'");
                Intent i = new Intent(ViewEntry.this,NewSchedule.class);
                if(CalledByMainActivity)
                {
                    i.putExtra("Caller","MainActivity");
                    i.putExtra("Caller2","CreateEditEntry");
                }
                if(CalledByImport) i.putExtra("Caller","Import");
                startActivity(i);
                finish();
                break;
            case R.id.action_edit:
                Intent in = new Intent(ViewEntry.this,CreateEditEntry.class);
                Integer AbsoluteRowDecrement = 0;
                String Temp = DayView.getText().toString().substring(0,3);
                if(Temp.equals("Mon"))AbsoluteRowDecrement=1;
                if(Temp.equals("Tue"))AbsoluteRowDecrement=2;
                if(Temp.equals("Wed"))AbsoluteRowDecrement=3;
                if(Temp.equals("Thu"))AbsoluteRowDecrement=4;
                if(Temp.equals("Fri"))AbsoluteRowDecrement=5;
                if(Temp.equals("Sat"))AbsoluteRowDecrement=6;
                if(Temp.equals("Sun"))AbsoluteRowDecrement=7;
                in.putExtra("Day",DayView.getText().toString().substring(0,3));
                in.putExtra("Time",StartingTimeView.getText().toString());
                in.putExtra("EndingTime",EndingTimeView.getText().toString());
                in.putExtra("AbsoluteRow",CellIndex-AbsoluteRowDecrement);
                in.putExtra("CellIndex",CellIndex);
                in.putExtra("Caller","ViewEntry");
                if(CalledByMainActivity)in.putExtra("InitialCaller","MainActivity");
                if(CalledByImport)in.putExtra("InitialCaller","Import");
                startActivity(in);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ViewEntry.this,NewSchedule.class);
        if(CalledByMainActivity)
        {
            intent.putExtra("Caller","MainActivity");
            intent.putExtra("Caller2","CreateEditEntry");
        }
        if(CalledByImport) intent.putExtra("Caller","Import");
        startActivity(intent);
        finish();
    }
}
