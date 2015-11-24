package com.example.darosale.distributedorderingsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MyActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.darosale.distributedorderingsystem.MESSAGE";
    public static final HashMap<String, Double> PRICES;

    static {
        PRICES = new HashMap<String, Double>();
        PRICES.put("Chicken & Waffles", 12.99);
        PRICES.put("Steak & Potatoes", 18.99);
        PRICES.put("Burger & Fries", 10.59);
    }

    public static String user = "default";
    public static HashMap<String, Integer> tableComps = new HashMap<String, Integer>();
    public static HashMap<String, HashMap<String, Integer>> tableOrders = new HashMap<String, HashMap<String, Integer>>();
    public static HashMap<String, String> accounts = new HashMap<String, String>();
    public static String vClock = "0,0,0,0,0,0,0,0,0,0";

    static {
        accounts.put("root", "passw0rd");
    }

    public static HashMap<Integer, String> queue = new HashMap<Integer, String>();

    static {
        queue.put(1, "Empty");
        queue.put(2, "Empty");
        queue.put(3, "Empty");
        queue.put(4, "Empty");
        queue.put(5, "Empty");
        queue.put(6, "Empty");
        queue.put(7, "Empty");
        queue.put(8, "Empty");
        queue.put(9, "Empty");
        queue.put(10, "Empty");
    }


    public static double getItemPrice(String item) {
        return PRICES.get(item);
    }

    public static void updateTableOrder(String table, String item, Integer qty) {
        if (tableOrders.containsKey(table)) {
            tableOrders.get(table).put(item, qty);
        } else {
            HashMap<String, Integer> newTable = new HashMap<String, Integer>();
            newTable.put(item, qty);
            tableOrders.put(table, newTable);
        }
    }

    public static HashMap<String, Integer> getTableOrder(String table) {
        return tableOrders.get(table);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        setTitle("JDA Restaurant");
        Log.d("Info", " Starting thread");
        ListenerThread lt = new ListenerThread();
        lt.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        EditText usr = (EditText) findViewById(R.id.username1);
        String setUser = usr.getText().toString();
        EditText pwd = (EditText) findViewById(R.id.pwd1);
        String passwd = pwd.getText().toString();
        TextView invalid = (TextView) findViewById(R.id.invalid1);
        if (accounts.containsKey(setUser)) {
            if (passwd.equals(accounts.get(setUser))) {
                invalid.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, TableLayout.class);
                user = setUser;
                startActivity(intent);
            }
        } else {
            invalid.setVisibility(View.VISIBLE);
        }
    }

    public void createAccount(View view) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MyActivity.this);
        builderSingle.setTitle("Create User Account");
        //builderSingle.setIcon(R.drawable.ic_launcher);
        LayoutInflater inflater = this.getLayoutInflater();
        final View inflaterView = inflater.inflate(R.layout.create_account, null);
        builderSingle.setView(inflaterView);
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText u = (EditText) inflaterView.findViewById(R.id.username2);
                EditText p = (EditText) inflaterView.findViewById(R.id.password2);
                String usr = u.getText().toString();
                String pwd = p.getText().toString();
                accounts.put(usr, pwd);
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }
}
