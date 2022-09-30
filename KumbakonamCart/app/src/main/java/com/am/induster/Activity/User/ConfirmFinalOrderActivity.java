package com.am.induster.Activity.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Activity.Register.RegisterActivity;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.SupportingFiles.SharedPref;
import com.am.induster.SupportingFiles.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

public class ConfirmFinalOrderActivity extends AppCompatActivity implements PaymentResultWithDataListener, PaymentStatusListener {
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputEmailAddress;
    private EditText InputEnterPriseName, InputAddress, InputCity, InputState, InputPostalCode, InputGSTNumber;
    private ProgressDialog loadingBar;

    private String totalAmount = "0";
    private EasyUpiPayment easyUpiPayment;
    String ID  = Util.random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");

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
        loadingBar = new ProgressDialog(this);

        InputPhoneNumber.setEnabled(false);
        Toast.makeText(this, "Total Price = \u20B9" + totalAmount, Toast.LENGTH_SHORT).show();
        CreateAccountButton.setText("Confirm Order = \u20B9" + totalAmount);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               ConfirmOrder();
            }
        });
        Checkout.preload(getApplicationContext());
        userInfoDisplay();

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConfirmOrder();
            }
        });

    }


    private void ConfirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;

        final String name = InputName.getText().toString();
        final String phone = InputPhoneNumber.getText().toString();
        String email = InputEmailAddress.getText().toString();
        String address = InputAddress.getText().toString();
        String city = InputCity.getText().toString();
        String state = InputState.getText().toString();
        String postalCode = InputPostalCode.getText().toString();
        String enterPriseName = InputEnterPriseName.getText().toString();

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
        else {
            showOrderAlert();
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
                        String pincode = dataSnapshot.child("pincode").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

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
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void openWhatsApp(String message){
        try {
            String text = message;

            String toNumber = "919789686338"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateAccount()
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
        else
        {
            loadingBar.setTitle("Updating dat");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("phone", phone);
            userdataMap.put("name", name);
            userdataMap.put("email", email);
            userdataMap.put("city", city);
            userdataMap.put("state1", state);
            userdataMap.put("country", "India");
            userdataMap.put("pincode", postalCode);
            userdataMap.put("image", "empty");
            userdataMap.put("address", address);
            userdataMap.put("puserid", Prevalent.currentOnlineUser.getPuserid());
            userdataMap.put("enterprise", enterPriseName);
            userdataMap.put("gst", gstNumber);

            ValidatephoneNumber(phone, userdataMap);
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
                if ((dataSnapshot.child(phone).exists()))
                {
                    RootRef.child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Congratulations, your order has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        finish();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void showOrderAlert() {
        String gstNumber = InputGSTNumber.getText().toString();
        CharSequence options[] = new CharSequence[]
                {
                        "Payment Gateway Redirect",
                        "Cash on delivery",
                        "Credits"
                };
        if (TextUtils.isEmpty(gstNumber))
        {
            options = new CharSequence[]
                {
                        "Payment Gateway Redirect",
                        "Cash on delivery"
                };
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmFinalOrderActivity.this);
        builder.setTitle("Payment Options");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(i == 0)
                {
                    PaymentApp paymentApp = PaymentApp.ALL;
                    ID  = Util.random();

                    // START PAYMENT INITIALIZATION
                    EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(ConfirmFinalOrderActivity.this)
                            .with(paymentApp)
                            .setPayeeVpa("adavan2107@okhdfcbank")
                            .setPayeeName("Dinesh Balaji")
                            .setTransactionId(Util.random())
                            .setTransactionRefId(ID)
                            .setPayeeMerchantCode(ID)
                            .setDescription("Automobiles parts")
                            .setAmount(totalAmount);

                    try {
                        // Build instance
                        easyUpiPayment = builder.build();

                        // Register Listener for Events
                        easyUpiPayment.setPaymentStatusListener(ConfirmFinalOrderActivity.this);

                        // Start payment / transaction
                        easyUpiPayment.startPayment();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        toast("Error: " + exception.getMessage());
                    }
                }
                if(i == 1)
                {
                    AddOrderDataToDB("COD",false,Util.random());
                }

                if(i == 2)
                {
                    AddOrderDataToDB("CREDITS",false,Util.random());
                }

            }
        });
        builder.show();
    }
    private void openApp() {
        String packageName = "com.google.android.gm";
        if (isAppInstalled(this, packageName))
            startActivity(getPackageManager().getLaunchIntentForPackage(packageName));
        else Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
    }

    public static boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    public void startPaymentGateway() {

        final Activity activity = this;

        final String saveCurrentDate, saveCurrentTime;

        final String name = InputName.getText().toString();
        final String phone = InputPhoneNumber.getText().toString();
        String email = InputEmailAddress.getText().toString();
        String address = InputAddress.getText().toString();
        String city = InputCity.getText().toString();
        String state = InputState.getText().toString();
        String postalCode = InputPostalCode.getText().toString();
        String enterPriseName = InputEnterPriseName.getText().toString();
        String gstNumber = InputGSTNumber.getText().toString();

        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_j61o6hr3pzzrHO");
        try {

            int finalAmount = Integer.parseInt(totalAmount) * 100; // converting 12p rupees into paisa

            JSONObject options = new JSONObject();
            options.put("name", "Induster");
            options.put("description", "Payment");
            //You can omit the image option to fetch the image from dashboard
            //   options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(finalAmount));
            options.put("order_id",Util.random());

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);

            JSONObject notes = new JSONObject();
            notes.put("Product_details","Spare parts");

            options.put("prefill", preFill);
            options.put("notes",notes);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();

        }
    }

    @Override
    public void onPaymentSuccess(String id, PaymentData paymentData) {
        Toast.makeText(this, "Payment Success with id : "+id, Toast.LENGTH_SHORT).show();
        AddOrderDataToDB("PaymentGateway",true,id);
    }

    @Override
    public void onPaymentError(int i, String response, PaymentData paymentData) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    public void AddOrderDataToDB(String paymentType, Boolean paymentSuccess, String orderID) {

        final String saveCurrentDate, saveCurrentTime;

        final String name = InputName.getText().toString();
        final String phone = InputPhoneNumber.getText().toString();
        String email = InputEmailAddress.getText().toString();
        String address = InputAddress.getText().toString();
        String city = InputCity.getText().toString();
        String state = InputState.getText().toString();
        String postalCode = InputPostalCode.getText().toString();
        String enterPriseName = InputEnterPriseName.getText().toString();
        String gstNumber = InputGSTNumber.getText().toString();


            loadingBar.setTitle("Confirm Order");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());


            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentDate.format(calForDate.getTime());

            final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                    .child("Orders")
                    .child(Util.random());

            HashMap<String, Object> ordersMap = new HashMap<>();
            ordersMap.put("totalAmount", totalAmount);
            ordersMap.put("phone", phone);
            ordersMap.put("name", name);
            ordersMap.put("email", email);
            ordersMap.put("city", city);
            ordersMap.put("state1", state);
            ordersMap.put("country", "India");
            ordersMap.put("pincode", postalCode);
            ordersMap.put("image", "empty");
            ordersMap.put("address", address);
            ordersMap.put("orderID", Util.random());
            ordersMap.put("enterprise", enterPriseName);
            ordersMap.put("gst", gstNumber);
            ordersMap.put("puserid", Prevalent.currentOnlineUser.getPuserid());
            ordersMap.put("date", saveCurrentDate);
            ordersMap.put("time", saveCurrentTime);
            ordersMap.put("state", "Not Shipped");
            ordersMap.put("paymentType", paymentType);
            ordersMap.put("paymentSuccess", paymentSuccess);
            ordersMap.put("productsArray", Prevalent.cartArray);
            ordersMap.put("productsQuantityArray", Prevalent.cartQuantityArray);

            ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference().child("Cart List")
                                .child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            updateAccount();
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
                                            openWhatsApp("Hi Admin, \nNew Order Placed\n" + "Name :" + name + "\n" + "Mobile :" + phone);
                                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                });
                    }
                }
            });
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());

        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Payment Cancelled by user");
    }

    private void onTransactionSuccess() {
        // Payment Success
        toast("Payment Success");
        Toast.makeText(this, "Payment Success with id : "+ID, Toast.LENGTH_SHORT).show();
        AddOrderDataToDB("PaymentGateway",true,ID);
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Payment Pending | Submitted");
    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Payment Failed");
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
