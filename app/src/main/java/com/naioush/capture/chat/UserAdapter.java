package com.naioush.capture.chat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naioush.capture.R;
import com.naioush.capture.User;

import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH> {




    List<User> userList;

    public UserAdapter( List<User> userList) {


        this.userList=userList;


    }

    @NonNull
    @Override
    public UserAdapter.UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row , parent, false);
        return new UserAdapter.UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserVH holder, int position) {
        holder.setData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class  UserVH extends RecyclerView.ViewHolder{

        ImageView userImage ;


        public UserVH(@NonNull View itemView) {
            super(itemView);

            userImage= itemView.findViewById(R.id.userImage);




        }

        public void setData(User data) {

            Utils.loadImage(itemView.getContext(),data.photo,userImage);


            itemView.setOnClickListener(v->{
//                Intent intent = new Intent(itemView.getContext(), ShowProductActivity.class);
//                intent.putExtra("productID",data.getProductId());
//                intent.putExtra("offerId",data.getId());
//                intent.putExtra("offerPrice",data.getPriceAfterDicount());
//                intent.putExtra("isOffer",true);
//                itemView.getContext().startActivity(intent);
//                Bungee.swipeRight(itemView.getContext());

                Toast.makeText(itemView.getContext(), data.Name+" *** ", Toast.LENGTH_SHORT).show();

            });


        }
    }



}
