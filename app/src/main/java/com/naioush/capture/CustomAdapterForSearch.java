package com.naioush.capture;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class CustomAdapterForSearch extends RecyclerView.Adapter<CustomAdapterForSearch.ViewHolderList> {

    private String[] localDataSet;
    List<User> users;
Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolderList extends RecyclerView.ViewHolder {
        ImageView profileImg;
        TextView userName;
        LinearLayout l1;
        public ViewHolderList(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
l1=view.findViewById(R.id.l1);
            profileImg=view.findViewById(R.id.profileImg);
            userName=view.findViewById(R.id.userName);

        }
        public void setUserData(String userName,String profileImg)  {
            Log.e("DATA",userName+" "+profileImg);
            this.userName.setText(userName);
            Uri imgUri=Uri.parse(profileImg);
            Picasso.get().load(imgUri.toString()).into(this.profileImg);



        }

    }

    public CustomAdapterForSearch(Context context, List<User> users) {
        this.context=context;
        this.users = users;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolderList onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_user_layout, viewGroup, false);

        return new ViewHolderList(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolderList viewHolder, final int position) {
viewHolder.setUserData(users.get(position).Name,users.get(position).photo);

viewHolder.l1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(context,UserProfile.class);
        i.putExtra("userID",users.get(position).key);
        i.putExtra("Name",users.get(position).Name);
        i.putExtra("photo",users.get(position).photo);
        i.putExtra("Mobile",users.get(position).Mobile);
        i.putExtra("countryPrefix",users.get(position).countryPrefix);
        context.startActivity(i);
    }
});

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }
}
