package com.naioush.capture.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.naioush.capture.R;
import com.naioush.capture.chat.Firebase.MyFirebaseProvider;
//import com.naioush.capture.utils.TimeAgo;
import com.naioush.capture.chat.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.ChatViewHolder> {
    List<Chat> chatList;
    Context context;
    private boolean isArabic;
    String url;

    public MyAdapter(String url, Context context, List<Chat> chatList, boolean isArabic) {
        this.url=url;
        this.chatList = chatList;
        this.context = context;
        this.isArabic = isArabic;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.row_chat, parent, false);
        return new ChatViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {
        final Chat chat = chatList.get(position);
        holder.SetData(chat);
        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(holder.itemView.getContext(), ChatActivity.class);
//             intent.putExtra("chat", chat);
//             holder.itemView.getContext().startActivity(intent);



            Bundle bundle = new Bundle();


//
            Map<String, Object> data1=new HashMap<>();
            data1.put("id",chat.getId());
            data1.put("supplier",chat.getUser1());
            data1.put("customer",chat.getUser2());

            MyFirebaseProvider.getChildDatabaseReference("chats").
                    child(chat.getId()).updateChildren(data1);
            MyFirebaseProvider.getChildDatabaseReference("users").
                    child(chat.getUser1().key).child(chat.getId()).updateChildren(data1);


            MyFirebaseProvider.getChildDatabaseReference("users").
                    child(chat.getUser2().key+"").child(chat.getId()).updateChildren(data1);

            bundle.putParcelable("chat", chat);
            Intent intent = new Intent(context, SendOffActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
            Bungee.swipeRight(context);
        });
    }



    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView chatimage;
        TextView chatname, chatlastDate, lastmessage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            chatimage = itemView.findViewById(R.id.chatimage);
            chatname = itemView.findViewById(R.id.chatname);
            chatlastDate = itemView.findViewById(R.id.chatlastDate);
            lastmessage = itemView.findViewById(R.id.lastmessage);
        }

        public void SetData(Chat chat) {
            //using picaso chatimage
            try{
//                String timeAge = new TimeAgo(itemView.getContext(), isArabic).getTimeAgo(new Date(chat.getChatlastDate()));
//                chatlastDate.setText(timeAge);
                lastmessage.setText(chat.getLastMessage());
//                Utils.loadImage(itemView.getContext(),url+ chat.getSupplier().getImage(), chatimage);
//                chatname.setText(chat.getSupplier().getName());
            }catch (Exception e){}
         }
    }


}
