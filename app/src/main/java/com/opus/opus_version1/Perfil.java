package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.opus.opus_version1.Internet.Internet;

import java.util.Objects;

public class Perfil extends AppCompatActivity {

    //Atributo Abeja
    ImageView gifBee;
    //Atributos
    SwitchMaterial CheckUser, Checkprovider;
    //Atributos De Transicion
    public static int zoomOut = R.anim.zoom_out;
    public static int translateRight = R.anim.translate_right_side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle("Perfiles");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Asignacion de ID Abeja
        gifBee = findViewById(R.id.abeja);
        Glide
                .with(Perfil.this)
                .load(R.drawable.bee_floating)
                .centerCrop()
                .into(gifBee);

        //Instancio los TextField del ID
        CheckUser = findViewById(R.id.CheckUsuario);
        Checkprovider = findViewById(R.id.CheckProovedor);
        //Instrucciones Al Dar Click
        CheckUser.setOnClickListener(v -> {
            if (Internet.isOnline(this)) {
                if (CheckUser.isChecked()) {
                    startActivity(new Intent(Perfil.this, Sign_Up.class));
                    overridePendingTransition(0, translateRight);
                    finish();
                }
            } else {
                Toast.makeText(Perfil.this, "¡Sin Acceso A Internet, Verifique Su Conexión.!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Flecha Atras
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            startActivity(new Intent(Perfil.this, OnBoarding.class));
            overridePendingTransition(0, zoomOut);
            finish();
        return super.onOptionsItemSelected(item);
    }

    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    public void onBackPressed() {
        Intent intent = new Intent(this, OnBoarding.class);
        startActivity(intent);
        overridePendingTransition(0, zoomOut);
        finish();
    }
}