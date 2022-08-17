package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {
    //Objetos
    TextView bienvenidoLabel, continuarLabel, nuevoUsuario, olvidasteContrasena;
    ImageView loginImageView;
    TextInputLayout usuarioTextField, contrasenaTextField;
    MaterialButton inicioSesion;
    TextInputEditText emailEditText, passwordEditText;
    //Objeto dialog.
    AlertDialog mdialog;
    //Objeto FireBase.
    private FirebaseAuth mAuth;
    //Objeto Google.
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 0;
    //Objeto Transicion
    public static int zoomOut = R.anim.zoom_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        //Instancio las Transiciones del ID
        loginImageView = findViewById(R.id.loginImageView);
        bienvenidoLabel = findViewById(R.id.bienvenidoLabel);
        continuarLabel = findViewById(R.id.continuarlabel);
        usuarioTextField = findViewById(R.id.usuarioTextField);
        contrasenaTextField = findViewById(R.id.contrasenaTextField);
        //Instancio los Botones del ID
        inicioSesion = findViewById(R.id.inicioSesion);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        signInButton = findViewById(R.id.loginGoogle);
        //Instancio los TextField del ID
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        olvidasteContrasena = findViewById(R.id.olvidasteContra);
        //Instancio el Objeto de Firebase
        mAuth = FirebaseAuth.getInstance();
        //Instancio el Objeto de Dialog
        mdialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere Un Momento")
                .setCancelable(false)
                .build();
        //Instrucciones Al Dar Click
        olvidasteContrasena.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPassword.class));
            overridePendingTransition(0,zoomOut);
            finish();
        });
        inicioSesion.setOnClickListener(v -> validate());
        signInButton.setOnClickListener(v -> signInWithGoogle());
        nuevoUsuario.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Sign_Up.class));
            overridePendingTransition(0,zoomOut);
            finish();
        });

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //Dialog Of Google
    //METODO ANTIGUO
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(Login.this, UserActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Fallo En Iniciar Sesión", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(Login.this, "Fallo Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //🡣🡣🡣Validacion De Los Campos🡣🡣🡣
    public void validate() {
        //Recoger Datos Email y Password
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim().toLowerCase();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim().toLowerCase();
        //Campo Email
        if (email.isEmpty()) {
            emailEditText.setError("Campo Vacio");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo Invalido");
            return;
        } else {
            emailEditText.setError(null);
        }
        //Campo Contraseña
        if (password.isEmpty()) {
            passwordEditText.setError("Campo Vacio");
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
        iniciarSesion(email, password);

    }

    //🡣🡣🡣Validar Si La Cuenta Existe🡣🡣🡣
    public void iniciarSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mdialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            startActivity(new Intent(Login.this, Bienvenido.class));
                            overridePendingTransition(0,zoomOut);
                            finish();
                            mdialog.dismiss();
                            passwordEditText.setText("");
                            emailEditText.setText("");
                        }, 3000);
                    } else {
                        Toast.makeText(Login.this, "Usuario y/o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas Salir De Opus")
                .setPositiveButton("Si", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                }).setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}