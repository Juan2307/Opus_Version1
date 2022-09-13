package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import java.util.Objects;

public class AcercaDe extends AppCompatActivity {

    //Atributos De Transicion
    public static int translateUp = R.anim.slide_out_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        setTitle("Acerca De Opus");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        WebView myWebView = findViewById(R.id.acercaDeOpus);
        myWebView.loadUrl("https://caesural-run.000webhostapp.com/");
    }

    //Flecha Atras
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(AcercaDe.this, Home.class));
        overridePendingTransition(0, translateUp);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AcercaDe.this, Home.class));
        overridePendingTransition(0, translateUp);
        finish();
        super.onBackPressed();
    }
}