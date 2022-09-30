package com.am.induster.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.am.induster.Activity.User.HomeActivity;
import com.am.induster.Activity.User.ProductDetailsActivity;
import com.am.induster.Adapter.CustomImageAdapter;
import com.am.induster.Adapter.CustomProductImageAdapter;
import com.am.induster.Model.Products;
import com.am.induster.Model.UserProducts;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class AdminProductDetailActivity extends AppCompatActivity
{
    private TextView productPrice, productDescription, productName, productOfferSize, productOffer, productBrand;
    private String productID = " ", state = "Normal", category = "Generall";
    private TextView closeTextBtn;
    private AutoScrollViewPager adsViewpager;
    String[]  ImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_details);

        productID = getIntent().getStringExtra("pid");

        productName  = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productOfferSize = (TextView) findViewById(R.id.product_offer_price_details);
        productOffer = (TextView) findViewById(R.id.product_offer);
        productBrand = (TextView) findViewById(R.id.product_brand_name);
        closeTextBtn = (TextView) findViewById(R.id.close_users_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        adsViewpager = findViewById(R.id.product_image_details);

        getProductDetails(productID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_product_admin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminProductDetailActivity.this, AdminUserCategoryActivity.class);
                intent.putExtra("pid", productID);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }



    private void getProductDetails(final String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.orderByChild("pid").equalTo(productID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                for (DataSnapshot data : task.getResult().getChildren()) {
                    Products products = data.getValue(Products.class);
                    productName.setText("Product Name : " + products.getPname());
                    productPrice.setText("Product Price :  \u20B9" + products.getPrice());
                    productDescription.setText("Product Description : " +products.getDescription());
                    productOffer.setText(products.getPofferprize() + "%");
                    productOfferSize.setText("Product Offer Price :  \u20B9" +products.getPoffer());
                    productBrand.setText("Product Brand : " +products.getPofferbrand());
                    if (products.getImages() != null) {
                        ArrayList<String> mProductImages = new ArrayList<>();
                        for (String value : products.getImages().values()) {
                            mProductImages.add(value);
                        }
                        CustomProductImageAdapter adapter = new CustomProductImageAdapter(AdminProductDetailActivity.this, mProductImages);
                        adsViewpager.setAdapter(adapter);
                    }
                }
            }
        }
    });

    }

}
