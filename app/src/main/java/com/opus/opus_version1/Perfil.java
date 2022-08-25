package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
public class Perfil extends AppCompatActivity {
    //Atributos
    LinearLayout error;
    ImageView imgSinInternet;
    TextView txtSinInternet;
    SwitchMaterial Usuario, Proovedor;
    //Atributos De Transicion
    public static int zoomOut = R.anim.zoom_out;
    public static int translateRight = R.anim.translate_right_side;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle("Perfiles");

        //Instancio ID
        error = findViewById(R.id.error);
        imgSinInternet = findViewById(R.id.imgSinInternet);
        txtSinInternet = findViewById(R.id.txtSinInternet);
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
            //Ocultamos
            error.setVisibility(View.INVISIBLE);
            imgSinInternet.setVisibility(View.VISIBLE);
            txtSinInternet.setVisibility(View.VISIBLE);
            txtSinInternet.setText("No Hay Conexion a Internet \n" +
                    "Prueba Estos Pasos Para Volver A Conectarte: \n" +
                    "✅Comprueba el Modem Y El Router \n" +
                    "✅Vuelve A Conectarte A Wifi O Datos \n");
            Toast.makeText(this, "No Se Pudo Conectar \n" + " Verifique El Acceso A Internet e Intente Nuevamente.", Toast.LENGTH_LONG).show();
        }
    }
    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    public void onBackPressed() {
        Intent intent = new Intent(this, OnBoarding.class);
        startActivity(intent);
        overridePendingTransition(0, zoomOut);
        finish();
    }
}