package com.example.bankingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bankingapp.R;
import com.example.bankingapp.Adapter.UserListAdapter;
import com.example.bankingapp.Models.User;
import com.example.bankingapp.Utils.DataBaseHelper;

import java.util.List;

public class AllUsers extends AppCompatActivity {

    private static final String TAG = "Bank.AllUsers";
    private DataBaseHelper dbHelper;
    private List<User> userList;
    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }

        init();
    }

    private void init() {
        dbHelper = new DataBaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.usersRecyclerView);
        adapter = new UserListAdapter();
        adapter.setClickHandler(position -> {
            User user = userList.get(position);
            Intent transfer = new Intent(AllUsers.this, TransferMoney.class);
            transfer.putExtra("UID", user.getId());
            startActivity(transfer);
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userList = dbHelper.getAllUsers();
        adapter.setUserList(userList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
