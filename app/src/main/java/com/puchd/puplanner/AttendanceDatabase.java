package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AttendanceDatabase extends SQLiteOpenHelper
{
    private static String DatabaseName = "AttendanceBase";
    private SQLiteDatabase database;

    @SuppressLint("WrongConstant")
    public AttendanceDatabase (Context context)
    {
        super(context,DatabaseName,null,1);
        database = context.openOrCreateDatabase(DatabaseName,SQLiteDatabase.CREATE_IF_NECESSARY,null);
        database.close();
    }

    public void CreateTables(String TableName)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TableName+" (Subject VARCHAR PRIMARY KEY, Attended INTEGER, TotalLectures INTEGER, StartingDay VARCHAR)");
        sqLiteDatabase.close();
    }

    public void DeleteTable(String TableName)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TableName);
        sqLiteDatabase.close();
    }

    public void InsertSubject(String TableName, String Subject, String StartingDay)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Subject",Subject);
        contentValues.put("Attended",0);
        contentValues.put("TotalLectures",0);
        contentValues.put("StartingDay",StartingDay);
        sqLiteDatabase.insert(TableName,null,contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<String> GetSubjects(String TableName)
    {
        ArrayList<String>Data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Subject FROM "+TableName,null);
        while (cursor.moveToNext())
        {
            Data.add(cursor.getString(cursor.getColumnIndex("Subject")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return Data;
    }

    public void DeleteSubject(String TableName,String Subject)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+TableName+" WHERE Subject='"+Subject+"'");
        sqLiteDatabase.close();
    }

    public ArrayList<String> GetData(String Table)
    {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Subject,StartingDay FROM "+Table,null);
        while(cursor.moveToNext())
        {
            data.add(cursor.getString(cursor.getColumnIndex("Subject"))+"_"+cursor.getString(cursor.getColumnIndex("StartingDay")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    public String GetStartingDate(String TableName, String Subject)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT StartingDay FROM "+TableName+" WHERE Subject='"+Subject+"'",null);
        cursor.moveToFirst();
        String Data = cursor.getString(cursor.getColumnIndex("StartingDay"));
        cursor.close();
        sqLiteDatabase.close();
        return Data;
    }

    public void UpdateStartingDate(String TableName,String Subject, String StartingDate)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+TableName+" SET StartingDay='"+StartingDate+"' WHERE Subject='"+Subject+"'");
        sqLiteDatabase.close();
    }

    public void ExecuteRandomQuery(String query)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    public void UpdateAttendanceLectures(String TableName,String Subject,Integer Lectures)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+TableName+" SET TotalLectures='"+Lectures+"' WHERE Subject='"+Subject+"'");
        sqLiteDatabase.close();
    }
    public void UpdateAttendedLectures(String TableName,String Subject,Integer Lectures)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE "+TableName+" SET Attended='"+Lectures+"' WHERE Subject='"+Subject+"'");
        sqLiteDatabase.close();
    }

    public String GetAttendanceData(String TableName,String Subject)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Attended,TotalLectures FROM "+TableName+" WHERE Subject='"+Subject+"'",null);
        cursor.moveToFirst();
        String data = cursor.getInt(cursor.getColumnIndex("Attended"))+"_"+cursor.getInt(cursor.getColumnIndex("TotalLectures"));
        cursor.close();
        sqLiteDatabase.close();
        return data;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
