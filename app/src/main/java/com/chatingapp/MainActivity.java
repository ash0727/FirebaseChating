 package com.chatingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chatingapp.Adapter.ViewPagerAdapter;
import com.chatingapp.Fragment.CallFragment;
import com.chatingapp.Fragment.ContectFragment;
import com.chatingapp.Fragment.FavFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

 public class MainActivity extends AppCompatActivity {
TabLayout tabLayout;
Toolbar mtoolbar;
ViewPager viewPager;
ViewPagerAdapter adapter;

FirebaseUser currentuser;
FirebaseAuth mAuth;
DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// new Tested Braches

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        currentuser =  mAuth.getCurrentUser();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.AddFragment(new ContectFragment(),"");
        adapter.AddFragment(new CallFragment(),"");
        adapter.AddFragment(new FavFragment(),"");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_people);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_call);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite);

        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
          super.onCreateOptionsMenu(menu);

          getMenuInflater().inflate(R.menu.toolbar_menu,menu);

          return true;
     }

     @Override
     public void onBackPressed() {
         super.onBackPressed();

         Intent intent = new Intent(getApplicationContext(),StartActivity.class);
         startActivity(intent);
         finish();

     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          super.onOptionsItemSelected(item);

          switch (item.getItemId()){

              case R.id.nav_setting:
                  startActivity(new Intent(getApplicationContext(),UserProfileActivtiy.class));
                  break;

              case R.id.nav_addgroup:
                  congoDilog();
                  break;

              case R.id.nav_logout:
                  SendUsertoLogint();
                  break;
          }

         return true;
     }

     EditText editText;

     public void congoDilog() {
         final Dialog dialog = new Dialog(MainActivity.this);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setTitle("");
         dialog.setContentView(R.layout.activity_congo);
         Button btnOK = dialog.findViewById(R.id.btnOK);
         btnOK.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SetGroupdatatoServer();
                 dialog.dismiss();
             }
         });

          editText = dialog.findViewById(R.id.edt_group);

         dialog.setCancelable(true);
         dialog.show();
         Window window = dialog.getWindow();
         window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

     }


     private void SetGroupdatatoServer(){

         databaseReference.child("Group").child(editText.getText().toString()).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 Toast.makeText(MainActivity.this, "Group Added Successfully", Toast.LENGTH_SHORT).show();
             }
         });

     }


     @Override
     protected void onStart() {
         super.onStart();

         if(currentuser == null){
             SendUsertoLogint();
         }

     }

     private void SendUsertoLogint(){

         mAuth.signOut();

         Intent intent = new Intent(MainActivity.this,MobileVerificaitionActivity.class);
         startActivity(intent);
         finish();
     }
 }
