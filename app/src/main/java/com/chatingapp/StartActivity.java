package com.chatingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

public class StartActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        mAuth = FirebaseAuth.getInstance();
        currentuser =  mAuth.getCurrentUser();

        ((Button)findViewById(R.id.btnLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MobileVerificaitionActivity.class);
                startActivity(intent);
            }
        });

        ((SubmitButton)findViewById(R.id.btnRegistration)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                },1000);



            }
        });


        findViewById(R.id.btnLoginWithEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,LoginActivity.class));
            }
        });

    }

    FirebaseUser currentuser;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();


        if(currentuser != null){
            SendUsertoMainActivity();
        }

    }

    private void SendUsertoMainActivity() {
        startActivity(new Intent(context,MainActivity.class));
    }
}
