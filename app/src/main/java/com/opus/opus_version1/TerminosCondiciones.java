package com.opus.opus_version1;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;


public class TerminosCondiciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones);
        setTitle("Terminos Y Condiciones");

        WebView myWebView = findViewById(R.id.terminosCondiciones);
        myWebView.loadUrl("https://politicasopus.000webhostapp.com/");
    }
}