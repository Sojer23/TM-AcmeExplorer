package com.example.acmeexplorer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.acmeexplorer.Security.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private final int DURATION_SPLASH=6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Cuando pase el tiempo fijado se lleva a la pantalla de login
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURATION_SPLASH);
    }
}
