package com.am.induster.Activity.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.am.induster.Adapter.ListAdminUserAdapter;
import com.am.induster.Model.Users;
import com.am.induster.Prevalent.Prevalent;
import com.am.induster.R;
import com.am.induster.SupportingFiles.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class AdminUserActivity extends AppCompatActivity
{

    List<Users> list;
    ListAdminUserAdapter adapter;
    ActionMode actionMode;
    AdminUserActivity.ActionCallback actionCallback;
    private RecyclerView mRecyclerView;
    private String parentDbName = "Users";
    private ProgressDialog loadingBar;
    private String saveCurrentDate, saveCurrentTime, productRandomKey;
    private TextView closeTextBtn;
    ArrayList<String> productKey  = new ArrayList<>();

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
        actionCallback = new AdminUserActivity.ActionCallback();
        adapter = new ListAdminUserAdapter(this, list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClick(new ListAdminUserAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, Users inbox, int position) {

                CharSequence options[] = new CharSequence[]
                        {
                                "Yes",
                                "No"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminUserActivity.this);
                builder.setTitle("Are you sure to delete this User?");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if(i == 0) {
                            String uID = productKey.get(position);
//                            deleteuser(list.get(position).getEmail(), list.get(position).getPassword(), uID);
                            DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                                  .child("Users").child(uID);
                            mPostReference.removeValue();
                            Toast.makeText(AdminUserActivity.this, "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                            getUserList();
                        }
                        else
                        {
                            finish();
                        }
                    }
                });
                builder.show();

            }

            @Override
            public void onLongPress(View view, Users inbox, int position) {

            }

        });

    }

    private void deleteuser(String email, String password, String uuid) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User account deleted.");
//                                               DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
//                                  .child("Users").child(uuid);
//                            mPostReference.removeValue();
                                                Toast.makeText(AdminUserActivity.this, "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
        }
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
                        productKey.add(postSnapshot.getKey());
                    }
                    loadingBar.dismiss();
                    init(list);
                }
                else
                {
                    Toast.makeText(AdminUserActivity.this, "No Users exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private class ActionCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Util.toggleStatusBarColor(AdminUserActivity.this, R.color.blue_grey_700);
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

                    }
                    mode.finish();
                    return true;
                case R.id.AddAllItem:
                    mode.finish();

                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
            Util.toggleStatusBarColor(AdminUserActivity.this, R.color.colorPrimary);
        }
    }
}
