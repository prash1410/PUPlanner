package com.puchd.puplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        if(getIntent().getStringExtra("Caller")!=null)
        {
            if(getIntent().getStringExtra("Caller").equals("HomeFragment"))
            {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        }
    }

}
