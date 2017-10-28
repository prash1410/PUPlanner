package com.puchd.puplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NewScheduleDatabase extends SQLiteOpenHelper
{
    static String DatabaseName = "NewSchedule";
    SQLiteDatabase database;

    public NewScheduleDatabase(Context context)
    {
        super(context,DatabaseName,null,1);
        database = context.openOrCreateDatabase(DatabaseName,SQLiteDatabase.CREATE_IF_NECESSARY,null);
        String query = "CREATE TABLE IF NOT EXISTS NewScheduleTable (CellIndex Integer, Day VARCHAR, StartingTime VARCHAR, EndingTime Varchar, Subject VARCHAR, Abbreviation VARCHAR, Teacher VARCHAR, Venue VARCHAR, Type VARCHAR, CellColor Integer, Importance Integer)";
        database.execSQL(query);
        query = "CREATE TABLE IF NOT EXISTS TimestampData (ScheduleName VARCHAR, DateCreated VARCHAR, DateModified VARCHAR)";
        database.execSQL(query);
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList getAllRows() {
        ArrayList<String> rowList = new ArrayList<>();
        String selectQuery = "SELECT * FROM NewScheduleTable";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext())
        {
            rowList.add(""+cursor.getInt(cursor.getColumnIndex("CellIndex"))+"_"+cursor.getString(cursor.getColumnIndex("Abbreviation"))+"_"+cursor.getInt(cursor.getColumnIndex("CellColor"))+"_"+cursor.getString(cursor.getColumnIndex("StartingTime"))+"_"+cursor.getString(cursor.getColumnIndex("EndingTime")));
        }
        cursor.close();
        db.close();

        return rowList;
    }

    public ArrayList FetchLessonTimings(String Day, String StartEnd)
    {
        String query = "SELECT * FROM NewScheduleTable WHERE Day='"+Day+"'";
        ArrayList<String>Data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext())
        {
            Data.add(cursor.getString(cursor.getColumnIndex(StartEnd)));
        }
        cursor.close();
        db.close();
        return Data;
    }

    public ArrayList FetchLesson(int CellIndex, String Abbreviation, String Day, String StartingTime)
    {
        String query = "";
        if(Day.isEmpty())
        {
            query = "SELECT * FROM NewScheduleTable WHERE CellIndex="+CellIndex+" AND Abbreviation='"+Abbreviation+"'";
        }
        if(Abbreviation.isEmpty())
        {
            query = "SELECT * FROM NewScheduleTable WHERE CellIndex="+CellIndex+" AND StartingTime='"+StartingTime+"'";
        }
        ArrayList<String> Data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            Data.add(""+cursor.getInt(cursor.getColumnIndex("CellIndex")));
            Data.add(cursor.getString(cursor.getColumnIndex("Day")));
            Data.add(cursor.getString(cursor.getColumnIndex("StartingTime")));
            Data.add(cursor.getString(cursor.getColumnIndex("EndingTime")));
            Data.add(cursor.getString(cursor.getColumnIndex("Subject")));
            Data.add(cursor.getString(cursor.getColumnIndex("Abbreviation")));
            Data.add(cursor.getString(cursor.getColumnIndex("Teacher")));
            Data.add(cursor.getString(cursor.getColumnIndex("Venue")));
            Data.add(cursor.getString(cursor.getColumnIndex("Type")));
            Data.add(""+cursor.getInt(cursor.getColumnIndex("CellColor")));
            Data.add(""+cursor.getInt(cursor.getColumnIndex("Importance")));
            cursor.close();
            db.close();
            return Data;
        }
        cursor.close();
        db.close();
        return Data;
    }



    public void DeleteLesson(String Query)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(Query);
        db.close();
    }

    public long RowCount()
    {
        database = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(database, "NewScheduleTable");
        database.close();
        return cnt;
    }

    public boolean InsertLesson(Integer CellIndex, String Day, String StartingTime, String EndingTime, String Subject, String Abbreviation, String Teacher, String Venue, String Type, Integer CellColor, Integer Importance)
    {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CellIndex",CellIndex);
        contentValues.put("Day",Day);
        contentValues.put("StartingTime",StartingTime);
        contentValues.put("EndingTime",EndingTime);
        contentValues.put("EndingTime",EndingTime);
        contentValues.put("Subject",Subject);
        contentValues.put("Abbreviation",Abbreviation);
        contentValues.put("Teacher",Teacher);
        contentValues.put("Venue",Venue);
        contentValues.put("Type",Type);
        contentValues.put("CellColor",CellColor);
        contentValues.put("Importance",Importance);
        long result = database.insert("NewScheduleTable",null,contentValues);
        database.close();
        if(result!=-1)return true;
        return false;
    }


    public ArrayList<String> DayLessons(String Column, String Day)
    {
        ArrayList<String> data = new ArrayList();
        String query = "SELECT * FROM NewScheduleTable WHERE Day='"+Day+"' ORDER BY CellIndex ASC";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext())
        {
            data.add(cursor.getString(cursor.getColumnIndex(Column)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    public ArrayList<Integer> DayLessonsColor(String Day)
    {
        ArrayList<Integer> data = new ArrayList();
        String query = "SELECT * FROM NewScheduleTable WHERE Day='"+Day+"' ORDER BY CellIndex ASC";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext())
        {
            data.add(cursor.getInt(cursor.getColumnIndex("CellColor")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    public void SaveNewSchedule(String ScheduleName)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ScheduleName);
        sqLiteDatabase.execSQL("CREATE TABLE "+ScheduleName+" AS SELECT * FROM NewScheduleTable");
    }

    public int GetTableCount()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'sqlite_sequence' AND name != 'NewScheduleTable' AND name != 'TimestampData';",null);
        int count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        return count;
    }

    public ArrayList<String> GetTableNames()
    {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata' AND name != 'sqlite_sequence' AND name != 'NewScheduleTable' AND name != 'TimestampData';",null);
        while(cursor.moveToNext())
        {
            data.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    public ArrayList<String> GetDataForAttendance(String Table)
    {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Subject,Type,CellColor,Teacher,Importance FROM "+Table,null);
        while(cursor.moveToNext())
        {
            data.add(""+cursor.getString(cursor.getColumnIndex("Subject"))+"_"+cursor.getString(cursor.getColumnIndex("Type"))+"_"+cursor.getString(cursor.getColumnIndex("Teacher"))+"_"+cursor.getInt(cursor.getColumnIndex("CellColor"))+"_"+cursor.getInt(cursor.getColumnIndex("Importance")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    public void CreateTimestamp(String Query)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.rawQuery(Query,null);
        sqLiteDatabase.close();
    }

    public ArrayList<String> GetLectureInstances(String TableName,String SubjectName)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String>Data = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Day FROM "+TableName+" WHERE Subject='"+SubjectName+"'",null);
        while (cursor.moveToNext())
        {
            Data.add(cursor.getString(cursor.getColumnIndex("Day")));
        }
        cursor.close();
        sqLiteDatabase.close();
        ArrayList<String> FinalData = new ArrayList<>();
        ArrayList<String> TempData = new ArrayList<>();
        for(String d:Data)
        {
            Boolean Present = false;
            for(String TD:TempData)
            {
                if(d.equals(TD))Present=true;
            }
            if(!Present)
            {
                TempData.add(d);
                int DayCounter = 0;
                for(int i=0;i<Data.size();i++)
                {
                    if(d.equals(Data.get(i)))DayCounter++;
                }
                if(d.equals("Sunday"))FinalData.add(5+"_"+DayCounter);
                if(d.equals("Monday"))FinalData.add(6+"_"+DayCounter);
                if(d.equals("Tuesday"))FinalData.add(7+"_"+DayCounter);
                if(d.equals("Wednesday"))FinalData.add(1+"_"+DayCounter);
                if(d.equals("Thursday"))FinalData.add(2+"_"+DayCounter);
                if(d.equals("Friday"))FinalData.add(3+"_"+DayCounter);
                if(d.equals("Saturday"))FinalData.add(4+"_"+DayCounter);
            }
        }
        return FinalData;
    }

    public ArrayList<String> NotificationFeeder(String TableName,String Day)
    {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT StartingTime FROM "+TableName+" WHERE Day='"+Day+"' ORDER BY CellIndex ASC",null);
        while(cursor.moveToNext())
        {
            data.add(cursor.getString(cursor.getColumnIndex("StartingTime")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    public String FinalNotificationFeeder(String TableName,String Day,String StartingTime)
    {
        String data;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Subject,StartingTime,EndingTime,Type,Teacher,Importance,Venue,CellColor,CellIndex FROM "+TableName+" WHERE Day='"+Day+"' AND StartingTime='"+StartingTime+"'",null);
        cursor.moveToFirst();
        data = (""+cursor.getString(cursor.getColumnIndex("Subject"))+"_"+cursor.getString(cursor.getColumnIndex("StartingTime"))+"_"+cursor.getString(cursor.getColumnIndex("EndingTime"))+"_"+cursor.getString(cursor.getColumnIndex("Type"))+"_"+cursor.getString(cursor.getColumnIndex("Teacher"))+"_"+cursor.getString(cursor.getColumnIndex("Venue"))+"_"+cursor.getInt(cursor.getColumnIndex("Importance"))+"_"+cursor.getInt(cursor.getColumnIndex("CellColor"))+"_"+cursor.getInt(cursor.getColumnIndex("CellIndex")));
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    public ArrayList ExportData(String TableName)
    {
        ArrayList<String> rowList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TableName;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext())
        {
            rowList.add(""+cursor.getInt(cursor.getColumnIndex("CellIndex"))+"_"+cursor.getString(cursor.getColumnIndex("Day"))+"_"+cursor.getString(cursor.getColumnIndex("StartingTime"))+"_"+cursor.getString(cursor.getColumnIndex("EndingTime"))+"_"+cursor.getString(cursor.getColumnIndex("Subject"))+"_"+cursor.getString(cursor.getColumnIndex("Abbreviation"))+"_"+cursor.getString(cursor.getColumnIndex("Teacher"))+"_"+cursor.getString(cursor.getColumnIndex("Venue"))+"_"+cursor.getString(cursor.getColumnIndex("Type"))+"_"+cursor.getInt(cursor.getColumnIndex("CellColor"))+"_"+cursor.getInt(cursor.getColumnIndex("Importance")));
        }
        cursor.close();
        db.close();
        return rowList;
    }

    public ArrayList<String> DayWidgetFeeder(String TableName,String Day)
    {
        ArrayList<String> Data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TableName+" WHERE Day='"+Day+"' ORDER BY CellIndex ASC",null);
        while (cursor.moveToNext())
        {
            Data.add(cursor.getString(cursor.getColumnIndex("Subject"))+"_"+cursor.getString(cursor.getColumnIndex("StartingTime"))+"_"+cursor.getString(cursor.getColumnIndex("EndingTime"))+"_"+cursor.getInt(cursor.getColumnIndex("CellColor"))+"_"+cursor.getString(cursor.getColumnIndex("Teacher"))+"_"+cursor.getString(cursor.getColumnIndex("Venue"))+"_"+cursor.getString(cursor.getColumnIndex("Type")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return Data;
    }
}
