package com.am.induster.Activity.User;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Adapter.ProductUserAdpter;
import com.am.induster.Model.Cart;
import com.am.induster.Model.UserProducts;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.ViewHolder.CartViewHolder;
import com.am.induster.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class OfferUserActivity  extends AppCompatActivity implements ProductUserAdpter.ItemClickListener

{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView closeTextBtn;
    ArrayList<UserProducts> productArray  = new ArrayList<>();
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_user_layout);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("UserProducts");

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_orders);
        setSupportActionBar(toolbar);
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
//        FirebaseRecyclerOptions<UserProducts> options =
//                new FirebaseRecyclerOptions.Builder<UserProducts>()
//                        .setQuery(ProductsRef.orderByChild("puserid").startAt(Prevalent.currentOnlineUser.getPuserid()), UserProducts.class)
//                        .build();
//
//
//        FirebaseRecyclerAdapter<UserProducts, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<UserProducts, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final UserProducts model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText(model.getDescription());
//                        holder.txtProductPrice.setText("Price: \u20B9"  + model.getPrice());
//                        holder.txtProductOffer.setText(model.getPoffer() + "%");
//                        holder.txtProductOfferPrice.setText("Offer Price: \u20B9" + model.getPofferprize());
//                        Picasso.with(OfferUserActivity.this).load(model.getImage()).fit().centerCrop().into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                Intent intent = new Intent(OfferUserActivity.this, ProductDetailsActivity.class);
//                                intent.putExtra("pid", model.getPid());
//                                startActivity(intent);
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
        loadingBar.setMessage("Dear User, please wait while we are loading product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child("UserProducts").orderByChild("puserid").equalTo(Prevalent.currentOnlineUser.getPuserid());

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productArray.clear();
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    UserProducts items = Item.getValue(UserProducts.class);
                    productArray.add(items);
                }

                ProductUserAdpter adapter = new ProductUserAdpter(OfferUserActivity.this, productArray);
                adapter.setClickListener(OfferUserActivity.this);
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

        Intent intent = new Intent(OfferUserActivity
                .this, ProductDetailsActivity.class);
        intent.putExtra("pid", productArray.get(position).getPid());
        startActivity(intent);

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
