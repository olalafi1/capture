package com.naioush.capture;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdaptershowFollowing extends RecyclerView.Adapter<CustomAdaptershowFollowing.ViewHolder> {
    ArrayList<User> users;
ArrayList<User>selecteduser=new ArrayList<>();

Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
ImageView profileImg;
TextView name;
CheckBox checkBox;
        public ViewHolder(View view) {
            super(view);

            profileImg=view.findViewById(R.id.profileImg);

            name=view.findViewById(R.id.name);
            checkBox=view.findViewById(R.id.checkbox);
            // Define click listener for the ViewHolder's View

        }


        public void setUserData(String photo,String Name)  {
            Uri imgUri=Uri.parse(photo);
            Picasso.get().load(imgUri.toString()).into(this.profileImg);

        name.setText(Name);

        }
        public void getTextView() {
        }


    }


    public CustomAdaptershowFollowing(Context context, ArrayList<User> users){
        this.users =users;
        this.context=context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.following, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
viewHolder.setUserData(users.get(position).photo,users.get(position).Name);
        SharedPreferences sp = context.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);


viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
     if(isChecked){
         selecteduser.add(users.get(position));

     }
     else{
         selecteduser.remove(users.get(position));
     }
    }
});










    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }
}
