package com.example.appbeacon;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

public class AddActivity extends AppCompatActivity {

    EditText name_input, uuid_input, major_input, minor_input, txpower_input;
    Button add_button,generate_uuid;
    String uuid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#253A4B")));

        name_input = findViewById(R.id.name_input);
        uuid_input = findViewById(R.id.uuid_input);
        //uuid = UUID.randomUUID().toString();
        //uuid_input.setText(uuid);
        major_input = findViewById(R.id.major_input);
        minor_input = findViewById(R.id.minor_input);
        txpower_input = findViewById(R.id.txpower_input);
        generate_uuid = findViewById(R.id.generate_uuid);
        add_button = findViewById(R.id.add_button2);
        //add_button.setEnabled(false);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(name_input.getText().equals("") || major_input.getText().equals("") || minor_input.getText().equals("") || txpower_input.getText().equals("")){

                }else{*/
                    MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(AddActivity.this);
                    myDatabaseHelper.addBeacon(name_input.getText().toString().trim(), uuid_input.getText().toString().trim(), Integer.valueOf(major_input.getText().toString().trim()) , Integer.valueOf(minor_input.getText().toString().trim()) , Integer.valueOf(txpower_input.getText().toString().trim()));
                //}
            }
        });
        generate_uuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uuid = generateUUID();
                add_button.setEnabled(true);
            }
        });

    }

    public String generateUUID(){
        String uuid = UUID.randomUUID().toString().toUpperCase();
        uuid_input.setText(uuid);
        return uuid;
    }
}