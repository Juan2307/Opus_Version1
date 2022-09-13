package com.opus.opus_version1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opus.opus_version1.Internet.Internet;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

public class Sign_Up extends AppCompatActivity {
    //Variables
    String hoy;
    public SimpleDateFormat simpleDateFormat;
    //Calendario
    Calendar calendar = Calendar.getInstance();
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private final int month = calendar.get(Calendar.MONTH);
    private final int year = calendar.get(Calendar.YEAR);


    AutoCompleteTextView autoCompleteTextView;
    EditText et_date;
    TextView edadTextField, ConCuenta, bienvenidoLabel, continuarLabel;
    TextInputLayout usuarioSingUpTextField, contrasenaTextField;
    ImageView signUpImageView;
    MaterialButton inicioSesion;
    TextInputEditText documentoTextField, nameTextField, lastnameTextField, telefonoTextField, emailEditText, passwordEditText, confirmPasswordEditText;
    CheckBox terminoCondiciones;
    // FireBase
    FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseAuth mAuth;
    // Transicion
    public static int translateUp = R.anim.slide_out_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("REGISTER");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Instancio las Transiciones del ID
        signUpImageView = findViewById(R.id.signUpImageView);
        bienvenidoLabel = findViewById(R.id.bienvenidoLabel);
        continuarLabel = findViewById(R.id.continuarLabel);
        usuarioSingUpTextField = findViewById(R.id.usuarioSingUpTextField);
        contrasenaTextField = findViewById(R.id.contrasenaTextField);
        //Instancio los Botones del ID
        inicioSesion = findViewById(R.id.inicioSesion);
        ConCuenta = findViewById(R.id.ConCuenta);
        //Instancio los TextField del ID
        documentoTextField = findViewById(R.id.documentoTextField);
        nameTextField = findViewById(R.id.nameTextField);
        lastnameTextField = findViewById(R.id.lastnameTextField);
        et_date = findViewById(R.id.et_date);
        //Fecha en el formato que queramos
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        hoy = simpleDateFormat.format(Calendar.getInstance().getTime());

