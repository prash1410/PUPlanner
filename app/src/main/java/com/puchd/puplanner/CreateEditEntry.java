package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class CreateEditEntry extends AppCompatActivity
{
    View subjectView,abbreviationView,teacherView,venueView,typeView;
    Animation anim,anim2;
    Boolean CalledByMainActivity = false;
    Boolean CalledByImport = false;
    CheckBox Importance;
    TextView ColorPicker;
    ColorDrawable colorDrawable;
    ListView ErrorList;
    ArrayList<Integer>CellIndex = new ArrayList<>();
    ArrayList<String> ErrorsList = new ArrayList<>();
    ArrayList<String> DayKeeper = new ArrayList<>();
    ArrayList<String> TimeKeeper = new ArrayList<>();
    ArrayList<String> DayTimeKeeper = new ArrayList<>();
    ArrayList<String> FinalDayList  = new ArrayList<>();
    ArrayList<String> FinalStartingTimeList = new ArrayList<>();
    ArrayList<String> FinalEndingTimeList = new ArrayList<>();
    ArrayList<DatabaseError> DatabaseErrors = new ArrayList<>();
    EditText Teacher,Venue,Type,Subject,Abbreviation;
    String Time,Caller;
    String Day,LastAbbreviation;
    int AbsoluteRow;
    boolean FirstTime = true;
    public LinkedHashMap<String,DayInfo> linkedHashMap = new LinkedHashMap<>();
    public ArrayList<DayInfo> arrayList = new ArrayList<>();
    DayTimeListAdapter dayTimeListAdapter;
    public ExpandableListView expandableListView;

    ToggleButton Mon;
    ToggleButton Tue;
    ToggleButton Wed;
    ToggleButton Thu;
    ToggleButton Fri;
    ToggleButton Sat;
    ToggleButton Sun;

    RelativeLayout TimeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int themeValue = ThemeSetter.getThemeID();
        if(themeValue == 1)setTheme(R.style.AppTheme_Dark_Actionbar);
        getTheme().applyStyle(AccentSetter.getStyleID(),true);
        getTheme().applyStyle(PrimaryColorSetter.getStyleID(),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createeditentry);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getStringExtra("InitialCaller")!=null)
        {
            if(getIntent().getStringExtra("InitialCaller").equals("MainActivity"))CalledByMainActivity=true;
            if(getIntent().getStringExtra("InitialCaller").equals("Import"))CalledByImport=true;
        }
        final RelativeLayout FocusThief = findViewById(R.id.FocusThief2);
        FocusThief.requestFocus();

        TimeLayout  = findViewById(R.id.TimeLayout);
        Day = getIntent().getStringExtra("Day");
        Time = getIntent().getStringExtra("Time");
        AbsoluteRow = getIntent().getIntExtra("AbsoluteRow",0);
        Caller = getIntent().getStringExtra("Caller");

        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_disappear);

        ColorPicker = findViewById(R.id.ColorPicker);
        colorDrawable = (ColorDrawable)ColorPicker.getBackground();
        ColorPicker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               ColorPickerDialogBuilder
                        .with(CreateEditEntry.this)
                        .setTitle("Pick a color")
                        .initialColor(colorDrawable.getColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor)
                            {
                                //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("Ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                //changeBackgroundColor(selectedColor);
                                ColorPicker.setBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });
        Importance = findViewById(R.id.Importance);
        expandableListView = findViewById(R.id.DayTimeList);
        dayTimeListAdapter = new DayTimeListAdapter(CreateEditEntry.this,arrayList);
        expandableListView.setAdapter(dayTimeListAdapter);
        Subject = findViewById(R.id.Subject);
        subjectView = findViewById(R.id.view_subject);
        Abbreviation = findViewById(R.id.SubjectAbbreviation);
        abbreviationView = findViewById(R.id.abbreviationView);
        teacherView = findViewById(R.id.teacherView);
        typeView = findViewById(R.id.typeView);
        venueView = findViewById(R.id.venueView);
        Abbreviation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    abbreviationView.setVisibility(View.VISIBLE);
                    abbreviationView.startAnimation(anim);
                } else abbreviationView.startAnimation(anim2);
            }
        });
        Teacher = findViewById(R.id.Teacher);
        Teacher.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    teacherView.setVisibility(View.VISIBLE);
                    teacherView.startAnimation(anim);
                } else teacherView.startAnimation(anim2);
            }
        });
        Venue = findViewById(R.id.Venue);
        Venue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    venueView.setVisibility(View.VISIBLE);
                    venueView.startAnimation(anim);
                } else venueView.startAnimation(anim2);
            }
        });
        Type = findViewById(R.id.Type);
        Type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    typeView.setVisibility(View.VISIBLE);
                    typeView.startAnimation(anim);
                } else typeView.startAnimation(anim2);
            }
        });
        if(Caller.equals("ViewEntry"))
        {
            NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(this);
            ArrayList<String> Data = newScheduleDatabase.FetchLesson(getIntent().getIntExtra("CellIndex",0),"",Day,Time);
            Subject.setText(Data.get(4));
            Abbreviation.setText(Data.get(5));
            LastAbbreviation = Abbreviation.getText().toString();
            if(!Data.get(6).isEmpty())
            {
                Teacher.setText(Data.get(6));
            }
            if(!Data.get(7).isEmpty())
            {
                Venue.setText(Data.get(7));
            }
            if(!Data.get(8).isEmpty())
            {
                Type.setText(Data.get(8));
            }
            ColorPicker.setBackgroundColor(Integer.valueOf(Data.get(9)));
            if(Integer.valueOf(Data.get(10))==0)Importance.setChecked(true);
        }
        Subject.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    subjectView.startAnimation(anim2);
                        if(!Subject.getText().toString().isEmpty())
                        {
                            String[] Trim = Subject.getText().toString().trim().split(" ");
                            if(Trim.length != 1)
                            {
                                StringBuilder AB = new StringBuilder();
                                String temp = "";
                                char Sub[] = Subject.getText().toString().toCharArray();
                                temp += Sub[0];
                                AB.append(temp.toUpperCase());
                                for(int i=1;i<Subject.getText().toString().length();i++)
                                {
                                    if(Sub[i] == ' ')
                                    {
                                        if(i+1<Subject.getText().toString().length())
                                        {
                                            if(Sub[i+1] != ' ')
                                            {
                                                temp = "";
                                                temp += Sub[i+1];
                                                AB.append(temp.toUpperCase());
                                            }
                                        }
                                    }
                                }
                                Abbreviation.setText(AB.toString());
                            }
                            else
                            {
                                if(Subject.getText().toString().length()>=3) Abbreviation.setText(Subject.getText().toString().substring(0,3).toUpperCase());
                            }
                        }
                }
                else
                {
                    subjectView.setVisibility(View.VISIBLE);
                    subjectView.startAnimation(anim);
                }
            }
        });
        Abbreviation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {
                    Abbreviation.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    FocusThief.requestFocus();
                }
                return false;
            }
        });
        Type.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {
                    Type.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), 0);
                    FocusThief.requestFocus();
                }
                return false;
            }
        });

        Mon = findViewById(R.id.Mon);
        Tue = findViewById(R.id.Tue);
        Wed = findViewById(R.id.Wed);
        Thu = findViewById(R.id.Thu);
        Fri = findViewById(R.id.Fri);
        Sat = findViewById(R.id.Sat);
        Sun = findViewById(R.id.Sun);
        Mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) setFirstDateTime("Monday");
                if(!isChecked) removeGroup("Monday");
            }
        });

        Tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) setFirstDateTime("Tuesday");
                if(!isChecked) removeGroup("Tuesday");
            }
        });

        Wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) setFirstDateTime("Wednesday");
                if(!isChecked) removeGroup("Wednesday");
            }
        });

        Thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) setFirstDateTime("Thursday");
                if(!isChecked) removeGroup("Thursday");
            }
        });

        Fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) setFirstDateTime("Friday");
                if(!isChecked) removeGroup("Friday");
            }
        });

        Sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) setFirstDateTime("Saturday");
                if(!isChecked) removeGroup("Saturday");
            }
        });

        Sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) setFirstDateTime("Sunday");
                if(!isChecked) removeGroup("Sunday");
            }
        });

        if(Day.equals("Mon"))Mon.setChecked(true);
        if(Day.equals("Tue"))Tue.setChecked(true);
        if(Day.equals("Wed"))Wed.setChecked(true);
        if(Day.equals("Thu"))Thu.setChecked(true);
        if(Day.equals("Fri"))Fri.setChecked(true);
        if(Day.equals("Sat"))Sat.setChecked(true);
        if(Day.equals("Sun"))Sun.setChecked(true);
    }

    public int addDay(String day,String StartTime,String EndTime)
    {
        int groupPosition;
        DayInfo dayInfo = linkedHashMap.get(day);
        if(dayInfo==null)
        {
            dayInfo = new DayInfo();
            dayInfo.setDayName(day);
            linkedHashMap.put(day, dayInfo);
            arrayList.add(dayInfo);
        }

        ArrayList<TimeInfo> timeList = dayInfo.gettimeList();
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.setStartingTime(StartTime);
        timeInfo.setEndingTime(EndTime);
        timeList.add(timeInfo);
        dayInfo.settimeList(timeList);

        groupPosition = arrayList.indexOf(dayInfo);
        return groupPosition;
    }

    public void setFirstDateTime(String day)
    {
        int groupPosition;
        if(FirstTime)
        {
            String Endtime = "";
            int temp;
            if(AbsoluteRow<32)
            {
                temp = (AbsoluteRow%7)+8;
                Endtime = ""+temp+":00 AM";
            }
            if(AbsoluteRow==32)
            {
                Endtime = "12:00 PM";
            }
            if(AbsoluteRow==40)
            {
                Endtime = "1:00 PM";
            }
            if(AbsoluteRow>40)
            {
                if(AbsoluteRow==48)Endtime="2:00 PM";
                if(AbsoluteRow==56)Endtime="3:00 PM";
                if(AbsoluteRow==64)Endtime="4:00 PM";
                if(AbsoluteRow==72)Endtime="5:00 PM";
                if(AbsoluteRow==80)Endtime="6:00 PM";
                if(AbsoluteRow==88)Endtime="7:00 PM";
                if(AbsoluteRow==96)Endtime="8:00 PM";
                if(AbsoluteRow==104)Endtime="9:00 PM";
            }
            if(Caller.equals("ViewEntry"))
            {
                Endtime=getIntent().getStringExtra("EndingTime");
            }
            groupPosition = addDay(day,Time,Endtime);
            dayTimeListAdapter.notifyDataSetChanged();
            expandableListView.expandGroup(groupPosition);
            expandableListView.setSelectedGroup(groupPosition);
        }
        else
        {
            groupPosition = addDay(day,"8:00 AM","9:00 AM");
            dayTimeListAdapter.notifyDataSetChanged();
            expandableListView.expandGroup(groupPosition);
            expandableListView.setSelectedGroup(groupPosition);
        }
        FirstTime = false;
    }

    public void removeGroup(String day)
    {
        DayInfo dayInfo = linkedHashMap.get(day);
        arrayList.remove(dayInfo);
        linkedHashMap.remove(day);
        dayTimeListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.donecancelbuttons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String Message = "Do you wish to discard this entry?";
                if(Caller.equals("ViewEntry"))Message="Do you wish to discard changes?";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(Message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(getApplicationContext(), NewSchedule.class);
                                if(CalledByMainActivity)
                                {
                                    intent.putExtra("Caller","MainActivity");
                                    intent.putExtra("Caller2","CreateEditEntry");
                                }
                                if(CalledByImport) intent.putExtra("Caller","Import");
                                startActivity(intent);
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
                break;
            case R.id.action_done:
                ValidateData();
                if(ErrorsList.isEmpty())
                {
                    DatabaseErrors.clear();
                    InsertLessons();
                    if(!DatabaseErrors.isEmpty())
                    {
                        AlertDialog.Builder Confirmation = new AlertDialog.Builder(this);
                        LayoutInflater inflater = this.getLayoutInflater();
                        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_database_errors,null);
                        ErrorList = dialogView.findViewById(R.id.DatabaseErrorsList);
                        DataBaseErrorCustomAdapter adapter = new DataBaseErrorCustomAdapter(DatabaseErrors, getApplicationContext());
                        ErrorList.setAdapter(adapter);
                        Confirmation.setView(dialogView)
                                .setTitle("The following lessons could not be added due to timing conflicts.")
                                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CreateEditEntry.this,NewSchedule.class);
                            if(CalledByMainActivity)
                            {
                                intent.putExtra("Caller","MainActivity");
                                intent.putExtra("Caller2","CreateEditEntry");
                            }
                            if(CalledByImport) intent.putExtra("Caller","Import");
                            startActivity(intent);
                            finish();
                        }
                    })
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                    }
                                });
                        AlertDialog Conf = Confirmation.create();
                        Conf.show();
                    }
                    else {
                        Intent intent = new Intent(CreateEditEntry.this,NewSchedule.class);
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
                break;
            case R.id.action_cancel:
                Intent intent = new Intent(CreateEditEntry.this,NewSchedule.class);
                if(CalledByMainActivity)
                {
                    intent.putExtra("Caller","MainActivity");
                    intent.putExtra("Caller2","CreateEditEntry");
                }
                if(CalledByImport) intent.putExtra("Caller","Import");
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ValidateData()
    {
        ErrorsList.clear();
        DayKeeper.clear();
        DayTimeKeeper.clear();
        TimeKeeper.clear();
        FinalDayList.clear();
        FinalStartingTimeList.clear();
        FinalEndingTimeList.clear();
        CellIndex.clear();
        if(Subject.getText().toString().isEmpty())
        {
            ErrorsList.add("Subject field cannot be empty");
        }
        if(Abbreviation.getText().toString().isEmpty())
        {
            ErrorsList.add("Abbreviation field cannot be empty");
        }
        if(dayTimeListAdapter.getGroupCount()==0)
        {
            ErrorsList.add("Please add one lesson at least");
        }
        for(int i=0;i<dayTimeListAdapter.getGroupCount();i++)
        {
            View groupv = dayTimeListAdapter.getGroupView(i,true,null,null);
            TextView day = groupv.findViewById(R.id.DayName);
            DayKeeper.add(day.getText().toString());
            if(dayTimeListAdapter.getChildrenCount(i)==0)
            {
                ErrorsList.add("Please add at least one lesson on "+day.getText().toString());
            }
            if(dayTimeListAdapter.getChildrenCount(i)!=0)
            {
                for(int j=0;j<dayTimeListAdapter.getChildrenCount(i);j++)
                {
                    View v;
                    if(j==dayTimeListAdapter.getChildrenCount(i)-1)
                    {
                        v = dayTimeListAdapter.getChildView(i,j,true,null,null);
                    }
                    else
                    {
                        v = dayTimeListAdapter.getChildView(i,j,false,null,null);
                    }
                    TextView StartingTimeTemp = v.findViewById(R.id.StartingTime);
                    TextView EndingTimeTemp = v.findViewById(R.id.EndingTime);
                    TimeKeeper.add(""+getHour(StartingTimeTemp.getText().toString())+":"+getMinute(StartingTimeTemp.getText().toString())+"-"+getHour(EndingTimeTemp.getText().toString())+":"+getMinute(EndingTimeTemp.getText().toString()));
                    DayTimeKeeper.add(day.getText().toString());
                    ValidateTime(getHour(StartingTimeTemp.getText().toString()),getMinute(StartingTimeTemp.getText().toString()),getHour(EndingTimeTemp.getText().toString()),getMinute(EndingTimeTemp.getText().toString()),day.getText().toString(),j+1);
                    if(ErrorsList.isEmpty())
                    {
                        FinalDayList.add(day.getText().toString());
                        FinalStartingTimeList.add(StartingTimeTemp.getText().toString());
                        FinalEndingTimeList.add(EndingTimeTemp.getText().toString());
                        int increment = 0;
                        if(day.getText().toString().substring(0,3).equals("Mon"))increment=1;
                        if(day.getText().toString().substring(0,3).equals("Tue"))increment=2;
                        if(day.getText().toString().substring(0,3).equals("Wed"))increment=3;
                        if(day.getText().toString().substring(0,3).equals("Thu"))increment=4;
                        if(day.getText().toString().substring(0,3).equals("Fri"))increment=5;
                        if(day.getText().toString().substring(0,3).equals("Sat"))increment=6;
                        if(day.getText().toString().substring(0,3).equals("Sun"))increment=7;
                        int cellIndex = 0;
                        if(getHour(StartingTimeTemp.getText().toString())>7 && getHour(StartingTimeTemp.getText().toString())<14)
                        {
                            cellIndex = 8*(getHour(StartingTimeTemp.getText().toString())%7)+increment;
                        }
                        if(getHour(StartingTimeTemp.getText().toString()) == 14)
                        {
                            cellIndex = 56 + increment;
                        }
                        if(getHour(StartingTimeTemp.getText().toString())>14 && getHour(StartingTimeTemp.getText().toString())<21)
                        {
                            cellIndex = 56+8*(getHour(StartingTimeTemp.getText().toString())%7)+increment;
                        }
                        CellIndex.add(cellIndex);
                    }
                }
                if(ErrorsList.isEmpty() && dayTimeListAdapter.getChildrenCount(i)>1)
                {
                    ArrayList<String>startingHour = new ArrayList<>();
                    ArrayList<String>startingMinute = new ArrayList<>();
                    ArrayList<String>endingMinute = new ArrayList<>();
                    ArrayList<String>endingHour = new ArrayList<>();
                    for(String Time:TimeKeeper)
                    {
                        String temp[] = Time.split("-");
                        String temp2[] = temp[0].split(":");
                        String temp3[] = temp[1].split(":");
                        startingHour.add(temp2[0]);
                        startingMinute.add(temp2[1]);
                        endingMinute.add(temp3[1]);
                        endingHour.add(temp3[0]);
                    }
                    ArrayList<Integer>Counter1 = new ArrayList<>();
                    ArrayList<Integer>Counter2 = new ArrayList<>();
                    for(int k=0;k<TimeKeeper.size();k++)
                    {
                        for(int l=0;l<TimeKeeper.size();l++)
                        {
                            if(k!=l)
                            {
                                Counter1.add(k);
                                Counter2.add(l);
                            }
                        }
                    }
                    boolean error = false;
                    for(int k=0;k<Counter1.size();k++)
                    {
                        if(Integer.valueOf(startingHour.get(Counter1.get(k)))>Integer.valueOf(startingHour.get(Counter2.get(k))) && Integer.valueOf(startingHour.get(Counter1.get(k)))<Integer.valueOf(endingHour.get(Counter2.get(k))))
                        {
                            if(!error)
                            {
                                ErrorsList.add("There appears to be a timing conflict on "+day.getText().toString());
                                error = true;
                            }
                        }
                        if(Integer.valueOf(endingHour.get(Counter1.get(k)))>Integer.valueOf(startingHour.get(Counter2.get(k))) && Integer.valueOf(startingHour.get(Counter1.get(k)))<Integer.valueOf(endingHour.get(Counter2.get(k))))
                        {
                            if(!error)
                            {
                                ErrorsList.add("There appears to be a timing conflict on "+day.getText().toString());
                                error=true;
                            }
                        }
                        if(Integer.valueOf(endingHour.get(Counter1.get(k))).equals(Integer.valueOf(startingHour.get(Counter2.get(k)))))
                        {
                            if(Integer.valueOf(endingMinute.get(Counter1.get(k)))>Integer.valueOf(startingMinute.get(Counter2.get(k))))
                            {
                                ErrorsList.add("There appears to be a timing conflict on "+day.getText().toString());
                                error=true;
                            }
                        }
                    }
                }
                TimeKeeper.clear();
            }
        }
        if(!ErrorsList.isEmpty())
        {
            AlertDialog.Builder Confirmation = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_errors,null);
            final ListView ErrorList = dialogView.findViewById(R.id.ErrorsList);
            ArrayAdapter<String> errorAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,ErrorsList);
            ErrorList.setAdapter(errorAdapter);
            Confirmation.setView(dialogView)
                    .setTitle("Please correct the following errors")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog Conf = Confirmation.create();
            Conf.show();
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

    public void ValidateTime(int StartingHour,int StartingMinute,int EndingHour,int EndingMinute, String Day, int timeRow)
    {
        boolean ErrorFound = false;
        if(StartingHour<8 || StartingHour>20)
        {
            ErrorsList.add("On "+Day+" Row "+timeRow+", Starting time is invalid");
            ErrorFound = true;
        }
        if(StartingHour == 20 && StartingMinute>0)
        {
            ErrorsList.add("On "+Day+" Row "+timeRow+", Starting time is invalid");
            ErrorFound = true;
        }
        if(EndingHour<8 || EndingHour>21)
        {
            ErrorsList.add("On "+Day+" Row "+timeRow+", Ending time is invalid");
            ErrorFound = true;
        }
        if(EndingHour == 21 && EndingMinute>0)
        {
            ErrorsList.add("On "+Day+" Row "+timeRow+", Ending time is invalid");
            ErrorFound = true;
        }
        if(!ErrorFound)
        {
            if(StartingHour>EndingHour)
            {
                ErrorsList.add("On "+Day+" Row "+timeRow+", discrepancy exists between Starting and Ending times");
                ErrorFound = true;
            }
            if(!ErrorFound)
            {
                if(StartingHour==EndingHour)
                {
                    if(StartingMinute>=EndingMinute)
                    {
                        ErrorsList.add("On "+Day+", Row "+timeRow+", discrepancy exists between Starting and Ending times");
                    }
                }
            }
        }
    }

    public void InsertLessons()
    {
        NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(this);
        if(Caller.equals("ViewEntry"))
        {
            newScheduleDatabase.DeleteLesson("DELETE FROM NewScheduleTable WHERE CellIndex="+getIntent().getIntExtra("CellIndex",0)+" AND Abbreviation='"+LastAbbreviation+"'");
        }
        int i=0;
        for(String Day:FinalDayList)
        {
            ArrayList<String>DatabaseStartingTimes = newScheduleDatabase.FetchLessonTimings(Day,"StartingTime");
            ArrayList<String>DatabaseEndingTimes = newScheduleDatabase.FetchLessonTimings(Day,"EndingTime");
            if(DatabaseStartingTimes.size()==0)
            {
                int ImportanceInt = 1;
                if(Importance.isChecked())ImportanceInt=0;
                newScheduleDatabase.InsertLesson(CellIndex.get(i),Day,FinalStartingTimeList.get(i),FinalEndingTimeList.get(i),Subject.getText().toString(),Abbreviation.getText().toString(),Teacher.getText().toString(),Venue.getText().toString(),Type.getText().toString(),colorDrawable.getColor(),ImportanceInt);
            }
            else
            {
                int j=0;
                boolean error = false;
                for(String StartingTimeTemp:DatabaseStartingTimes)
                {
                    int DatabaseStartingHour = getHour(StartingTimeTemp);
                    int DatabaseStartingMinute = getMinute(StartingTimeTemp);
                    int DatabaseEndingHour = getHour(DatabaseEndingTimes.get(j));
                    int DatabaseEndingMinute = getMinute(DatabaseEndingTimes.get(j));
                    int TempStartingHour = getHour(FinalStartingTimeList.get(i));
                    int TempStartingMinute = getMinute(FinalStartingTimeList.get(i));
                    int TempEndingHour = getHour(FinalEndingTimeList.get(i));
                    int TempEndingMinute = getMinute(FinalEndingTimeList.get(i));
                    float DatabaseStartingTime = DatabaseStartingHour+((float)DatabaseStartingMinute)/100;
                    float DatabaseEndingTime = DatabaseEndingHour+((float)DatabaseEndingMinute)/100;
                    float TempStartingTime = TempStartingHour+((float)TempStartingMinute)/100;
                    float TempEndingTime = TempEndingHour+((float)TempEndingMinute)/100;
                    if(DatabaseStartingTime==TempStartingTime) error=true;
                    if(DatabaseEndingTime==TempEndingTime)error=true;
                    if(TempStartingTime>DatabaseStartingTime && TempStartingTime<DatabaseEndingTime) error=true;
                    if(TempEndingTime>DatabaseStartingTime && TempEndingTime<DatabaseEndingTime) error=true;
                    if(TempStartingTime<DatabaseStartingTime && TempEndingTime>DatabaseEndingTime) error=true;
                    j++;
                }
                if(error) DatabaseErrors.add(new DatabaseError(colorDrawable.getColor(),Subject.getText().toString(),Abbreviation.getText().toString(),Day+" "+FinalStartingTimeList.get(i)+"-"+FinalEndingTimeList.get(i)));
                if(!error)
                {
                    int ImportanceInt = 1;
                    if(Importance.isChecked())ImportanceInt=0;
                    newScheduleDatabase.InsertLesson(CellIndex.get(i),Day,FinalStartingTimeList.get(i),FinalEndingTimeList.get(i),Subject.getText().toString(),Abbreviation.getText().toString(),Teacher.getText().toString(),Venue.getText().toString(),Type.getText().toString(),colorDrawable.getColor(),ImportanceInt);
                }
            }
            i++;
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you wish to discard this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(getApplicationContext(), NewSchedule.class);
                        if(CalledByMainActivity)
                        {
                            intent.putExtra("Caller","MainActivity");
                            intent.putExtra("Caller2","CreateEditEntry");
                        }
                        if(CalledByImport) intent.putExtra("Caller","Import");
                        startActivity(intent);
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

}
