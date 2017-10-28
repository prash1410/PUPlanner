package com.puchd.puplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper
{
    static String DatabaseName = "UserProfile";
    SQLiteDatabase database;

    public Database(Context context)
    {
        super(context,DatabaseName,null,1);
        database = context.openOrCreateDatabase(DatabaseName,SQLiteDatabase.CREATE_IF_NECESSARY,null);
        String query = "CREATE TABLE IF NOT EXISTS UserProfileTable (ProfileType VARCHAR, Department VARCHAR, Branch VARCHAR, Semester VARCHAR, Name VARCHAR, RollNo VARCHAR, Email VARCHAR, ProfileImageURL VARCHAR)";
        database.execSQL(query);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public long CreateProfile(String profile, String deptt, String branch, String Semester, String n, String rollno, String email, String URL)
    {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProfileType",profile);
        contentValues.put("Department",deptt);
        contentValues.put("Branch",branch);
        contentValues.put("Semester",Semester);
        contentValues.put("Name",n);
        contentValues.put("RollNo",rollno);
        contentValues.put("Email",email);
        contentValues.put("ProfileImageURL",URL);
        long result = database.insert("UserProfileTable",null,contentValues);
        return result;
    }

    public boolean UserPresent()
    {
        Cursor cursor = database.rawQuery("SELECT * FROM UserProfileTable",null);
        if(cursor.moveToFirst())
        {
            cursor.close();
            return true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }

    public String FetchData(String entry)
    {
        Cursor cursor = database.rawQuery("SELECT * FROM UserProfileTable",null);
        String data = "";
        if(cursor.moveToFirst())
        {
            data = cursor.getString(cursor.getColumnIndex(entry));
            cursor.close();
            return data;
        }
        else
        {
            cursor.close();
            return data;
        }
    }
}
