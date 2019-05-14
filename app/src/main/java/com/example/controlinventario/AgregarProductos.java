package com.example.controlinventario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    DatabaseReference reference;
    Product product;
    ProgressDialog progressDialog;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://inventario-21125.appspot.com/");    //change the url according to your firebase app



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
        progressDialog.setMessage("Uploading");

        btnAddImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                volver();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(filePath != null && tvName.getText().length()==0 && tvDescription.getText().length()==0 && tvPrice.getText().length()==0){
                    final String name = tvName.getText().toString();
                    int price = Integer.parseInt(tvPrice.getText().toString());
                    String description = tvDescription.getText().toString();

                    product.setName(name);
                    product.setPrice(price);
                    product.setDescription(description);
                    product.setImageAddress(storageRef.toString());

                    progressDialog.show();
                    StorageReference childRef = storageRef.child(name + ".jpg");

                    UploadTask uploadTask = childRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            reference.child(name).setValue(product);
                            Toast.makeText(getApplicationContext(), "Product added succesfully", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Can't upload the product", Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Fill the blanks and select a picture", Toast.LENGTH_LONG).show();
                }

                tvName.setText("");
                tvPrice.setText("");
                tvDescription.setText("");
            }
        });
    }

    private void volver() {
        Intent i = new Intent(this, Menu.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath= data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
