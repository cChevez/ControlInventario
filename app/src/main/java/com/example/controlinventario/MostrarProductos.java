package com.example.controlinventario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MostrarProductos extends AppCompatActivity {

    Button btnBack;
    Button btnDelete;
    EditText tvDelete;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    String productName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        tvDelete = findViewById(R.id.tvDelete);
        listView = findViewById(R.id.lvProducts);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                volver();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = tvDelete.getText().toString();
                deleteProduct(productName);
            }
        });
    }

    private void deleteProduct(String productName) {
        myRef = FirebaseDatabase.getInstance().getReference("Product").child(productName);

        Log.d("TAG","Delete: " + myRef);

        myRef.removeValue();

        Toast.makeText(getApplicationContext(), "Product deleted succesfully", Toast.LENGTH_LONG).show();
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.child("Product").getChildren()){
            Product product = ds.getValue(Product.class);

            Log.d("TAG", "showData: name " + product.getName());
            Log.d("TAG", "showData: price " + product.getPrice());
            Log.d("TAG", "showData: photoAddress " + product.getImageAddress());
            Log.d("TAG", "showData: description " + product.getDescription());

            arrayList.add("Name: " + product.getName());
            arrayList.add("Price: " + String.valueOf(product.getPrice()));
            arrayList.add("Photo: " + product.getImageAddress());
            arrayList.add("Description: " + product.getDescription());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(adapter);
        }
    }

    private void volver() {
        Intent i = new Intent(this, Menu.class);
        startActivity(i);
        finish();
    }
}
