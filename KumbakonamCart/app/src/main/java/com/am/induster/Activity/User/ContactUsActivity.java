package com.am.induster.Activity.User;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.am.induster.Activity.Admin.AdminProductDetailActivity;
import com.am.induster.Adapter.CustomProductImageAdapter;
import com.am.induster.Model.UserProducts;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.am.induster.Model.Products;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class ContactUsActivity extends AppCompatActivity
{
    private TextView closeTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        closeTextBtn = (TextView) findViewById(R.id.close_users_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

}

