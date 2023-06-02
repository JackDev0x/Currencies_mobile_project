package com.example.myapplication57;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

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

//    void addCurrency(String code, double mid, String date){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(COLUMN_code, code);
//        cv.put(COLUMN_mid, mid);
//        cv.put(COLUMN_date, date);
//        long result = db.insert(TABLE_NAME, null, cv);
//        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
//        }
//    }

    void addCurrency(String code, double mid, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_code, code);
        cv.put(COLUMN_mid, mid);
        cv.put(COLUMN_date, date);
        long result = db.insert(TABLE_NAME, null, cv);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(result == -1){
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Cursor readData(String query){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteRows(String date){

        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "DELETE FROM " + TABLE_NAME + " WHERE Date = ?";
        db.execSQL(sqlQuery, new String[]{date});
//        SQLiteDatabase db = this.getWritableDatabase();
//        long result = db.delete(TABLE_NAME, "Date=?", new String[]{row_Date});
//        if(result == -1){
//            Toast.makeText(context,"Failed to delete", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(context,"Successfully deleted", Toast.LENGTH_SHORT).show();
//
//        }
    }

    void startPeriodicWork() {
        Constraints constraints = new Constraints.Builder()
                .build();

        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(CurrencyWorker.class, 5, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
//        WorkManager workManager = WorkManager.getInstance(context);
//        workManager.enqueue(periodicWorkRequest);
    }

        private static final String CHANNEL_ID = "my_channel_id";
        private static final String CHANNEL_NAME = "My Channel";
        private static final String CHANNEL_DESCRIPTION = "Channel Description";

        public static void showNotification(Context context, String title, String message) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription(CHANNEL_DESCRIPTION);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_baseline_person_26yo_24)
                    .setAutoCancel(true);

            notificationManager.notify(/* unique notification id */ 1, builder.build());
        }
}
