package com.example.myapplication57;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MyService extends Service {

    private Handler handler;
    private Runnable runnable;
    private static final long INTERVAL = 43200000; // Interwał czasowy (12 godzin)

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        getDataFromApi();
        runnable = new Runnable() {
            @Override
            public void run() {
                // pobieranie z api
                getDataFromApi();

                handler.postDelayed(this, INTERVAL); // Uruchom ponownie zadanie po upływie interwału
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, INTERVAL); // Uruchom zadanie po rozpoczęciu usługi
        return START_STICKY; // Jeśli usługa zostanie zatrzymana, to ponownie ją uruchom
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Zatrzymaj zadanie przy zniszczeniu usługi
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getDataFromApi() {
        System.out.println("tu");
        // Kod do wyświetlania powiadomienia
    }
}