package com.am.induster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.am.induster.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ScrollingActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBarLayout;

    String ShowOrHideWebViewInitialUse = "show";
    private WebView myWebView ;
    private LottieAnimationView lav_delivery;

    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private Button btn_call,btn_message,btn_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setCollapsingToolbarTitle();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottom_sheet.setVisibility(View.VISIBLE);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottom_sheet.setVisibility(View.GONE);
                }

            }
        });

        bottom_sheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        btn_call = (Button) findViewById(R.id.btn_call);
        btn_message = (Button) findViewById(R.id.btn_message);
        btn_whatsapp = (Button) findViewById(R.id.btn_whatsapp);

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        lav_delivery = (LottieAnimationView) findViewById(R.id.lav_delivery);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new CustomWebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        myWebView.loadUrl("https://www.kumbakonamonline.store/");

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openWhatsApp();
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSmsMsgFnc();
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendCall();
            }
        });
    }

    public void setCollapsingToolbarTitle() {

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Expanded);

        // This is the most important when you are putting custom TextView in CollapsingToolbar
        collapsingToolbar.setTitle(" ");

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //when collapsingToolbar at that time display actionbar title
                    collapsingToolbar.setTitle(getResources().getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    //carefull there must a space between double quote otherwise it dose't work
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {

            // only make it invisible the FIRST time the app is run

                webview.setVisibility(webview.INVISIBLE);
                lav_delivery.setVisibility(View.VISIBLE);
                lav_delivery.setProgress(0);
                lav_delivery.pauseAnimation();
                lav_delivery.playAnimation();

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            ShowOrHideWebViewInitialUse = "hide";

            lav_delivery.setProgress(0);
            lav_delivery.pauseAnimation();
            lav_delivery.setVisibility(View.GONE);

            view.setVisibility(myWebView.VISIBLE);
            super.onPageFinished(view, url);

        }
    }

    public void openWhatsApp(){
        try {
            String text = "Hi Kumbkonam Cart";// Replace with your message.

            String toNumber = "919962961194"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    void sendSmsMsgFnc()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            try
            {
                SmsManager smsMgrVar = SmsManager.getDefault();
                smsMgrVar.sendTextMessage("919962961194", null, "Hi Kumbkonam Cart", null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception ErrVar)
            {
                Toast.makeText(getApplicationContext(),ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }

    }

    void sendCall()
    {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:919962961194"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
        {
            try
            {
                startActivity(i);
            }
            catch (Exception ErrVar)
            {
                Toast.makeText(getApplicationContext(),ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 11);
            }
        }

    }
}
