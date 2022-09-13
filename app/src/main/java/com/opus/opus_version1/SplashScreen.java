package com.opus.opus_version1;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {

    //Atributos
    ImageView backgroundImage;
    TextView poweredByLine;
    //Animations
    Animation sideAnim, bottonAnim;
    SharedPreferences onBoardingScreen;
    public static int translateUp = R.anim.slide_out_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setTitle("");

        //Hooks
        backgroundImage = findViewById(R.id.background_image);
        poweredByLine = findViewById(R.id.power_by_line);

        //Animations
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottonAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //set Animations on elements
        backgroundImage.setAnimation(sideAnim);
        poweredByLine.setAnimation(bottonAnim);

        new Handler().postDelayed(() -> {

            onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
            boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

            if (isFirstTime) {
                SharedPreferences.Editor editor = onBoardingScreen.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();
            }
            startActivity(new Intent(getApplicationContext(), OnBoarding.class));
            overridePendingTransition(0, translateUp);
            finish();
        }, 4000);
    }

    //🡣🡣🡣Proceso Al Dar Click a Retroceder Bloquear🡣🡣🡣
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}