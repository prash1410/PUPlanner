package com.puchd.puplanner;


public class DayLessons
{
    Integer LessonColor;
    String LessonName;
    String LessonAbbreviation;
    String LessonDayTime;
    String LessonType;
    String LessonTeacher;
    String LessonVenue;

    public DayLessons(Integer LessonColor, String LessonName, String LessonAbbreviation, String LessonDayTime, String LessonType,String LessonTeacher,String LessonVenue)
    {
        this.LessonColor = LessonColor;
        this.LessonName = LessonName;
        this.LessonAbbreviation = LessonAbbreviation;
        this.LessonDayTime = LessonDayTime;
        this.LessonType = LessonType;
        this.LessonTeacher = LessonTeacher;
        this.LessonVenue = LessonVenue;
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

    public String getLessonType()
    {
        return LessonType;
    }
    public String getLessonTeacher()
    {
        return LessonTeacher;
    }
    public String getLessonVenue()
    {
        return LessonVenue;
    }
}
