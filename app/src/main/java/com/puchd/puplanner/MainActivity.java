package com.puchd.puplanner;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    NewScheduleDatabase newScheduleDatabase;
    Context context;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!isMyServiceRunning(BackgroundService.class)) startService(new Intent(this, BackgroundService.class));


        Intent intent = getIntent();
        String Action = "";
        if(intent.getStringExtra("Action")!=null)
        Action = intent.getStringExtra("Action");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageView DrawerProfilePicture = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.DrawerProfilePic);
        Database databaseobject = new Database(context);
        if(databaseobject.FetchData("ProfileType").equals("Local"))
        {
            String path = Environment.getExternalStorageDirectory().toString();
            path += "/Android/data/com.puchd.puplanner/";
            File imgFile = new  File(path+"LocalProfilePic.png");
            if(imgFile.exists())
            {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                DrawerProfilePicture.setImageBitmap(myBitmap);
            }
        }
        if(databaseobject.FetchData("ProfileType").equals("Google"))
        {
            String URL = databaseobject.FetchData("ProfileImageURL");
            Glide.with(this).load(URL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(DrawerProfilePicture);
        }
        TextView UserDisplayName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userdisplayname);
        TextView UserDisplayRoll = (TextView)navigationView.getHeaderView(0).findViewById(R.id.userdisplayroll);
        UserDisplayName.setText(databaseobject.FetchData("Name"));
        UserDisplayRoll.setText(databaseobject.FetchData("RollNo"));

        newScheduleDatabase = new NewScheduleDatabase(this);
        if(newScheduleDatabase.GetTableCount()>=1)
        {
            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.add(R.id.Group1,115,4,"Manage schedule");
            menuItem.setIcon(R.drawable.ic_nav_item);
            menuItem.setCheckable(true);

            MenuItem menuItem1 = menu.add(R.id.Group1,113,2,"Week View");
            menuItem1.setIcon(R.drawable.ic_nav_item);
            menuItem1.setCheckable(true);

            MenuItem menuItem2 = menu.add(R.id.Group1,114,3,"Day View");
            menuItem2.setIcon(R.drawable.ic_nav_item);
            menuItem2.setCheckable(true);

            MenuItem menuItem3 = menu.add(R.id.Group1,116,5,"Attendance");
            menuItem3.setIcon(R.drawable.ic_nav_item);
            menuItem3.setCheckable(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        displaySelectedScreen(R.id.nav_home);

        if(Action.equals("DefaultDeleted"))
        {
            String ScheduleName = intent.getStringExtra("ScheduleName");
            Bundle bundle = new Bundle();
            bundle.putString("OldName",ScheduleName);
            bundle.putString("Action","DefaultDeleted");
            navigationView.getMenu().getItem(3).setChecked(true);
            android.support.v4.app.Fragment fragment = new ManageScheduleFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        if(Action.equals("DefaultChanged"))
        {
            String ScheduleName = intent.getStringExtra("ScheduleName");
            Bundle bundle = new Bundle();
            bundle.putString("OldName",ScheduleName);
            bundle.putString("Action","DefaultChanged");
            navigationView.getMenu().getItem(3).setChecked(true);
            android.support.v4.app.Fragment fragment = new ManageScheduleFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        if(Action.equals("DefaultRenamed"))
        {
            String ScheduleName = intent.getStringExtra("ScheduleName");
            String NewScheduleName = intent.getStringExtra("NewScheduleName");
            Bundle bundle = new Bundle();
            bundle.putString("OldName",ScheduleName);
            bundle.putString("NewScheduleName",NewScheduleName);
            bundle.putString("Action","DefaultRenamed");
            navigationView.getMenu().getItem(3).setChecked(true);
            android.support.v4.app.Fragment fragment = new ManageScheduleFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you wish to exit the application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            Intent intent = new Intent(getApplicationContext(), LoginCheck.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.

        int ItemID = item.getItemId();

        if(ItemID == R.id.nav_about_us)
        {
            startActivity(new Intent(MainActivity.this, AboutUs.class));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        if(ItemID==113)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(MainActivity.this, NewSchedule.class);
            intent.putExtra("Caller","MainActivity");
            ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in,R.anim.slide_out);
            startActivity(intent, options.toBundle());
            return true;
        }
        if(ItemID==114)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(MainActivity.this, Day_View.class);
            intent.putExtra("Caller","MainActivity");
            ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in,R.anim.slide_out);
            startActivity(intent, options.toBundle());
            return true;
        }
        else
        {
            displaySelectedScreen(item.getItemId());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    private void displaySelectedScreen(int itemId)
    {
        android.support.v4.app.Fragment fragment = null;
        switch (itemId)
        {
            case R.id.nav_home:
                if(navigationView.getMenu().getItem(1)!=null)navigationView.getMenu().getItem(1).setChecked(false);
                if(navigationView.getMenu().getItem(2)!=null)navigationView.getMenu().getItem(2).setChecked(false);
                //if(navigationView.getMenu().getItem(3)!=null)navigationView.getMenu().getItem(3).setChecked(false);
                fragment = new HomeFragment();
                break;
            case R.id.nav_holidays:
                if(navigationView.getMenu().getItem(1)!=null)navigationView.getMenu().getItem(1).setChecked(false);
                if(navigationView.getMenu().getItem(2)!=null)navigationView.getMenu().getItem(2).setChecked(false);
                //if(navigationView.getMenu().getItem(3)!=null)navigationView.getMenu().getItem(3).setChecked(false);
                fragment = new HolidaysFragment();
                break;
            case R.id.nav_settings:
                if(navigationView.getMenu().getItem(1)!=null)navigationView.getMenu().getItem(1).setChecked(false);
                if(navigationView.getMenu().getItem(2)!=null)navigationView.getMenu().getItem(2).setChecked(false);
                //if(navigationView.getMenu().getItem(3)!=null)navigationView.getMenu().getItem(3).setChecked(false);
                fragment = new SettingsFragment();
                break;
            case 115:
                navigationView.getMenu().getItem(3).setChecked(true);
                Bundle bundle = new Bundle();
                bundle.putString("OldName","");
                fragment = new ManageScheduleFragment();
                fragment.setArguments(bundle);
                break;
            case 116:
                navigationView.getMenu().getItem(4).setChecked(true);
                fragment = new AttendanceFragment();
                break;
        }
        if (fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
