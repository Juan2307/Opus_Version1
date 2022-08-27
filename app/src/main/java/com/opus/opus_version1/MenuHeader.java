package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuHeader extends AppCompatActivity {

    CircleImageView profileImageView;
    TextView app_name;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_header);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        profileImageView = findViewById(R.id.portada);
        app_name = findViewById(R.id.name);
        getUserinfo();

        //Objeto FireBase User
        /*user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            menu_slogan.setText(user.getEmail());
        }*/
    }

    private void getUserinfo() {
        databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists() && datasnapshot.getChildrenCount() > 0) {
                    String name = datasnapshot.child("nombre").getValue().toString();
                    app_name.setText(name);

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
}