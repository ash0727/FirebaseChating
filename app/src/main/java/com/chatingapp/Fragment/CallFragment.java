package com.chatingapp.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chatingapp.GroupChatActivity;
import com.chatingapp.R;
import com.chatingapp.UserProfileActivtiy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CallFragment extends Fragment {
View v;
ListView list_group;
ArrayList<String> groupArraylist;
ArrayAdapter<String> arrayAdapter;
DatabaseReference GroupRef;

SubmitButton submitButton;

    public CallFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.callfragment,container,false);

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        submitButton = v.findViewById(R.id.btn_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), UserProfileActivtiy.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                },1000);


            }
        });


        Initialization();

        RetriveGroupDate();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    private void Initialization() {
        list_group = v.findViewById(R.id.list_group);

        groupArraylist = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,groupArraylist){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);

                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);

                return view;
            }
        };

        list_group.setAdapter(arrayAdapter);

        list_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                intent.putExtra("group",groupArraylist.get(i));
                startActivity(intent);
            }
        });

    }


    private void RetriveGroupDate() {

        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                groupArraylist.clear();
                groupArraylist.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
