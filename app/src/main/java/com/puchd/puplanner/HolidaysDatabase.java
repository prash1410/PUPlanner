package com.puchd.puplanner;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class HolidaysDatabase extends SQLiteOpenHelper
{
    @SuppressLint("WrongConstant")
    public HolidaysDatabase(Context context)
    {
        super(context,"HolidaysBase",null,1);
        SQLiteDatabase database;
        database = context.openOrCreateDatabase("HolidaysBase",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        String query = "CREATE TABLE IF NOT EXISTS Holidays(Day INTEGER, Month INTEGER, Year INTEGER, Title VARCHAR, Description VARCHAR, Days INTEGER, PRIMARY KEY (Day,Month,Year))";
        database.execSQL(query);
        database.close();
    }

    public boolean InsertHoliday(Integer Day, Integer Month, Integer Year, String Title, String Description, Integer Days)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Day",Day);
        contentValues.put("Month",Month);
        contentValues.put("Year",Year);
        contentValues.put("Title",Title);
        contentValues.put("Description",Description);
        contentValues.put("Days",Days);
        long result = sqLiteDatabase.insert("Holidays",null,contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }

    public ArrayList<String> FetchHolidays()
    {
        ArrayList<String> Data = new ArrayList();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Holidays",null);
        while (cursor.moveToNext())
        {
            Data.add(""+cursor.getInt(cursor.getColumnIndex("Day"))+"_"+cursor.getInt(cursor.getColumnIndex("Month"))+"_"+cursor.getInt(cursor.getColumnIndex("Year"))+"_"+cursor.getString(cursor.getColumnIndex("Title"))+"_"+cursor.getInt(cursor.getColumnIndex("Days"))+"_"+cursor.getString(cursor.getColumnIndex("Description")));
        }
        cursor.close();
        sqLiteDatabase.close();
        return Data;
    }

    public boolean NoHolidaysAdded()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM Holidays";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        Integer Count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        return Count < 1;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
