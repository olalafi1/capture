package com.naioush.capture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
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
    SharedPreferences sp;
    private int count;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
ImageView send,profileImg,postImg,comImg;
TextView userName,title,NO_Likes,location;
EditText commentET;
RecyclerView commentsRv;
LinearLayout likeLayout;
ImageView likeHeart;
        public ViewHolder(View view) {
            super(view);
send=view.findViewById(R.id.send_comment);
commentET=view.findViewById(R.id.commentContent);
commentsRv=view.findViewById(R.id.commentsRv);
likeHeart=view.findViewById(R.id.likeHeart);
likeLayout=view.findViewById(R.id.likeLayer);
            NO_Likes=view.findViewById(R.id.NO_Likes);
            location=view.findViewById(R.id.location);
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
            Uri imgUri1=Uri.parse(profileImg);
            Picasso.get().load(imgUri.toString()).into(this.profileImg);

            FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

User u=snapshot.getValue(User.class);
                    Picasso.get().load(u.photo.toString()).into(comImg);

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });


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





        viewHolder.location.setText(posts.get(position).location);




        SharedPreferences sp = context.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
FirebaseDatabase.getInstance().getReference("Posts").child(posts.get(position).key).child("Likes").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
viewHolder.NO_Likes.setText(snapshot.getChildrenCount()+"Likes");
        for (DataSnapshot snapshot1:snapshot.getChildren())  {
    Log.e("key!@#",
            snapshot1.getKey().equals(
                    sp.getString("userkey",""))+"");

    if(snapshot1.getKey().equals(
            sp.getString("userkey",""))){
        viewHolder.likeHeart.setImageResource(R.drawable.filllike);

        viewHolder.likeHeart.setTag("liked");




    }
    else{
        viewHolder.likeHeart.setImageResource(R.drawable.likes);
        viewHolder.likeHeart.setTag("notliked");














    }

} }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }
});
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






        viewHolder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                String mDrawableName = "filllike" ;
                int resID = viewHolder.likeHeart.getResources().getIdentifier(mDrawableName , "drawable" ,context.getPackageName()) ;
                Drawable myImage = context.getResources().getDrawable(R.drawable.likes);
                Drawable myImage1 = context.getResources().getDrawable(R.drawable.filllike);
             String key=   FirebaseDatabase.getInstance().getReference("Posts").child(posts.get(position).key).child("Likes").push().getKey();


if(viewHolder.likeHeart.getTag().toString().equals("liked")){
    viewHolder.likeHeart.setImageResource(R.drawable.likes);
    viewHolder.likeHeart.setTag("notliked");
    Log.e("!@#$$$$$",viewHolder.likeHeart.getTag().toString());
    FirebaseDatabase.getInstance().getReference("Posts")
            .child(posts.get(position).key).
            child("Likes").child(sp.getString("userkey","")).removeValue();



    FirebaseDatabase.getInstance().getReference("Notification")
            .child(sp.getString("userkey","")).
          addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                  for (DataSnapshot ds:snapshot.getChildren()) {
                      Log.e("DataSnapShot",ds.getValue().toString());
                      LikeNotification LikeInfo = ds.getValue(LikeNotification.class);

try {
    if (LikeInfo.key.equals(sp.getString("userkey", ""))
            && LikeInfo.postId.equals(posts.get(position).key)) {

        FirebaseDatabase.getInstance().getReference("Notification")
                .child(posts.get(position).createdBy).child(ds.getKey()).child("status").setValue("hidden");

    }
}catch (Exception e){}
  /*              if(ds.getValue(LikeNotification.class).postId.equals(posts.get(position).key)){
                    if(ds.getValue(LikeNotification.class).userKey.equals(posts.get(position).createdBy)) {
                        FirebaseDatabase.getInstance().getReference("Notification").child(sp.getString("userkey",""))
                                .child(ds.getKey()).removeValue();





                    }

                    }
*/

                  }


              }

              @Override
              public void onCancelled(@NonNull @NotNull DatabaseError error) {

              }
          });





}
else{
    viewHolder.likeHeart.setImageResource(R.drawable.filllike);
    viewHolder.likeHeart.setTag("liked");

 FirebaseDatabase.getInstance().getReference("Posts")
            .child(posts.get(position).key).
            child("Likes").child(sp.getString("userkey","")).setValue("");


    Log.e("!@#$$",viewHolder.likeHeart.getResources()+""+resID);

    String notifKey=   FirebaseDatabase.getInstance().getReference("Notification")
            .child(sp.getString("userkey","")).push().getKey();
    FirebaseDatabase.getInstance().getReference("Notification")
            .child(posts.get(position).createdBy).child(notifKey).child("userKey").setValue(sp.getString("userkey",""));


    FirebaseDatabase.getInstance().getReference("Notification")
            .child(posts.get(position).createdBy).child(notifKey).child("postId").setValue(posts.get(position).key);

    FirebaseDatabase.getInstance().getReference("Notification")
            .child(posts.get(position).createdBy).child(notifKey).child("status").setValue("show");


    count = 0;
      /* FirebaseDatabase.getInstance().getReference("Notification")
            .child(sp.getString("userKey","")).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    for (DataSnapshot ds:snapshot.getChildren()) {


                        Log.e("done","NotDone "+ds.getValue());

                        if((ds.getValue(LikeNotification.class).userKey.equals(sp.getString("userkey","")))
                       &&( ds.getValue(LikeNotification.class).postId.equals(posts.get(position).key))){
                            String notifKey=   FirebaseDatabase.getInstance().getReference("Notification")
                                    .child(posts.get(position).createdBy).push().getKey();
                            FirebaseDatabase.getInstance().getReference("Notification")
                                    .child(posts.get(position).createdBy).child(notifKey).child("userKey").setValue(sp.getString("userkey",""));


                            FirebaseDatabase.getInstance().getReference("Notification")
                                    .child(posts.get(position).createdBy).child(notifKey).child("postId").setValue(posts.get(position).key);

                        }
                        if(count==snapshot.getChildrenCount()){

                            }

                        count++;

                    }
                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });




*/



}


                   // viewHolder.likeHeart.setImageResource(R.drawable.likes);
            }
        });




        FirebaseDatabase.getInstance().getReference().child("Users").
                child(posts.get(position).createdBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
             User u=snapshot.getValue(User.class);

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
