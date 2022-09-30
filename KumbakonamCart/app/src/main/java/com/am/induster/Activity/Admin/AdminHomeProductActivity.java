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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.Login.LoginAdminActivity;
import com.am.induster.Activity.User.HomeProductActivity;
import com.am.induster.Activity.User.ProductDetailsActivity;
import com.am.induster.Adapter.ProductAdapter;
import com.am.induster.Adapter.ProductUserAdpter;
import com.am.induster.Model.Products;
import com.am.induster.Model.UserProducts;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AdminHomeProductActivity extends AppCompatActivity implements ProductAdapter.ItemClickListener
{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String category = "Generall";
    private TextView closeTextBtn, historyTextButton;
    ArrayList<Products> productArray  = new ArrayList<>();
    ArrayList<String> productKey  = new ArrayList<>();
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_product);

        category = getIntent().getStringExtra("category");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);

        closeTextBtn = (TextView) findViewById(R.id.close_products_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        Prevalent.userProducts = null;

        loadingBar = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart()
    {
        super.onStart();

//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(ProductsRef.orderByChild("category").startAt(category), Products.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText(model.getDescription());
//                        holder.txtProductPrice.setText("Price: \u20B9" + model.getPrice());
//                        holder.txtProductOffer.setText(model.getPofferprize() + "%");
//                        holder.txtProductOfferPrice.setText("Offer Price: \u20B9" + model.getPoffer());
//                        Picasso.with(AdminHomeProductActivity.this).load(model.getImage()).fit().centerCrop().placeholder(R.drawable.app_icon).into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                Intent intent = new Intent(AdminHomeProductActivity.this, AdminProductDetailActivity.class);
//                                intent.putExtra("pid", model.getPid());
//                                Prevalent.userProducts = model;
//                                startActivity(intent);
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
        Query myMostViewedPostsQuery = RootRef.child("Products").orderByChild("category").equalTo(category);

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

                ProductAdapter adapter = new ProductAdapter(AdminHomeProductActivity.this, productArray);
                adapter.setClickListener(AdminHomeProductActivity.this);
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
                        "Product Detail",
                        "Delete Product"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeProductActivity.this);
        builder.setTitle("Options");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(i == 0)
                {
                    Intent intent = new Intent(AdminHomeProductActivity.this, AdminProductDetailActivity.class);
                    intent.putExtra("pid", productArray.get(position).getPid());
                    Prevalent.userProducts = productArray.get(position);
                    startActivity(intent);
                }
                else
                {
                    String uID = productKey.get(position);
                    RemoveOrder(uID,productArray.get(position).getPid());
                }

            }
        });
        builder.show();
    }

    private void RemoveOrder(String uID, String pid)
    {
        CharSequence options[] = new CharSequence[]
                {
                        "Yes",
                        "No"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeProductActivity.this);
        builder.setTitle("Are you sure to delete this Product?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(i == 0)
                {
                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                            .child("Products").child(uID);
                    mPostReference.removeValue();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query applesQuery = ref.child("UserProducts").orderByChild("pid").equalTo(pid);

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
                else
                {
                    finish();
                }

            }
        });
        builder.show();
    }
}
