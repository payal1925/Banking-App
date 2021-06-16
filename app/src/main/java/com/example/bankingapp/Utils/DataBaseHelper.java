package com.example.bankingapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bankingapp.Models.Transfer;
import com.example.bankingapp.Models.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String NAME = "bank.db";
    public static final int VERSION = 1;
    private final String TABLE_USER = "user";
    private final String USER_COL_ID = "user_id";
    private final String USER_COL_NAME = "name";
    private final String USER_COL_EMAIL = "email";
    private final String USER_COL_BALANCE = "current_balance";
    private final String TABLE_TRANSFERS = "transfers";
    private final String TRANSFERS_COL_ID = "transfer_id";
    private final String TRANSFERS_COL_DATETIME = "transaction_date";
    private final String TRANSFERS_COL_FROM = "from_account";
    private final String TRANSFERS_COL_TO = "to_account";
    private final String TRANSFERS_COL_AMOUNT = "amount";

    private Cursor cursor;

    public DataBaseHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_User = "create table " + TABLE_USER + "( "
                + USER_COL_ID + " number primary key, "
                + USER_COL_NAME + " text not null, "
                + USER_COL_EMAIL + " text, "
                + USER_COL_BALANCE + " real not null);";

        String create_Transfers = "create table " + TABLE_TRANSFERS + "( "
                + TRANSFERS_COL_ID + " integer primary key autoincrement, "
                + TRANSFERS_COL_DATETIME + " datetime default (datetime('now')), "
                + TRANSFERS_COL_FROM + " number not null, "
                + TRANSFERS_COL_TO + " number not null, "
                + TRANSFERS_COL_AMOUNT + " real not null);";

        sqLiteDatabase.execSQL(create_User);
        sqLiteDatabase.execSQL(create_Transfers);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_USER);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_TRANSFERS);
        onCreate(sqLiteDatabase);
    }

    public void insertToUser(User user) {
        /*
         * Inserts User to User Table
         */
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_ID, user.getId());
        contentValues.put(USER_COL_NAME, user.getName());
        contentValues.put(USER_COL_EMAIL, user.getEmail());
        contentValues.put(USER_COL_BALANCE, user.getCurrent_balance());
        database.insert(TABLE_USER, null, contentValues);
        database.close();
    }

    private void insertToTransfers(Transfer transfer) {
        /*
         *   Helper method to insert a transfer to Transfers Table
         */
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSFERS_COL_FROM, transfer.getFrom_account());
        contentValues.put(TRANSFERS_COL_TO, transfer.getTo_account());
        contentValues.put(TRANSFERS_COL_AMOUNT, transfer.getAmount());
        database.insert(TABLE_TRANSFERS, null, contentValues);
        database.close();
    }

    public List<User> getAllUsers() {
        /*
         * Gets List of all Users registered in database
         */
        SQLiteDatabase database = getReadableDatabase();
        String query = "select * from " + TABLE_USER + " order by " + USER_COL_NAME;
        List<User> userList = new ArrayList<>();
        cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            userList.add(new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3)));
            cursor.moveToNext();
        }
        database.close();
        return userList;
    }
    public List<Transfer> getMyTransfers(long account_no) {
        /*
         * Gets all transfers concerned with the given account
         */
        String account = String.valueOf(account_no);
        SQLiteDatabase database = getReadableDatabase();
        String query = "select * from " + TABLE_TRANSFERS
                + " where " + TRANSFERS_COL_FROM + "=?"
                + " or " + TRANSFERS_COL_TO + "=?"
                + " order by " + TRANSFERS_COL_DATETIME + " desc";
        List<Transfer> transferList = new ArrayList<>();
        cursor = database.rawQuery(query, new String[]{ account, account });
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            transferList.add(new Transfer(cursor.getString(1),cursor.getLong(2), cursor.getLong(3), cursor.getDouble(4)));
            cursor.moveToNext();
        }
        database.close();
        return transferList;
    }

    public User getUser(long uid) {
        /*
         * Gets user of specified UID
         */
        SQLiteDatabase database = getReadableDatabase();
        String query = "select * from " + TABLE_USER
                + " where " + USER_COL_ID + "=?";
        cursor = database.rawQuery(query, new String[]{""+uid});
        cursor.moveToFirst();
        User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
        database.close();
        return user;
    }

    private boolean changeAmount(String uid, double amount) {
        /*
         *   Helper method to update amount for given uid
         */
        SQLiteDatabase database = getWritableDatabase();
        String query = "select " + USER_COL_BALANCE + " from " + TABLE_USER
                + " where " + USER_COL_ID + "=?";
        cursor = database.rawQuery(query, new String[]{uid});
        cursor.moveToFirst();

        double balance = cursor.getDouble(0);
        balance += amount;
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_BALANCE, balance);
        int result = database.update(TABLE_USER, contentValues, USER_COL_ID + "=?", new String[]{uid});
        database.close();
        return result == 1;
    }

    public boolean sendMoney(long from, long to, double amount) {
        if (changeAmount(String.valueOf(from), -amount) && changeAmount(String.valueOf(to), amount)) {
            insertToTransfers(new Transfer(null, from,to,amount));
            return true;
        }
        return false;
    }

}
