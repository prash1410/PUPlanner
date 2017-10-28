package com.puchd.puplanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;


public class FileReceiver extends Activity
{
    Boolean StartingTagValid = false,EndingTagValid=false;
    int CellIndex = 0,CellColor=0,Importance=0;
    String Day="",StartingTime="",EndingTime="",Subject="",Abbreviation="",Teacher="",Venue="",Type="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_VIEW.equals(action))
        {
            String ScheduleData;
            try
            {
                ScheduleData = readTextFromUri(getIntent().getData());
                if(ValidXML(ScheduleData))
                {
                    XMLParser(ScheduleData);
                    Intent i = new Intent(getApplicationContext(),NewSchedule.class);
                    i.putExtra("Caller","Import");
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Selected XML doesn't appear to contain a valid schedule",Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean ValidXML(String XMLData) throws XmlPullParserException,IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(XMLData));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if(xpp.getName().equals("ScheduleName"))StartingTagValid=true;
                    break;
                case XmlPullParser.END_TAG:
                    if(xpp.getName().equals("ScheduleName"))EndingTagValid=true;
            }
            eventType = xpp.next();
        }
        if(StartingTagValid && EndingTagValid)return true;
        return false;
    }

    public void XMLParser(String XMLData) throws XmlPullParserException,IOException
    {
        NewScheduleDatabase newScheduleDatabase = new NewScheduleDatabase(getApplicationContext());
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(XMLData));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if(xpp.getName().equals("CellIndex"))CellIndex = Integer.valueOf(xpp.nextText());
                    if(xpp.getName().equals("Day"))Day = xpp.nextText();
                    if(xpp.getName().equals("StartingTime"))StartingTime = xpp.nextText();
                    if(xpp.getName().equals("EndingTime"))EndingTime = xpp.nextText();
                    if(xpp.getName().equals("Subject"))Subject = xpp.nextText();
                    if(xpp.getName().equals("Abbreviation"))Abbreviation = xpp.nextText();
                    if(xpp.getName().equals("Teacher"))Teacher = xpp.nextText();
                    if(xpp.getName().equals("Venue"))Venue = xpp.nextText();
                    if(xpp.getName().equals("Type"))Type = xpp.nextText();
                    if(xpp.getName().equals("CellColor"))CellColor = Integer.valueOf(xpp.nextText());
                    if(xpp.getName().equals("Importance"))
                    {
                        if(xpp.nextText().equals(""))Importance = 0;
                        else Importance = 1;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(xpp.getName().equals("Record")) newScheduleDatabase.InsertLesson(CellIndex,Day,StartingTime,EndingTime,Subject,Abbreviation,Teacher,Venue,Type,CellColor,Importance);
            }
            eventType = xpp.next();
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line);
        }
        inputStream.close();
        return stringBuilder.toString();
    }
}
