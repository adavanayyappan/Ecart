package com.am.induster.Activity.User;

import android.app.ProgressDialog;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.Admin.AdminUserProductsActivity;
import com.am.induster.Activity.Admin.CustomCategoryAdapter;
import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Adapter.OrderProductAdapter;
import com.am.induster.Adapter.ProductUserAdpter;
import com.am.induster.Model.AdminOrders;
import com.am.induster.Model.Products;
import com.am.induster.Model.UserProducts;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.ViewHolder.ProductViewHolder;
import com.firebase.ui.auth.data.model.User;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeProductActivity extends AppCompatActivity implements ProductUserAdpter.ItemClickListener
{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String category = "Generall";
    private TextView closeTextBtn;
    ArrayList<UserProducts> productArray  = new ArrayList<>();
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_product);

        category = getIntent().getStringExtra("category");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("UserProducts");

        Paper.init(this);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        loadingBar = new ProgressDialog(this);

        closeTextBtn = (TextView) findViewById(R.id.close_products_btn);
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
//                        .setQuery(ProductsRef.orderByChild("category").startAt(category), UserProducts.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<UserProducts, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<UserProducts, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final UserProducts model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText("Description: "  + model.getDescription());
//                        holder.txtProductPrice.setText("Price: \u20B9"  + model.getPrice());
//                        holder.txtProductOffer.setText(model.getPoffer() + "%");
//                        holder.txtProductOfferPrice.setText("Offer Price: \u20B9" + model.getPofferprize());
//                        Picasso.with(HomeProductActivity.this).load(model.getImage()).fit().centerCrop().into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                Intent intent = new Intent(HomeProductActivity
//                                        .this, ProductDetailsActivity.class);
//                                intent.putExtra("pid", model.getPid());
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
        loadingBar.setMessage("Dear User, please wait while we are loading product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child("UserProducts").orderByChild("category").equalTo(category);

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productArray.clear();
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    UserProducts items = Item.getValue(UserProducts.class);
                    Log.e("UserProducts"," "+items.getPname());
                    Log.e("UserProducts"," "+dataSnapshot.exists());
                    if (items.getPuserid().equals("Generall") || items.getPuserid().equals(Prevalent.currentOnlineUser.getPuserid())) {
                        productArray.add(items);
                    }
                }

                ProductUserAdpter adapter = new ProductUserAdpter(HomeProductActivity.this, productArray);
                adapter.setClickListener(HomeProductActivity.this);
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

        Intent intent = new Intent(HomeProductActivity
                .this, ProductDetailsActivity.class);
        intent.putExtra("pid", productArray.get(position).getPid());
        startActivity(intent);

    }
}
