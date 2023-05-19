package com.example.appbeacon;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    String name;
    String uuid;
    int major;
    int minor;
    double rssi;
    double distance;
    double txpower;

    TextView name_textView, uuid_textView, major_textView, minor_textView, rssi_textView,distance_textView,txpower_textView,proximity_textView;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#253A4B")));

        name_textView = findViewById(R.id.name_text);
        uuid_textView = findViewById(R.id.uuid_text);
        major_textView = findViewById(R.id.major_text);
        minor_textView = findViewById(R.id.minor_text);
        rssi_textView = findViewById(R.id.rssi_text);
        distance_textView = findViewById(R.id.distance_text);
        txpower_textView = findViewById(R.id.tx_text);
        proximity_textView = findViewById(R.id.proximity_text);

        getData();
        setData();

        /*title = findViewById(R.id.title);
        description = findViewById(R.id.description);



        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("myImage") && getIntent().hasExtra("data1") && getIntent().hasExtra("data2")) {
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            myImage = getIntent().getIntExtra("myImage", 1);
        }else{
            Toast.makeText(this, "Nessun dato", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        title.setText(data1);
        description.setText(data2);*/
    }

    //Recupero dati dalla precedente activity con Intent
    private void getData(){
        if(getIntent().hasExtra("name") && getIntent().hasExtra("uuid") && getIntent().hasExtra("Major") && getIntent().hasExtra("Minor") && getIntent().hasExtra("Rssi") && getIntent().hasExtra("Distance") && getIntent().hasExtra("TxPower")){
            name = getIntent().getStringExtra("name");
            uuid = getIntent().getStringExtra("uuid");
            major = getIntent().getIntExtra("Major",0);
            minor = getIntent().getIntExtra("Minor",0);
            rssi = getIntent().getDoubleExtra("Rssi",0);
            distance = (getIntent().getDoubleExtra("Distance",0))*100.0/(100.0);
            txpower = getIntent().getDoubleExtra("TxPower",0);
        }else{
            Toast.makeText(this, "Nessun dato!", Toast.LENGTH_SHORT).show();
        }
    }

    //Inserisco i dati recuperati nei TextView presenti nella seconda activity
    private void setData(){
        name_textView.setText(name);
        uuid_textView.setText(uuid);
        major_textView.setText(""+major);
        minor_textView.setText(""+minor);
        String rssi_double = Double.toString(rssi);
        rssi_textView.setText(rssi_double);
        distance_textView.setText(String.valueOf(distance));
        if(distance < 1.0){
            proximity_textView.setText("Vicino");
        }else {
            proximity_textView.setText("Lontano");
        }
        txpower_textView.setText(String.valueOf(txpower));
    }

}