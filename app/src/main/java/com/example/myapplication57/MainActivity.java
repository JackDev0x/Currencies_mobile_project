package com.example.myapplication57;

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

import com.example.myapplication57.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


//    private MyService myService;

    @Override
    protected void onStart() {
        super.onStart();

        //Intent serviceIntent = new Intent(this, MyService.class);
        //startService(serviceIntent);


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//    @Override
//    public void onDataReceived(String currency, String code, double mid, String date) {
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {

//                DataBaseHelper CurrDb = new DataBaseHelper(MainActivity.this);
//                CurrDb.addCurrency(code.trim(), mid, date.trim());
//            }
//        });
//    }
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Button btnHello = findViewById(R.id.btnHello);
//        btnHello.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//
//            }
//        });



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_person)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        Intent serviceIntent = new Intent(this, MyService.class);
//        startService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


//    @Override
//    protected void onPostExecute(String result) {
//        TextView textView = findViewById(R.id.text_gallery);
//        textView.setText(result);
//    }


    public void ListaDzisiajButton(View view) throws IOException {

        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

    public void buttonDel(View view) {

        String date = ((EditText) findViewById(R.id.editTextCurrencyDate)).getText().toString();

        DataBaseHelper myDb = new DataBaseHelper(MainActivity.this);
        myDb.deleteRows(date);
    }

    public void SciagnijPojedynczaWalute(View view) throws IOException {

//        for(int j= 10; j<29; j++) {
//            EditText code = findViewById(R.id.editTextCurrencyCode);
//            TextView textView = findViewById(R.id.textViewOutput);
//
//            OkHttpClient client = new OkHttpClient();
//
//            String urlX = "http://api.nbp.pl/api/exchangerates/tables/a/2023-05-"+j;
//
//            Request request = new Request.Builder()
//                    .url(urlX)
//                    .build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String NBP_Response = response.body().string();
//
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        JsonNode rootNode = objectMapper.readTree(NBP_Response);
//
//                        JsonNode ratesNode = rootNode.get(0).get("rates");
//                        if (ratesNode.isArray()) {
//                            for (JsonNode rateNode : ratesNode) {
//                                String currency = rateNode.get("currency").asText();
//                                String code = rateNode.get("code").asText();
//                                double mid = rateNode.get("mid").asDouble();
//                                String date = rootNode.get(0).get("effectiveDate").asText();
//                                String Strmid = "" + mid;
//

//                                System.out.println("Currency: " + currency);
//                                System.out.println("Code: " + code);
//                                System.out.println("Mid: " + mid);
//                                System.out.println("Date: " + date);
//
//                                MainActivity.this.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        DataBaseHelper CurrDb = new DataBaseHelper(MainActivity.this);
//                                        CurrDb.addCurrency(code.trim(), Double.valueOf(Strmid.trim()), date.trim());
//                                    }
//                                });
//
//
//                            }
//                        }
//                    }
//                }
//            });
//        }
        EditText code = findViewById(R.id.editTextCurrencyCode);
        TextView textView = findViewById(R.id.textViewOutput);



        OkHttpClient client = new OkHttpClient();

        String urlX = "http://api.nbp.pl/api/exchangerates/rates/a/"+code.getText().toString()+"/";

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

                    Gson gson = new Gson();
                    Root root = gson.fromJson(NBP_Response, Root.class);

                    double mid = root.rates.get(0).mid;
                    String midStr = code.getText().toString() +": " +mid;

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(midStr);
                        }
                    });
                }
            }
        });
    }


//        EditText code = findViewById(R.id.editTextCurrencyCode);
//        TextView textView = findViewById(R.id.textViewOutput);
//
//
//
//
//        OkHttpClient client = new OkHttpClient();
//
//        String urlX = "http://api.nbp.pl/api/exchangerates/tables/a/2023-03-14";
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
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    DataBaseHelper CurrDb = new DataBaseHelper(MainActivity.this);
//                                    CurrDb.addCurrency(code.trim(), Double.valueOf(Strmid.trim()), date.trim());
//                                }
//                            });
//
//
//                        }
//                    }
//                }
//            }
//        });


//        String result = "";
//        try {
//            URL url = new URL("");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            InputStream inputStream = conn.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                result += line;
//            }
//            bufferedReader.close();
//            inputStream.close();
//        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
//        }

}

