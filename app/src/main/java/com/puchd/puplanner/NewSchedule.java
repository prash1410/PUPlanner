package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NewSchedule extends AppCompatActivity implements View.OnClickListener
{
    TypedValue gridColor, clickHighlightColor;
    AttendanceDatabase attendanceDatabase;
    Calendar calendar;
    Boolean CalledByMainActivity = false;
    Boolean CalledByImport = false;
    int Width;
    int Height;
    RelativeLayout relativeLayout;
    TextView[] Cells = new TextView[112];
    String[] data = new String[112];
    int CellCounter = 0;
    int LastClickID = 0;
    NewScheduleDatabase newScheduleDatabase;
    Integer[] Colors = new Integer[112];
    String[] ScheduleData = new String[112];
    TextView[] Lessons = new TextView[112];
    Integer[] YOffset = new Integer[112];
    Integer[] Duration = new Integer[112];

    ArrayList<Integer>MultipleLessons = new ArrayList<>();
    Integer ExtraCounter=0;
    Integer[] SpareColors = new Integer[112];
    String[] SpareScheduleData = new String[112];
    TextView[] SpareLessons = new TextView[112];
    Integer[] SpareYOffset = new Integer[112];
    Integer[] SpareDuration = new Integer[112];

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int themeValue = ThemeSetter.getThemeID();
        if(themeValue == 1)setTheme(R.style.AppTheme_Dark_Actionbar);
        getTheme().applyStyle(AccentSetter.getStyleID(),true);
        getTheme().applyStyle(PrimaryColorSetter.getStyleID(),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newschedule);

        gridColor = new TypedValue();
        getTheme().resolveAttribute(R.attr.gridColor, gridColor, true);

        clickHighlightColor = new TypedValue();
        getTheme().resolveAttribute(R.attr.clickHighlightColor, clickHighlightColor, true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        newScheduleDatabase = new NewScheduleDatabase(this);
        attendanceDatabase = new AttendanceDatabase(this);
        if(getIntent().getStringExtra("Caller")==null) Objects.requireNonNull(getSupportActionBar()).setTitle("Create a new schedule");
        if(getIntent().getStringExtra("Caller")!=null)
        {
            if(getIntent().getStringExtra("Caller").equals("NewSchedule"))
            {
                getSupportActionBar().setTitle("Create a new schedule");
                newScheduleDatabase.DeleteLesson("DELETE FROM NewScheduleTable");
            }
            if(getIntent().getStringExtra("Caller").equals("MainActivity"))
            {
                CalledByMainActivity=true;
                getSupportActionBar().setTitle(sharedPreferences.getString("DefaultSchedule","")+" - Week View");
                if(getIntent().getStringExtra("Caller2")==null)
                {
                    newScheduleDatabase.DeleteLesson("DELETE FROM NewScheduleTable");
                    newScheduleDatabase.DeleteLesson("INSERT INTO NewScheduleTable SELECT * FROM "+sharedPreferences.getString("DefaultSchedule",""));
                }
            }
            if(getIntent().getStringExtra("Caller").equals("Import"))
            {
                CalledByImport=true;
                getSupportActionBar().setTitle("Import schedule");
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        Width = size.x;
        Height = size.y;
        data = new String[]{"","Mon","Tue","Wed","Thu","Fri","Sat","Sun", "8 AM","","","","","","","", "9 AM","","","","","","","", "10 AM","","","","","","","", "11 AM","","","","","","","", "12 PM","","","","","","","", "1 PM","","","","","","","", "2 PM","","","","","","","", "3 PM","","","","","","","", "4 PM","","","","","","","", "5 PM","","","","","","","", "6 PM","","","","","","","", "7 PM","","","","","","","", "8 PM","","","","","","","",};
        ScheduleData = new String[]{"","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","","","","","","","","","","","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","",};
        SpareScheduleData = new String[]{"","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","","","","","","","","","","","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","", "","","","","","","","",};
        relativeLayout = findViewById(R.id.ScheduleDataContainer);
        int LeftMargin=0;
        int TopMargin=0;

        for(int i=1;i<=14;i++)
        {
            for(;CellCounter<8*i;CellCounter++)
            {
                Cells[CellCounter] = new TextView(getApplicationContext());
                Cells[CellCounter].setBackgroundColor(gridColor.data);
                Cells[CellCounter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                Cells[CellCounter].setTextColor(Color.parseColor("#FFFFFFFF"));
                Cells[CellCounter].setGravity(Gravity.CENTER);
                Cells[CellCounter].setPadding(0,0,0,0);
                Cells[CellCounter].setWidth((Width/8)-2);
                Cells[CellCounter].setHeight(126);
                Cells[CellCounter].setX(LeftMargin);
                Cells[CellCounter].setY(TopMargin);
                Cells[CellCounter].setText(data[CellCounter]);
                LeftMargin = LeftMargin + (Width/8);
                int position = CellCounter;
                if(position == 8 | position == 16 | position == 24 | position == 32 | position == 40 | position == 48 | position == 56 | position == 64 | position == 72 | position == 80 | position == 88 | position == 96 | position == 104) Cells[CellCounter].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                Cells[CellCounter].setId(CellCounter);
                relativeLayout.addView(Cells[CellCounter]);
                if(position == 8 | position == 16 | position == 24 | position == 32 | position == 40 | position == 48 | position == 56 | position == 64 | position == 72 | position == 80 | position == 88 | position == 96 | position == 104 | position == 0 | position == 1 | position == 2 | position == 3 | position == 4 | position == 5 | position == 6 | position == 7){}
                else Cells[CellCounter].setOnClickListener(this);
            }
            LeftMargin = 0;
            TopMargin = TopMargin+126+2;
        }
        UpdateSchedule();
        for(int i=9;i<112;i++)
        {
            if(!ScheduleData[i].isEmpty())
            {
                Lessons[i] = new TextView(this);
                Lessons[i].setBackgroundColor(Colors[i]);
                Lessons[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                Lessons[i].setTextColor(Color.parseColor("#FFFFFFFF"));
                Lessons[i].setGravity(Gravity.CENTER);
                Lessons[i].setPadding(0,0,0,0);
                Lessons[i].setWidth((Width/8)-2);
                Lessons[i].setHeight(Duration[i]);
                Lessons[i].setX(Cells[i].getX());
                Lessons[i].setY(Cells[i].getY()+YOffset[i]);
                Lessons[i].setText(ScheduleData[i]);
                int ID = 103+i;
                Lessons[i].setId(ID);
                relativeLayout.addView(Lessons[i]);
                Lessons[i].setOnClickListener(this);
            }
        }
        if(!MultipleLessons.isEmpty())
        {
            int Key=0;
            for(int Value:MultipleLessons)
            {
                SpareLessons[Key] = new TextView(this);
                SpareLessons[Key].setBackgroundColor(SpareColors[Key]);
                SpareLessons[Key].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                SpareLessons[Key].setTextColor(Color.parseColor("#FFFFFFFF"));
                SpareLessons[Key].setGravity(Gravity.CENTER);
                SpareLessons[Key].setPadding(0,0,0,0);
                SpareLessons[Key].setWidth((Width/8)-2);
                SpareLessons[Key].setHeight(SpareDuration[Key]);
                SpareLessons[Key].setX(Cells[Value].getX());
                SpareLessons[Key].setY(Cells[Value].getY()+SpareYOffset[Key]);
                SpareLessons[Key].setText(SpareScheduleData[Key]);
                int ID = 500+Value;
                SpareLessons[Key].setId(ID);
                relativeLayout.addView(SpareLessons[Key]);
                SpareLessons[Key].setOnClickListener(this);
                Key++;
            }
        }
    }

    public void UpdateSchedule()
    {
        ArrayList<String> Rows;
        Rows = newScheduleDatabase.getAllRows();
        for(String SingleRow:Rows)
        {
            String Temp[] = SingleRow.split("_");
            if(ScheduleData[Integer.valueOf(Temp[0])].isEmpty())
            {
                ScheduleData[Integer.valueOf(Temp[0])] = Temp[1];
                Colors[Integer.valueOf(Temp[0])] = Integer.valueOf(Temp[2]);
                YOffset[Integer.valueOf(Temp[0])] = getMinute(Temp[3])*2;
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                Date date=null,date1 = null;
                try {
                    date = simpleDateFormat.parse(""+getHour(Temp[3])+":"+getMinute(Temp[3]));
                    date1 = simpleDateFormat.parse(""+getHour(Temp[4])+":"+getMinute(Temp[4]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long TempDuration = (Objects.requireNonNull(date1).getTime() - date.getTime())/60000;
                Duration[Integer.valueOf(Temp[0])] = (int)(TempDuration*2 + TempDuration*0.13);
            }
            else
            {
                MultipleLessons.add(Integer.valueOf(Temp[0]));
                SpareScheduleData[ExtraCounter] = Temp[1];
                SpareColors[ExtraCounter] = Integer.valueOf(Temp[2]);
                SpareYOffset[ExtraCounter] = getMinute(Temp[3])*2;
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date date=null,date1 = null;
                try {
                    date = simpleDateFormat.parse(""+getHour(Temp[3])+":"+getMinute(Temp[3]));
                    date1 = simpleDateFormat.parse(""+getHour(Temp[4])+":"+getMinute(Temp[4]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long TempDuration = (Objects.requireNonNull(date1).getTime() - date.getTime())/60000;
                SpareDuration[ExtraCounter] = (int)(TempDuration*2 + TempDuration*0.13);
                ExtraCounter++;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!CalledByMainActivity)ExitOptions();
                else SaveToDefaultSchedule(sharedPreferences.getString("DefaultSchedule",""));
                break;
            case R.id.day_view:
                Intent intent = new Intent(com.puchd.puplanner.NewSchedule.this,Day_View.class);
                if(CalledByImport)intent.putExtra("Caller","Import");
                startActivity(intent);
                finish();
                break;
            case R.id.save:
                if(newScheduleDatabase.RowCount()>0) SaveDialog();
                else
                {
                    Snackbar snackbar = Snackbar.make(relativeLayout,"Please add at least one lesson to save schedule",Snackbar.LENGTH_LONG);
                    View snack = snackbar.getView();
                    snack.setBackgroundColor(Color.parseColor("#4052b5"));
                    snackbar.show();
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
        if(!CalledByMainActivity)ExitOptions();
        else SaveToDefaultSchedule(sharedPreferences.getString("DefaultSchedule",""));
    }

    public void ExitOptions()
    {
        if(newScheduleDatabase.RowCount()>0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you wish to save this schedule?")
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            SaveDialog();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            newScheduleDatabase.DeleteLesson("DROP TABLE IF EXISTS NewScheduleTable");
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            newScheduleDatabase.DeleteLesson("DROP TABLE IF EXISTS NewScheduleTable");
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_schedule_menu, menu);
        if(CalledByMainActivity)
        {
            MenuItem menuItem = menu.findItem(R.id.day_view);
            menuItem.setVisible(false);
            MenuItem menuItem1 = menu.findItem(R.id.save);
            menuItem1.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v)
    {
        if(v.getId()<112)
        {
            if(LastClickID!=0)
            {
                TextView LastClickedTextView = findViewById(LastClickID);
                LastClickedTextView.setText("");
                LastClickedTextView.setBackgroundColor(gridColor.data);
                LastClickedTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            }
            TextView ClickedTextView = (TextView)v;
            if(LastClickID == ClickedTextView.getId())
            {
                int CellIndex = ClickedTextView.getId();
                int Row = CellIndex-CellIndex%8;
                TextView textView1 = findViewById(Row);
                String temp[] = textView1.getText().toString().split(" ");
                TextView DayText = findViewById(CellIndex%8);
                Intent i = new Intent(NewSchedule.this, CreateEditEntry.class);
                i.putExtra("Day",DayText.getText().toString());
                i.putExtra("Time",temp[0]+":00 "+temp[1]);
                i.putExtra("AbsoluteRow",Row);
                i.putExtra("Caller","NewSchedule");
                i.putExtra("CellIndex",CellIndex);
                if(CalledByMainActivity)i.putExtra("InitialCaller","MainActivity");
                if(CalledByImport)i.putExtra("InitialCaller","Import");
                startActivity(i);
                finish();
            }
            else LastClickID = ClickedTextView.getId();
            ClickedTextView.setBackgroundColor(clickHighlightColor.data);
            ClickedTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            ClickedTextView.setGravity(Gravity.CENTER);
            ClickedTextView.setPadding(0,0,0,0);
            ClickedTextView.setText("+");
        }

        else
        {
            Intent intent = new Intent(com.puchd.puplanner.NewSchedule.this,ViewEntry.class);
            int CellIndex;
            String Abbreviation;
            if(v.getId()<500)
            {
                CellIndex = v.getId()-103;
                Abbreviation = ScheduleData[CellIndex];
            }
            else
            {
                CellIndex = v.getId()-500;
                TextView txt = findViewById(v.getId());
                Abbreviation = txt.getText().toString();
            }
            intent.putExtra("CellIndex",CellIndex);
            intent.putExtra("Abbreviation",Abbreviation);
            if(CalledByMainActivity)intent.putExtra("Caller","MainActivity");
            if(CalledByImport)intent.putExtra("Caller","Import");
            startActivity(intent);
            finish();
        }
    }

    public int getHour(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        if(timeDiv[1].equals("PM") && HourSplit[0].equals("12"))return 12;
        if(timeDiv[1].equals("PM"))return Integer.valueOf(HourSplit[0])+12;
        if(timeDiv[1].equals("AM") && HourSplit[0].equals("12"))return 0;
        return Integer.valueOf(HourSplit[0]);
    }

    public int getMinute(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        return Integer.valueOf(HourSplit[1]);
    }

    public void SaveDialog()
    {
        AlertDialog.Builder SaveSchedule = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.dialog_save_new_schedule,null);
        RelativeLayout DialogLayout = dialogView.findViewById(R.id.SaveDialogLayout);
        final CheckBox checkBox = dialogView.findViewById(R.id.SetDefault);
        if(newScheduleDatabase.GetTableCount()<1)
        {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
            checkBox.setTextColor(Color.GRAY);
        }
        DialogLayout.requestFocus();
        final EditText ScheduleName = DialogLayout.findViewById(R.id.ScheduleName);
        SaveSchedule.setView(dialogView)
                .setTitle("Save schedule")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(!ScheduleName.getText().toString().isEmpty())
                        {
                            String TableName = ScheduleName.getText().toString().replace(' ','_');
                            calendar = Calendar.getInstance();
                            newScheduleDatabase.SaveNewSchedule(TableName);
                            attendanceDatabase.CreateTables(TableName);
                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                            String CreationDate = simpleDateFormat.format(calendar.getTime());
                            newScheduleDatabase.CreateTimestamp("INSERT INTO TimestampData VALUES ('"+TableName+"','"+CreationDate+"','"+CreationDate+"')");

                            ArrayList<String> SubjectData;
                            ArrayList<String> Subjects = new ArrayList<>();
                            SubjectData = newScheduleDatabase.GetDataForAttendance(TableName);
                            for(String SubjectD:SubjectData)
                            {
                                Boolean Found = false;
                                String SubjectTemp[] = SubjectD.split("_");
                                for(int i=0;i<Subjects.size();i++)
                                {
                                    if(Subjects.get(i).equals(SubjectTemp[0]))Found=true;
                                }
                                if(!Found)Subjects.add(SubjectTemp[0]);
                            }
                            for(String tempSubject:Subjects)
                            {
                                attendanceDatabase.InsertSubject(TableName,tempSubject,CreationDate);
                            }

                            if(checkBox.isChecked())
                            {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("DefaultSchedule",TableName);
                                editor.apply();
                                //attendanceDatabase.UpdateLastNotification(0,0);
                            }
                            newScheduleDatabase.DeleteLesson("DROP TABLE IF EXISTS NewScheduleTable");
                            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                            Objects.requireNonNull(notificationManager).cancelAll();
                            new AlarmsManager(getApplicationContext());
                            WidgetUpdater();
                            //updateMyWidgets(getApplicationContext());
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Kindly enter a name for the new schedule",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog Save = SaveSchedule.create();
        Save.show();
    }

    public void SaveToDefaultSchedule(String DefaultSchedule)
    {
        if(newScheduleDatabase.RowCount()==0)
        {
            Snackbar snackbar = Snackbar.make(relativeLayout,"Please add at least one lesson to save schedule",Snackbar.LENGTH_LONG);
            View snack = snackbar.getView();
            snack.setBackgroundColor(Color.parseColor("#4052b5"));
            snackbar.show();
        }
        else
        {
            newScheduleDatabase.DeleteLesson("DELETE FROM "+DefaultSchedule);
            newScheduleDatabase.DeleteLesson("INSERT INTO "+DefaultSchedule+" SELECT * FROM NewScheduleTable");
            newScheduleDatabase.DeleteLesson("DELETE FROM NewScheduleTable");
            calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String CreationDate = simpleDateFormat.format(calendar.getTime());
            newScheduleDatabase.CreateTimestamp("UPDATE TimestampData SET DateModified='"+CreationDate+"' WHERE ScheduleName='"+DefaultSchedule+"'");
            ArrayList<String> SubjectData;
            ArrayList<String> Subjects = new ArrayList<>();
            SubjectData = newScheduleDatabase.GetDataForAttendance(DefaultSchedule);
            for(String SubjectD:SubjectData)
            {
                Boolean Found = false;
                String SubjectTemp[] = SubjectD.split("_");
                for(int i=0;i<Subjects.size();i++)
                {
                    if(Subjects.get(i).equals(SubjectTemp[0]))Found=true;
                }
                if(!Found)Subjects.add(SubjectTemp[0]);
            }
            ArrayList<String>StoredSubjects = attendanceDatabase.GetSubjects(DefaultSchedule);
            for(String tempStoredSubjects:StoredSubjects)
            {
                Boolean Match = false;
                for(int i=0;i<Subjects.size();i++)
                {
                    if(tempStoredSubjects.equals(Subjects.get(i)))Match=true;
                }
                if(!Match)attendanceDatabase.DeleteSubject(DefaultSchedule,tempStoredSubjects);
            }
            for(String tempSubject:Subjects)
            {
                attendanceDatabase.InsertSubject(DefaultSchedule,tempSubject,CreationDate);
            }
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Objects.requireNonNull(notificationManager).cancelAll();
            new AlarmsManager(getApplicationContext());
            WidgetUpdater();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }


    public void WidgetUpdater()
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider_Day.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.WidgetDayList);
    }
}
