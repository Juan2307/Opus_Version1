package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opus.opus_version1.Fragment_Home.CategoriesAdapter;
import com.opus.opus_version1.Fragment_Home.FeaturedAdapter;
import com.opus.opus_version1.Fragment_Home.FeaturedHelperClass;
import com.opus.opus_version1.Fragment_Home.MostViewedAdpater;
import com.opus.opus_version1.Fragment_Home.MostViewedHelperClass;
import com.opus.opus_version1.Fragment_Home.categoriesHelperClasses;

import java.util.ArrayList;
import java.util.Objects;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Atributos
    static final float END_SCALE = 0.7f;
    RecyclerView featuredRecycler, mostViewedRecycler, categoriesRecycler;
    RecyclerView.Adapter adapter;
    GradientDrawable gradient1, gradient2, gradient3, gradient4;
    //Atributos Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    public static int translateRight = R.anim.translate_right_side;
    public static int translateUp = R.anim.slide_out_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        categoriesRecycler = findViewById(R.id.categories_recycler);
        //TextView emailTextView = findViewById(R.id.emailTextView);
        //TextView nameTextView = findViewById(R.id.nameTextView);

        featuredRecycler();
        mostViewedRecycler();
        categoriesRecycler();

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        naviagtionDrawer();

        //Objeto FireBase User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /*if (user != null) {
            emailTextView.setText(user.getEmail());
        }*/

        //Objetos FireBase
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //Obtengo el ID Ingresado
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //Obtengo los datos de ID.

        /*mDatabaseReference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Si el User Existe.
                if (dataSnapshot.exists()) {
                    //Guardo el Nombre y Apellido En Las Variables.
                    String name = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                    String apellido = Objects.requireNonNull(dataSnapshot.child("apellido").getValue()).toString();
                    String telefono = Objects.requireNonNull(dataSnapshot.child("telefono").getValue()).toString();
                    //Actualizo las Variables
                    nameTextView.setText(name + " " + apellido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    //Navigation Drawer Functions
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            case R.id.nav_perfil:
                startActivity(new Intent(Home.this, ActualizarDatos.class));
                overridePendingTransition(0, translateRight);
                finish();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Deseas Cerrar Sesion?")
                        .setPositiveButton("Si", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(this, Login.class));
                            overridePendingTransition(0, translateUp);
                            super.onBackPressed();
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder.show();
                break;
        }
        return true;
    }

    //Recycler Views Functions
    private void naviagtionDrawer() {
        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(view -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.juan, "Juan Leandro", "Desarrollador Back-End"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.guillermo, "Guillermo Valencia", "Desarrollador Full-Stack"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.margarita, "Margarita Alvarez", "Intructora De Ingenieria En Sistemas"));

        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);
    }

    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<MostViewedHelperClass> mostViewedLocations = new ArrayList<>();
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.sebastian, "Sebastian", "Intructor De Ingles"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.juan, "Juan", "Desarrollador Back-End"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.guillermo, "Guillermo", "Desarrollador Full-Stack"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.lina, "Lina", "Intructora De Ingenieria Sanitaria"));

        adapter = new MostViewedAdpater(mostViewedLocations);
        mostViewedRecycler.setAdapter(adapter);

    }

    private void categoriesRecycler() {

        //All Gradients
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});


        ArrayList<categoriesHelperClasses> categoriesHelperClasses = new ArrayList<>();
        categoriesHelperClasses.add(new categoriesHelperClasses(gradient1, R.drawable.belleza, "Belleza"));
        categoriesHelperClasses.add(new categoriesHelperClasses(gradient2, R.drawable.mcdonald_img, "Crafts"));
        categoriesHelperClasses.add(new categoriesHelperClasses(gradient3, R.drawable.mcdonald_img, "Restaurant"));
        categoriesHelperClasses.add(new categoriesHelperClasses(gradient4, R.drawable.ingenieria, "Ingenieria"));
        categoriesHelperClasses.add(new categoriesHelperClasses(gradient1, R.drawable.medicina, "Medicina"));


        categoriesRecycler.setHasFixedSize(true);
        adapter = new CategoriesAdapter(categoriesHelperClasses);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);

    }

    //🡣🡣🡣Proceso Al Dar Click a Retroceder🡣🡣🡣
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Deseas Salir De Opus")
                    .setPositiveButton("Si", (dialog, which) -> super.onBackPressed()).setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }
}