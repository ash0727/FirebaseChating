package com.chatingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    ImageButton btnSendMessage;
    EditText edtText;
    ScrollView scrollView;
    TextView txtDisplayMessage;
    String GroupName,currentUserid,currentUserName,currDate,currTime;
    FirebaseAuth mAuth;
    DatabaseReference mGrouRef,mUserRef,GroupMessageKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        Initialization();

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        mGrouRef = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupName);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        GetUserDate();

    }

    private void Initialization() {

        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        GroupName = getIntent().getStringExtra("group");

        getSupportActionBar().setTitle(GroupName);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        edtText = findViewById(R.id.edtText);
        scrollView = findViewById(R.id.my_scroll_view);
        txtDisplayMessage = findViewById(R.id.txtDisplayMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMessageinDatabase();

                edtText.setText("");

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mGrouRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void DisplayMessage(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()){
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatname = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            txtDisplayMessage.append(chatname + " : \n" + chatMessage + "\n" + chatTime + "       " + chatDate + " \n\n\n"  );

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }

    }

    private void SaveMessageinDatabase() {

        String message = edtText.getText().toString().trim();
        String messagekey =  mGrouRef.push().getKey();

        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "Please write Message First....", Toast.LENGTH_SHORT).show();
        }
        else {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            currDate = currentDate.format(calendar.getTime());


            Calendar calendarForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currTime = currentTimeFormat.format(calendarForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            mGrouRef.updateChildren(groupMessageKey);


            GroupMessageKey = mGrouRef.child(messagekey);

            HashMap<String,Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name",currentUserName);
            messageInfoMap.put("message",message);
            messageInfoMap.put("date",currDate);
            messageInfoMap.put("time",currTime);

            GroupMessageKey.updateChildren(messageInfoMap);

        }


    }

    private void GetUserDate(){

        mUserRef.child(currentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
