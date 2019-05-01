package com.example.controlinventario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AgregarProductos extends AppCompatActivity {

    EditText tvName;
    EditText tvPrice;
    EditText tvDescription;
    Button btnAddImage;
    Button btnAddProduct;
    Button btnBack;
    ImageButton image;
    DatabaseReference reference;
    Product product;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    int Gallery_intent = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_productos);

        tvName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvProductPrice);
        tvDescription = findViewById(R.id.tvProductDescription);
        btnAddImage = findViewById(R.id.btnProductImage);
        btnAddProduct = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        reference = FirebaseDatabase.getInstance().getReference().child("Product");
        product = new Product();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        btnAddImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGallery();
            }
        });



        btnAddProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = tvName.getText().toString();
                int price = Integer.parseInt(tvPrice.getText().toString());
                String description = tvDescription.getText().toString();

                product.setName(name);
                product.setPrice(price);
                product.setDescription(description);
                product.setImageAddress(storageReference.toString());

                reference.push().setValue(product);
                Toast.makeText(getApplicationContext(), "Product added succesfully", Toast.LENGTH_LONG).show();

                tvName.setText("");
                tvPrice.setText("");
                tvDescription.setText("");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                volver();
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,Gallery_intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_intent && resultCode == RESULT_OK){
            Uri uri = data.getData();
            image.setImageURI(uri);
            storageReference = FirebaseStorage.getInstance().getReference().child("Product").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void volver() {
        Intent i = new Intent(this, Menu.class);
        startActivity(i);
        finish();
    }
}
