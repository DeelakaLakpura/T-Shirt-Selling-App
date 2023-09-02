package com.dsmini.Shirtify.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.dsmini.Shirtify.Admin.AdminHome;
import com.dsmini.Shirtify.R;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences prefs;
    boolean firstStart;
    boolean isGoToGps = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Paper.init(SplashScreen.this);
        final String temp=Paper.book().read("active");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(temp!=null){
                    if(temp.equals("user")){
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                        finish();
                    }
                    if(temp.equals("admin")){
                        startActivity(new Intent(SplashScreen.this, AdminHome.class));
                        finish();
                    }
                }
                else{
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        },3000);
    }
}