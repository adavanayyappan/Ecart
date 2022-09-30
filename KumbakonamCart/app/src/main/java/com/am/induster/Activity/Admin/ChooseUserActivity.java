package com.am.induster.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Activity.Login.LoginAdminActivity;
import com.am.induster.Activity.Splash.SplashActivity;
import com.am.induster.R;

public class ChooseUserActivity extends AppCompatActivity {

    private Button loginUser,loginAdmin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        loginUser = (Button) findViewById(R.id.login_btn);
        loginAdmin = (Button) findViewById(R.id.login_admin_btn);

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseUserActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseUserActivity.this, LoginAdminActivity.class);
                startActivity(intent);

            }
        });


    }
}
