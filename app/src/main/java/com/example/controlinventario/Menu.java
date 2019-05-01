package com.example.controlinventario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button btnAddProducts;
    Button btnShowProducts;
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnAddProducts = findViewById(R.id.btnAddProduct);
        btnShowProducts = findViewById(R.id.btnShowProducts);
        btnLogOut= findViewById(R.id.btnLogOut);

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
    }
}
