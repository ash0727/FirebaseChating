package com.chatingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.chatingapp.Adapter.MessageAdapter;
import com.chatingapp.Model.Chats;
import com.chatingapp.Model.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageAcrtivity extends AppCompatActivity {

    Toolbar mtoolbar;
    String UserName,userid;

    FirebaseUser fuser;
    DatabaseReference reference;

    EditText edtText;
    ImageButton btnSendMessage;
    List<Chats> chatsList;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_acrtivity);

       Intilization();

    }

   private void Intilization(){
       recyclerView = findViewById(R.id.recylerview);
       recyclerView.setHasFixedSize(true);

       LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
       linearLayoutManager.setStackFromEnd(true);
       recyclerView.setLayoutManager(linearLayoutManager);
       edtText = findViewById(R.id.edtText);
       btnSendMessage = findViewById(R.id.btnSendMessage);
       mtoolbar = findViewById(R.id.toolbar);
       setSupportActionBar(mtoolbar);

       UserName = getIntent().getStringExtra("name");
       userid = getIntent().getStringExtra("uid");
       getSupportActionBar().setTitle(UserName);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });

       fuser = FirebaseAuth.getInstance().getCurrentUser();
       reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               Contact user = dataSnapshot.getValue(Contact.class);
               getSupportActionBar().setTitle(user.getName());

               ReadMessage(fuser.getUid(),userid,user.getImage());
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       btnSendMessage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               String message = edtText.getText().toString().trim();

               if(!message.equals(""))
               {
                   SendMessages(fuser.getUid(),userid,message);
               }

               edtText.setText("");

           }
       });

   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MessageAcrtivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void SendMessages(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

       HashMap<String, Object> hashMap = new HashMap<>();
       hashMap.put("sender",sender);
       hashMap.put("receiver",receiver);
       hashMap.put("message",message);

       reference.child("Chats").push().setValue(hashMap);

   }

   private void ReadMessage(final String myid, final String userid, int imgurl){

        chatsList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    Chats mChat = snapshot.getValue(Chats.class);

                    if(mChat.getReceiver().equals(myid) && mChat.getSender().equals(userid) ||
                            mChat.getReceiver().equals(userid) && mChat.getSender().equals(myid))
                    {
                        chatsList.add(mChat);
                    }


                    messageAdapter = new MessageAdapter(MessageAcrtivity.this,chatsList);
                    recyclerView.setAdapter(messageAdapter);



                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

   }
}
