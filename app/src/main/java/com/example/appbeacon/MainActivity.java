package com.example.appbeacon;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static int SPLASH_TIME_OUT_CARD = 5000;
    private static int SPLASH_TIME_OUT_DIALOG_PROGRESS = 4000;
    public static final String TAG = "BeaconsEverywhere";
    private static final String UNIQUE_ID = "com.example.myapp.boostrapRegion";
    private BeaconManager beaconManager;
    private Region mAllBeaconsRegion;
    private Region mAllBeaconsRegion1;
    public static final String PROXIMITY_UUID = "ACFD065E-C3C0-11E3-9BBE-1A514932AC01";
    public static final String PROXIMITY_UUID1 = "50765CB7-D9EA-4E21-99A4-FA879613A492";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private static final long DEFAULT_SCAN_PERIOD_MS = 10000l;
    private static final String ALL_BEACONS_REGION = "AllBeaconsRegion";

    TextView distance_text;
    TextView zero_result;
    Button startScanButton;
    Button stopScanButton;

    RecyclerView recyclerView;
    LinearLayoutManager llm = new LinearLayoutManager(this);

    public static final int BLUETOOTH_REQ_CODE = 1;
    BluetoothAdapter bluetoothAdapter;
    private int CompanyIdentifier = 0x0059;
    private MyAdapter myAdapter;

    //Alert Dialog per controllo Bluetooth on
    AlertDialog.Builder builder;
    ActionBar actionBar;

    //ProgressDialog
    ProgressDialog dialog;

    private ArrayList<BeaconData> mBeaconData;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAllBeaconsRegion = new Region(UNIQUE_ID, Identifier.parse(PROXIMITY_UUID), null, null);
        mAllBeaconsRegion1 = new Region(UNIQUE_ID, Identifier.parse(PROXIMITY_UUID1), null, null);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.bind(this);
        //beaconManager.setDebug(true);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#253A4B")));

        recyclerView = findViewById(R.id.recyclerView);

        mBeaconData = new ArrayList<BeaconData>();
        myAdapter = new MyAdapter(getApplicationContext(),mBeaconData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(myAdapter);

        startScanButton = findViewById(R.id.startScan);
        stopScanButton = findViewById(R.id.stopScan);
        TextView tv = findViewById(R.id.tv);
        ImageView iv = findViewById(R.id.imageView6);
        zero_result = findViewById(R.id.textView4);
        zero_result.setVisibility(View.INVISIBLE);

        //Alert "Mancata attivazione del Bluetooth"
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Attenzione: Bluetooth OFF");
        builder.setMessage("Attivare il Bluetooth per effettuare la scansione!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bluetoothIntent, BLUETOOTH_REQ_CODE);

                Toast.makeText(getBaseContext(), "BLUETOOTH ON" , Toast.LENGTH_LONG ).show();
            }
        });
        builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Attivazione Bluetooth annullata!" , Toast.LENGTH_LONG ).show();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //Gestione pulsate "INIZIA SCANSIONE"
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Messaggio pulsante!");
                tv.setVisibility(View.INVISIBLE);
                iv.setVisibility(View.INVISIBLE);

                if(BluetoothAdapter.getDefaultAdapter().isEnabled() == true){
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.show();
                    //Creo dialog view per progress dialog
                    dialog.setContentView(R.layout.dialog_view);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    //startScanBeacon();
//                    onBeaconServiceConnect();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                        }
                    },SPLASH_TIME_OUT_DIALOG_PROGRESS);
                    //Popolo Card View
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //startScanBeacon();
                            //mBeaconData = BeaconData.createBeacon();
                            //myAdapter = new MyAdapter(getApplicationContext(), mBeaconData);
                            //recyclerView.setAdapter(myAdapter);
                            //recyclerView.setLayoutManager(llm);

                            stopScanButton.setEnabled(true);
                            startScanButton.setEnabled(false);
                            Toast.makeText(getBaseContext(), "Inizio scansione" , Toast.LENGTH_LONG ).show();
                            startBeacon();
                            beaconManager.setDebug(true);
                        }
                    },SPLASH_TIME_OUT_CARD);
                }else{
                    Toast.makeText(getBaseContext(), "bluetooth non attivato" , Toast.LENGTH_LONG ).show();
                    builder.show();
                }

            }
        });

        //Gestione pulsante "INTERROMPI SCANSIONE"
        stopScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Interrompo scansione" , Toast.LENGTH_LONG ).show();
                startScanButton.setEnabled(true);
                stopScanButton.setEnabled(false);
                onDestroy();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void startBeacon(){
        beaconManager.bind(this);
        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(10000l);
        beaconManager.setBackgroundBetweenScanPeriod(DEFAULT_SCAN_PERIOD_MS);
        beaconManager.setBackgroundScanPeriod(DEFAULT_SCAN_PERIOD_MS);
        beaconManager.setForegroundBetweenScanPeriod(DEFAULT_SCAN_PERIOD_MS);
        beaconManager.setForegroundScanPeriod(DEFAULT_SCAN_PERIOD_MS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {


        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(TAG, "didEnterRegion");
                //sendNotification();
            }

            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG, "didExitRegion");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.d(TAG, "I have just switched from seeing/not seeing beacons: ");
            }
        });

        /*beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for(Beacon oneBeacon : beacons) {
                    Log.d(TAG, "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                }
            }
        });
*/

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, final Region region) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(beacons.size() == 0){
                            Log.d(TAG, "iBeacon not found");
                            zero_result.setVisibility(View.VISIBLE);
                        }else{
                            zero_result.setVisibility(View.INVISIBLE);
                        }
                        for(final Beacon beacon : beacons){
                            Log.d(TAG, "iBeacon found : UUID: " + beacon.getId1() + " Major: "+ beacon.getId2() + " Minor: "+beacon.getId3() + " Distance:" +beacon.getDistance() + " RSSI: " +beacon.getRssi() + " TX Power: " +beacon.getTxPower());
                            mBeaconData = BeaconData.create(beacon.getBluetoothName(), String.valueOf(beacon.getId1()),Integer.parseInt(String.valueOf(beacon.getId2())),Integer.parseInt(String.valueOf(beacon.getId3())),beacon.getRssi(),beacon.getDistance(),beacon.getTxPower());
                            Log.d(TAG, " " + beacons.size());
                            //myAdapter.clear();
                            /*myAdapter = new MyAdapter(getApplicationContext(), mBeaconData);
                            //recyclerView.setAdapter(myAdapter);
                            //recyclerView.setLayoutManager(llm);
                            myAdapter.addAll(mBeaconData);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setLayoutManager(llm);*/

                        }

                    }
                });
                sendNotification(beacons.size());
                myAdapter.addAll(mBeaconData);
                myAdapter = new MyAdapter(getApplicationContext(), mBeaconData);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }
        });


        try {
            //beaconManager.startRangingBeaconsInRegion(mAllBeaconsRegion);
            beaconManager.startRangingBeaconsInRegion(new Region("my unique id", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(int beaconSize){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
        builder.setContentTitle("AppBeacon");
        builder.setContentText(beaconSize + " beacon rilevati!");
        builder.setSmallIcon(R.drawable.icons);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu , menu);

        MenuItem itemSwitch = menu.findItem(R.id.mySwitch);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(MainActivity.this, "Questo dispositivo non supporta il Bluetooth", Toast.LENGTH_LONG).show();
        }

        itemSwitch.setActionView(R.layout.use_switch);
        final Switch sw = (Switch) menu.findItem(R.id.mySwitch).getActionView().findViewById(R.id.action_switch);
        if(BluetoothAdapter.getDefaultAdapter().isEnabled() == true){
            sw.setChecked(true);
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bluetoothIntent, BLUETOOTH_REQ_CODE);
                    Toast.makeText(getBaseContext(), "BLUETOOTH ON" , Toast.LENGTH_LONG ).show();
                }else{
                    bluetoothAdapter.disable();
                    startScanButton.setVisibility(View.VISIBLE);
                    startScanButton.setEnabled(true);
                    stopScanButton.setVisibility(View.INVISIBLE);
                    stopScanButton.setEnabled(false);
                    //onDestroy();
                    Toast.makeText(getBaseContext(), "BLUETOOTH OFF" , Toast.LENGTH_LONG ).show();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.my_device){
            Intent intent = new Intent(MainActivity.this, MyDevice.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.info){
            Toast.makeText(this, "Info", Toast.LENGTH_SHORT).show();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Toast.makeText(MainActivity.this, "Bluetooth is ON", Toast.LENGTH_SHORT).show();
        }else{
            if(resultCode == RESULT_CANCELED){
                Toast.makeText(MainActivity.this, "Bluetooth operation is cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void show(View view){
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dialog.dismiss();
    }

}
