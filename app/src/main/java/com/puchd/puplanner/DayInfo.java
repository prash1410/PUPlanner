package com.puchd.puplanner;

import java.util.ArrayList;

public class DayInfo
{
    private String DayName;
    private ArrayList<TimeInfo> timeList = new ArrayList<>();
    public String getDayName()
    {
        return DayName;
    }
    public void setDayName(String DayName)
    {
        this.DayName = DayName;
    }
    public ArrayList<TimeInfo> gettimeList()
    {
        return timeList;
    }
    public void settimeList(ArrayList<TimeInfo>timeList)
    {
        this.timeList = timeList;
    }
}
