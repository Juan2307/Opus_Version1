package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.opus.opus_version1.Internet.NetworkChangeListener;

public class Perfil extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    //Atributos
    SwitchMaterial Usuario, Proovedor;
    //Atributos De Transicion
    public static int zoomOut = R.anim.zoom_out;
    public static int translateRight = R.anim.translate_right_side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle("Perfiles");

        //Instancio los TextField del ID
        Usuario = findViewById(R.id.Usuario);
        Proovedor = findViewById(R.id.Proovedor);
        //Conexion
        ConnectivityManager con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Instrucciones Al Dar Click
            Usuario.setOnClickListener(v -> {
                int id = v.getId();
                if (id == R.id.Usuario) {
                    startActivity(new Intent(Perfil.this, Sign_Up.class));
                    overridePendingTransition(0, translateRight);
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "No Se Pudo Conectar \n" + " Verifique El Acceso A Internet e Intente Nuevamente.", Toast.LENGTH_LONG).show();
        }
    }

    //Internet
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    public void onBackPressed() {
        Intent intent = new Intent(this, OnBoarding.class);
        startActivity(intent);
        overridePendingTransition(0, zoomOut);
        finish();
    }
}