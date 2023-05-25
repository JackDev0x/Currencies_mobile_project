package com.example.myapplication57;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.time.LocalDate;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "CurrencyLibrary.db";
    private static final int DatabaseVersion = 1;
    private static final String TABLE_NAME = "currencies_history";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_code = "Currency_code";
    private static final String COLUMN_mid = "column_mid";
    private static final String COLUMN_date = "Date";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_code + " TEXT, " +
                COLUMN_mid + " DOUBLE, " +
                COLUMN_date + " TEXT);";
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addCurrency(String code, double mid, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_code, code);
        cv.put(COLUMN_mid, mid);
        cv.put(COLUMN_date, date);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
