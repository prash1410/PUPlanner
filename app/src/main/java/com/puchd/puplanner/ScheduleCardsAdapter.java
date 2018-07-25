package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ScheduleCardsAdapter extends RecyclerView.Adapter<ScheduleCardsAdapter.MyViewHolder>
{
    private NewScheduleDatabase newScheduleDatabase;
    private AttendanceDatabase attendanceDatabase;
    private SharedPreferences sharedPreferences;
    private Context mContext;
    private List<ScheduleCards> list;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView ScheduleNameView;
        public ImageButton Delete,Default,Edit,Share;
        public MyViewHolder(View view)
        {
            super(view);
            ScheduleNameView = view.findViewById(R.id.ScheduleNameView);
            Delete = view.findViewById(R.id.Delete);
            Default = view.findViewById(R.id.Default);
            Edit = view.findViewById(R.id.Edit);
            Share = view.findViewById(R.id.Share);
        }
    }

    public ScheduleCardsAdapter(Context mContext, List<ScheduleCards> list)
    {
        this.mContext = mContext;
        this.list = list;
        newScheduleDatabase = new NewScheduleDatabase(mContext);
        attendanceDatabase = new AttendanceDatabase(mContext);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        final ScheduleCards scheduleCards = list.get(position);
        holder.ScheduleNameView.setText(scheduleCards.getScheduleName());
        if(scheduleCards.getScheduleName().equals(sharedPreferences.getString("DefaultSchedule","None")))holder.Default.setBackgroundResource(R.drawable.carddefaultset);
        holder.Delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int ElementCount = list.size();
                String OldName = scheduleCards.getScheduleName();
                newScheduleDatabase.DeleteLesson("DROP TABLE IF EXISTS "+OldName);
                newScheduleDatabase.CreateTimestamp("DELETE FROM TimestampData WHERE ScheduleName='"+OldName+"'");
                attendanceDatabase.DeleteTable(OldName);
                if(ElementCount>1)
                {
                    if(OldName.equals(sharedPreferences.getString("DefaultSchedule","None")))
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("DefaultSchedule",newScheduleDatabase.GetTableNames().get(0));
                        editor.apply();
                        //attendanceDatabase.UpdateLastNotification(0,0);
                        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
                        Objects.requireNonNull(notificationManager).cancelAll();
                        new AlarmsManager(mContext);
                        WidgetUpdater();
                    }
                    Intent intent = new Intent(mContext,MainActivity.class);
                    intent.putExtra("Action","DefaultDeleted");
                    intent.putExtra("ScheduleName",OldName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                }
                if(ElementCount==1)
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("DefaultSchedule");
                    editor.apply();
                    //attendanceDatabase.UpdateLastNotification(0,0);
                    NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
                    Objects.requireNonNull(notificationManager).cancelAll();
                    new AlarmsManager(mContext);
                    WidgetUpdater();
                    Intent intent = new Intent(mContext,MainActivity.class);
                    intent.putExtra("Action","LastDeleted");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                }
            }
        });
        holder.Default.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!scheduleCards.getScheduleName().equals(sharedPreferences.getString("DefaultSchedule","None")))
                {
                    String OldName = scheduleCards.getScheduleName();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("DefaultSchedule",scheduleCards.getScheduleName());
                    editor.apply();
                    NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
                    Objects.requireNonNull(notificationManager).cancelAll();
                    new AlarmsManager(mContext);
                    WidgetUpdater();
                    Intent intent = new Intent(mContext,MainActivity.class);
                    intent.putExtra("Action","DefaultChanged");
                    intent.putExtra("ScheduleName",OldName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                }
            }
        });
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)
            {
                AlertDialog.Builder RenameSchedule = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = ((FragmentActivity)mContext).getLayoutInflater();
                @SuppressLint("InflateParams")
                View dialogView = inflater.inflate(R.layout.dialog_rename,null);
                RelativeLayout DialogLayout = dialogView.findViewById(R.id.RenameDialogLayout);
                DialogLayout.requestFocus();
                final EditText ScheduleName = DialogLayout.findViewById(R.id.ScheduleNewName);
                RenameSchedule.setView(dialogView)
                        .setTitle("Rename "+scheduleCards.getScheduleName())
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(!ScheduleName.getText().toString().isEmpty())
                                {
                                    String OldName = scheduleCards.getScheduleName();
                                    String TableName = ScheduleName.getText().toString().replace(' ','_');
                                    newScheduleDatabase.DeleteLesson("ALTER TABLE "+scheduleCards.getScheduleName()+" RENAME TO "+TableName);
                                    attendanceDatabase.ExecuteRandomQuery("ALTER TABLE "+scheduleCards.getScheduleName()+" RENAME TO "+TableName);
                                    if(sharedPreferences.getString("DefaultSchedule","").equals(scheduleCards.getScheduleName()))
                                    {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("DefaultSchedule",TableName);
                                        editor.apply();
                                        //attendanceDatabase.UpdateLastNotification(0,0);
                                        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
                                        Objects.requireNonNull(notificationManager).cancelAll();
                                        new AlarmsManager(mContext);
                                        WidgetUpdater();
                                    }
                                    Intent intent = new Intent(mContext,MainActivity.class);
                                    intent.putExtra("Action","DefaultRenamed");
                                    intent.putExtra("ScheduleName",OldName);
                                    intent.putExtra("NewScheduleName",TableName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    mContext.startActivity(intent);
                                }
                                else
                                {
                                    Snackbar snackbar = Snackbar.make(v,"Please specify a name",Snackbar.LENGTH_LONG);
                                    View snack = snackbar.getView();
                                    snack.setBackgroundColor(Color.parseColor("#4052b5"));
                                    snackbar.show();
                                }
                            }
                        });
                AlertDialog Save = RenameSchedule.create();
                Save.show();
            }
        });
        holder.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(isExternalStorageWritable())
                {
                    File cacheDir = mContext.getCacheDir();
                    File file = new File(cacheDir,scheduleCards.getScheduleName()+".xml");
                    if(file.exists())file.delete();
                    try
                    {
                        ArrayList<String> ExportData = newScheduleDatabase.ExportData(scheduleCards.getScheduleName());
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        XmlSerializer xmlSerializer = Xml.newSerializer();
                        StringWriter stringWriter = new StringWriter();
                        xmlSerializer.setOutput(stringWriter);
                        xmlSerializer.startDocument("UTF-8", true);
                        xmlSerializer.startTag(null,"ScheduleName");
                        xmlSerializer.text(scheduleCards.getScheduleName());
                        xmlSerializer.endTag(null,"ScheduleName");
                        String Data = stringWriter.toString();
                        fileOutputStream.write(Data.getBytes());
                        for(String DataRecord:ExportData)
                        {
                            stringWriter.getBuffer().setLength(0);
                            String DataRecordTemp[] = DataRecord.split("_");
                            xmlSerializer.startTag(null, "Record");
                            xmlSerializer.startTag(null, "CellIndex");
                            xmlSerializer.text(DataRecordTemp[0]);
                            xmlSerializer.endTag(null, "CellIndex");
                            xmlSerializer.startTag(null, "Day");
                            xmlSerializer.text(DataRecordTemp[1]);
                            xmlSerializer.endTag(null, "Day");
                            xmlSerializer.startTag(null, "StartingTime");
                            xmlSerializer.text(DataRecordTemp[2]);
                            xmlSerializer.endTag(null, "StartingTime");
                            xmlSerializer.startTag(null, "EndingTime");
                            xmlSerializer.text(DataRecordTemp[3]);
                            xmlSerializer.endTag(null, "EndingTime");
                            xmlSerializer.startTag(null, "Subject");
                            xmlSerializer.text(DataRecordTemp[4]);
                            xmlSerializer.endTag(null, "Subject");
                            xmlSerializer.startTag(null, "Abbreviation");
                            xmlSerializer.text(DataRecordTemp[5]);
                            xmlSerializer.endTag(null, "Abbreviation");
                            xmlSerializer.startTag(null, "Teacher");
                            xmlSerializer.text(DataRecordTemp[6]);
                            xmlSerializer.endTag(null, "Teacher");
                            xmlSerializer.startTag(null, "Venue");
                            xmlSerializer.text(DataRecordTemp[7]);
                            xmlSerializer.endTag(null, "Venue");
                            xmlSerializer.startTag(null, "Type");
                            xmlSerializer.text(DataRecordTemp[8]);
                            xmlSerializer.endTag(null, "Type");
                            xmlSerializer.startTag(null, "CellColor");
                            xmlSerializer.text(DataRecordTemp[9]);
                            xmlSerializer.endTag(null, "CellColor");
                            xmlSerializer.startTag(null, "Importance");
                            xmlSerializer.text(DataRecordTemp[10]);
                            xmlSerializer.endTag(null, "Importance");
                            xmlSerializer.endTag(null, "Record");
                            String RecordData = stringWriter.toString();
                            fileOutputStream.write(RecordData.getBytes());
                        }
                        stringWriter.getBuffer().setLength(0);
                        xmlSerializer.endDocument();
                        xmlSerializer.flush();
                        Data = stringWriter.toString();
                        fileOutputStream.write(Data.getBytes());
                        fileOutputStream.close();

                        File cacheFile = new File(mContext.getCacheDir(),scheduleCards.getScheduleName()+".xml");
                        Uri uri = FileProvider.getUriForFile(mContext,"com.puchd.puplanner.fileProvider",cacheFile);
                        Intent intent = ShareCompat.IntentBuilder.from((Activity) mContext)
                                .setChooserTitle("Share schedule")
                                .setType("text/xml")
                                .setStream(uri)
                                .createChooserIntent()
                                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        mContext.startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void WidgetUpdater()
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, WidgetProvider_Day.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.WidgetDayList);
    }

}
