package com.am.induster.Activity.User;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.am.induster.Activity.Admin.AdminAddNewProductActivity;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.SupportingFiles.SharedPref;
import com.am.induster.SupportingFiles.Util;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettinsActivity extends AppCompatActivity
{
    private CircleImageView profileImageView;
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputEmailAddress;
    private EditText InputEnterPriseName, InputAddress, InputCity, InputState, InputPostalCode, InputGSTNumber;
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    private static final int GalleryPick = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settins);

        storageProfilePrictureRef =  FirebaseStorage.getInstance().getReference("Profile pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        InputEmailAddress = (EditText) findViewById(R.id.register_useremail_input);
        InputEnterPriseName = (EditText) findViewById(R.id.register_enterprise_name_input);
        InputAddress = (EditText) findViewById(R.id.register_address_input);
        InputCity = (EditText) findViewById(R.id.register_city_input);
        InputState = (EditText) findViewById(R.id.register_state_input);
        InputPostalCode = (EditText) findViewById(R.id.register_postalcode_input);
        InputGSTNumber = (EditText) findViewById(R.id.register_gstnumber_input);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_btn);


        userInfoDisplay();


        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                userInfoSaved();
            }
        });


        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               userInfoSaved();
            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";
                OpenGallery();
            }
        });
    }

    public void OpenGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(SettinsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SettinsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GalleryPick);
            } else {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GalleryPick);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String email = InputEmailAddress.getText().toString();
        String address = InputAddress.getText().toString();
        String city = InputCity.getText().toString();
        String state = InputState.getText().toString();
        String postalCode = InputPostalCode.getText().toString();
        String enterPriseName = InputEnterPriseName.getText().toString();
        String gstNumber = InputGSTNumber.getText().toString();

        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("phone", phone);
        userdataMap.put("name", name);
        userdataMap.put("email", email);
        userdataMap.put("city", city);
        userdataMap.put("state1", state);
        userdataMap.put("country", "India");
        userdataMap.put("pincode", postalCode);
        userdataMap.put("address", address);
        userdataMap.put("puserid", Prevalent.currentOnlineUser.getPuserid());
        userdataMap.put("enterprise", enterPriseName);
        userdataMap.put("gst", gstNumber);
        userdataMap.put("image", "empty");
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userdataMap);

        startActivity(new Intent(SettinsActivity.this, HomeActivity.class));
        Toast.makeText(SettinsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {

            if (data.getClipData() != null) {

                int countClipData = data.getClipData().getItemCount();
                int currentImageSlect = 0;
                while (currentImageSlect < countClipData) {

                    imageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                    currentImageSlect = currentImageSlect + 1;
                }
                profileImageView.setImageURI(imageUri);
            } else {
                imageUri = data.getData();
                profileImageView.setImageURI(imageUri);
            }
        }
    }




    private void userInfoSaved()
    {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String email = InputEmailAddress.getText().toString();
        String address = InputAddress.getText().toString();
        String city = InputCity.getText().toString();
        String state = InputState.getText().toString();
        String postalCode = InputPostalCode.getText().toString();
        String enterPriseName = InputEnterPriseName.getText().toString();
        String gstNumber = InputGSTNumber.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
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
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
        else
        {
            updateOnlyUserInfo();
        }
    }



    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                                String name = InputName.getText().toString();
                                String phone = InputPhoneNumber.getText().toString();
                                String email = InputEmailAddress.getText().toString();
                                String address = InputAddress.getText().toString();
                                String city = InputCity.getText().toString();
                                String state = InputState.getText().toString();
                                String postalCode = InputPostalCode.getText().toString();
                                String enterPriseName = InputEnterPriseName.getText().toString();
                                String gstNumber = InputGSTNumber.getText().toString();

                                HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("phone", phone);
                                userdataMap.put("name", name);
                                userdataMap.put("email", email);
                                userdataMap.put("city", city);
                                userdataMap.put("state1", state);
                                userdataMap.put("country", "India");
                                userdataMap.put("pincode", postalCode);
                                userdataMap.put("address", address);
                                userdataMap.put("puserid", Prevalent.currentOnlineUser.getPuserid());
                                userdataMap.put("enterprise", enterPriseName);
                                userdataMap.put("gst", gstNumber);
                                userdataMap.put("image", myUrl);
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userdataMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettinsActivity.this, HomeActivity.class));
                                Toast.makeText(SettinsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettinsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay()
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("name").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String city =  dataSnapshot.child("city").getValue().toString();
                        String state =  dataSnapshot.child("state1").getValue().toString();
                        String gst = dataSnapshot.child("gst").getValue().toString();
                        String enterpriseName = dataSnapshot.child("enterprise").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String pincode = dataSnapshot.child("pincode").getValue().toString();


                        InputName.setText(name);
                        InputPhoneNumber.setText(phone);
                        if (!address.equals("empty")) {
                            InputAddress.setText(address);
                        }
                        if (!city.equals("empty")) {
                            InputCity.setText(city);
                        }
                        if (!state.equals("empty")) {
                            InputState.setText(state);
                        }
                        if (!gst.equals("empty")) {
                            InputGSTNumber.setText(gst);
                        }
                        if (!pincode.equals("empty")) {
                            InputPostalCode.setText(pincode);
                        }
                        if (!enterpriseName.equals("empty")) {
                            InputEnterPriseName.setText(enterpriseName);
                        }
                        if (!email.equals("empty")) {
                            InputEmailAddress.setText(email);
                        }

                        Picasso.with(SettinsActivity.this).load(image).fit().centerCrop().placeholder(R.drawable.profile).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
