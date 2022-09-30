package com.am.induster.Activity.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Activity.User.HomeActivity;
import com.am.induster.Adapter.OrderProductAdapter;
import com.am.induster.Model.AdminOrders;
import com.am.induster.Model.Cart;
import com.am.induster.Model.Products;
import com.am.induster.Model.Users;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminUserProductsActivity extends AppCompatActivity
{
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private TextView closeTextBtn;

    private String userID = " ";
    ArrayList<Products> cartArray = new ArrayList<>();
    ArrayList<String> cartQuantityArray  = new ArrayList<>();
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userID = getIntent().getStringExtra("uid");

        productsList =  findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);
        closeTextBtn = (TextView) findViewById(R.id.close_products_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        loadingBar = new ProgressDialog(this);
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        loadingBar.setTitle("");
        loadingBar.setMessage("Dear Admin, please wait while we are loading product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child("Orders").orderByChild("orderID").equalTo(userID);

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cartArray.clear();
                cartQuantityArray.clear();
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    AdminOrders orders = Item.getValue(AdminOrders.class);
                    cartArray = orders.getProductsArray();
                    cartQuantityArray = orders.getProductsQuantityArray();
                }

                OrderProductAdapter adapter = new OrderProductAdapter(AdminUserProductsActivity.this, cartArray, cartQuantityArray);
                productsList.setAdapter(adapter);
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
