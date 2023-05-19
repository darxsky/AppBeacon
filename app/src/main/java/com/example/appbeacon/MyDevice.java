package com.example.appbeacon;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyDevice extends AppCompatActivity {
    AdapterDevice adapterDevice;

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    MyDatabaseHelper myDatabaseHelper;
    ArrayList<String> beacon_id, beacon_name, beacon_uuid, beacon_major, beacon_minor, beacon_txpower;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydevice);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#253A4B")));
        
        recyclerView = findViewById(R.id.recyclerViewDevice);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDevice.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDatabaseHelper = new MyDatabaseHelper(MyDevice.this);
        beacon_id = new ArrayList<>();
        beacon_name = new ArrayList<>();
        beacon_uuid = new ArrayList<>();
        beacon_major = new ArrayList<>();
        beacon_minor = new ArrayList<>();
        beacon_txpower = new ArrayList<>();

        displayData();

        adapterDevice = new AdapterDevice(MyDevice.this,this, beacon_id, beacon_name, beacon_uuid, beacon_major, beacon_minor, beacon_txpower);
        recyclerView.setAdapter(adapterDevice);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyDevice.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void displayData(){
        Cursor cursor = myDatabaseHelper.readDataByDb();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "Nessun dato", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                beacon_id.add(cursor.getString(0));
                beacon_name.add(cursor.getString(1));
                beacon_uuid.add(cursor.getString(2));
                beacon_major.add(cursor.getString(3));
                beacon_minor.add(cursor.getString(4));
                beacon_txpower.add(cursor.getString(5));


            }
        }
    }
}
