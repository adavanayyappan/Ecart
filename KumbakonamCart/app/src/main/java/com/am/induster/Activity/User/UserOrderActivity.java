package com.am.induster.Activity.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Activity.Admin.AdminOrdersHistoryActivity;
import com.am.induster.Activity.Admin.AdminUserProductsActivity;
import com.am.induster.Model.AdminOrders;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserOrderActivity extends AppCompatActivity
{
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private TextView closeTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
        closeTextBtn = (TextView) findViewById(R.id.close_orders_btn);
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

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef.orderByChild("puserid").equalTo(Prevalent.currentOnlineUser.getPuserid()), AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, UserOrderActivity.AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, UserOrderActivity.AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserOrderActivity.AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model)
                    {
                        holder.userName.setText("Name: " + model.getName());
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount: \u20B9" + model.getTotalAmount());
                        holder.userDateTime.setText("Order at: " + model.getDate() + " " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity()+ ", " + model.getState1()+ ", " + model.getCountry()+ "-" + model.getPincode());
                        holder.userState.setText("Status: " + model.getState());

                        holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(UserOrderActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", model.getOrderID());
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UserOrderActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new UserOrderActivity.AdminOrdersViewHolder(view);
                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress,userState;
        private Button ShowOrdersBtn;

        public AdminOrdersViewHolder(View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userState = itemView.findViewById(R.id.order_status);
            ShowOrdersBtn = itemView.findViewById(R.id.show_products_btn);
        }
    }
}
