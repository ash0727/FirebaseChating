package com.e.fragmentwithrecycleview.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.fragmentwithrecycleview.Adapter.RecylerViewAdapter;
import com.e.fragmentwithrecycleview.Model.Contact;
import com.e.fragmentwithrecycleview.Model.sharedpref;
import com.e.fragmentwithrecycleview.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ContectFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    List<Contact> contacts;

    static int pReqCode = 1;
    static int REQUESTCODE= 1;
        Uri picture;
    CircleImageView imgHead;
    sharedpref sharedprefl;

    public ContectFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contectfragment,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recylerview);
        sharedprefl = new sharedpref(getContext());
        imgHead = (CircleImageView) v.findViewById(R.id.imgHead);

        if(sharedprefl.getImgaes() != null){
            Uri image = Uri.parse(sharedprefl.getImgaes());
            imgHead.setImageURI(image);
        }

        imgHead.setOnClickListener(new View.OnClickListener() {
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

        RecylerViewAdapter recylerViewAdapter = new RecylerViewAdapter(getContext(),contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recylerViewAdapter);
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contacts = new ArrayList<>();

        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));
        contacts.add(new Contact("Asif","(111) 253534345",R.drawable.ic_person_black_24dp));

    }

    private void opengallery() {

        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,REQUESTCODE);

    }

    private void checkandrequestforpermission() {

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(getContext(), "Please accept for required permission ", Toast.LENGTH_SHORT).show();

            }

            else {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},pReqCode
                );
            }

        }
        else {
            opengallery();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            picture = data.getData();
            sharedprefl.setImgaes(picture.toString());
            imgHead.setImageURI(picture);
        }


    }
}
