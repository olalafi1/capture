package com.naioush.capture.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naioush.capture.R;
import com.naioush.capture.User;
import com.naioush.capture.chat.Firebase.MyFirebaseProvider;
import com.naioush.capture.databinding.ActivitySendOffBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SendOffActivity extends AppCompatActivity {
    StorageReference mImageStorage;
    ProgressDialog mProgressDialog;

    FirebaseUser user;
    FirebaseAuth auth;
    final List<Chat> chatList = new ArrayList<>();
    List<Message> messageList;
    SendOffActivity.MyAdapter myAdapter;
    RecyclerView messagrs_rv;
    Chat chat;
    String chatImage;
    String title;
    ImageView sendMessage, sendImage;
    EditText messageText;
    String secureImageURI;
    private Toolbar toolbar;
    private String chatId="";

    List<User> usersList =new ArrayList<>();
    UserAdapter adapter;
    RecyclerView usersRV;
    EditText messageET;
    SharedPreferences sp;
    ActivitySendOffBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySendOffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

      auth=FirebaseAuth.getInstance();
      user = auth.getCurrentUser();
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);

        System.out.println(user.getUid()+" HANOOD");

        mImageStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        messagrs_rv = findViewById(R.id.messagrs_rv);
        messageList = new ArrayList<>();
        myAdapter = new MyAdapter(SendOffActivity.this, messageList, chatId, chatImage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messagrs_rv.setLayoutManager(linearLayoutManager);
        messagrs_rv.setLayoutManager(linearLayoutManager);
        messagrs_rv.setItemAnimator(new DefaultItemAnimator());
        setInitialData();


      /*    chat=new Chat();
        Bundle extras = getIntent().getExtras();
        chat = (Chat) extras.getParcelable("chat");


        chatImage = chat.getUser1().photo;
        title = chat.getUser1().Name;
        chatId = chat.getId();

*/
        //TODO INIT UI

//        initUI();


        messageET = findViewById(R.id.messageText);
        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(messageET.getText().toString().isEmpty()){
                    binding.typeMessageArea.sendRecord.setVisibility(View.VISIBLE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.GONE);
                }else{
                    binding.typeMessageArea.sendRecord.setVisibility(View.GONE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(messageET.getText().toString().isEmpty()){
                    binding.typeMessageArea.sendRecord.setVisibility(View.VISIBLE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.GONE);
                }else{
                    binding.typeMessageArea.sendRecord.setVisibility(View.GONE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(messageET.getText().toString().isEmpty()){
                    binding.typeMessageArea.sendRecord.setVisibility(View.VISIBLE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.GONE);
                }else{
                    binding.typeMessageArea.sendRecord.setVisibility(View.GONE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.typeMessageArea.sendMessage.setOnClickListener(v->{
            Toast.makeText(this, "Message  Sent ", Toast.LENGTH_SHORT).show();
            String text = binding.typeMessageArea.messageText.getText().toString().trim();
            if (text.length() == 0) return;
                //73hoAK57oPewUA30Krao9CygCmc2
            //chat.getUser1().key
            //KncqA9FCDMf0oP5HZE20tcUxXnI3
            //auth.getCurrentUser().getUid()
            Message message = new Message("-1", new Date().getTime(), "KncqA9FCDMf0oP5HZE20tcUxXnI3",  ("73hoAK57oPewUA30Krao9CygCmc2")  ,  null, text);
            MyFirebaseProvider.DoSendMessage(message, chatId, (result, key) -> {
                if (result)  binding.typeMessageArea.messageText.setText("");

                });

        });


        binding.typeMessageArea.sendRecord.setOnClickListener(v->{
            Toast.makeText(this, "Record  Sent ", Toast.LENGTH_SHORT).show();

        });


        binding.typeMessageArea.emoji.setOnClickListener(v->{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.typeMessageArea.messageText, InputMethodManager.SHOW_IMPLICIT);
        });

        binding.typeMessageArea.attachFile.setOnClickListener(v->{
            Toast.makeText(this, "File  Sent ", Toast.LENGTH_SHORT).show();
        });

        binding.typeMessageArea.sendImage.setOnClickListener(v->{
            Toast.makeText(this, "Image  Sent ", Toast.LENGTH_SHORT).show();
        });

        usersRV= findViewById(R.id.usersRV);
        adapter= new UserAdapter(usersList);

        usersRV.setAdapter(adapter);
        loadUsers();
       try {
           String chatId = chatList.get(chatList.size() - 1).id;

           MyFirebaseProvider.getChildDatabaseReference("chats").
                   child(chatId).child("Messages").addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                   Message message = dataSnapshot.getValue(Message.class);
                   String mUid = message.getUid() + "";
                   if (mUid.equals(auth.getCurrentUser().getUid())) {
                       Message1 message1 = new Message1(message.getId(), message.getCreatedAt(), message.getUid(), message.getTo(), message.getImageUrl(), message.getMsg());
                       message1.setImageUrl(message.getImageUrl());
                       messageList.add(message1);

                   } else {
                       Message2 message2 = new Message2(message.getId(), message.getCreatedAt(), message.getUid(), message.getTo(), message.getImageUrl(), message.getMsg());
                       message2.setImageUrl(message.getImageUrl());
                       messageList.add(message2);
                   }


                   myAdapter.notifyItemInserted(messageList.size());
                   messagrs_rv.scrollToPosition(messageList.size() - 1);
               }

               @Override
               public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                   try {
                       Message message = dataSnapshot.getValue(Message.class);
                       int pos = messageList.indexOf(message);
                       messageList.remove(message);
                       messageList.add(pos, message);
                       // boolean x =  messageList.add(dataSnapshot.getValue(Message.class));
                       myAdapter.notifyDataSetChanged();
                       messagrs_rv.scrollToPosition(pos);
                       Toast.makeText(SendOffActivity.this, "Edit  message " + pos, Toast.LENGTH_SHORT).show();
                   } catch (Exception e) {

                   }

               }

               @Override
               public void onChildRemoved(DataSnapshot dataSnapshot) {
                   boolean x = messageList.remove(dataSnapshot.getValue(Message.class));
                   myAdapter.notifyDataSetChanged();
                   messagrs_rv.scrollToPosition(messageList.size() - 1);
                   Toast.makeText(SendOffActivity.this, "Delete message " + x, Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onChildMoved(DataSnapshot dataSnapshot, String s) {
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
       }catch (Exception e){
           System.out.println("No available chats");
       }


    }


    public void setInitialData() {
        chatList.clear();
        //auth.getCurrentUser().getUid()
        MyFirebaseProvider.getChildDatabaseReference("Users").child("KncqA9FCDMf0oP5HZE20tcUxXnI3").child("chats").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatList.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Chat c = snapshot1.getValue(Chat.class);
                            chatList.add(c);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void loadUsers() {

        usersList.clear();
        MyFirebaseProvider.getChildDatabaseReference("Users").
               addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                User user= snapshot.getValue(User.class);


                    usersList.add(user);


                    adapter.notifyDataSetChanged();



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

    }


   /* private void initUI() {
        toolbar = findViewById(R.id.chatToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        findViewById(R.id.back1).setOnClickListener(v -> finish());

//        final CircleImageView chatHeaderImage = findViewById(R.id.chatHeaderImage);
//        Picasso.get().load(chatImage).networkPolicy(NetworkPolicy.OFFLINE).
//                into(chatHeaderImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.d("aaa", chatImage);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                        Picasso.get().load(chatImage).into(chatHeaderImage);
//                    }
//                });
//
//        TextView titleOfChat = findViewById(R.id.titleOfChat);
//        titleOfChat.setText(title);

        messagrs_rv = findViewById(R.id.messagrs_rv);
        messageList = new ArrayList<>();
        myAdapter = new MyAdapter(SendOffActivity.this, messageList, chatId, chatImage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messagrs_rv.setLayoutManager(linearLayoutManager);
        messagrs_rv.setLayoutManager(linearLayoutManager);
        messagrs_rv.setItemAnimator(new DefaultItemAnimator());


        messageText = findViewById(R.id.messageText);
        messageText.setOnClickListener(v -> {
            myAdapter.notifyDataSetChanged();
            messagrs_rv.scrollToPosition(messageList.size() - 1);
            messagrs_rv.scrollToPosition(messageList.size());
        });

        sendMessage = findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(v -> {
            String text = messageText.getText().toString().trim();
            if (text.length() == 0) return;

            Message message = new Message("-1", new Date().getTime(), auth.getCurrentUser().getUid(),  (chat.getUser1().key)  ,  null, text);
            MyFirebaseProvider.DoSendMessage(message, chatId, (result, key) -> {
                if (result) messageText.setText("");
            });
        });


        sendImage = findViewById(R.id.sendImage);
        sendImage.setOnClickListener(v -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).setMinCropWindowSize(500, 500) //to be square
                .start(SendOffActivity.this));


        messagrs_rv.setAdapter(myAdapter);

    }

*/
    //TODO HELLLL

    /*
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(chatId+auth.getCurrentUser().getUid());
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseMessaging.getInstance().subscribeToTopic(chatId+auth.getCurrentUser().getUid());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressDialog.setTitle("uplod your Image");
                mProgressDialog.setMessage("please wait while sending your  image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();
                File thumpFilePath = new File(resultUri.getPath());

                Bitmap thum_bitmap = null;
                try {
                    thum_bitmap = new Compressor(this).
                            setMaxHeight(500)
                            .setMaxWidth(500)
                            .setQuality(90)
                            .compressToBitmap(thumpFilePath);


                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thum_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] dataBytes = baos.toByteArray();
                final long randomNumber = new Date().getTime();


                final StorageReference secure_thump_filePath = mImageStorage.
                        child("uploadImage").child("thump_file")
                        .child(auth.getCurrentUser().getUid() + randomNumber + ".jpg");
                String text = messageText.getText().toString().trim();
                final Message message = new Message("-1", new Date().getTime(), auth.getCurrentUser().getUid(),  chat.getUser1().key, null, text);
                messageText.setText("");
                try {
                    UploadTask uploadTask;
                    uploadTask = secure_thump_filePath.putBytes(dataBytes);
                    Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return secure_thump_filePath.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            secureImageURI = downloadUri.toString();
                            message.setImageUrl(secureImageURI);
                            MyFirebaseProvider.DoSendMessage(message, chatId, (result1, key) -> {
                                if (result1) ;
                            });
                        } else {
                            // Handle failures
                            // ...
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
                mProgressDialog.dismiss();
            } else
                Toast.makeText(SendOffActivity.this, "fail sent  your image !!! ", Toast.LENGTH_SHORT).show();
        }
    }

*/
    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Message> messageList;
        Context context;
        String chatId;
        String chatImage;

        public MyAdapter(Context context, List<Message> messageList, String chatId, String chatImage) {
            this.messageList = messageList;
            this.context = context;
            this.chatId = chatId;
            this.chatImage = chatImage;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            View view;
            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(context).inflate(R.layout.message_row1, parent, false);
                    return new SendOffActivity.MyAdapter.MessageViewHolder1(view);
                case 1:
                    view = LayoutInflater.from(context).inflate(R.layout.message_row2, parent, false);
                    return new SendOffActivity.MyAdapter.MessageViewHolder2(view);

            }
            return null;
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


            Message m = messageList.get(position);


            if (m instanceof Message1)
                ((SendOffActivity.MyAdapter.MessageViewHolder1) holder).setData(messageList.get(position));
            else
                ((SendOffActivity.MyAdapter.MessageViewHolder2) holder).setDate(messageList.get(position), chatImage);
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }


        @Override
        public int getItemViewType(int position) {
            if (messageList == null) return 0;

            Message object = messageList.get(position);
            if (object instanceof Message1)
                return 0;
            else
                return 1;

        }


        class MessageViewHolder1 extends RecyclerView.ViewHolder {
            TextView textView;
            ImageView messageImage, imageView2;
            TextView timeTv;

            public MessageViewHolder1(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textViewMessage1);
//                messageImage = itemView.findViewById(R.id.messageImage);
//                timeTv = itemView.findViewById(R.id.timeTv);
//                imageView2 = itemView.findViewById(R.id.imageView3);
            }

            public void setData(final Message message) {

//                String timeAge = new TimeAgo(itemView.getContext(), LocaleChanger.getLocale().toString().equals("ar")).getTimeAgo(new Date(message.getCreatedAt()));
//                timeTv.setText(timeAge);

                    if (message.getMsg().length() == 0)
                        textView.setVisibility(View.GONE);
                else
                    textView.setText(message.getMsg());  // de encrypt or decrypt !!

                Utils.loadImage(itemView.getContext(), message.getImageUrl(), imageView2);


                if (message.getImageUrl() != null) {
                    messageImage.setVisibility(View.VISIBLE);
                    Utils.loadImage(itemView.getContext(), message.getImageUrl(), messageImage);
                    messageImage.setOnClickListener(v -> Log.d("TestImage", message.getImageUrl()));
                }
            }
        }


        class MessageViewHolder2 extends RecyclerView.ViewHolder {
            TextView textView, timeTv;
            ImageView imageView2, messageImage;
            View itemView;

            public MessageViewHolder2(View itemView) {
                super(itemView);
                this.itemView = itemView;
                textView = itemView.findViewById(R.id.textViewMessage2);
//                imageView2 = itemView.findViewById(R.id.imageView3);
//                messageImage = itemView.findViewById(R.id.messageImage);
//                timeTv = itemView.findViewById(R.id.timeTv);
            }

            public void setDate(final Message message, final String chatImage) {

                if (message.getMsg().length() == 0)
                    textView.setVisibility(View.GONE);
                else
                    textView.setText(message.getMsg());  // de encrypt or decrypt !!
                //picasso Code

//                String timeAge = new TimeAgo(itemView.getContext(), LocaleChanger.getLocale().toString().equals("ar")).getTimeAgo(new Date(message.getCreatedAt()));
//                timeTv.setText(timeAge);

                if (message.getImageUrl() != null) {
                    messageImage.setVisibility(View.VISIBLE);
                    Utils.loadImage(itemView.getContext(), message.getImageUrl(), messageImage);
                    messageImage.setOnClickListener(v -> {

                    });


                }


                Utils.loadImage(itemView.getContext(), chatImage, imageView2);


            }

        }


    }

}