package com.am.induster.Activity.Splash;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.am.induster.Activity.Admin.ChooseUserActivity;
import com.am.induster.Activity.Login.LoginAdminActivity;
import com.am.induster.R;


public class

SplashAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_admin);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashAdminActivity.this, ChooseUserActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}

