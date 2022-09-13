package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.opus.opus_version1.Internet.Internet;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActualizarFoto extends AppCompatActivity {

    String id;
    CircleImageView profileImageView;
    Button closeButton, saveButton, deleteButton;
    TextView profileChangeBtn;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    Uri imagenUri;
    String myUri = "";
    String sinfoto = "https://firebasestorage.googleapis.com/v0/b/loginopus-23248.appspot.com/o/Profile%20Pic%2Fsinfoto.png?alt=media&token=42183b89-536c-405c-85e2-97f5d613253a";
    StorageTask uploadTask;
    StorageReference storageProfilePicsRef;
    //Atributos De Transicion
    public static int zoomOut = R.anim.zoom_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_foto);
        setTitle("Actualizar Foto");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        //Obtengo el ID Ingresado
        id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        //Asignacion de Id
        profileImageView = findViewById(R.id.profile_image);
        closeButton = findViewById(R.id.btnClose);
        saveButton = findViewById(R.id.btnSave);
        deleteButton = findViewById(R.id.btnDelete);
        profileChangeBtn = findViewById(R.id.change_profile_btn);

        //Proceso Al Dar Click
        profileChangeBtn.setOnClickListener(view -> CropImage.activity().setAspectRatio(1, 1).start(ActualizarFoto.this));
        closeButton.setOnClickListener(view -> {
            startActivity(new Intent(ActualizarFoto.this, ActualizarDatos.class));
            finish();
        });
        saveButton.setOnClickListener(view -> {
            if (Internet.isOnline(ActualizarFoto.this)) {
                uploadProfileImage();
            } else {
                Toast.makeText(ActualizarFoto.this, "¡Sin Acceso A Internet, Verifique Su Conexión.!", Toast.LENGTH_SHORT).show();
            }
        });
        deleteButton.setOnClickListener(v -> {
            if (Internet.isOnline(ActualizarFoto.this)) {
                final StorageReference fileRef = storageProfilePicsRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + ".jpg");
                databaseReference.child(id).child("image").removeValue();
                fileRef.delete();
                Toast.makeText(ActualizarFoto.this, "¡Foto Eliminada!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActualizarFoto.this, "¡Sin Acceso A Internet, Verifique Su Conexión.!", Toast.LENGTH_SHORT).show();
            }
        });
        getUserinfo();
    }

    private void getUserinfo() {
        databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists() && datasnapshot.getChildrenCount() > 0) {
                    if (datasnapshot.hasChild("image")) {
                        String image = Objects.requireNonNull(datasnapshot.child("image").getValue()).toString();
                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imagenUri = result.getUri();
            profileImageView.setImageURI(imagenUri);
        } else {
            Toast.makeText(ActualizarFoto.this, "Error Intente De Nuevo", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your Data");
        progressDialog.show();
        if (imagenUri != null) {
            final StorageReference fileRef = storageProfilePicsRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + ".jpg");
            uploadTask = fileRef.putFile(imagenUri);

            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", myUri);

                    databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                    progressDialog.dismiss();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(ActualizarFoto.this, "Image Not Select", Toast.LENGTH_SHORT).show();
        }
    }

    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ActualizarDatos.class));
        overridePendingTransition(0, zoomOut);
        finish();
    }

    //🡣🡣🡣Flecha Atras🡣🡣🡣
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, ActualizarDatos.class));
        overridePendingTransition(0, zoomOut);
        finish();
        return super.onOptionsItemSelected(item);
    }
}