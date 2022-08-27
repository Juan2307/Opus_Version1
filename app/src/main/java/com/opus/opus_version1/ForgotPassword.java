package com.opus.opus_version1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.opus.opus_version1.Internet.NetworkChangeListener;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ForgotPassword extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    //Variable
    MaterialButton recuperarBoton;
    TextInputEditText emailEditText;
    TextView nuevoUsuario;
    //Variable dialog.
    AlertDialog mdialog;
    //Variable Transicion
    public static int translateRight = R.anim.translate_right_side;
    public static int zoomOut = R.anim.zoom_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Olvide Mi Contraseña");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Instancio el Objeto de Dialog
        mdialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere Un Momento")
                .setCancelable(false)
                .build();
        //Instancio el Objeto de Dialog
        mdialog = new SpotsDialog.Builder().setContext(this).setMessage("Espere Un Momento").setCancelable(false).build();
        //Instancio ID
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        //Instancio los Botones del ID
        recuperarBoton = findViewById(R.id.recuperarBoton);
        //Instancio los TextField del ID
        emailEditText = findViewById(R.id.emailEditText);
        //Conexion
        ConnectivityManager con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        //Al Dar Click Procesos
        nuevoUsuario.setOnClickListener(v -> {
            if (networkInfo != null && networkInfo.isConnected()) {
                startActivity(new Intent(ForgotPassword.this, SplashScreen.class));
                overridePendingTransition(0, translateRight);
                finish();
            } else
                Toast.makeText(ForgotPassword.this, " No Hay Conexion a Internet.", Toast.LENGTH_LONG).show();
        });
        recuperarBoton.setOnClickListener(v -> {
            if (networkInfo != null && networkInfo.isConnected()) {
                validate();
            } else
                Toast.makeText(ForgotPassword.this, " No Hay Conexion a Internet.", Toast.LENGTH_LONG).show();
        });
    }

    //Validacion De Datos
    public void validate() {
        //Obtengo los datos de E-mail.
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError("Campos Vacios");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo Invalido");
        } else {
            sendEmail(email);
        }
    }

    //Proceso Enviar Correo
    public void sendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(ForgotPassword.this, "¡Correo Enviado!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ForgotPassword.this, "¡Correo No Registra En La Base De Datos!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Flecha Atras
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ForgotPassword.this, Login.class));
            overridePendingTransition(0, zoomOut);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    //Dar Click Hacia Atras
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPassword.this, Login.class));
        overridePendingTransition(0, zoomOut);
        finish();
    }
}