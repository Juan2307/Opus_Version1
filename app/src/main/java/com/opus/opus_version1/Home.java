package com.opus.opus_version1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opus.opus_version1.Fragment_Home.CategoriesAdapter;
import com.opus.opus_version1.Fragment_Home.FeaturedAdapter;
import com.opus.opus_version1.Fragment_Home.FeaturedHelperClass;
import com.opus.opus_version1.Fragment_Home.MostViewedAdpater;
import com.opus.opus_version1.Fragment_Home.MostViewedHelperClass;
import com.opus.opus_version1.Fragment_Home.categoriesHelperClasses;
import com.opus.opus_version1.Internet.NetworkChangeListener;

import java.util.ArrayList;
import java.util.Objects;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_STORAGE_CODE = 1000;
    public static final String URL_DESCARGA = "https://firebasestorage.googleapis.com/v0/b/loginopus-23248.appspot.com/o/Mockups%2Fmockups.pdf?alt=media&token=bc604e47-9ba8-494d-b1c1-9491ba3537d5";
    public static final String DESCRIPCION_PDF = "https://caesural-run.000webhostapp.com/";
    public static final String NOMBRE_PDF = "Mockups.pdf";
    //Conexion
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ConnectivityManager con;
    NetworkInfo networkInfo;
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
    //Atributos Firebase
    String id;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    //Atributos Animaciones
    public static int translateRight = R.anim.translate_right_side;
    public static int translateUp = R.anim.slide_out_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        //Conexion
        con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = con.getActiveNetworkInfo();
        //Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        mostViewedRecycler = findViewById(R.id.most_viewed_recycler);
        categoriesRecycler = findViewById(R.id.categories_recycler);
        TextView nameTextView = findViewById(R.id.app_user);

        featuredRecycler();
        mostViewedRecycler();
        categoriesRecycler();

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);

        naviagtionDrawer();


        //Objetos FireBase
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //Obtengo el ID Ingresado
        id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //Obtengo los datos de ID.
        mDatabaseReference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Si el User Existe.
                if (dataSnapshot.exists()) {
                    //Guardo el Nombre y Apellido En Las Variables.
                    String name = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                    //Actualizo las Variables
                    nameTextView.setText("¡Hola " + name + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Navigation Drawer Functions
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //Boton Home
            case R.id.nav_home:
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                break;


            //Boton Perfil
            case R.id.nav_perfil:

                if (networkInfo != null && networkInfo.isConnected()) {
                    startActivity(new Intent(Home.this, ActualizarDatos.class));
                    overridePendingTransition(0, translateRight);
                    finish();
                } else {
                    Toast.makeText(this, "No Se Pudo Conectar\n Verifique El Acceso A Internet e Intente Nuevamente.", Toast.LENGTH_LONG).show();
                }
                break;


            //Boton Actualizar
            case R.id.nav_actualizar:
                if (networkInfo != null && networkInfo.isConnected()) {
                    mAuth.sendPasswordResetEmail(Objects.requireNonNull(user.getEmail())).addOnCompleteListener(task -> {
                        Toast.makeText(this, "¡Correo Enviado!", Toast.LENGTH_SHORT).show();
                        if (drawerLayout.isDrawerVisible(GravityCompat.START))
                            drawerLayout.closeDrawer(GravityCompat.START);
                    });
                } else {
                    Toast.makeText(this, "No Se Pudo Conectar\n Verifique El Acceso A Internet e Intente Nuevamente.", Toast.LENGTH_LONG).show();
                }


                break;


            //Boton Eliminar
            case R.id.nav_eliminar://Conexion
                if (networkInfo != null && networkInfo.isConnected()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("¿Deseas Eliminar Tu Cuenta Opus?")
                            .setPositiveButton("Si", (dialog, which) -> {
                                if (networkInfo != null && networkInfo.isConnected()) {
                                    //Eliminar El Usuario
                                    user.delete().addOnCompleteListener(task1 -> {
                                        //Llamar al metodo SignOut para salir de aqui
                                        signOut();
                                    });
                                } else {
                                    Toast.makeText(this, "No Se Pudo Conectar\n Verifique El Acceso A Internet e Intente Nuevamente.", Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                    builder.show();
                } else {
                    Toast.makeText(this, "No Se Pudo Conectar\n Verifique El Acceso A Internet e Intente Nuevamente.", Toast.LENGTH_LONG).show();
                }
                break;


            //Boton Acerca De
            case R.id.nav_acercade:
                startActivity(new Intent(this, AcercaDe.class));
                break;


            //Boton Mockups
            case R.id.nav_mockups:
                //Conexion
                if (networkInfo != null && networkInfo.isConnected()) {
                    descargar();
                } else {
                    Toast.makeText(this, "No Se Pudo Conectar\n Verifique El Acceso A Internet e Intente Nuevamente.", Toast.LENGTH_LONG).show();
                }
                break;


            //Boton Salir
            case R.id.nav_logout:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("¿Deseas Cerrar Sesion?")
                        .setPositiveButton("Si", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(this, Login.class));
                            overridePendingTransition(0, translateUp);
                            super.onBackPressed();
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
                builder2.show();
                break;
        }
        return true;
    }

    private void descargar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_STORAGE_CODE);
            } else {
                startDownloading();
            }
        } else {

        }
    }

    private void startDownloading() {
        String url = URL_DESCARGA;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Opus");
        request.setDescription(DESCRIPCION_PDF);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, NOMBRE_PDF);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloading();
                } else {
                    Toast.makeText(this, "Permitir Porfavor Para Continuar.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Metodo Eliminar Cuenta
    private void signOut() {
        user.delete().addOnCompleteListener(task1 -> {
            mDatabaseReference.child("Usuarios").child(id).removeValue();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Home.this, "Cuenta Eliminada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });
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

    //Internet
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
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