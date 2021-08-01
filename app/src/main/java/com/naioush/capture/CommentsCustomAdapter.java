package com.naioush.capture;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.naioush.capture.Login.userKey;

public class CommentsCustomAdapter extends RecyclerView.Adapter<CommentsCustomAdapter.ViewHolder> {
    ArrayList<comments> comment;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
TextView userName,content;
ImageView userImg;


public ViewHolder(View view) {
            super(view);
    userName=view.findViewById(R.id.userName);
    content=view.findViewById(R.id.content);
    userImg=view.findViewById(R.id.profileImg);
            // Define click listener for the ViewHolder's View

        }
public void setData(String content){

this.content.setText(content);
}
        public void getTextView() {
        }
        private void setPhoto(String photoURL,String userName) {
           try{ Uri imgUri=Uri.parse(photoURL);

            Picasso.get().load(imgUri.toString()).into(this.userImg);
this.userName.setText(userName);


           }catch (Exception e){}
        }
    }


    public CommentsCustomAdapter(Context context, ArrayList<comments>comment) {
        this.comment=comment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comments, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setData( comment.get(position).content);

        FirebaseDatabase.getInstance().getReference("Users").child(comment.get(position).createdBy).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        User u=snapshot.getValue(User.class);
        Log.e("NAMEUSER#",u.Name);
         viewHolder.setPhoto( u.photo,u.Name);
    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }
});

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return comment.size();
    }
}
