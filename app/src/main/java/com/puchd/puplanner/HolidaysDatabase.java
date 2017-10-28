package com.puchd.puplanner;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HolidaysDatabase extends SQLiteOpenHelper
{
    public HolidaysDatabase(Context context)
    {
        super(context,"HolidaysBase",null,1);
        SQLiteDatabase database;
        database = context.openOrCreateDatabase("HolidaysBase",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        String query1 = "DROP TABLE IF EXISTS HOLIDAYS";
        database.execSQL(query1);
        String query = "CREATE TABLE IF NOT EXISTS Holidays(Day INTEGER, Month INTEGER, YEAR Integer, Title VARCHAR, Description VARCHAR)";
        database.execSQL(query);
        database.close();
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
