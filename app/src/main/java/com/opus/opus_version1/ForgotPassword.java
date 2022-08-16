package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {
    //Objetos.
    MaterialButton recuperarBoton;
    TextInputEditText emailEditText;
    TextView nuevoUsuario;
    //Objeto Transicion
    public static int translateRight = R.anim.translate_right_side;
    public static int translateLeft = R.anim.translate_left_side;
    public static int zoomOut = R.anim.zoom_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Olvide Mi Contraseña");
        //Instancio los Botones del ID
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        recuperarBoton = findViewById(R.id.recuperarBoton);
        //Instancio los TextField del ID
        emailEditText = findViewById(R.id.emailEditText);
        //Al Dar Click Procesos
        nuevoUsuario.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPassword.this, Sign_Up.class));
            overridePendingTransition(0,translateRight);
            finish();
        });
        recuperarBoton.setOnClickListener(v -> validate());
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

    //Dar Click Hacia Atras Ir Al Login
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPassword.this, Login.class));
        overridePendingTransition(0,zoomOut);
        finish();
    }
}