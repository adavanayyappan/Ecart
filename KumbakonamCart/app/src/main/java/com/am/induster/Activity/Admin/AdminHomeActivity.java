package com.am.induster.Activity.Admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Activity.Login.LoginAdminActivity;
import com.am.induster.Activity.User.CartActivity;
import com.am.induster.Activity.User.HomeActivity;
import com.am.induster.Activity.User.ProductDetailsActivity;
import com.am.induster.Activity.User.SettinsActivity;
import com.am.induster.Adapter.CustomImageAdapter;
import com.am.induster.Adapter.ProductAdapter;
import com.am.induster.Model.Products;
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


public class AdminHomeActivity extends AppCompatActivity
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
        setContentView(R.layout.activity_admin_home);

        arrayList = getResources().getStringArray(R.array.bike_category);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Prevalent.userProducts = null;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_product_admin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminHomeActivity.this, AdminAddNewProductActivity.class);
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
        Picasso.with(AdminHomeActivity.this).load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).fit().centerCrop().into(profileImageView);

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


        CustomCategoryAdapter adapter = new CustomCategoryAdapter(AdminHomeActivity.this, arrayList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = RootRef.child("Products").orderByChild("productType").equalTo("Banner");

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

                CustomImageAdapter adapter = new CustomImageAdapter(AdminHomeActivity.this, productList);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
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
            Intent intent = new Intent(AdminHomeActivity.this, AdminHomeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders)
        {
            Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_users)
        {
            Intent intent = new Intent(AdminHomeActivity.this, AdminUserActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_banners)
        {
            Intent intent = new Intent(AdminHomeActivity.this, AdminBannerActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            FirebaseAuth.getInstance().signOut();
            Prevalent.currentOnlineUser = null;

            Intent intent = new Intent(AdminHomeActivity.this, LoginAdminActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(AdminHomeActivity.this, AdminHomeProductActivity.class);
        intent.putExtra("category", arrayList[position]);
        startActivity(intent);
    }
}

