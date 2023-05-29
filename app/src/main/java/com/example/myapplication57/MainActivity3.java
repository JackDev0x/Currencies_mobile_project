package com.example.myapplication57;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import com.example.myapplication57.databinding.ActivityMain3Binding;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PrintManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;



public class MainActivity3 extends AppCompatActivity {

    private ListView lstHistory;
    private Button printButton;
    private String[] itemss = new String[1];
    public static final String SELECTED_ITEM = "selectedItem";
    private AppBarConfiguration appBarConfiguration;
    public String pdfName;

    private ActivityMain3Binding binding;

    private ActivityResultLauncher<Intent> createDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Lista_scroll ls = (Lista_scroll) getSupportFragmentManager().findFragmentById(R.id.lst_frag_history);
        String selectedIt = (String) getIntent().getExtras().get(SELECTED_ITEM);

        Intent intent = getIntent();
        if (intent != null) {
            String selectedItem = intent.getStringExtra("selectedItem");

            DataBaseHelper hisDB;
            ArrayList<String> id, code, mid, date;
            hisDB = new DataBaseHelper(MainActivity3.this);
            id = new ArrayList<>();
            code = new ArrayList<>();
            mid = new ArrayList<>();
            date = new ArrayList<>();

            Cursor cur = hisDB.readData("SELECT * FROM currencies_history WHERE Currency_code='" + selectedItem.substring(0, 3) + "' ORDER BY DATE DESC");

            pdfName = selectedItem.substring(0, 3);
            if (cur.getCount() == 0) {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            } else {
                cur.moveToFirst();
                while (cur.moveToNext()) {
                    id.add(cur.getString(0));
                    code.add(cur.getString(1));
                    mid.add(cur.getString(2));
                    date.add(cur.getString(3));
                }
            }

            itemss = new String[id.size()];
            for (int i = 0; i < id.size(); i++) {
                itemss[i] = date.get(i) + "                                       " + mid.get(i);
            }

            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setTitle(code.get(0));

            lstHistory = findViewById(R.id.lst_frag_history);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemss);
            lstHistory.setAdapter(adapter);
        }

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main3);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Poproś o uprawnienia do zapisu na zewnętrznym składzie
                    ActivityCompat.requestPermissions(MainActivity3.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    createPdf(pdfName);
                }
            }
        });


        createDocumentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");
                                FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());

                                PdfDocument document = createPdfDocument();
                                document.writeTo(fileOutputStream);
                                document.close();

                                Toast.makeText(MainActivity3.this, "PDF saved successfully", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity3.this, "Error while saving PDF", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private PdfDocument createPdfDocument() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(lstHistory.getWidth(), lstHistory.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawColor(Color.BLACK);
        lstHistory.draw(canvas);
        document.finishPage(page);
        return document;
    }

    private void createPdf(String namePdf) {
        Intent createDocumentIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        createDocumentIntent.addCategory(Intent.CATEGORY_OPENABLE);
        createDocumentIntent.setType("application/pdf");
        createDocumentIntent.putExtra(Intent.EXTRA_TITLE, namePdf);
        createDocumentLauncher.launch(createDocumentIntent);
    }
}
