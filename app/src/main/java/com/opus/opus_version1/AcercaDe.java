package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class AcercaDe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        setTitle("Acerca De Opus");
        WebView myWebView = (WebView) findViewById(R.id.acercaDeOpus);
        myWebView.loadUrl("https://caesural-run.000webhostapp.com/");
    }
}