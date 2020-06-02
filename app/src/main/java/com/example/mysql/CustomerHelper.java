//Author: Mordy dabah
package com.example.mysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class CustomerHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "customer.db";
    public static final String TABLE_CUSTOMER = "tblcustomer";
    public static final int DATABASEVERSION = 1;
    public static final String COLUMN_ID = "customerId";
    public static final String COLUMN_FIRST_NAME = "firstName";
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_AVG_PRICE = "avg_shopping_price";

    private static final String CREATE_TABLE_CUSTOMER
            = "CREATE TABLE IF NOT EXISTS " +
            TABLE_CUSTOMER + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FIRST_NAME + " VARCHAR,"
            + COLUMN_LAST_NAME + " VARCHAR,"
            + COLUMN_CITY + " VARCHAR,"
            + COLUMN_AVG_PRICE + " INTEGER " + ");";

    private SQLiteDatabase database;

    public CustomerHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
        // TODO Auto-generated constructor stub
    }

    public CustomerHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CUSTOMER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        onCreate(db);
    }

    public void open() {
        database = this.getWritableDatabase();
    }

    public Customer createCustomer(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(CustomerHelper.COLUMN_FIRST_NAME, customer.getFirstName());
        values.put(CustomerHelper.COLUMN_LAST_NAME, customer.getLastName());
        values.put(CustomerHelper.COLUMN_CITY, customer.getCity());
        values.put(CustomerHelper.COLUMN_AVG_PRICE, customer.getAvg_shopping_price());
        long insertId = database.insert(CustomerHelper.TABLE_CUSTOMER, null, values);

        customer.setCustomerId(insertId);
        return customer;
    }

    public boolean isExistingCustomer(Customer customer) {
        String select = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " +
                CustomerHelper.COLUMN_FIRST_NAME + " = '" + customer.getFirstName() + "' AND " +
                CustomerHelper.COLUMN_LAST_NAME + " = '" + customer.getLastName() + "' AND " +
                CustomerHelper.COLUMN_CITY + " = '" + customer.getCity() + "';";
        Cursor c = database.rawQuery(select, null);
        return c.getCount() >= 1;
    }

    public ArrayList<Customer> showAll() {
        ArrayList<Customer> customers = new ArrayList<>();
        String select_all = "SELECT * FROM " + TABLE_CUSTOMER + ";";
        Cursor c = database.rawQuery(select_all, null);
        while (c.moveToNext()) {
            int customer_id = c.getInt(0);
            String firstname = c.getString(1);
            String lastname = c.getString(2);
            String city = c.getString(3);
            int avg_price = c.getInt(4);
            Customer customer = new Customer(customer_id, firstname, lastname, city, avg_price);
            customers.add(customer);
        }
        return customers;
    }

    public boolean deleteCustomer(long customer_id) {
        return database.delete(TABLE_CUSTOMER, CustomerHelper.COLUMN_ID + "=" + customer_id, null) > 0;
    }

    public void updateCustomer(Customer customer) {
        ContentValues update_customer = new ContentValues();
        update_customer.put(CustomerHelper.COLUMN_FIRST_NAME, customer.getFirstName());
        update_customer.put(CustomerHelper.COLUMN_LAST_NAME, customer.getLastName());
        update_customer.put(CustomerHelper.COLUMN_CITY, customer.getCity());
        update_customer.put(CustomerHelper.COLUMN_AVG_PRICE, customer.getAvg_shopping_price());
        database.update(TABLE_CUSTOMER, update_customer, CustomerHelper.COLUMN_ID + "=" + customer.getCustomerId(), null);
    }

    public ArrayList<Customer> getCustomersAvgPrice(int avg_price) {
        ArrayList<Customer> customers = new ArrayList<>();
        String select_avg_price = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " +
                CustomerHelper.COLUMN_AVG_PRICE + " <= '" + avg_price +
                "' ORDER BY " + CustomerHelper.COLUMN_AVG_PRICE + " DESC" + ";";
        Cursor c = database.rawQuery(select_avg_price, null);
        while (c.moveToNext()) {
            int customer_id = c.getInt(0);
            String firstname = c.getString(1);
            String lastname = c.getString(2);
            String city = c.getString(3);
            int avg_price_for_customer = c.getInt(4);
            Customer customer = new Customer(customer_id, firstname, lastname, city, avg_price_for_customer);
            customers.add(customer);
        }
        return customers;
    }

    public ArrayList<Customer> getCustomersSubString(String substring) {
        ArrayList<Customer> customers = new ArrayList<>();
        String select_substring = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " +
                CustomerHelper.COLUMN_FIRST_NAME + " LIKE '%" + substring + "%';";
        Cursor c = database.rawQuery(select_substring, null);
        while (c.moveToNext()) {
            int customer_id = c.getInt(0);
            String firstname = c.getString(1);
            String lastname = c.getString(2);
            String city = c.getString(3);
            int avg_price_for_customer = c.getInt(4);
            Customer customer = new Customer(customer_id, firstname, lastname, city, avg_price_for_customer);
            customers.add(customer);
        }
        return customers;
    }
}
