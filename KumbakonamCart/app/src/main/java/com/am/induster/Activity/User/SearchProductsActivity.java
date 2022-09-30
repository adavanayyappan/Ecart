package com.am.induster.Activity.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Adapter.ProductUserAdpter;
import com.am.induster.Model.Products;
import com.am.induster.Model.UserProducts;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.ViewHolder.ProductViewHolder;
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

public class SearchProductsActivity extends AppCompatActivity implements ProductUserAdpter.ItemClickListener
{
    private Button SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput = "";
    private TextView closeTextBtn;
    private DatabaseReference ProductsRef;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<UserProducts> productArray  = new ArrayList<>();
    private ProgressDialog loadingBar;
    DatabaseReference RootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        ProductsRef = FirebaseDatabase.getInstance().getReference("UserProducts");
        RootRef = FirebaseDatabase.getInstance().getReference();

        inputText = findViewById(R.id.search_product_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        loadingBar = new ProgressDialog(this);

        searchList.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        searchList.setLayoutManager(layoutManager);
        closeTextBtn = (TextView) findViewById(R.id.close_users_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SearchInput = inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                .setQuery(ProductsRef.orderByChild("pname").startAt(SearchInput), Products.class)
//                .build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText(model.getDescription());
//                        holder.txtProductPrice.setText("Price: \u20B9"  + model.getPrice());
//                        holder.txtProductOffer.setText(model.getPoffer() + "%");
//                        holder.txtProductOfferPrice.setText("Offer Price: \u20B9" + model.getPofferprize());
//                        Picasso.with(SearchProductsActivity.this).load(model.getImage()).fit().centerCrop().into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view)
//                            {
//                                Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
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
//
//        searchList.setAdapter(adapter);
//        adapter.startListening();
        loadingBar.setTitle("");
        loadingBar.setMessage("Dear User, please wait while we are loading product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Query myMostViewedPostsQuery = RootRef.child("UserProducts");

        myMostViewedPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productArray.clear();
                for (DataSnapshot Item : dataSnapshot.getChildren()) {
                    UserProducts items = Item.getValue(UserProducts.class);

                    if (items.getPuserid().equals("Generall") || items.getPuserid().equals(Prevalent.currentOnlineUser.getPuserid())) {
                        if (items.getPname().toLowerCase().contains(SearchInput.toLowerCase())) {
                            productArray.add(items);
                        }
                    }
                }

                ProductUserAdpter adapter = new ProductUserAdpter(SearchProductsActivity.this, productArray);
                adapter.setClickListener(SearchProductsActivity.this);
                searchList.setAdapter(adapter);
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(SearchProductsActivity
                .this, ProductDetailsActivity.class);
        intent.putExtra("pid", productArray.get(position).getPid());
        startActivity(intent);

    }
}
