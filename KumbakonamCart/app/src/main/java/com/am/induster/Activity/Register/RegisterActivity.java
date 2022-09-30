package com.am.induster.Activity.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.R;
import com.am.induster.SupportingFiles.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity
{
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword, InputEmailAddress;
    private EditText InputEnterPriseName, InputAddress, InputCity, InputState, InputPostalCode, InputGSTNumber;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        InputEmailAddress = (EditText) findViewById(R.id.register_useremail_input);
        InputEnterPriseName = (EditText) findViewById(R.id.register_enterprise_name_input);
        InputAddress = (EditText) findViewById(R.id.register_address_input);
        InputCity = (EditText) findViewById(R.id.register_city_input);
        InputState = (EditText) findViewById(R.id.register_state_input);
        InputPostalCode = (EditText) findViewById(R.id.register_postalcode_input);
        InputGSTNumber = (EditText) findViewById(R.id.register_gstnumber_input);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });
    }



    private void CreateAccount()
    {
        final String name = InputName.getText().toString();
        final String phone = InputPhoneNumber.getText().toString();
        final String password = InputPassword.getText().toString();
        final String email = InputEmailAddress.getText().toString();
        final String address = InputAddress.getText().toString();
        final String city = InputCity.getText().toString();
        final String state = InputState.getText().toString();
        final String postalCode = InputPostalCode.getText().toString();
        final String enterPriseName = InputEnterPriseName.getText().toString();
        final String gstNumber = InputGSTNumber.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        else if (!Util.isValidEmailId(email))
        {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "Please write your address...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(city))
        {
            Toast.makeText(this, "Please write your city...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(state))
        {
            Toast.makeText(this, "Please write your state...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(postalCode))
        {
            Toast.makeText(this, "Please write your state...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(enterPriseName))
        {
            Toast.makeText(this, "Please write your enterprise name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("phone", phone);
                                userdataMap.put("password", password);
                                userdataMap.put("name", name);
                                userdataMap.put("email", email);
                                userdataMap.put("city", city);
                                userdataMap.put("state1", state);
                                userdataMap.put("country", "India");
                                userdataMap.put("pincode", postalCode);
                                userdataMap.put("image", "empty");
                                userdataMap.put("address", address);
                                userdataMap.put("puserid", Util.random());
                                userdataMap.put("enterprise", enterPriseName);
                                userdataMap.put("gst", gstNumber);

                                ValidatephoneNumber(phone, userdataMap);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }



    private void ValidatephoneNumber(final String phone, final HashMap<String, Object> userdataMap)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference("Users");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child(phone).exists()))
                {
                    RootRef.child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}