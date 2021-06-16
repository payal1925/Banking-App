package com.example.bankingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankingapp.R;
import com.example.bankingapp.Adapter.TransactionListAdapter;
import com.example.bankingapp.Models.Transfer;
import com.example.bankingapp.Models.User;
import com.example.bankingapp.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TransferMoney extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "Bank.TransferMoney";
    private DataBaseHelper dbHelper;
    private User user;
    private long uid;
    private ActionBar supportActionBar;
    private TransactionListAdapter adapter;
    private ArrayList<String> userNames;
    private TextView email;
    private TextView balance;
    private TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }

        uid = getIntent().getLongExtra("UID", -1);
        if (uid != -1)
            init();
    }

    private void init() {
        dbHelper = new DataBaseHelper(this);
        user = dbHelper.getUser(uid);
        if(supportActionBar != null)
            supportActionBar.setTitle(user.getName());

        Button sendMoney = findViewById(R.id.sendMoney);
        sendMoney.setOnClickListener(this);

        email = findViewById(R.id.emailId);
        balance = findViewById(R.id.currentBalance);
        account = findViewById(R.id.account_no);

        RecyclerView recyclerView = findViewById(R.id.transactionList);
        adapter = new TransactionListAdapter(user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        userNames = new ArrayList<>();
        for(User u: dbHelper.getAllUsers()) {
            if(u.getId() != user.getId())
                userNames.add(u.getId() + " | " + u.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = dbHelper.getUser(uid);
        email.setText(user.getEmail());
        account.setText(String.valueOf(user.getId()));
        balance.setText(String.valueOf(user.getCurrent_balance()));
        List<Transfer> myTransfers = dbHelper.getMyTransfers(user.getId());
        adapter.setList(myTransfers);
        adapter.notifyDataSetChanged();
    }

    private void transferDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.transfer_dialog, null, false);
        Spinner spinner = v.findViewById(R.id.users);
        EditText editText = v.findViewById(R.id.amount);
        Button cancel = v.findViewById(R.id.cancel);
        Button confrim = v.findViewById(R.id.confirm);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = spinner.getSelectedItem().toString().split( " \\| ")[0];
                String amt = editText.getText().toString().trim();

                if (amt.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double amount = Double.parseDouble(amt);
                        if (amount > user.getCurrent_balance()) {
                            Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        } else {
                            if (dbHelper.sendMoney(user.getId(), Integer.parseInt(id), amount)) {
                                Toast.makeText(getApplicationContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                onResume();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Transaction Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sendMoney) {
            transferDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
