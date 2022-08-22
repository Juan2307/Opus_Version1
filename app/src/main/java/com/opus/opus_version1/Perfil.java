package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class Perfil extends AppCompatActivity {
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
        //Instrucciones Al Dar Click
        Usuario.setOnClickListener(v -> {
            int id = v.getId();
            if (id == R.id.Usuario) {
                startActivity(new Intent(Perfil.this, Sign_Up.class));
                overridePendingTransition(0, translateRight);
                finish();
            }
        });
    }
    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    public void onBackPressed() {
        Intent intent = new Intent(this, OnBoarding.class);
        startActivity(intent);
        overridePendingTransition(0, zoomOut);
        finish();
    }
}