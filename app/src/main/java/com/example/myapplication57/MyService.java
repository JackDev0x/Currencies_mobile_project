//package com.example.myapplication57;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class MyService extends Service {
//
//    private static final int NOTIFICATION_ID = 1;
//    private static final String CHANNEL_ID = "ForegroundServiceChannel";
//    private MyServiceListener serviceListener;
//
//    private Handler handler;
//    private Runnable runnable;
//    private DataBaseHelper dbHelper;
//
//    public void setServiceListener(MyServiceListener listener) {
//        this.serviceListener = listener;
//    }
//
//    public void removeServiceListener() {
//        this.serviceListener = null;
//    }
//
//    private static final String TAG = "MyService";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        handler = new Handler();
//        dbHelper = new DataBaseHelper(this);
//
//        // Definiowanie kodu, który ma być wykonywany cyklicznie co 5 minut
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                // Tutaj możesz umieścić dowolny kod, który ma być wykonywany co 5 minut
//                // Przykład zapisywania danych do bazy danych:
//                String code = "ABC"; // Przykładowy kod waluty
//                double mid = 1.2345; // Przykładowa wartość średnia waluty
//                String date = "2023-06-02"; // Przykładowa data
//
//                dbHelper.addCurrency(code, mid, date);
//
//                handler.postDelayed(this, 5 * 60 * 1000); // Wywołanie runnable po 5 minutach
//            }
//        };
//        Log.d(TAG, "Usługa została utworzona.");
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "Usługa została uruchomiona w tle.");
//
//        //MyService myService = new MyService();
//        //MainActivity mainActivity = new MainActivity();
//        //myService.setServiceListener(mainActivity);
//
//        OkHttpClient client = new OkHttpClient();
//
//        String urlX = "http://api.nbp.pl/api/exchangerates/tables/a/today/";
//
//        Request request = new Request.Builder()
//                .url(urlX)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if(response.isSuccessful()){
//                    String NBP_Response = response.body().string();
//
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    JsonNode rootNode = objectMapper.readTree(NBP_Response);
//
//                    JsonNode ratesNode = rootNode.get(0).get("rates");
//                    if (ratesNode.isArray()) {
//                        for (JsonNode rateNode : ratesNode) {
//                            String currency = rateNode.get("currency").asText();
//                            String code = rateNode.get("code").asText();
//                            double mid = rateNode.get("mid").asDouble();
//                            String date = rootNode.get(0).get("effectiveDate").asText();
//                            String Strmid = "" +mid;
//
//                            // Dodaj kod do zapisu danych do bazy danych lub innej operacji
//
//                            // Przykład wypisania danych
//                            System.out.println("Currency: " + currency);
//                            System.out.println("Code: " + code);
//                            System.out.println("Mid: " + mid);
//                            System.out.println("Date: " + date);
//
//
//                            if (serviceListener != null) {
//                                serviceListener.onDataReceived(currency, code, mid, date);
//                            }
//
//                            sendPushNotificationUpload(getApplicationContext(), "Dane pobrane", "Pobrano dane z API");
//
//
////                            MainActivity.this.runOnUiThread(new Runnable() {
////                                @Override
////                                public void run() {
////                                    DataBaseHelper CurrDb = new DataBaseHelper(MainActivity.this);
////                                    CurrDb.addCurrency(code.trim(), Double.valueOf(Strmid.trim()), date.trim());
////                                }
////                            });
//
//
//                        }
//                    }
//                }
//            }
//        });
//
//        createNotificationChannel();
//        Intent notificationIntent = null;
//        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Foreground Service")
//                .setContentText("Running in the background")
//                .setSmallIcon(R.drawable.ic_baseline_person_26yo_24)
//                //.setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(NOTIFICATION_ID, notification);
//        return START_STICKY;
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Foreground Service Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "Usługa została zatrzymana.");
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // Ta usługa nie obsługuje komunikacji z innymi komponentami, więc zwracamy null.
//        return null;
//    }
//
//    public void sendPushNotification(Context context, String title, String message) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String channelId = "channel_id"; // Unikalny identyfikator kanału powiadomień
//        String channelName = "channel_name"; // Nazwa kanału powiadomień
//        int importance = NotificationManager.IMPORTANCE_HIGH; // Priorytet powiadomienia
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
//                .setSmallIcon(R.drawable.ic_baseline_person_26yo_24) // Ikona powiadomienia (może być dowolny obrazek zasobów)
//                .setContentTitle(title) // Tytuł powiadomienia
//                .setContentText(message) // Treść powiadomienia
//                .setPriority(NotificationCompat.PRIORITY_HIGH) // Priorytet powiadomienia
//                .setAutoCancel(true); // Automatyczne zamykanie powiadomienia po kliknięciu
//
//        notificationManager.notify(0, builder.build()); // Wyświetlanie powiadomienia
//    }
//
//    public void sendPushNotificationUpload(Context context, String title, String message) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String channelId = "channel_id"; // Unikalny identyfikator kanału powiadomień
//        String channelName = "channel_name"; // Nazwa kanału powiadomień
//        int importance = NotificationManager.IMPORTANCE_HIGH; // Priorytet powiadomienia
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
//                .setSmallIcon(R.drawable.ic_baseline_person_26yo_24) // Ikona powiadomienia (może być dowolny obrazek zasobów)
//                .setContentTitle(title) // Tytuł powiadomienia
//                .setContentText(message) // Treść powiadomienia
//                .setPriority(NotificationCompat.PRIORITY_HIGH) // Priorytet powiadomienia
//                .setAutoCancel(true); // Automatyczne zamykanie powiadomienia po kliknięciu
//
//        notificationManager.notify(0, builder.build()); // Wyświetlanie powiadomienia
//    }
//}