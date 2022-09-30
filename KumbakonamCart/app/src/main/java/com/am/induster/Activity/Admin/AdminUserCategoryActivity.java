package com.am.induster.Activity.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.am.induster.Activity.Login.LoginActivity;
import com.am.induster.Activity.User.HomeActivity;
import com.am.induster.Adapter.ListAdminUserAdapter;
import com.am.induster.Model.Users;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.SupportingFiles.UserList;
import com.am.induster.SupportingFiles.Util;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class AdminUserCategoryActivity extends AppCompatActivity
{

    List<Users> list;
    ListAdminUserAdapter adapter;
    ActionMode actionMode;
    ActionCallback actionCallback;
    private RecyclerView mRecyclerView;
    private String parentDbName = "Users";
    private ProgressDialog loadingBar;
    private String saveCurrentDate, saveCurrentTime, productRandomKey;
    private TextView closeTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_category);

        mRecyclerView = (RecyclerView) findViewById(R.id.userList);
        loadingBar = new ProgressDialog(this);
        getUserList();

        closeTextBtn = (TextView) findViewById(R.id.close_users_btn);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

    }

    private void init(List<Users> list) {
        actionCallback = new ActionCallback();
        adapter = new ListAdminUserAdapter(this, list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClick(new ListAdminUserAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, Users inbox, int position) {
                if (adapter.selectedItemCount() > 0) {
                    toggleActionBar(position);
                } else {
                    Toast.makeText(AdminUserCategoryActivity.this, "clicked " + inbox.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongPress(View view, Users inbox, int position) {
                toggleActionBar(position);
            }

        });

    }

    /*
       toggling action bar that will change the color and option
     */

    private void toggleActionBar(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionCallback);
        }
        toggleSelection(position);
    }

    /*
       toggle selection of items and show the count of selected items on the action bar
     */

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.selectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    /*
       after selection, we need to delete selected items by tapping on delete icon
     */
    private void deleteInbox() {
        List<Integer> selectedItemPositions = adapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            adapter.removeItems(selectedItemPositions.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserList()
    {
        loadingBar.setTitle("Users");
        loadingBar.setMessage("Dear Admin, please wait while loading...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference(parentDbName);


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    list = new ArrayList<Users>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                        Users usersData = postSnapshot.getValue(Users.class);
                        Log.e("data",usersData.getName() + "");
                        list.add(usersData);
                    }
                    loadingBar.dismiss();
                    init(list);
                }
                else
                {
                    Toast.makeText(AdminUserCategoryActivity.this, "No Users exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveUserProductInfoToDatabase(String userID)
    {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = Util.random();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("upid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("pid", Prevalent.userProducts.getPid());
        productMap.put("description", Prevalent.userProducts.getDescription());
        productMap.put("image", Prevalent.userProducts.getImage());
        productMap.put("images", Prevalent.userProducts.getImages());
        productMap.put("category", Prevalent.userProducts.getCategory());
        productMap.put("price", Prevalent.userProducts.getPrice());
        productMap.put("pname", Prevalent.userProducts.getPname());
        productMap.put("puserid", userID);
        productMap.put("poffer", Prevalent.userProducts.getPofferprize());
        productMap.put("pofferprize", Prevalent.userProducts.getPoffer());
        productMap.put("productType", Prevalent.userProducts.getProductType());
        productMap.put("pofferbrand", Prevalent.userProducts.getPofferbrand());

        final DatabaseReference ProductsRef;
        ProductsRef = FirebaseDatabase.getInstance().getReference("UserProducts");

        ProductsRef.child(Util.random()).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            finish();
                            loadingBar.dismiss();
                            Toast.makeText(AdminUserCategoryActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminUserCategoryActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class ActionCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Util.toggleStatusBarColor(AdminUserCategoryActivity.this, R.color.blue_grey_700);
            mode.getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.AddItem:
                    List<Integer> selectedItemPositions = adapter.getSelectedItems();
                    for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                        Users itemUser = list.get(i);
                        SaveUserProductInfoToDatabase(itemUser.getPuserid());
                    }
                    mode.finish();
                    return true;
                case R.id.AddAllItem:
                    mode.finish();
                    SaveUserProductInfoToDatabase("Generall");
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
            Util.toggleStatusBarColor(AdminUserCategoryActivity.this, R.color.colorPrimary);
        }
    }
}