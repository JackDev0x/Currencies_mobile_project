package com.example.myapplication57;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrencyWorker extends Worker {

    private DataBaseHelper dbHelper;
    private boolean dataAdding;

    public CurrencyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        dbHelper = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Code to be executed periodically in the background
        OkHttpClient client = new OkHttpClient();

        String urlX = "http://api.nbp.pl/api/exchangerates/tables/a/today";

        Request request = new Request.Builder()
                .url(urlX)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String NBP_Response = response.body().string();

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(NBP_Response);

                    JsonNode ratesNode = rootNode.get(0).get("rates");
                    if (ratesNode.isArray()) {
                        for (JsonNode rateNode : ratesNode) {
                            String currency = rateNode.get("currency").asText();
                            String code = rateNode.get("code").asText();
                            double mid = rateNode.get("mid").asDouble();
                            String date = rootNode.get(0).get("effectiveDate").asText();
                            String Strmid = "" + mid;

                            // Dodaj kod do zapisu danych do bazy danych lub innej operacji

                            // Przykład wypisania danych
                            System.out.println("Currency: " + currency);
                            System.out.println("Code: " + code);
                            System.out.println("Mid: " + mid);
                            System.out.println("Date: " + date);

                            //Cursor cur = dbHelper.readData("SELECT max(Date) from currencies_history where _id = (SELECT max(_id) FROM currencies_history)");
                            Cursor cur = dbHelper.readData("SELECT max(Date) from currencies_history where Currency_code == '"+code+"'");

                            cur.moveToFirst();
                            System.out.println("cur.get(i): " + cur.getString(0));
                            //System.out.println(cur.getString(1));
                            String dbDate = cur.getString(0);
                            if(date.toString().equals(dbDate.toString())){
                                dataAdding = false;
                                cur.close();
                            }
                            else{

                                dbHelper.addCurrency(code, mid, date);
                                dataAdding = true;
                                dbHelper.showNotification(getApplicationContext(), "Nowe dane", "Dodano dzisiejsze wartosci do bazy danych");
                                cur.close();

                            }

                        }
                    }
                }
            }
        });

        System.out.println("Wykonywanie CurrencyWorker -------------- tu");
        if(dataAdding == true) {
            dbHelper.showNotification(getApplicationContext(), "Nowe dane", "Dodano dzisiejsze wartosci do bazy danych");
        }



        return Result.success();
    }
}
