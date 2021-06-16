package com.example.bankingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bankingapp.R;
import com.example.bankingapp.Models.User;
import com.example.bankingapp.Utils.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Bank.MainActivity";
    private Button viewAll;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.hide();

        init();
        generateDummy();
    }

    private void init() {
        dbHelper = new DataBaseHelper(this);
        viewAll = findViewById(R.id.allUsers);
        viewAll.setOnClickListener(view -> {
            Intent users = new Intent(MainActivity.this, AllUsers.class);
            startActivity(users);
        });
        viewAll.setEnabled(false);
    }

    private void generateDummy(){
        User[] userList = {
                new User(101,"Ziya", "ziya123@gmail.com", 6000),
                new User(102,"Pihu", "pihu123@gmail.com", 10000),
                new User(103,"Harsh", "harsh123@gmail.com", 5000),
                new User(104,"Druv", "druv123@gmail.com", 1100),
                new User(105,"Karan", "karan123@gmail.com", 79000),
                new User(106,"Tiya", "tiya123@gmail.com", 5000),
                new User(107,"Rashi", "rashi123@gmail.com", 11000),
                new User(108,"Kanak", "kanak123@gmail.com", 8000),
                new User(109,"Sapna", "sapna123@gmail.com", 70000),
                new User(1010,"Zoe", "zoe123@gmail.com", 50000)
        };
        new GenerateDummyData().execute(userList);
    }


    @SuppressLint("StaticFieldLeak")
    private class GenerateDummyData extends AsyncTask<User, Void, Void>{

        @Override
        protected Void doInBackground(User... users) {
            for(User user: users) {
                dbHelper.insertToUser(user);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            viewAll.setEnabled(true);
        }
    }
}