package com.example.appbeacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "beacon.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_beacon";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_UUID = "uuid";
    private static final String COLUMN_NAME = "name_beacon";
    private static final String COLUMN_MAJOR = "major";
    private static final String COLUMN_MINOR = "minor";
    private static final String COLUMN_TXPOWER = "txpower";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_NAME + " TEXT, " +
                            COLUMN_UUID + " TEXT, " +
                            COLUMN_MAJOR + " INTEGER, " +
                            COLUMN_MINOR + " INTEGER, " +
                            COLUMN_TXPOWER + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void addBeacon(String name, String uuid, int major, int minor, int txpower ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_UUID, uuid);
        cv.put(COLUMN_MAJOR, major);
        cv.put(COLUMN_MINOR, minor);
        cv.put(COLUMN_TXPOWER, txpower);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1 ){
            Toast.makeText(context,"Inserimento non effettuato", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Inserimento effettuato", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readDataByDb(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String id_row, String name, String uuid, String major, String minor, String txpower){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_UUID,uuid);
        cv.put(COLUMN_MAJOR,major);
        cv.put(COLUMN_MINOR,minor);
        cv.put(COLUMN_TXPOWER,txpower);
        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{id_row});
        if(result == -1){
            Toast.makeText(context, "Aggiornamento non effettuato", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Aggiornamento effettuato", Toast.LENGTH_SHORT).show();
        }
    }

    void delete_row(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Cancellazione non effettuata", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Cancellazione effettuata", Toast.LENGTH_SHORT).show();
        }
    }

}
