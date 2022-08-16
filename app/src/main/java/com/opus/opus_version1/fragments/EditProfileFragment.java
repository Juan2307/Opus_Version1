package com.opus.opus_version1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opus.opus_version1.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView nameTextField, apellidoTextField, telefonoTextField, emailTextView;
    MaterialButton btnActualizarDatos;
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EditProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Objetos
        nameTextField = view.findViewById(R.id.nameTextField);
        apellidoTextField = view.findViewById(R.id.apellidoTextField);
        telefonoTextField = view.findViewById(R.id.TelefonoTextField);
        emailTextView = view.findViewById(R.id.emailTextView);
        btnActualizarDatos = view.findViewById(R.id.ActualizarDatos);
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
                    nameTextField.setText(name);
                    apellidoTextField.setText(apellido);
                    telefonoTextField.setText(telefono);
                }
                //Objeto FireBase User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    emailTextView.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btnActualizarDatos.setOnClickListener(v -> validate());
    }

    public void validate() {
        String name = Objects.requireNonNull(nameTextField.getText()).toString().trim().toLowerCase();
        String apellido = Objects.requireNonNull(apellidoTextField.getText()).toString().trim().toLowerCase();
        String telefono = Objects.requireNonNull(telefonoTextField.getText()).toString().trim();
        if (name.isEmpty()) {
            nameTextField.setError("Campo Vacio");
            return;
        } else {
            nameTextField.setError(null);
        }
        if (apellido.isEmpty()) {
            apellidoTextField.setError("Campo Vacio");
            return;
        } else {
            apellidoTextField.setError(null);
        }
        if (telefono.isEmpty()) {
            telefonoTextField.setError("Campo Vacio");
            return;
        } else {
            telefonoTextField.setError(null);
        }
        update_Data(name, apellido, telefono);
    }

    public void update_Data(String name, String apellido, String telefono) {
        Map<String, Object> Data = new HashMap<>();
        Data.put("nombre", name);
        Data.put("apellido", apellido);
        Data.put("telefono", telefono);
        mDatabaseReference.child("Usuarios").child(id).updateChildren(Data).
                addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Datos Actualizados", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Hubo Un Error Al Actualizar Los Datos", Toast.LENGTH_SHORT).show());
    }
}