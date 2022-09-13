package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuHeader extends AppCompatActivity {

    String id;
    CircleImageView profileImageView;
    TextView app_name;
    TextView correo;
    //Firebase
    DatabaseReference databaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_header);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        profileImageView = findViewById(R.id.portada);
        app_name = findViewById(R.id.name);
        app_name.setText("xd");

        correo = findViewById(R.id.correo);
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }
}