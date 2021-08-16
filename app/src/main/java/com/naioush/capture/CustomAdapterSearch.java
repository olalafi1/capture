package com.naioush.capture;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.naioush.capture.Login.userKey;

public class CustomAdapterSearch extends RecyclerView.Adapter<CustomAdapterSearch.ViewHolder> {
    ArrayList<User> Users;
  Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
ImageView profileImg;
TextView userName;

        public ViewHolder(View view) {
            super(view);

            profileImg=view.findViewById(R.id.profileImg);
            userName=view.findViewById(R.id.userName);
            // Define click listener for the ViewHolder's View

        }


        public void setUserData(String userName,String profileImg)  {
            Log.e("DATA",userName+" "+profileImg);
this.userName.setText(userName);
            Uri imgUri=Uri.parse(profileImg);
            Picasso.get().load(imgUri.toString()).into(this.profileImg);



        }
        public void getTextView() {
        }
    }


    public CustomAdapterSearch(Context context, ArrayList<User> Users){
        this.Users =Users;
        this.context=context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_user_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {


viewHolder.setUserData(Users.get(position).Name,Users.get(position).photo);










    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 0;
    }
}
