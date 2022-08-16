package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class Sign_Up extends AppCompatActivity {
    //Objetos
    TextView nuevoUsuario, bienvenidoLabel, continuarLabel;
    TextInputLayout usuarioSingUpTextField, contrasenaTextField;
    ImageView signUpImageView;
    MaterialButton inicioSesion;
    TextInputEditText documentoTextField, nameTextField, lastnameTextField, telefonoTextField, emailEditText, passwordEditText, confirmPasswordEditText;
    CheckBox terminoCondiciones;
    //Objetos FireBase.
    FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("REGISTER");
        //Instancio las Transiciones del ID
        signUpImageView = findViewById(R.id.signUpImageView);
        bienvenidoLabel = findViewById(R.id.bienvenidoLabel);
        continuarLabel = findViewById(R.id.continuarLabel);
        usuarioSingUpTextField = findViewById(R.id.usuarioSingUpTextField);
        contrasenaTextField = findViewById(R.id.contrasenaTextField);
        //Instancio los Botones del ID
        inicioSesion = findViewById(R.id.inicioSesion);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        //Instancio los TextField del ID
        documentoTextField = findViewById(R.id.documentoTextField);
        nameTextField = findViewById(R.id.nameTextField);
        lastnameTextField = findViewById(R.id.lastnameTextField);
        telefonoTextField = findViewById(R.id.telefonoTextField);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        //Instancio el Objeto de Firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Usuarios");
        mAuth = FirebaseAuth.getInstance();
        //Instancio ID Del CheckBox
        terminoCondiciones = findViewById(R.id.idChec1);
        //Al Dar Click Proceso
        inicioSesion.setOnClickListener(v -> validate());
        nuevoUsuario.setOnClickListener(v -> onBackPressed());
        terminoCondiciones.setOnClickListener(v -> {
            int id = v.getId();
            if (id == R.id.idChec1) {
                Uri uri = Uri.parse("https://politicasopus.000webhostapp.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void validate() {
        //Recoger Datos
        String documento = Objects.requireNonNull(Objects.requireNonNull(documentoTextField.getText()).toString().trim());
        String nombre = Objects.requireNonNull(Objects.requireNonNull(nameTextField.getText()).toString().trim().toLowerCase());
        String apellido = Objects.requireNonNull(Objects.requireNonNull(lastnameTextField.getText()).toString().trim().toLowerCase());
        String telefono = Objects.requireNonNull(telefonoTextField.getText()).toString().trim();
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim().toLowerCase();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString().trim();
        //Campo documento
        if (documento.isEmpty()) {
            documentoTextField.setError("¡Campo Vacio!");
            return;
        } else if (documento.length() < 7) {
            documentoTextField.setError("Se Necesitan Mas De 7 Caracteres");
            return;
        } else {
            documentoTextField.setError(null);
        }
        //Campo Nombre
        if (nombre.isEmpty()) {
            nameTextField.setError("¡Campo Vacio!");
            return;
        } else {
            nameTextField.setError(null);
        }
        //Campo Apellido
        if (apellido.isEmpty()) {
            lastnameTextField.setError("¡Campo Vacio!");
            return;
        } else {
            lastnameTextField.setError(null);
        }
        //Campo Telefono
        if (telefono.isEmpty()) {
            telefonoTextField.setError("¡Campo Vacio!");
            return;
        } else if (telefono.length() < 10) {
            telefonoTextField.setError("Se Necesitan Mas De 10 numeros");
            return;
        } else {
            telefonoTextField.setError(null);
        }
        //Campo Email
        if (email.isEmpty()) {
            emailEditText.setError("¡Campo Vacio!");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("¡Correo Invalido!");
            return;
        } else {
            emailEditText.setError(null);
        }
        //Campo Password
        if (password.isEmpty()) {
            passwordEditText.setError("¡Campo Vacio!");
            return;
        } else if (password.length() < 8) {
            passwordEditText.setError("Se Necesitan Mas De 8 Caracteres");
            return;
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            passwordEditText.setError("Al Menos Un Numero");
            return;
        } else {
            passwordEditText.setError(null);
        }
        //Campo ConfirmPassword
        if (confirmPassword.isEmpty()) {
            passwordEditText.setError("¡Campo Vacio!");
            return;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Deben Ser Iguales");
            return;
        } else {
            confirmPasswordEditText.setError(null);
        }
        if (terminoCondiciones.isChecked()) {
            //Ir Al Metodo Registrar
            registrar(documento, nombre, apellido, telefono, email, password);
        } else {
            Toast.makeText(Sign_Up.this, "Aceptar Terminos Y Condiciones", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrar(String documento, String nombre, String apellido, String telefono, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Destinos destinos = new Destinos(documento, nombre, apellido, telefono, email);
                myRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(destinos); //getUid.CHIL documento
                Intent intent = new Intent(Sign_Up.this, Bienvenido.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(Sign_Up.this, "Cuenta Ya Vinculada", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sign_Up.this, Login.class);
        startActivity(intent);
        finish();
    }
}