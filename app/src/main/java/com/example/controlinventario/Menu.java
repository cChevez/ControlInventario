package com.example.controlinventario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {

    Button btnAddProducts;
    Button btnShowProducts;
    Button btnLogOut;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnAddProducts = findViewById(R.id.btnAddProduct);
        btnShowProducts = findViewById(R.id.btnShowProducts);
        btnLogOut = findViewById(R.id.btnLogOut);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AgregarProductos.class);
                startActivity(i);
                finish();
            }
        });

        btnShowProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MostrarProductos.class);
                startActivity(i);
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Disconnecting");
                progressDialog.show();
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(),"Disconnected",Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
