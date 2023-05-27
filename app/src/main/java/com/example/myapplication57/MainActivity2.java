package com.example.myapplication57;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
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

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication57.Models.Root;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;

import java.io.IOException;
import java.net.URL;


import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{



    @Override
    public void onItemClick(String item) {
        // Tutaj umieść kod obsługujący kliknięcie, np. uruchomienie nowej aktywności
        Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
        intent.putExtra("selectedItem", item);
        startActivity(intent);
    }

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<String> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




        moviesList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        OkHttpClient client = new OkHttpClient();

        String urlX = "http://api.nbp.pl/api/exchangerates/tables/a/2023-05-25";

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
                                    synchronized (moviesList) {
                                        moviesList.add(code + "                  " + mid);

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
        recyclerAdapter = new RecyclerAdapter(moviesList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerAdapter = new RecyclerAdapter(moviesList);
        recyclerAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(recyclerAdapter);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(moviesList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

    };


}