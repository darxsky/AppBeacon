package com.example.appbeacon;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText input_name, input_uuid, input_major, input_minor, input_txpower;
    Button update_button,delete_button;
    String id,name,uuid,major,minor,txpower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#253A4B")));


        input_name = findViewById(R.id.name_input2);
        input_uuid = findViewById(R.id.uuid_input2);
        input_major = findViewById(R.id.major_input2);
        input_minor = findViewById(R.id.minor_input2);
        input_txpower = findViewById(R.id.txpower_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        getIntentDate();
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(UpdateActivity.this);
                name = input_name.getText().toString().trim();
                uuid = input_uuid.getText().toString().trim();
                major = input_major.getText().toString().trim();
                minor = input_minor.getText().toString().trim();
                txpower = input_txpower.getText().toString().trim();
                myDatabaseHelper.updateData(id, name, uuid, major, minor, txpower);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }


    void getIntentDate(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("uuid") && getIntent().hasExtra("major") && getIntent().hasExtra("minor") && getIntent().hasExtra("txpower")){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            uuid = getIntent().getStringExtra("uuid");
            major = getIntent().getStringExtra("major");
            minor = getIntent().getStringExtra("minor");
            txpower = getIntent().getStringExtra("txpower");

            input_name.setText(name);
            input_uuid.setText(uuid);
            input_major.setText(major);
            input_minor.setText(minor);
            input_txpower.setText(txpower);

        }else{
            Toast.makeText(this, "Nessun dato", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminazione " + name + "?");
        builder.setMessage("Sicuro di voler eliminare il beacon " + name + "?");
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(UpdateActivity.this);
                myDatabaseHelper.delete_row(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}