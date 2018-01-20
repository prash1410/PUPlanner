package com.puchd.puplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEditHoliday extends AppCompatActivity
{
    NumberPicker daysPicker;
    HolidaysDatabase holidaysDatabase;
    Context context = this;
    TextView Title,Description,DateTextView,dateLabel;
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
        holidaysDatabase = new HolidaysDatabase(getApplicationContext());
        DateTextView = (TextView)findViewById(R.id.DateTextView);
        calendar = Calendar.getInstance();
        String CurrentDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime())+", "+Html.fromHtml(getFormattedDate());
        DateTextView.setText(CurrentDay);
        dateLabel = (TextView)findViewById(R.id.DateTV);
        CreateEditHolidayLayout = (RelativeLayout)findViewById(R.id.CreateEditHolidayLayout);
        daysPicker = (NumberPicker) findViewById(R.id.days_picker);
        DateLayout = (RelativeLayout)findViewById(R.id.DateLayout);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DateLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
                {
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
                                daysPicker.setValue(1);
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

        daysPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                if(newVal>1)
                {
                    updateDateUI();
                }
                if(newVal==1)
                {
                    restoreUI();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.donecancelbuttons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CreateDiscardDialog();
                break;
            case R.id.action_done:
                SaveHoliday(mDay,mMonth,mYear,Title.getText().toString(),Description.getText().toString());
                break;
            case R.id.action_cancel:
                CreateDiscardDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SaveHoliday(int Day, int Month, int Year, String Title, String Description)
    {
        if(holidaysDatabase.InsertHoliday(Day,Month,Year,Title,Description,1)) Toast.makeText(getApplicationContext(),"Successfully inserted",Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
    }

    public void CreateDiscardDialog()
    {
        String Message = "Do you wish to discard this entry?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getFormattedDate()
    {
        String dayNumberSuffix = getDayNumberSuffix(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM, YYYY");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getDayNumberSuffix(int day)
    {
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

    public void updateDateUI()
    {
        dateLabel.setText("Dates");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.MONTH, mMonth);
        cal.set(Calendar.YEAR, mYear);
        cal.add(Calendar.DATE, daysPicker.getValue()-1);
        Date date = new Date(mYear, mMonth, mDay-1);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        String Day = simpledateformat.format(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        String Day2 = dateFormat.format(cal.getTime());
        dateFormat = new SimpleDateFormat("dd");
        int mDay2 = Integer.valueOf(dateFormat.format(cal.getTime()));
        dateFormat = new SimpleDateFormat("MM");
        int mMonth2 = Integer.valueOf(dateFormat.format(cal.getTime()))-1/**/;
        dateFormat = new SimpleDateFormat("yyyy");
        int mYear2 = Integer.valueOf(dateFormat.format(cal.getTime()));
        DateTextView.setText(Day+", "+mDay + Html.fromHtml(getDayNumberSuffix(mDay))+" " + (new DateFormatSymbols().getMonths()[mMonth]) + ", " + mYear+"\nto\n"+Day2+", "+mDay2 + Html.fromHtml(getDayNumberSuffix(mDay2))+" " + (new DateFormatSymbols().getMonths()[mMonth2]) + ", " + mYear2);
    }

    public void restoreUI()
    {
        dateLabel.setText("Date");
        Date date = new Date(mYear, mMonth, mDay-1);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        String Day = simpledateformat.format(date);
        DateTextView.setText(Day+", "+mDay + Html.fromHtml(getDayNumberSuffix(mDay))+" " + (new DateFormatSymbols().getMonths()[mMonth]) + ", " + mYear);
    }
}
