package com.e.fragmentwithrecycleview.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.e.fragmentwithrecycleview.Model.Contact;
import com.e.fragmentwithrecycleview.Model.sharedpref;
import com.e.fragmentwithrecycleview.R;

import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.MyViewHolder> {

    Context context;
    List<Contact> mData;
    Dialog myDialog;
    static int pReqCode = 1;
    static int REQUESTCODE= 1;
    sharedpref sharedpref;

    public RecylerViewAdapter(Context context, List<Contact> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        sharedpref = new sharedpref(context);

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_contect,parent,false);
        MyViewHolder holder = new MyViewHolder(v);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.txt_conName.setText(mData.get(position).getName());
        holder.txt_conphone.setText(mData.get(position).getPhone());
        holder.img_contect.setImageResource(mData.get(position).getPhoto());

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.dialog_contact);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        holder.lnr_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedpref.cleashared();
                TextView txt_diaName = (TextView) myDialog.findViewById(R.id.txt_diaName);
                TextView txt_diaPhone = (TextView) myDialog.findViewById(R.id.txt_diaPhone);

                ImageView img_diaProfile = (ImageView) myDialog.findViewById(R.id.img_diaProfile);

                txt_diaName.setText(mData.get(holder.getAdapterPosition()).getName());
                txt_diaPhone.setText(mData.get(holder.getAdapterPosition()).getPhone());

                img_diaProfile.setImageResource(mData.get(holder.getAdapterPosition()).getPhoto());
                myDialog.show();
            }
        });

//        holder.img_contect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(Build.VERSION.SDK_INT >= 200){
//
//                    checkandrequestforpermission();
//
//                }
//                else {
//                    opengallery();
//                }
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_conName,txt_conphone;
        private ImageView  img_contect;
        private LinearLayout lnr_parent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_conName = (TextView) itemView.findViewById(R.id.txt_conName);
            txt_conphone = (TextView) itemView.findViewById(R.id.txt_conphone);

            img_contect = (ImageView) itemView.findViewById(R.id.img_contect);

            lnr_parent = (LinearLayout) itemView.findViewById(R.id.lnr_parent);

        }
    }


//    private void opengallery() {
//
//        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
//        gallery.setType("image/*");
//        ((Activity) context).startActivityForResult(gallery,REQUESTCODE);
//
//    }
//
//    private void checkandrequestforpermission() {
//
//        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
//
//            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,Manifest.permission.READ_EXTERNAL_STORAGE)){
//
//            }
//
//            else {
//                ActivityCompat.requestPermissions((Activity) context,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},pReqCode
//                );
//            }
//
//        }
//        else {
//            opengallery();
//        }
//
//
//    }



}
