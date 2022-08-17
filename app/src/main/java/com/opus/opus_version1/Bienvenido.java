package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Bienvenido extends AppCompatActivity {
    //Objeto Abeja
    ImageView abeja;
    //Variable Nombre
    private TextView nombrebienvenida;
    //Objeto FireBase
    private DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    public static int zoomOut = R.anim.zoom_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);
        setTitle("Bienvenida");
        //Asignacion De Variables Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //Asignacion de ID TextField
        nombrebienvenida = findViewById(R.id.nombrebienvenida);
        //Asignacion de ID Abeja
        abeja = findViewById(R.id.abeja);
        Glide
                .with(Bienvenido.this)
                .load(R.drawable.abeja)
                .centerCrop()
                .into(abeja);
        //Duracion y Siguiente EmptyActivyty
        new Handler().postDelayed(() -> {
            Toast.makeText(this, "Ingresando", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Bienvenido.this, UserActivity.class));
            overridePendingTransition(0, zoomOut);
            finish();
        }, 4000);
        //ObtenciÓn De Datos Y Actualizarlos En La Vista
        getUserInfo();
    }

    //Proceso De Obtener Datos.
    private void getUserInfo() {
        //Obtengo el ID Ingresado
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //Obtengo los datos de ID.
        mDatabaseReference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Si el User Existe.
                if (dataSnapshot.exists()) {
                    //Guardo el Nombre y Apellido En Las Variables.
                    String name = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                    String apellido = Objects.requireNonNull(dataSnapshot.child("apellido").getValue()).toString();
                    //Actualizo las Variables
                    nombrebienvenida.setText(name + " " + apellido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
