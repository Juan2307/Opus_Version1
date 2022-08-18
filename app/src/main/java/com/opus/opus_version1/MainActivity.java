package com.opus.opus_version1;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //Objetos
    TextView deTextView, codeliaTextView;
    ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        //Agregar Animacion
        Animation anamacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation anamacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);
        //Instancio De Los ID
        deTextView = findViewById(R.id.deTextView);
        codeliaTextView = findViewById(R.id.codeliaTextView);
        logoImageView = findViewById(R.id.logoImageView);
        //Asignacion De Las Animaciones
        deTextView.setAnimation(anamacion2);
        codeliaTextView.setAnimation(anamacion2);
        logoImageView.setAnimation(anamacion1);

        new Handler().postDelayed(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
            //Si Inicio Sesion Va A UserActivity
            if (user != null || account != null) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }//Si No Inicio Sesion Hace La Transición Al Login
            else {
                Intent intent = new Intent(this, Login.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logoImageView, "logoImageTrans");
                pairs[1] = new Pair<View, String>(codeliaTextView, "textTrans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
                startActivity(intent, options.toBundle());
                finish();
            }
        }, 4000);

    }
}