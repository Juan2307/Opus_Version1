package com.opus.opus_version1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.opus.opus_version1.fragments.HomeFragment;
import com.opus.opus_version1.fragments.ProfileFragment;
import com.opus.opus_version1.fragments.SettingsFragment;

public class UserActivity extends AppCompatActivity {
    //Objetos
    BottomNavigationView mBottomNavigation;
    public static int zoomOut = R.anim.zoom_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("OPUS");
        mBottomNavigation = findViewById(R.id.bottomNavigation);
        //Fragment Por Defecto
        showSelectedFragment(new HomeFragment());
        //Al Dar Click Proceso
        mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {

            if (menuItem.getItemId() == R.id.menu_home) {
                showSelectedFragment(new HomeFragment());
            }
            if (menuItem.getItemId() == R.id.menu_profile) {
                showSelectedFragment(new ProfileFragment());
            }
            if (menuItem.getItemId() == R.id.menu_settings) {
                showSelectedFragment(new SettingsFragment());
            }
            return true;
        });
    }

    //Metodo Permitir Elejir El Fragment.
    private void showSelectedFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas Salir De Opus")
                .setPositiveButton("Si", (dialog, which) -> {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                    overridePendingTransition(0,zoomOut);
                    finish();
                }).setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}