package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActualizarDatos extends AppCompatActivity {
    //Atributos
    TextView nameTextField, apellidoTextField, telefonoTextField, emailTextView;
    MaterialButton btnActualizarDatos;
    //Atributos FireBase.
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    //Atributos De Transicion
    public static int zoomOut = R.anim.zoom_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos);
        setTitle("Actualizar Datos");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Instancio los TextField del ID
        nameTextField = findViewById(R.id.nameTextField);
        apellidoTextField = findViewById(R.id.apellidoTextField);
        telefonoTextField = findViewById(R.id.TelefonoTextField);
        emailTextView = findViewById(R.id.emailTextView);
        //Instancio los Botones del ID
        btnActualizarDatos = findViewById(R.id.ActualizarDatos);
        //Obtengo los datos de ID.
        mDatabaseReference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Si el User Existe.
                if (dataSnapshot.exists()) {
                    //Guardo el Nombre y Apellido En Las Variables.
                    String name = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                    String apellido = Objects.requireNonNull(dataSnapshot.child("apellido").getValue()).toString();
                    String telefono = Objects.requireNonNull(dataSnapshot.child("telefono").getValue()).toString();
                    //Actualizo las Variables
                    nameTextField.setText(name);
                    apellidoTextField.setText(apellido);
                    telefonoTextField.setText(telefono);
                }
                //Objeto FireBase User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    emailTextView.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Instrucciones Al Dar Click
        btnActualizarDatos.setOnClickListener(v -> validate());
    }

    //🡣🡣🡣Validacion De Los Campos🡣🡣🡣
    public void validate() {
        //Recoger Datos Nombre,Apellido y Telefono.
        String name = Objects.requireNonNull(nameTextField.getText()).toString().trim().toLowerCase();
        String apellido = Objects.requireNonNull(apellidoTextField.getText()).toString().trim().toLowerCase();
        String telefono = Objects.requireNonNull(telefonoTextField.getText()).toString().trim();
        //Campo Nombre
        if (name.isEmpty()) {
            nameTextField.setError("Campo Vacio");
            return;
        }
        else {
            nameTextField.setError(null);
        }
        //Campo Apellido
        if (apellido.isEmpty()) {
            apellidoTextField.setError("Campo Vacio");
            return;
        }
        else {
            apellidoTextField.setError(null);
        }
        //Campo Telefono
        if (telefono.isEmpty()) {
            telefonoTextField.setError("Campo Vacio");
            return;
        }
        else {
            telefonoTextField.setError(null);
        }
        update_Data(name, apellido, telefono);
    }

    //🡣🡣🡣Cambio De Datos En FireBase🡣🡣🡣
    public void update_Data(String name, String apellido, String telefono) {
        //Actualiza Los Datos
        Map<String, Object> Data = new HashMap<>();
        Data.put("nombre", name);
        Data.put("apellido", apellido);
        Data.put("telefono", telefono);
        mDatabaseReference.child("Usuarios").child(id).updateChildren(Data).
                addOnSuccessListener(unused -> Toast.makeText(this, "Datos Actualizados", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Hubo Un Error Al Actualizar Los Datos", Toast.LENGTH_SHORT).show());
    }

    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
        overridePendingTransition(0, zoomOut);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ActualizarDatos.this, Home.class));
            overridePendingTransition(0, zoomOut);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}