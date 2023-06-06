package com.example.myapplication57;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.content.Intent;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


import java.io.IOException;


import okhttp3.Call;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{

private RecyclerView recy;


    @Override
    public void onItemClick(String item) {
        // Tutaj umieść kod obsługujący kliknięcie, np. uruchomienie nowej aktywności
        //String selectedItem = items[position];
        Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
        intent.putExtra("selectedItem", item);
        startActivity(intent);
    }

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<String> List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




        List = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

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
                            String Strmid = "" +mid;

                            // Dodaj kod do zapisu danych do bazy danych lub innej operacji

                            // Przykład wypisania danych
                            System.out.println("Currency: " + currency);
                            System.out.println("Code: " + code);
                            System.out.println("Mid: " + mid);
                            System.out.println("Date: " + date);


                            MainActivity2.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (List) {
                                        List.add(code + "                  " + mid);

                                    }
                                    synchronized (recyclerAdapter) {
                                        recyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                            });


                        }
                    }
                }
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(List);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerAdapter = new RecyclerAdapter(List);
        recyclerAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(recyclerAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Values today");

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(List, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

    };

//    private void updateDataInDatabase() {
//        // Przykładowy kod dla bazy danych SQLite
//        SQLiteDatabase database = dbHelper.getWritableDatabase();
//
//        // Pobierz ID rekordu przycisku, który był przenoszony
//        int movedButtonId = // Pobierz ID przycisku z moviesList lub viewHolder
//
//                // Aktualizuj rekord w bazie danych zgodnie z nowym indeksem
//                ContentValues values = new ContentValues();
//        values.put("button_order", moviesList.indexOf(movedButtonId)); // Aktualizuj kolejność przycisku
//        String whereClause = "button_id = ?";
//        String[] whereArgs = {String.valueOf(movedButtonId)};
//        database.update("buttons_table", values, whereClause, whereArgs);
//
//        database.close();
//    }


}