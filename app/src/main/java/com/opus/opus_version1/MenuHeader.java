package com.opus.opus_version1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuHeader extends AppCompatActivity {

    TextView menu_slogan;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_header);

        menu_slogan = findViewById(R.id.menu_slogan);

        //Objeto FireBase User
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            menu_slogan.setText(user.getEmail());
        }
    }
}