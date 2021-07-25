package com.naioush.capture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.naioush.capture.Login.userKey;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    ArrayList<Post> posts;
    ArrayList<comments> comment;
    ArrayList<ArrayList<comments> >commentArr;
    Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
ImageView send,profileImg,postImg,comImg;
TextView userName,title;
EditText commentET;
RecyclerView commentsRv;
        public ViewHolder(View view) {
            super(view);
send=view.findViewById(R.id.send_comment);
commentET=view.findViewById(R.id.commentContent);
commentsRv=view.findViewById(R.id.commentsRv);
            profileImg=view.findViewById(R.id.profileImg);
            postImg=view.findViewById(R.id.postImg);
            comImg=view.findViewById(R.id.comImg);
            userName=view.findViewById(R.id.userName);
            title=view.findViewById(R.id.title);
            // Define click listener for the ViewHolder's View

        }
        public void setData(String postImg,String title) throws IOException {
            this.title.setText(title);

            Uri imgUri=Uri.parse(postImg);
            Picasso.get().load(imgUri.toString()).into(this.postImg);

        }

        public void setUserData(String userName,String profileImg)  {
this.userName.setText(userName);
            Uri imgUri=Uri.parse(profileImg);
            Picasso.get().load(imgUri.toString()).into(this.profileImg);
            Picasso.get().load(imgUri.toString()).into(this.comImg);

        }
        public void getTextView() {
        }
    }


    public CustomAdapter(Context context, ArrayList<Post> posts){
        this.posts =posts;
        this.context=context;
        comment=new ArrayList<>();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        java.util.Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
      String  formattedDate = df.format(c);
        try {
            viewHolder.setData(posts.get(position).photo,posts.get(position).title);
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewHolder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase FB=FirebaseDatabase.getInstance();
                DatabaseReference DBRef=FB.getReference("Posts");
                Map<String,String> comment=new HashMap<>();
                comment.put("Date",formattedDate);
                comment.put("createdBy",userKey);
                comment.put("postID",posts.get(position).key);
                comment.put("content",viewHolder.commentET.getText().toString());
                String key=      DBRef.child(posts.get(position).key).child("comments").push().getKey();
                DBRef.child(posts.get(position).key).child("comments").child(key).setValue(comment);
                Toast.makeText(context,"Posted Successfully",Toast.LENGTH_LONG).show();
                viewHolder.commentET.setText("");


            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").
                child(posts.get(position).createdBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
             Log.e("User",snapshot.getValue().toString());   User u=snapshot.getValue(User.class);
                viewHolder.setUserData(u.Name,u.photo);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        CommentsCustomAdapter adapter=new CommentsCustomAdapter(context,comment);
        viewHolder.commentsRv.setAdapter(adapter);
        viewHolder.commentsRv.setLayoutManager(new LinearLayoutManager(context));
        adapter.notifyDataSetChanged();





        FirebaseDatabase.getInstance().getReference().child("Posts").
                child(posts.get(position).key).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                comment.clear();
                for (DataSnapshot data:snapshot.getChildren()){

                    comment.add(data.getValue(comments.class));

                    adapter.notifyDataSetChanged();

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });









    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }
}
