package com.am.induster.Activity.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.Admin.AdminHomeActivity;
import com.am.induster.Activity.Admin.AdminHomeProductActivity;
import com.am.induster.Activity.Admin.CustomCategoryAdapter;
import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Adapter.CustomImageAdapter;
import com.am.induster.Adapter.CustomImageUserAdapter;
import com.am.induster.Model.Products;
import com.am.induster.Model.UserProducts;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , CustomCategoryAdapter.ItemClickListener
{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AutoScrollViewPager adsViewpager;
    private ArrayList<Products> productList = new ArrayList<Products>();
    private String [] arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        arrayList = getResources().getStringArray(R.array.bike_category);
        ProductsRef = FirebaseDatabase.getInstance().getReference("UserProducts");

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_product_admin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);



        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        Picasso.with(HomeActivity.this).load(Prevalent.currentOnlineUser.getImage()).fit().centerCrop().placeholder(R.drawable.profile).into(profileImageView);


        recyclerView = findViewById(R.id.services_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        adsViewpager = findViewById(R.id.ads_viewpager);
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        CustomCategoryAdapter adapter = new CustomCategoryAdapter(HomeActivity.this, arrayList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child("UserProducts").orderByChild("productType").equalTo("Banner");

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productList.clear();
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    Products items = Item.getValue(Products.class);
                    Log.e("UserProducts"," "+items.getPname());
                    Log.e("UserProducts"," "+dataSnapshot.exists());
                    productList.add(items);
                }

                CustomImageUserAdapter adapter = new CustomImageUserAdapter(HomeActivity.this, productList);
                adsViewpager.setAdapter(adapter);
                adsViewpager.startAutoScroll();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_search)
        {
            Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_cart)
        {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(HomeActivity.this, SettinsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_offers)
        {
            Intent intent = new Intent(HomeActivity.this, OfferUserActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders)
        {
            Intent intent = new Intent(HomeActivity.this, UserOrderActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            FirebaseAuth.getInstance().signOut();
            Prevalent.currentOnlineUser = null;

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_contactus)
        {
            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(HomeActivity.this, HomeProductActivity.class);
        intent.putExtra("category", arrayList[position]);
        startActivity(intent);
    }
}