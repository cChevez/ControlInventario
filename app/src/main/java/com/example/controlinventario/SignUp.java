package com.example.controlinventario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    Button btnCreateUser;
    Button btnBack;
    EditText tvUsername;
    EditText tvEmail;
    EditText tvPass;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPass = findViewById(R.id.tvPass);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnBack = findViewById(R.id.btnBack);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        btnCreateUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                registrar();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ingresar();
            }
        });

        listener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    openAccount();
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void openAccount() {
        Intent i = new Intent(this, Menu.class);
        startActivity(i);
        finish();
    }

    private void ingresar() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void registrar() {
        final String email = tvEmail.getText().toString();
        final String pass = tvPass.getText().toString();
        String usuario = tvUsername.getText().toString();

        if(!email.isEmpty() && !pass.isEmpty() && !usuario.isEmpty()){
            progressDialog.setMessage("Creating User");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Error, user can not be created",Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(listener != null){
            mAuth.removeAuthStateListener(listener);
        }
    }
}
