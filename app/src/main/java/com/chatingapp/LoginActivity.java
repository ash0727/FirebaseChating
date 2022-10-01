package com.chatingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference Rootref;
    EditText edt_email,edt_pass;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        Rootref = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);

        ((Button)findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateNewAccount();

            }
        });

        ((TextView)findViewById(R.id.txtmobilelogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MobileVerificaitionActivity.class));
            }
        });


    }

    private void CreateNewAccount(){

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(edt_email.getText().toString(),edt_pass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            StoreUserid();
                            SendMainActivity();
                            Toast.makeText(LoginActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();


                    }
                });

    }

    private void StoreUserid() {

        String uid = mAuth.getCurrentUser().getUid();
        Rootref.child("Users").child(uid).setValue("");

    }

    private void SendMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
