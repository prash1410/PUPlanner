package com.puchd.puplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashscreen extends Activity implements AnimationListener
{
    Context context;
    SharedPreferences prefs = null;

    //To ignore backbutton on splashscreen
    @Override
    public void onBackPressed()
    {

    }

    ImageView Logo;
    Animation Move,Fade_In;
    TextView ProjectX;

    // Splash screen timer

    static int SPLASH_TIME_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ThemeSetter.setThemeID(getApplicationContext());
        AccentSetter.setStyleID(getApplicationContext());
        PrimaryColorSetter.setStyleID(getApplicationContext());
        int themeValue = ThemeSetter.getThemeID();
        if(themeValue == 1)setTheme(R.style.AppTheme_Dark_Actionbar);
        getTheme().applyStyle(AccentSetter.getStyleID(),true);
        getTheme().applyStyle(PrimaryColorSetter.getStyleID(),true);
        super.onCreate(savedInstanceState);
        context = this;
        //To make our activity fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);
        prefs = getSharedPreferences("com.puchd.puplanner", MODE_PRIVATE);

        Logo = findViewById(R.id.imgLogo);

        if(prefs.getBoolean("firstrun",true))
        {
            SPLASH_TIME_OUT = 3500;
            Move = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move);
            Fade_In = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
            Move.setAnimationListener(this);
            Logo.startAnimation(Move);
        }
        else
        {
            SPLASH_TIME_OUT = 1200;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                Logo.setImageDrawable(getResources().getDrawable(R.drawable.image, getApplicationContext().getTheme()));
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                BitmapDrawable drawable = (BitmapDrawable)Logo.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                float scaleWidth = metrics.scaledDensity;
                float scaleHeight = metrics.scaledDensity;

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth/3, scaleHeight/3);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

                Logo.setImageBitmap(resizedBitmap);
            } else
            {
                Logo.setImageDrawable(getResources().getDrawable(R.drawable.image));
            }

        }

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(prefs.getBoolean("firstrun",true))
                {
                    Intent i = new Intent(splashscreen.this, Walkthrough.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Database databaseobject = new Database(context);
                    if(databaseobject.UserPresent())
                    {
                        Intent i = new Intent(splashscreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(splashscreen.this, LoginCheck.class);
                        startActivity(i);
                        finish();
                    }
                }
                prefs.edit().putBoolean("firstrun", false).apply();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        if(animation == Move)
        {
            ProjectX = findViewById(R.id.text);
            ProjectX.setText("Project X");
            ProjectX.startAnimation(Fade_In);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}
