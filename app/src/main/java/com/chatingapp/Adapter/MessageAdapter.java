package com.chatingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chatingapp.Model.Chats;
import com.chatingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myholder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<Chats> chats;
    FirebaseUser fuser;

    public MessageAdapter(Context context,List<Chats> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MSG_TYPE_LEFT)
        {
            view  = LayoutInflater.from(context).inflate(R.layout.chat_itemview_left,parent,false);
        }
        else {
            view  = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
        }

        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {

        Chats mchats = chats.get(position);
        holder.show_message.setText(""+mchats.getMessage());

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class myholder extends RecyclerView.ViewHolder{

        TextView show_message;
        ImageView profileImage;

        public myholder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.txtMessage);
            profileImage = itemView.findViewById(R.id.img_profile);
        }
    }

    @Override
    public int getItemViewType(int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(chats.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }
}
