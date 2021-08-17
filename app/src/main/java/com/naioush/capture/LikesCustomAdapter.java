package com.naioush.capture;

import android.annotation.SuppressLint;
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

public class LikesCustomAdapter extends RecyclerView.Adapter<LikesCustomAdapter.ViewHolder> {
    ArrayList<LikeNotification> Likes;

Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
ImageView profileImg,postImg;
TextView title;

        public ViewHolder(View view) {
            super(view);

            profileImg=view.findViewById(R.id.profileImg);
            postImg=view.findViewById(R.id.postImg);

            title=view.findViewById(R.id.title);
            // Define click listener for the ViewHolder's View

        }


        public void setUserData(String photo,String Name)  {
            Uri imgUri=Uri.parse(photo);
            Picasso.get().load(imgUri.toString()).into(this.profileImg);

        title.setText(Name+" Likes your Post");

        }
        public void getTextView() {
        }

        public void setPostPhoto(String photo) {

            Uri imgUri=Uri.parse(photo);
            Picasso.get().load(imgUri.toString()).into(this.postImg);

        }
    }


    public LikesCustomAdapter(Context context, ArrayList<LikeNotification> Likes){
        this.Likes =Likes;
        this.context=context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.likelayout_rc, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {









        SharedPreferences sp = context.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);









        FirebaseDatabase.getInstance().getReference("Users").child(Likes.get(position).userKey)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
User u=snapshot.getValue(User.class);
viewHolder.setUserData(u.photo,u.Name);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        FirebaseDatabase.getInstance().getReference("Posts").child(Likes.get(position).postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Post p=snapshot.getValue(Post.class);
                        viewHolder.setPostPhoto(p.photo);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });











    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return Likes.size();
    }
}
