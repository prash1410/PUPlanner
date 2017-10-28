package com.puchd.puplanner;

public class DatabaseError
{
    Integer LessonColor;
    String LessonName;
    String LessonAbbreviation;
    String LessonDayTime;

    public DatabaseError(Integer LessonColor, String LessonName, String LessonAbbreviation, String LessonDayTime)
    {
        this.LessonColor = LessonColor;
        this.LessonName = LessonName;
        this.LessonAbbreviation = LessonAbbreviation;
        this.LessonDayTime = LessonDayTime;
    }

    public Integer getLessonColor()
    {
        return LessonColor;
    }

    public String getLessonName()
    {
        return LessonName;
    }

    public String getLessonAbbreviation()
    {
        return LessonAbbreviation;
    }

    public String getLessonDayTime()
    {
        return LessonDayTime;
    }
}
