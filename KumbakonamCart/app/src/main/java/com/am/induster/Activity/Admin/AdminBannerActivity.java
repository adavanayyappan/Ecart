package com.am.induster.Activity.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.User.CartActivity;
import com.am.induster.Activity.User.HomeProductActivity;
import com.am.induster.Activity.User.OfferUserActivity;
import com.am.induster.Activity.User.ProductDetailsActivity;
import com.am.induster.Adapter.ProductAdapter;
import com.am.induster.Adapter.ProductUserAdpter;
import com.am.induster.Model.Cart;
import com.am.induster.Model.Products;
import com.am.induster.Model.UserProducts;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.ViewHolder.CartViewHolder;
import com.am.induster.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdminBannerActivity extends AppCompatActivity implements ProductAdapter.ItemClickListener

{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView closeTextBtn;
    ArrayList<Products> productArray  = new ArrayList<>();
    ArrayList<String> productKey  = new ArrayList<>();
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_banner);

        ProductsRef = FirebaseDatabase.getInstance().getReference("Products");

        Paper.init(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_product_admin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminBannerActivity.this, AdminBannerAddActivity.class);
                startActivity(intent);
            }
        });

        loadingBar = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
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
//        Log.e("ds", Prevalent.currentOnlineUser.getPhone());
//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(ProductsRef.orderByChild("productType").startAt("Banner"), Products.class)
//                        .build();
//
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, final int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText(model.getDescription());
//                        holder.txtProductPrice.setText("Price: \u20B9"  + model.getPrice());
//                        holder.txtProductOffer.setText(model.getPofferprize() + "%");
//                        holder.txtProductOfferPrice.setText("Offer Price: \u20B9" + model.getPoffer());
//                        Picasso.with(AdminBannerActivity.this).load(model.getImage()).fit().centerCrop().into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//
//                                CharSequence options[] = new CharSequence[]
//                                        {
//                                                "Yes",
//                                                "No"
//                                        };
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminBannerActivity.this);
//                                builder.setTitle("Are you sure to delete this banner?");
//
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i)
//                                    {
//                                        if(i == 0)
//                                        {
//                                            String uID = getRef(position).getKey();
//                                            RemoveOrder(uID);
//
//                                        }
//                                        else
//                                        {
//                                            finish();
//                                        }
//
//                                    }
//                                });
//                                builder.show();
//
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                    {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
//                        ProductViewHolder holder = new ProductViewHolder(view);
//                        return holder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();

        loadingBar.setTitle("");
        loadingBar.setMessage("Dear Admin, please wait while we are loading product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child("Products").orderByChild("productType").equalTo("Banner");

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productArray.clear();
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    Products items = Item.getValue(Products.class);
                    Log.e("UserProducts"," "+items.getPname());
                    Log.e("UserProducts"," "+dataSnapshot.exists());
                    productArray.add(items);
                    productKey.add(Item.getKey());
                }

                ProductAdapter adapter = new ProductAdapter(AdminBannerActivity.this, productArray);
                adapter.setClickListener(AdminBannerActivity.this);
                recyclerView.setAdapter(adapter);
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        CharSequence options[] = new CharSequence[]
                {
                        "Yes",
                        "No"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminBannerActivity.this);
        builder.setTitle("Are you sure to delete this banner?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(i == 0)
                {
                    String uID = productKey.get(position);
                    RemoveOrder(uID);
                    productArray.remove(position);
                }
                else
                {
                    finish();
                }

            }
        });
        builder.show();

    }

    private void RemoveOrder(String uID)
    {
        HashMap<String, Object> oredrsMap = new HashMap<>();
        oredrsMap.put("productType", "Expired");
        ProductsRef.child(uID).updateChildren(oredrsMap);

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("Products").child(uID);
        mPostReference.removeValue();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("UserProducts").orderByChild("pid").equalTo(uID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                onStart();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}