package com.example.controlinventario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String imageURL;
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
        imageURL = "";

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
                if (filePath != null) {
                    if (tvName.getText().length() != 0) {
                        if (tvDescription.getText().length() != 0) {
                            if (tvPrice.getText().length() != 0) {
                                final String name = tvName.getText().toString();
                                int price = Integer.parseInt(tvPrice.getText().toString());
                                String description = tvDescription.getText().toString();

                                product.setName(name);
                                product.setPrice(price);
                                product.setDescription(description);
                                product.setImageAddress(storageRef.toString());

                                progressDialog.show();
                                final StorageReference childRef = storageRef.child(name + ".jpg");

                                UploadTask uploadTask = childRef.putFile(filePath);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        reference.child(name).setValue(product);
                                        Toast.makeText(getApplicationContext(), "Product added succesfully", Toast.LENGTH_LONG).show();
                                        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Uri downloadUri = uri;
                                                imageURL = downloadUri.toString();
                                                product.setImageAddress(imageURL);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Can't upload the product", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Please insert a price for the product", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please insert a description for the product", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please insert a name for the product", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();
                }
                Log.d("TAG", "addData: name " + product.getName());
                Log.d("TAG", "addData: price " + product.getPrice());
                Log.d("TAG", "addData: photoAddress " + product.getImageAddress());
                Log.d("TAG", "addData: description " + product.getDescription());
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
                Toast.makeText(getApplicationContext(), "Picture selected", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
