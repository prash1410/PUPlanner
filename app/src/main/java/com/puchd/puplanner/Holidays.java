package com.puchd.puplanner;


public class Holidays
{
    Integer intday,Month,Year;
    String Title, Description;

    public Holidays (Integer day, Integer Month, Integer Year, String Title, String Description)
    {
        this.intday = day;
        this.Month = Month;
        this.Year = Year;
        this.Title = Title;
        this.Description = Description;
    }

    public Integer getintday()
    {
        return intday;
    }

    public Integer getMonth()
    {
        return Month;
    }

    public Integer getYear()
    {
        return Year;
    }

    public String getTitle()
    {
        return Title;
    }

    public String getDescription()
    {
        return Description;
    }
}
