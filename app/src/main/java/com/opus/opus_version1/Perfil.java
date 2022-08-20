package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Perfil extends AppCompatActivity {

    public static int zoomOut = R.anim.zoom_out;
    public static int translateRight = R.anim.translate_right_side;
    SwitchMaterial Usuario, Proovedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Usuario = findViewById(R.id.Usuario);
        Proovedor = findViewById(R.id.Proovedor);

        Usuario.setOnClickListener(v -> {
            int id = v.getId();
            if (id == R.id.Usuario) {
                startActivity(new Intent(Perfil.this, Sign_Up.class));
                finish();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, OnBoarding.class);
        startActivity(intent);
        overridePendingTransition(0, zoomOut);
        finish();
    }
}