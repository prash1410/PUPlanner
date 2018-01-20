package com.puchd.puplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;


public class HomeFragment extends Fragment implements View.OnClickListener
{

    int[] DateCardImages = new int[]{R.drawable.datecard1,R.drawable.datecard2,R.drawable.datecard3,R.drawable.datecard4,R.drawable.datecard5,R.drawable.datecard6};
    TextView TimeDateView,TimeDayView;
    NestedScrollView nestedScrollView;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionMenu floatingActionMenu;
    ImageView TimeContextualImage;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FloatingActionButton floatingActionButton,floatingActionButton1;
    NewScheduleDatabase newScheduleDatabase;
    int TableCount = 1;
    View v;
    int CellIndex = 0,CellColor=0,Importance=0;
    String Day="",StartingTime="",EndingTime="",Subject="",Abbreviation="",Teacher="",Venue="",Type="";
    Boolean StartingTagValid=false,EndingTagValid=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        newScheduleDatabase = new NewScheduleDatabase(getActivity());
        TableCount = newScheduleDatabase.GetTableCount();
        TimeContextualImage = (ImageView)v.findViewById(R.id.TimeContextualImage);
        Random random = new Random();
        TimeContextualImage.setBackgroundResource(DateCardImages[random.nextInt(DateCardImages.length)]);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        TimeDateView = (TextView)v.findViewById(R.id.TimeDateTextView);
        TimeDayView = (TextView)v.findViewById(R.id.TimeDayTextView);
        Calendar calendar = Calendar.getInstance();
        String Date = getFormattedDate(),Day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());
        TimeDateView.setText(Html.fromHtml(Date));
        TimeDayView.setText(Day);
        TimeDateView.startAnimation(fadeInAnimation);
        TimeDayView.startAnimation(fadeInAnimation);
        final RelativeLayout relativeLayout1 = (RelativeLayout)v.findViewById(R.id.Parent);
        final RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.card_recycler_view);
        nestedScrollView = (NestedScrollView)v.findViewById(R.id.NestedScroll);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if(scrollY>oldScrollY) floatingActionMenu.hideMenuButton(true);
                else floatingActionMenu.showMenuButton(true);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                Fragment fragment = new HomeFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });
        floatingActionButton = (FloatingActionButton)v.findViewById(R.id.CreateNewScheduleFAB);
        floatingActionButton.setOnClickListener(this);
        floatingActionButton1 = (FloatingActionButton)v.findViewById(R.id.ImportNewScheduleFAB);
        floatingActionButton1.setOnClickListener(this);
        floatingActionMenu = (FloatingActionMenu)v.findViewById(R.id.fab_menu);
        floatingActionMenu.hideMenuButton(true);
        floatingActionMenu.showMenuButton(true);
        floatingActionMenu.bringToFront();
        floatingActionMenu.setClosedOnTouchOutside(true);
        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened)
            {
                if (opened) floatingActionMenu.setBackgroundColor(Color.parseColor("#DAEAEAEA"));
                else floatingActionMenu.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new UnscrollableLayoutManager(getActivity()));
        recyclerView.setAdapter(new DataAdapter());

        recyclerView.setOnTouchListener(new SwipeDismissTouchListener(recyclerView, null, new SwipeDismissTouchListener.DismissCallbacks()
        {
            @Override
            public boolean canDismiss(Object token)
            {
                return true;
            }

            @Override
            public void onDismiss(View view, Object token)
            {
                editor = preferences.edit();
                editor.putString("Dismissed","yes");
                editor.apply();
                relativeLayout1.removeView(recyclerView);
            }
        }));
        if(TableCount>1)
        {
            editor = preferences.edit();
            editor.putString("Dismissed","yes");
            editor.apply();
        }
        if(!preferences.getString("Dismissed","").isEmpty() || TableCount>=1)relativeLayout1.removeView(recyclerView);
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Dashboard");
        final Animation FadeIn = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
        final Animation FadeOut = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_out);
        final Runnable r = new Runnable() {public void run()
        {
            Random random = new Random();
            TimeContextualImage.startAnimation(FadeOut);
            TimeContextualImage.setBackgroundResource(DateCardImages[random.nextInt(DateCardImages.length)]);
            TimeContextualImage.startAnimation(FadeIn);
            TimeContextualImage.postDelayed(this, 10000);
        }};
        TimeContextualImage.postDelayed(r,10000);
        if(newScheduleDatabase.GetTableCount()>0)
        {
            TextView UpcomingTV = (TextView)view.findViewById(R.id.UpcomingClassesTextView);
            UpcomingTV.setVisibility(View.VISIBLE);
            View UpcomingLiner = view.findViewById(R.id.UpcomingClassesTextViewLiner);
            UpcomingLiner.setVisibility(View.VISIBLE);
            int Counter = 0;
            RelativeLayout OngoingClassesCardsContainer = (RelativeLayout)view.findViewById(R.id.OngoingClassesCardsContainer);
            RelativeLayout UpcomingClassesCardsContainer = (RelativeLayout)view.findViewById(R.id.UpcomingClassesCardsContainer);
            Calendar calendar = Calendar.getInstance();
            ArrayList<String> LessonList = newScheduleDatabase.DayWidgetFeeder(preferences.getString("DefaultSchedule",""),new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime()));
            for(String Lesson:LessonList)
            {
                String Temp[] = Lesson.split("_");
                int StartingHour = getHour(Temp[1]),StartingMinute = getMinute(Temp[1]),EndingHour = getHour(Temp[2]),EndingMinute = getMinute(Temp[2]);
                int StartingTime=0,EndingTime=0;
                if(StartingMinute<10) StartingTime = Integer.valueOf(""+StartingHour+"0"+StartingMinute);
                else StartingTime = Integer.valueOf(""+StartingHour+""+StartingMinute);
                if(EndingMinute<10)EndingTime = Integer.valueOf(""+EndingHour+"0"+EndingMinute);
                else EndingTime = Integer.valueOf(""+EndingHour+""+EndingMinute);
                int CurrentHour = new Time(System.currentTimeMillis()).getHours();
                int CurrentMinute = new Time(System.currentTimeMillis()).getMinutes();
                int CurrentTime = 0;
                if(CurrentMinute<10)CurrentTime = Integer.valueOf(""+CurrentHour+"0"+CurrentMinute);
                else CurrentTime = Integer.valueOf(""+CurrentHour+""+CurrentMinute);
                if(CurrentTime>=StartingTime && CurrentTime<EndingTime)
                {
                    CardView cardView = new CardView(getActivity());
                    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.card_ongoing_class, null);
                    RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.OngoingDetailsContainer);
                    RelativeLayout relativeLayout1 = (RelativeLayout)v.findViewById(R.id.OngoingCardWrapper);
                    TextView OngoingSubjectView = (TextView)v.findViewById(R.id.OngoingSubjectName);
                    TextView OngoingTimeView = (TextView)v.findViewById(R.id.OngoingSubjectTimings);
                    TextView OngoingSubjectTeacherType = (TextView)v.findViewById(R.id.OngoingSubjectTeacherType);
                    TextView OngoingText = (TextView)v.findViewById(R.id.Ongoing);
                    TextView OngoingTimeRemaining = (TextView)v.findViewById(R.id.OngoingTimeRemaining);
                    OngoingSubjectView.setText(Temp[0]);
                    OngoingTimeView.setText(Temp[1]+" to "+Temp[2]);
                    try {OngoingSubjectTeacherType.setText(Temp[4]);}
                    catch(ArrayIndexOutOfBoundsException exception) {OngoingSubjectTeacherType.setVisibility(View.GONE);}
                    String Ongoing;
                    try {Ongoing = "Ongoing "+Temp[6]+" ~ "+getTimeDifference(CurrentHour,CurrentMinute,StartingHour,StartingMinute)+" ago";}
                    catch(ArrayIndexOutOfBoundsException exception) {Ongoing = "Ongoing class ~ "+getTimeDifference(CurrentHour,CurrentMinute,StartingHour,StartingMinute)+" ago";}
                    try {Ongoing += " ~ "+Temp[5];}
                    catch(ArrayIndexOutOfBoundsException exception) {}
                    OngoingText.setText(Ongoing);
                    String RemainingTime = "Ends in "+getTimeDifference(CurrentHour,CurrentMinute,EndingHour,EndingMinute);
                    OngoingTimeRemaining.setText(RemainingTime);
                    GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {0, Integer.valueOf(Temp[3])});
                    relativeLayout.setBackgroundDrawable(gd);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setStroke(1,Color.GRAY);
                    gradientDrawable.setCornerRadius(9);
                    relativeLayout1.setBackgroundDrawable(gradientDrawable);
                    cardView.addView(v);
                    cardView.setId(Counter);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    if(Counter>0) params.addRule(RelativeLayout.BELOW,Counter-1);
                    params.setMargins(0,0,0,15);
                    cardView.setLayoutParams(params);
                    cardView.setElevation(15);
                    cardView.setRadius(9);
                    OngoingClassesCardsContainer.addView(cardView);
                    Counter++;
                }
            }
            if(Counter>0)
            {
                TextView textView = (TextView)view.findViewById(R.id.OngoingClassesTextView);
                textView.setVisibility(View.VISIBLE);
                View view1 = view.findViewById(R.id.OngoingClassesTextViewLiner);
                view1.setVisibility(View.VISIBLE);
            }
            int UpcomingCounter = 1410;
            boolean ImmediateNextFound = false;
            for(String Lesson:LessonList)
            {
                String Temp[] = Lesson.split("_");
                int StartingHour = getHour(Temp[1]), StartingMinute = getMinute(Temp[1]), EndingHour = getHour(Temp[2]), EndingMinute = getMinute(Temp[2]);
                int StartingTime;
                if (StartingMinute < 10) StartingTime = Integer.valueOf("" + StartingHour + "0" + StartingMinute);
                else StartingTime = Integer.valueOf("" + StartingHour + "" + StartingMinute);
                int CurrentHour = new Time(System.currentTimeMillis()).getHours();
                int CurrentMinute = new Time(System.currentTimeMillis()).getMinutes();
                int CurrentTime = 0;
                if (CurrentMinute < 10) CurrentTime = Integer.valueOf("" + CurrentHour + "0" + CurrentMinute);
                else CurrentTime = Integer.valueOf("" + CurrentHour + "" + CurrentMinute);

                if(ImmediateNextFound)
                {
                    CardView cardView = new CardView(getActivity());
                    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.card_pending_classes, null);
                    RelativeLayout relativeLayout1 = (RelativeLayout)v.findViewById(R.id.PendingCardWrapper);
                    TextView OngoingSubjectView = (TextView)v.findViewById(R.id.PendingSubjectName);
                    TextView OngoingTimeView = (TextView)v.findViewById(R.id.PendingSubjectTimings);
                    OngoingSubjectView.setText(Temp[0]);
                    OngoingTimeView.setText(Temp[1]+" to "+Temp[2]);
                    GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {0, Integer.valueOf(Temp[3])});
                    relativeLayout1.setBackgroundDrawable(gd);
                    cardView.addView(v);
                    cardView.setId(UpcomingCounter);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW,UpcomingCounter-1);
                    params.setMargins(0,0,0,10);
                    cardView.setLayoutParams(params);
                    cardView.setElevation(15);
                    cardView.setRadius(9);
                    UpcomingClassesCardsContainer.addView(cardView);
                    UpcomingCounter++;
                }

                if(CurrentTime<StartingTime && !ImmediateNextFound)
                {
                    CardView cardView = new CardView(getActivity());
                    LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.card_upcoming_classes, null);
                    RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.UpcomingDetailsContainer);
                    RelativeLayout relativeLayout1 = (RelativeLayout)v.findViewById(R.id.UpcomingCardWrapper);
                    TextView OngoingSubjectView = (TextView)v.findViewById(R.id.UpcomingSubjectName);
                    TextView OngoingTimeView = (TextView)v.findViewById(R.id.UpcomingSubjectTimings);
                    TextView OngoingSubjectTeacherType = (TextView)v.findViewById(R.id.UpcomingSubjectTeacherType);
                    TextView OngoingText = (TextView)v.findViewById(R.id.Upcoming);
                    OngoingSubjectView.setText(Temp[0]);
                    OngoingTimeView.setText(Temp[1]+" to "+Temp[2]);
                    try {OngoingSubjectTeacherType.setText(Temp[4]);}
                    catch(ArrayIndexOutOfBoundsException exception) {OngoingSubjectTeacherType.setVisibility(View.GONE);}
                    String Ongoing;
                    try {Ongoing = "Upcoming "+Temp[6]+" ~ "+getTimeDifference(CurrentHour,CurrentMinute,StartingHour,StartingMinute);}
                    catch(ArrayIndexOutOfBoundsException exception) {Ongoing = "Upcoming class ~ "+getTimeDifference(CurrentHour,CurrentMinute,StartingHour,StartingMinute);}
                    try {Ongoing += " ~ "+Temp[5];}
                    catch(ArrayIndexOutOfBoundsException exception) {}
                    OngoingText.setText(Ongoing);
                    GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {0, Integer.valueOf(Temp[3])});
                    relativeLayout.setBackgroundDrawable(gd);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setStroke(1,Color.GRAY);
                    gradientDrawable.setCornerRadius(9);
                    relativeLayout1.setBackgroundDrawable(gradientDrawable);
                    cardView.addView(v);
                    cardView.setId(UpcomingCounter);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    cardView.setLayoutParams(params);
                    cardView.setElevation(20);
                    cardView.setRadius(9);
                    UpcomingClassesCardsContainer.addView(cardView);
                    UpcomingCounter++;
                    ImmediateNextFound = true;
                }
            }
            if(UpcomingCounter==1411)
            {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,15);
                UpcomingClassesCardsContainer.findViewById(1410).setLayoutParams(params);
            }
            if(UpcomingCounter==1410)
            {
                CardView cardView = new CardView(getActivity());
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.card_no_upcoming, null);
                RelativeLayout relativeLayout1 = (RelativeLayout)v.findViewById(R.id.NoUpcomingCardWrapper);
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1,Color.GRAY);
                gradientDrawable.setCornerRadius(9);
                relativeLayout1.setBackgroundDrawable(gradientDrawable);
                cardView.addView(v);
                cardView.setId(UpcomingCounter);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,15);
                cardView.setLayoutParams(params);
                cardView.setElevation(20);
                cardView.setRadius(9);
                UpcomingClassesCardsContainer.addView(cardView);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.CreateNewScheduleFAB:
                floatingActionMenu.toggle(true);
                Intent intent = new Intent(v.getContext(),NewSchedule.class);
                intent.putExtra("Caller","NewSchedule");
                v.getContext().startActivity(intent);
                break;
            case R.id.ImportNewScheduleFAB:
                floatingActionMenu.toggle(true);
                new MaterialFilePicker()
                        .withSupportFragment(this)
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.xml$")) // Filtering files and directories by file name using regexp// Set directories filterable (false by default)
                        .withHiddenFiles(false) // Show hidden files and folders
                        .start();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            //Toast.makeText(getActivity(),filePath,Toast.LENGTH_SHORT).show();
            try {
                if(ValidXML(filePath))
                {
                    XMLParser(filePath);
                    Intent intent = new Intent(getActivity(),NewSchedule.class);
                    intent.putExtra("Caller","Import");
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(),"Selected XML doesn't appear to contain a valid schedule",Toast.LENGTH_LONG).show();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean ValidXML(String Path) throws XmlPullParserException,IOException
    {
        File file = new File(Path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        char[] inputBuffer = new char[fileInputStream.available()];
        inputStreamReader.read(inputBuffer);
        String XMLData = new String(inputBuffer);
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

    public void XMLParser(String Path) throws XmlPullParserException,IOException
    {
        File file = new File(Path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        char[] inputBuffer = new char[fileInputStream.available()];
        inputStreamReader.read(inputBuffer);
        String XMLData = new String(inputBuffer);
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

    public String getTimeDifference(int CurrentHour,int CurrentMinute, int GivenHour, int GivenMinute)
    {
        if(GivenHour>CurrentHour || GivenHour<CurrentHour)
        {
            if(GivenHour<CurrentHour)
            {
                int Temp = GivenHour;
                GivenHour = CurrentHour;
                CurrentHour = Temp;
            }
            int HourDifference = (GivenHour-CurrentHour)*60;
            int MinuteDifference = (GivenMinute-CurrentMinute);
            int TotalDifference = HourDifference+MinuteDifference;
            if(TotalDifference<60)
            {
                if(TotalDifference>1)return ""+TotalDifference+" mins";
                else return ""+TotalDifference+" min";
            }
            else
            {
                int Hours = TotalDifference/60,Minutes = TotalDifference%60;
                if(Minutes==0 && Hours>1)return ""+Hours+" hrs";
                if(Minutes==0 && Hours==1)return ""+Hours+" hr";
                if(Minutes<=1 && Hours==1)return ""+Hours+" hr "+Minutes+" min";
                if(Minutes<=1 && Hours>1)return ""+Hours+" hrs "+Minutes+" min";
                if(Minutes>1 && Hours>1)return ""+Hours+" hrs "+Minutes+" mins";
                if(Minutes>1 && Hours==1)return ""+Hours+" hr "+Minutes+" mins";
            }
        }
        if(GivenHour==CurrentHour)
        {
            int MinutesDifference = 0;
            if(GivenMinute>=CurrentMinute)MinutesDifference=GivenMinute-CurrentMinute;
            if(GivenMinute<CurrentMinute)MinutesDifference=CurrentMinute-GivenMinute;
            if(MinutesDifference>1)return ""+MinutesDifference+" mins";
            else return ""+MinutesDifference+" min";
        }
        return "";
    }

    public int getHour(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        if(timeDiv[1].equals("PM") && HourSplit[0].equals("12"))return 12;
        if(timeDiv[1].equals("PM"))return Integer.valueOf(HourSplit[0])+12;
        if(timeDiv[1].equals("AM") && HourSplit[0].equals("12"))return 0;
        return Integer.valueOf(HourSplit[0]);
    }

    public int getMinute(String time)
    {
        String timeDiv[] = time.split(" ");
        String HourSplit[] = timeDiv[0].split(":");
        return Integer.valueOf(HourSplit[1]);
    }

    private String getFormattedDate(){
        String dayNumberSuffix = getDayNumberSuffix(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "<sup>th</sup>";
        }
        switch (day % 10) {
            case 1:
                return "<sup>st</sup>";
            case 2:
                return "<sup>nd</sup>";
            case 3:
                return "<sup>rd</sup>";
            default:
                return "<sup>th</sup>";
        }
    }

}

class DataAdapter extends RecyclerView.Adapter
{
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.welcome_card, viewGroup, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton CreateNew;
        ViewHolder(View view)
        {
            super(view);
            CreateNew = (ImageButton)view.findViewById(R.id.NewScheduleButton);
            CreateNew.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(v.getContext(),NewSchedule.class);
                    intent.putExtra("Caller","NewSchedule");
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {return 1;}
}
