package com.am.induster.Activity.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.am.induster.Activity.Admin.AdminHomeActivity;
import com.am.induster.Activity.Admin.AdminUserCategoryActivity;
import com.am.induster.Activity.User.HomeActivity;
import com.am.induster.Activity.Register.RegisterActivity;
import com.am.induster.Model.Users;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton, CreateAccountButton;
    private ProgressDialog loadingBar;
    private final String parentDbName = "Users";
    private com.rey.material.widget.CheckBox chkBoxRememberMe;
    private FirebaseAuth mAuth;
    private TextView forgot_password_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        LoginButton = (Button) findViewById(R.id.login_btn);
        CreateAccountButton = (Button) findViewById(R.id.create_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        forgot_password_link = (TextView) findViewById(R.id.forgot_password_link);
        loadingBar = new ProgressDialog(this);


        chkBoxRememberMe = (com.rey.material.widget.CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        LoginButton.setText("Login");
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgot_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }


    private void LoginUser() {
        final String phone = InputPhoneNumber.getText().toString();
        final String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your email address...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(phone, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                AllowAccessToAccount(phone, password);

                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void AllowAccessToAccount(final String phone, final String password) {
        if (chkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child(parentDbName).orderByChild("email").equalTo(phone);

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users usersData = null;
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    usersData = Item.getValue(Users.class);
                }

                if (!dataSnapshot.exists()) {
                    Paper.book().destroy();
                    FirebaseAuth.getInstance().signOut();
                    Prevalent.currentOnlineUser = null;
                    Toast.makeText(LoginActivity.this, "Blocked...! Please contact Admin Support", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    return;
                }

                Log.e("usersData"," "+usersData.getPhone());
                Log.e("usersData"," "+dataSnapshot.exists());

                if (parentDbName.equals("Admins")) {
                    Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                } else if (parentDbName.equals("Users")) {
                    Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    Prevalent.currentOnlineUser = usersData;
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child("Users").orderByChild("email").equalTo(phone);

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users usersData = null;
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    usersData = Item.getValue(Users.class);
                }

                Toast.makeText(LoginActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                if (!dataSnapshot.exists()) {
                    Paper.book().destroy();
                    FirebaseAuth.getInstance().signOut();
                    Prevalent.currentOnlineUser = null;
                    Toast.makeText(LoginActivity.this, "Blocked...! Please contact Admin Support", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    return;
                }

                Log.e("usersData"," "+usersData.getPhone());
                Log.e("usersData"," "+dataSnapshot.exists());

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                Prevalent.currentOnlineUser = usersData;
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this, "Recover Password link sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}