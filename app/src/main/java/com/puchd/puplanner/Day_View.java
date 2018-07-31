package com.puchd.puplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Day_View extends AppCompatActivity
{
    Boolean CalledByMainActivity = false;
    Boolean CalledByImport = false;
    NewScheduleDatabase newScheduleDatabase;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int themeValue = ThemeSetter.getThemeID();
        if(themeValue == 1)setTheme(R.style.AppTheme_Dark_Actionbar);
        getTheme().applyStyle(AccentSetter.getStyleID(),true);
        getTheme().applyStyle(PrimaryColorSetter.getStyleID(),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Day View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(getIntent().getStringExtra("Caller")!=null)
                {
                    if(getIntent().getStringExtra("Caller").equals("MainActivity")) CalledByMainActivity=true;
                    if(getIntent().getStringExtra("Caller").equals("Import")) CalledByImport=true;
                }
                if(CalledByMainActivity)
                {
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    newScheduleDatabase = new NewScheduleDatabase(getApplicationContext());
                    newScheduleDatabase.DeleteLesson("DELETE FROM NewScheduleTable");
                    newScheduleDatabase.DeleteLesson("INSERT INTO NewScheduleTable SELECT * FROM "+sharedPreferences.getString("DefaultSchedule",""));
                }
                ViewPager viewPager = findViewById(R.id.dayviewpager);
                viewPager.setAdapter(new DayFragmentAdapter(getSupportFragmentManager(), Day_View.this));
                if(CalledByMainActivity)
                {
                    String weekDay;
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EE", Locale.US);
                    Calendar calendar = Calendar.getInstance();
                    weekDay = dayFormat.format(calendar.getTime());
                    int CurrentItem = 0;
                    if(weekDay.equals("Mon"))CurrentItem=0;
                    if(weekDay.equals("Tue"))CurrentItem=1;
                    if(weekDay.equals("Wed"))CurrentItem=2;
                    if(weekDay.equals("Thu"))CurrentItem=3;
                    if(weekDay.equals("Fri"))CurrentItem=4;
                    if(weekDay.equals("Sat"))CurrentItem=5;
                    if(weekDay.equals("Sun"))CurrentItem=6;
                    viewPager.setCurrentItem(CurrentItem);
                }
                TabLayout tabLayout = findViewById(R.id.sliding_days);
                tabLayout.setupWithViewPager(viewPager);
            }
        }, 100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(CalledByMainActivity)
                {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(),NewSchedule.class);
                    if(CalledByImport)intent.putExtra("Caller","Import");
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed()
    {
        if(CalledByMainActivity)
        {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(),NewSchedule.class);
            if(CalledByImport)intent.putExtra("Caller","Import");
            startActivity(intent);
            finish();
        }
    }
}
