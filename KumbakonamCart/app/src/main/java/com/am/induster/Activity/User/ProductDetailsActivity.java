package com.am.induster.Activity.User;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class ProductDetailsActivity extends AppCompatActivity
{
    private Button addToCartBtn;
    private TextView productPrice, productDescription, productName, productOfferSize, productOffer, productBrand;
    private String productID = " ", state = "Normal", category = "Generall";
    private String productImage = " ";
    private Integer qunatity = 1;
    private TextView closeTextBtn;
    private AutoScrollViewPager adsViewpager;
    private EditText quantity_spinner;
    ArrayList<Integer> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        addToCartBtn = (Button) findViewById(R.id.pd_add_to_cart_btn);
        productName  = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productOfferSize = (TextView) findViewById(R.id.product_offer_price_details);
        productBrand = (TextView) findViewById(R.id.product_brand_name);
        productOffer = (TextView) findViewById(R.id.product_offer);
        closeTextBtn = (TextView) findViewById(R.id.close_users_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        adsViewpager = findViewById(R.id.product_image_details);
        quantity_spinner = (EditText) findViewById(R.id.quantity_spinner);

//        for (int i = 1; i <= 100; i++) {
//            items.add(i);
//        }
//        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
//        quantity_spinner.setAdapter(adapter);

        getProductDetails(productID);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(Integer.parseInt(quantity_spinner.getText().toString()) > 0) {
                    addingToCartList();
                }

            }
        });

//        quantity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });

   /*     addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "You can add purchase more products, once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }

            }
        });*/
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString().replace("Product Name : ",""));
        cartMap.put("price", productPrice.getText().toString().replace("Product Price :  \u20B9",""));
        cartMap.put("poffer", productOffer.getText().toString().replace("%",""));
        cartMap.put("pofferbrand", productBrand.getText().toString().replace("Brand :",""));
        cartMap.put("pofferprize", productOfferSize.getText().toString().replace("Product Offer Price :  \u20B9",""));
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", Integer.parseInt(quantity_spinner.getText().toString()));
        cartMap.put("image", productImage);
        cartMap.put("discount", "");
        cartMap.put("cartProducts", Prevalent.cartProducts);

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }

                    }
                });

    }

    private void getProductDetails(final String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("UserProducts");

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

                        Prevalent.cartProducts  =  products;
                        productImage =  products.getImage();
                        productName.setText("Product Name : " + products.getPname());
                        productPrice.setText("Product Price :  \u20B9" + products.getPrice());
                        productDescription.setText("Product Description : " +products.getDescription());
                        productOffer.setText(products.getPoffer() + "%");
                        productBrand.setText("Brand :" + products.getPofferbrand());
                        productOfferSize.setText("Product Offer Price :  \u20B9" +products.getPofferprize());
                        if (products.getImages() != null) {
                            ArrayList<String> mProductImages = new ArrayList<>();
                            for (String value : products.getImages().values()) {
                                mProductImages.add(value);
                            }
                            CustomProductImageAdapter adapter = new CustomProductImageAdapter(ProductDetailsActivity.this, mProductImages);
                            adsViewpager.setAdapter(adapter);
                        }
                    }
                }
            }
        });

    }

}
