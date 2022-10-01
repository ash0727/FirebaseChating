package com.chatingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class UserProfileActivtiy extends AppCompatActivity {

    EditText edt_username,edt_status,edt_mobile;
    Button btn_update;
    FirebaseAuth mAuth;
    DatabaseReference databaseRef,RootRef;
    ProgressDialog progressDialog;
    ImageView imageView;
    static int pReqCode = 1;
    static int REQUESTCODE= 1;
    StorageReference UserProfileImageRef;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_activtiy);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RootRef = FirebaseDatabase.getInstance().getReference();

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        imageView = findViewById(R.id.imageView);
        edt_username = findViewById(R.id.edt_username);
        edt_status = findViewById(R.id.edt_status);
        edt_mobile = findViewById(R.id.edt_mobile);
        btn_update = findViewById(R.id.btn_udpate);

        getDateFromServer();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDatatoServer();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){

                    checkandrequestforpermission();

                }
                else {
                    opengallery();
                }
            }
        });


    }

    private void checkandrequestforpermission() {

        if(ContextCompat.checkSelfPermission(UserProfileActivtiy.this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) UserProfileActivtiy.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(UserProfileActivtiy.this, "Please accept for required permission ", Toast.LENGTH_SHORT).show();

            }

            else {
                ActivityCompat.requestPermissions((Activity) UserProfileActivtiy.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},pReqCode
                );
            }

        }
        else {
            opengallery();
        }


    }


    private void opengallery() {

        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,REQUESTCODE);

    }

    Uri picture;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            picture = data.getData();

            CropImage.activity(picture)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

//            imageView.setImageURI(picture);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(userid + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserProfileActivtiy.this, "Proflie Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();

//                            referem

                        }
                        else {
                            Toast.makeText(UserProfileActivtiy.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        final String downloadUlr = uri.toString();

                        databaseRef.child(userid)
                                .child("image")
                                .setValue(downloadUlr)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            Toast.makeText(UserProfileActivtiy.this, "Image Store in Database Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(UserProfileActivtiy.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                    }
                });


            }

        }


    }

    private void getDateFromServer(){

        progressDialog = new ProgressDialog(UserProfileActivtiy.this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseRef.child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status") && dataSnapshot.hasChild("image"))){

                            edt_username.setText(dataSnapshot.child("name").getValue().toString());
                            edt_status.setText(dataSnapshot.child("status").getValue().toString());
                            Glide.with(UserProfileActivtiy.this).load(dataSnapshot.child("image").getValue().toString()).into(imageView);

                        }
                        else
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status"))){

                            edt_username.setText(dataSnapshot.child("name").getValue().toString());
                            edt_status.setText(dataSnapshot.child("status").getValue().toString());

                        }
                        else {
                            Toast.makeText(UserProfileActivtiy.this, "Please Update Profile", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });

    }

    private void SendDatatoServer(){

        progressDialog = new ProgressDialog(UserProfileActivtiy.this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String , String> param = new HashMap<>();

        param.put("uid",mAuth.getCurrentUser().getUid());
        param.put("name",edt_username.getText().toString().trim());
        param.put("status",edt_status.getText().toString().trim());
        param.put("phone",edt_mobile.getText().toString().trim());
        databaseRef.child(mAuth.getCurrentUser().getUid()).setValue(param).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UserProfileActivtiy.this, "Update Successfull", Toast.LENGTH_SHORT).show();
                    SendMainActivity();

                }
                else {
                    Toast.makeText(UserProfileActivtiy.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }
        });

    }

    private void SendMainActivity(){
        Intent intent = new Intent(UserProfileActivtiy.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
