package com.example.myapplication57;

import android.app.Application;

public class MyApplication57 extends Application {

    private static boolean isServiceRunning = false;

    public static boolean isServiceRunning() {
        return isServiceRunning;
    }

    public static void setServiceRunning(boolean serviceRunning) {
        isServiceRunning = serviceRunning;
    }
    private void initializeWorkManager() {
        // Inicjalizuj WorkManager
        // ...
    }
    @Override
    public void onCreate() {
        super.onCreate();

        if (!isServiceRunning) {
            initializeWorkManager();
            isServiceRunning = true;

            System.out.println("Tworzenie się aplikacji");
            DataBaseHelper dbHelper = new DataBaseHelper(this);
            dbHelper.startPeriodicWork();
        }
    }
}
