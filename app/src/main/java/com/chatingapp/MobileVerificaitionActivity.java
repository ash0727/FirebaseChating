package com.chatingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MobileVerificaitionActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference Rootref;
    DatabaseReference databaseRef;

    EditText edt_mobile, edt_otp;
    ProgressDialog progressDialog;
    Button btnSubmit, btnVerify;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verificaition);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Rootref = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        edt_mobile = findViewById(R.id.edt_mobile);
        edt_otp = findViewById(R.id.edt_otp);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnVerify = findViewById(R.id.btnVerify);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edt_mobile.getText().toString().trim())) {
                    Toast.makeText(MobileVerificaitionActivity.this, "Please Enter Mobile no.", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    String phoneNumber = "+91"+ edt_mobile.getText().toString().trim();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            MobileVerificaitionActivity.this,               // Activity (for callback binding)
                            mCallbacks);
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                progressDialog.dismiss();
                Toast.makeText(MobileVerificaitionActivity.this, "Invilde Phone Number", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                mVerificationId = verificationId;
                mResendToken = token;
                progressDialog.dismiss();


                Toast.makeText(MobileVerificaitionActivity.this, "Code Send", Toast.LENGTH_SHORT).show();

            }

        };

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edt_otp.getText().toString().trim())) {
                    Toast.makeText(MobileVerificaitionActivity.this, "Please Enter OTP.", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressDialog.show();

                    String verificationcode = edt_otp.getText().toString().trim();

                    PhoneAuthCredential phoneAuthProvider = PhoneAuthProvider.getCredential(mVerificationId,verificationcode);
                    signInWithPhoneAuthCredential(phoneAuthProvider);

                }

            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(MobileVerificaitionActivity.this, "You are Login Successfully..", Toast.LENGTH_SHORT).show();
                            SendDatatoServer();
                        } else {
                            Toast.makeText(MobileVerificaitionActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                });

    }

    private void SendMainActivity(){
        Intent intent = new Intent(MobileVerificaitionActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void SendDatatoServer(){

        progressDialog = new ProgressDialog(MobileVerificaitionActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String , String> param = new HashMap<>();

        param.put("uid",mAuth.getCurrentUser().getUid());
        param.put("phone",edt_mobile.getText().toString().trim());
        databaseRef.child(mAuth.getCurrentUser().getUid()).setValue(param).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    SendMainActivity();
                }
                else {
                    Toast.makeText(MobileVerificaitionActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }
        });

    }

}


