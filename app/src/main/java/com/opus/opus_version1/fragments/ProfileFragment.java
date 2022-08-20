package com.opus.opus_version1.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opus.opus_version1.Login;
import com.opus.opus_version1.R;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Objetos
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView telefonoTextView = view.findViewById(R.id.telefonoTextView);
        ImageView imageView = view.findViewById(R.id.imageView);
        MaterialButton btnActualizarDatos = view.findViewById(R.id.btnActualizarDatos);
        MaterialButton logoutButton = view.findViewById(R.id.logoutButton);


        //Objeto FireBase User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailTextView.setText(user.getEmail());
        }
        //Objetos FireBase
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //Obtengo el ID Ingresado
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //Obtengo los datos de ID.
        mDatabaseReference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
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
                    telefonoTextView.setText(telefono);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        view.findViewById(R.id.logoutButton).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("¿Deseas Cerrar Sesion?")
                    .setPositiveButton("Si", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), Login.class));
                    }).setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        view.findViewById(R.id.btnActualizarDatos).setOnClickListener(v -> {
            // Create new fragment and transaction
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setReorderingAllowed(true);
            // Replace whatever is in the fragment_container view with this fragment
            transaction.replace(R.id.linearLayoutReemplazar, EditProfileFragment.newInstance("Antony", ""));
            // Commit the transaction
            transaction.commit();
            //Ocultar botones
            imageView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            telefonoTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            btnActualizarDatos.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
        });
    }
}