package com.puchd.puplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEditHoliday extends AppCompatActivity
{
    Context context = this;
    TextView Title,Description,DateTextView;
    View view_title,title_view;
    Animation anim,anim2;
    Calendar calendar;
    int mYear,mMonth,mDay;
    RelativeLayout CreateEditHolidayLayout,DateLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_holiday);
        DateTextView = (TextView)findViewById(R.id.DateTextView);
        calendar = Calendar.getInstance();
        String CurrentDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime())+", "+Html.fromHtml(getFormattedDate());
        DateTextView.setText(CurrentDay);
        CreateEditHolidayLayout = (RelativeLayout)findViewById(R.id.CreateEditHolidayLayout);
        DateLayout = (RelativeLayout)findViewById(R.id.DateLayout);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DateLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                                String Day = simpledateformat.format(date);
                                DateTextView.setText(Day+", "+dayOfMonth + Html.fromHtml(getDayNumberSuffix(dayOfMonth))+" " + (new DateFormatSymbols().getMonths()[monthOfYear]) + ", " + year);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        CreateEditHolidayLayout.requestFocus();
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_disappear);

        title_view = findViewById(R.id.view_title);
        view_title = findViewById(R.id.second_view_title);

        Title = (EditText) findViewById(R.id.Title);
        Description = (EditText) findViewById(R.id.Description);

        Title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_title.setVisibility(View.VISIBLE);
                    view_title.startAnimation(anim);
                } else {
                    view_title.startAnimation(anim2);
                }
            }
        });


    }

    private String getFormattedDate()
    {
        String dayNumberSuffix = getDayNumberSuffix(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM, YYYY");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "<sup>th</sup>";
        }
        switch (day % 10) {
            case 1:
                return "<sup>st</sup>";
            case 2:
                return "<sup>nd</sup>";
            case 3:
                return "<sup>rd</sup>";
            default:
                return "<sup>th</sup>";
        }
    }
}
