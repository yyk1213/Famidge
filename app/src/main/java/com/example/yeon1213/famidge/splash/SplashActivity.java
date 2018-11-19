package com.example.yeon1213.famidge.splash;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.yeon1213.famidge.R;
import com.example.yeon1213.famidge.SharedPreferencesUtility;
import com.example.yeon1213.famidge.interfaces.ActivityHandler;
import com.example.yeon1213.famidge.logIn.LoginActivity;
import com.example.yeon1213.famidge.main.MainActivity;

public class SplashActivity extends AppCompatActivity implements ActivityHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesUtility.checkAutoLogin(getApplicationContext())) {

                    if (SharedPreferencesUtility.getID(getApplicationContext()) != null) {
                        if (SharedPreferencesUtility.getPassword(getApplicationContext()) != null) {
                           startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
            }
        }, 3000);
    }
}
