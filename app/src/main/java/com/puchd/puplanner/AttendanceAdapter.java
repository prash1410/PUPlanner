package com.puchd.puplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>
{
    private Context mContext;
    private List<AttendanceCards> list;
    AttendanceDatabase attendanceDatabase;
    NewScheduleDatabase newScheduleDatabase;
    SharedPreferences sharedPreferences;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView SubjectNameView,AttendancePercentage,NumberAttended,AttendanceNeeded;
        public ImageButton AttendanceEdit,AttendanceCalendar;

        public MyViewHolder(View view)
        {
            super(view);
            SubjectNameView = (TextView)view.findViewById(R.id.SubjectNameView);
            AttendancePercentage = (TextView)view.findViewById(R.id.AttendancePercentage);
            NumberAttended = (TextView)view.findViewById(R.id.NumberAttended);
            AttendanceNeeded = (TextView)view.findViewById(R.id.AttendanceNeeded);
            AttendanceEdit = (ImageButton)view.findViewById(R.id.AttendanceEdit);
            AttendanceCalendar = (ImageButton)view.findViewById(R.id.AttendanceCalendar);
        }
    }

    public AttendanceAdapter(Context mContext, List<AttendanceCards> list)
    {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.MyViewHolder holder, int position)
    {
        final AttendanceCards attendanceCards = list.get(position);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String DefaultSchedule = sharedPreferences.getString("DefaultSchedule","");
        final String CardName = attendanceCards.getSubjectName();
        holder.SubjectNameView.setText(CardName);
        attendanceDatabase = new AttendanceDatabase(mContext);
        String Date[] = attendanceDatabase.GetStartingDate(DefaultSchedule,CardName).split("-");
        String Year[] = Date[2].split(" ");
        final int mYear = Integer.valueOf(Year[0]);
        final int mMonth = Integer.valueOf(Date[1])-1;
        final int mDay = Integer.valueOf(Date[0]);
        holder.AttendanceCalendar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        monthOfYear++;
                        attendanceDatabase.UpdateStartingDate(DefaultSchedule,CardName,""+dayOfMonth+"-"+monthOfYear+"-"+year);
                        int LecturesDelivered = ComputeLectures(DefaultSchedule,CardName);
                        String LecturesAttendedTemp[] = attendanceDatabase.GetAttendanceData(DefaultSchedule,CardName).split("_");
                        if(Integer.valueOf(LecturesAttendedTemp[0])>LecturesDelivered)attendanceDatabase.UpdateAttendedLectures(DefaultSchedule,CardName,LecturesDelivered);
                        UpdateFragment();
                        Snackbar snackbar = Snackbar.make(v,"Starting date set to "+dayOfMonth+"-"+monthOfYear+"-"+year+" for "+CardName,Snackbar.LENGTH_LONG);
                        View snack = snackbar.getView();
                        snack.setBackgroundColor(Color.parseColor("#4052b5"));
                        snackbar.show();
                    }
                }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        String AttendanceData[] = attendanceDatabase.GetAttendanceData(DefaultSchedule,CardName).split("_");
        holder.NumberAttended.setText("Attended "+AttendanceData[0]+"/"+AttendanceData[1]);
        float Attendance = 0;
        int LecturesNeeded = 0;
        if(Integer.valueOf(AttendanceData[1])>0)
        {
            int temp1 = Integer.valueOf(AttendanceData[0]);
            int temp2 = Integer.valueOf(AttendanceData[1]);
            float AttendanceTemp = (float)temp1/temp2;
            AttendanceTemp = AttendanceTemp*100;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            Attendance = Float.valueOf(decimalFormat.format(AttendanceTemp));
            LecturesNeeded = (int) Math.ceil(0.75*Integer.valueOf(AttendanceData[1]) - Integer.valueOf(AttendanceData[0]));
            holder.AttendanceNeeded.setText(""+LecturesNeeded+" more needed for 75%");
        }
        if(LecturesNeeded<=0)holder.AttendanceNeeded.setText("Attendance above 75%");
        if(Integer.valueOf(AttendanceData[1])==0)holder.AttendanceNeeded.setText("");
        holder.AttendancePercentage.setText(""+Attendance+"%");

        holder.AttendanceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder ChangeAttendance = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.dialog_attendance_edit,null);
                final RelativeLayout r = (RelativeLayout)dialogView.findViewById(R.id.AttendanceEditDialogLayout);
                r.requestFocus();
                final DiscreteSeekBar dsba = (DiscreteSeekBar)dialogView.findViewById(R.id.AttendedSeekBar);
                final EditText DeliveredNumber = (EditText)dialogView.findViewById(R.id.DeliveredNumber);
                final TextView AttendedNumber = (TextView) dialogView.findViewById(R.id.AttendedNumber);
                String AttendanceDataTemp[] = attendanceDatabase.GetAttendanceData(DefaultSchedule,CardName).split("_");
                dsba.setMax(Integer.valueOf(AttendanceDataTemp[1]));
                dsba.setProgress(Integer.valueOf(AttendanceDataTemp[0]));
                DeliveredNumber.setText(AttendanceDataTemp[1]);
                AttendedNumber.setText(AttendanceDataTemp[0]);
                DeliveredNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId == EditorInfo.IME_ACTION_DONE)
                        {
                            dsba.setMax(Integer.valueOf(DeliveredNumber.getText().toString()));
                            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            DeliveredNumber.clearFocus();
                            r.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
                dsba.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                    @Override
                    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                        AttendedNumber.setText(""+dsba.getProgress());
                    }
                    @Override
                    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                    }
                });
                DeliveredNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        if(!DeliveredNumber.getText().toString().equals("")) dsba.setMax(Integer.valueOf(DeliveredNumber.getText().toString()));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                ChangeAttendance.setView(dialogView)
                        .setTitle("Edit attendance")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        })
                        .setPositiveButton("Save", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                if(DeliveredNumber.hasFocus())
                                {
                                    dsba.setMax(Integer.valueOf(DeliveredNumber.getText().toString()));
                                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(dialogView.getWindowToken(), 0);
                                    DeliveredNumber.clearFocus();
                                    r.requestFocus();
                                }
                                attendanceDatabase.UpdateAttendanceLectures(DefaultSchedule,CardName,Integer.valueOf(DeliveredNumber.getText().toString()));
                                attendanceDatabase.UpdateAttendedLectures(DefaultSchedule,CardName,dsba.getProgress());
                                UpdateFragment();
                            }
                        });
                AlertDialog Save = ChangeAttendance.create();
                Save.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void UpdateFragment()
    {
        android.support.v4.app.Fragment fragment = new AttendanceFragment();
        FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    public int ComputeLectures(String DefaultSchedule,String CardName)
    {
        String Date[] = attendanceDatabase.GetStartingDate(DefaultSchedule,CardName).split("-");
        String Year[] = Date[2].split(" ");
        int mYear = Integer.valueOf(Year[0]);
        int mMonth = Integer.valueOf(Date[1])-1;
        int mDay = Integer.valueOf(Date[0]);

        int FinalNumberofDays = 0;
        newScheduleDatabase = new NewScheduleDatabase(mContext);
        for(String temp:newScheduleDatabase.GetLectureInstances(DefaultSchedule,CardName))
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date StartingDate = null,CurrentDate = null;
            Calendar c = Calendar.getInstance();
            try {
                StartingDate = dateFormat.parse(""+mDay+"/"+mMonth+"/"+mYear);
                CurrentDate = dateFormat.parse(""+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String tempSplit[] = temp.split("_");
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(StartingDate);
            cal2.setTime(CurrentDate);
            int numberOfDays = 0;
            while (cal1.before(cal2))
            {
                if(Integer.valueOf(tempSplit[0])==cal1.get(Calendar.DAY_OF_WEEK))
                {
                    numberOfDays++;
                }
                cal1.add(Calendar.DATE,1);
            }
            numberOfDays = numberOfDays*Integer.valueOf(tempSplit[1]);
            FinalNumberofDays = FinalNumberofDays+numberOfDays;
        }
        attendanceDatabase.UpdateAttendanceLectures(DefaultSchedule,CardName,FinalNumberofDays);
        return FinalNumberofDays;
    }

}
