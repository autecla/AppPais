package com.lincs.mobcare.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.lincs.mobcare.R;
import com.lincs.mobcare.authentication.login.LoginAngelActivity;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    FrameLayout progressBarHolder;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_splash);

        progressBarHolder = findViewById(R.id.progressBarHolder);
        progressBar = findViewById(R.id.progressBar);

        int SPLASH_TIME_OUT = 1500;
        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginAngelActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