        edadTextField = findViewById(R.id.edadTextField);
        telefonoTextField = findViewById(R.id.telefonoTextField);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        autoCompleteTextView = findViewById(R.id.dropdown);
        String[] Professions = getResources().getStringArray(R.array.profesiones);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, R.layout.list_item, Professions);
        autoCompleteTextView.setAdapter(adapter);
        //Instancio el Objeto de Firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Usuarios");
        mAuth = FirebaseAuth.getInstance();
        //Instancio ID Del CheckBox
        terminoCondiciones = findViewById(R.id.CheckTerminos);


        //Al Dar Click Proceso
        et_date.setOnClickListener(v -> {
            //Ventana
            DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_Up.this, (view, year1, month1, day1) -> {
                // +1 Porque Enero es 0
                month1 = month1 + 1;
                String date = day1 + "/" + month1 + "/" + year1;
                //Mostrar en el Edit Text
                et_date.setText(date);
                try {//Objeto
                    Date date1 = simpleDateFormat.parse(date);
                    Date date2 = simpleDateFormat.parse(hoy);
                    long starDate = date1.getTime();
                    long endDate = date2.getTime();
                    //Fecha Seleccionada menor Actual haga:
                    if (starDate <= endDate) {
                        Period period = new Period(starDate, endDate, PeriodType.yearMonthDay());
                        int years = period.getYears();
                        if (years >= 18) {
                            String edad = String.valueOf(years);
                            edadTextField.setText(edad);
                        } else {
                            Toast.makeText(this, "Edad No Apta Para Usar Opus", Toast.LENGTH_LONG).show();
                        }
                    } else { // La fecha de nacimiento no debe ser mayor que la fecha de hoy.
                        Toast.makeText(this, "Birth Date should not be larger than today´s date.", Toast.LENGTH_SHORT).show();
                    }//Exepcion
                } catch (ParseException e) {
                    e.printStackTrace();//Capturar Exepcion
                }
            }, year, month, day);
            datePickerDialog.show();
        });
        terminoCondiciones.setOnClickListener(v -> {
            if (terminoCondiciones.isChecked()) {
                startActivity(new Intent(this, TerminosCondiciones.class));
            } else {
                Toast.makeText(this, "Debes Aceptar Terminos Y Condiciones", Toast.LENGTH_SHORT).show();
            }
        });
        inicioSesion.setOnClickListener(v -> {
            if (Internet.isOnline(this)) {
                validate();
            }
        });
        ConCuenta.setOnClickListener(v -> {
            startActivity(new Intent(Sign_Up.this, Login.class));
            overridePendingTransition(0, translateUp);
            finish();
        });
    }

    //🡣🡣🡣Validacion De Los Campos🡣🡣🡣
    public void validate() {
        //Recoger Datos
        String documento = Objects.requireNonNull(Objects.requireNonNull(documentoTextField.getText()).toString().trim());
        String nombre = Objects.requireNonNull(Objects.requireNonNull(nameTextField.getText()).toString().trim().toLowerCase());
        String apellido = Objects.requireNonNull(Objects.requireNonNull(lastnameTextField.getText()).toString().trim().toLowerCase());
        String fecha = Objects.requireNonNull(et_date.getText()).toString().trim();
        String edad = Objects.requireNonNull(edadTextField.getText()).toString().trim();
        String telefono = Objects.requireNonNull(telefonoTextField.getText()).toString().trim();
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim().toLowerCase();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString().trim();
        String profesion = Objects.requireNonNull(autoCompleteTextView.getText()).toString().trim();

        //Campo documento
        if (documento.isEmpty()) {
            documentoTextField.setError("¡Campo Vacio!");
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Faltan Mas Campos", Toast.LENGTH_SHORT).show();
            return;
        } else if (nombre.length() < 3) {
            nameTextField.setError("¿Cual Es Tu Nombre");
            Toast.makeText(this, "Verifica El Campo Nombre", Toast.LENGTH_SHORT).show();
            return;
        } else {
            nameTextField.setError(null);
        }
        //Campo Apellido
        if (apellido.isEmpty()) {
            lastnameTextField.setError("¡Campo Vacio!");
            Toast.makeText(this, "Faltan Mas Campos", Toast.LENGTH_SHORT).show();
            return;
        } else if (apellido.length() < 4) {
            lastnameTextField.setError("¿Cual Es Tu Apellido?");
            Toast.makeText(this, "Verifica El Campo Apellido", Toast.LENGTH_SHORT).show();
            return;
        } else {
            lastnameTextField.setError(null);
        }
        //Campo Fecha
        if (fecha.isEmpty()) {
            et_date.setError("¡Campo Vacio!");
            Toast.makeText(this, "Faltan Mas Campos Porfavor Verificar Nuevamente", Toast.LENGTH_SHORT).show();
            return;
        } else {
            et_date.setError(null);
        }
        //Campo Edad
        if (edad.isEmpty()) {
            et_date.setError("¡Debes ser Mayor De Edad Para Usar La App!");
            Toast.makeText(this, "Faltan Mas Campos Porfavor Verificar Nuevamente", Toast.LENGTH_SHORT).show();
            return;
        } else {
            edadTextField.setError(null);
        }

        //Campo Telefono
        if (telefono.isEmpty()) {
            telefonoTextField.setError("¡Campo Vacio!");
            Toast.makeText(this, "Faltan Mas Campos Porfavor Verificar", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Faltan Mas Campos Porfavor Verificar", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Faltan Mas Campos Porfavor Verificar", Toast.LENGTH_SHORT).show();
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
            confirmPasswordEditText.setError("¡Campo Vacio!");
            Toast.makeText(this, "Faltan Mas Campos Porfavor Verificar", Toast.LENGTH_SHORT).show();
            return;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Deben Ser Iguales");
            return;
        } else {
            confirmPasswordEditText.setError(null);
        }
        if (profesion.isEmpty()) {
            autoCompleteTextView.setError("¡Campo Vacio!");
            Toast.makeText(this, "Faltan Un Campo", Toast.LENGTH_SHORT).show();
            return;
        } else {
            autoCompleteTextView.setError(null);
        }

        if (terminoCondiciones.isChecked()) {
            //Ir Al Metodo Registrar
            registrar(documento, nombre, apellido, fecha, edad, telefono, email, password, profesion);
        } else {
            Toast.makeText(this, "Debes Aceptar Terminos Y Condiciones", Toast.LENGTH_SHORT).show();
        }
    }

    //🡣🡣🡣Validar Si La Cuenta Existe o La Crea🡣🡣🡣
    public void registrar(String documento, String nombre, String apellido, String
            fechaNacimiento, String edad, String telefono, String email, String password, String profesion) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Destinos destinos = new Destinos(documento, nombre, apellido, fechaNacimiento, edad, telefono, email, profesion);
                myRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(destinos); //getUid.CHIL documento
                startActivity(new Intent(Sign_Up.this, Bienvenido.class));
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(Sign_Up.this, "Cuenta Ya Vinculada", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Sign_Up.this, Perfil.class));
        overridePendingTransition(0, translateUp);
        finish();
    }

    //Flecha Atras
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(Sign_Up.this, Perfil.class));
        overridePendingTransition(0, translateUp);
        finish();
        return super.onOptionsItemSelected(item);
    }
}