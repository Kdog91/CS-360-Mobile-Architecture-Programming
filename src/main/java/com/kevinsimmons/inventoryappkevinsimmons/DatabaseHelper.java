package com.kevinsimmons.inventoryappkevinsimmons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "InventoryApp.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    // Inventory table
    private static final String TABLE_INVENTORY = "inventory";
    private static final String COL_ITEM_ID = "id";
    private static final String COL_ITEM_NAME = "item_name";
    private static final String COL_ITEM_QTY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUsersTable);

        String createInventoryTable = "CREATE TABLE " + TABLE_INVENTORY + " (" +
                COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ITEM_NAME + " TEXT NOT NULL, " +
                COL_ITEM_QTY + " INTEGER NOT NULL)";
        db.execSQL(createInventoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        onCreate(db);
    }

    // ---------- USER METHODS ----------

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // ---------- INVENTORY CRUD METHODS ----------

    public long addItem(String itemName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ITEM_NAME, itemName);
        values.put(COL_ITEM_QTY, quantity);

        long id = db.insert(TABLE_INVENTORY, null, values);
        db.close();
        return id;
    }

    public List<InventoryItem> getAllItems() {
        List<InventoryItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVENTORY, null, null, null, null, null, COL_ITEM_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_NAME));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_QTY));
                itemList.add(new InventoryItem(id, name, qty));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public int updateItem(int id, String newName, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ITEM_NAME, newName);
        values.put(COL_ITEM_QTY, newQuantity);

        int rowsAffected = db.update(TABLE_INVENTORY, values, COL_ITEM_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INVENTORY, COL_ITEM_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}