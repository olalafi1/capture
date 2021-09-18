package com.naioush.capture.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaTimestamp;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SendOffActivity extends AppCompatActivity {
    StorageReference mImageStorage;
    StorageReference mAudioStorage;
    ProgressDialog mProgressDialog;

    FirebaseUser user;
    FirebaseAuth auth;
    //    final List<Chat> chatList = new ArrayList<>();
    List<Message> messageList;
    SendOffActivity.MyAdapter myAdapter;
    RecyclerView messagrs_rv;
    Chat chat;
    String chatImage;
    String title;
    ImageView sendMessage, sendImage;
    EditText messageText;
    String secureImageURI;
    String secureAudioURI;
    private Toolbar toolbar;
    private String chatId = "";
    private String receiverID;

    List<User> usersList = new ArrayList<>();
    List<UserChats> userChatsList = new ArrayList<>();


    UserAdapter adapter;
    RecyclerView usersRV;
    EditText messageET;
    SharedPreferences sp;
    ActivitySendOffBinding binding;


    //TODO Record Audio Section


    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    private int flag1 = 0;


    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(SendOffActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(SendOffActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SendOffActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


    //TODO Finished  Record Requirements Section


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendOffBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        random = new Random();

        mImageStorage = FirebaseStorage.getInstance().getReference();
        mAudioStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        messagrs_rv = findViewById(R.id.messagrs_rv);
        messageList = new ArrayList<>();
        myAdapter = new MyAdapter(SendOffActivity.this, messageList, chatId, chatImage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messagrs_rv.setLayoutManager(linearLayoutManager);
        messagrs_rv.setLayoutManager(linearLayoutManager);
        messagrs_rv.setItemAnimator(new DefaultItemAnimator());

        messagrs_rv.setAdapter(myAdapter);
        sp = getSharedPreferences("loginSaved", Context.MODE_PRIVATE);


        loadUsers();
        setInitialData();


        initUI();


    }


    private void updateData() {

        if (chatId.isEmpty()) {
            chatId = new Date().getTime() + "";
        }
        if (receiverID == null) {
            return;
        }

//        MyFirebaseProvider.getChildDatabaseReference("chats").
//                child(chatId).child("user") .setValue(sp.getString("userkey",""));

        MyFirebaseProvider.getChildDatabaseReference("Users").
                child(sp.getString("userkey", "")).child("chats").child(chatId).child("user").setValue(sp.getString("userkey", ""));

        MyFirebaseProvider.getChildDatabaseReference("Users").
                child(sp.getString("userkey", "")).child("chats").child(chatId).child("chatId").setValue(chatId);

        MyFirebaseProvider.getChildDatabaseReference("Users").
                child(receiverID).child("chats").child(chatId).child("user").setValue(sp.getString("userkey", ""));

        MyFirebaseProvider.getChildDatabaseReference("Users").
                child(receiverID).child("chats").child(chatId).child("chatId").setValue(chatId);
        loadLastChat();
    }

    public void setInitialData() {
        userChatsList.clear();
        //
        MyFirebaseProvider.getChildDatabaseReference("Users").child(sp.getString("userkey", "")).child("chats").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userChatsList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            UserChats c1 = snapshot1.getValue(UserChats.class);
                            userChatsList.add(c1);
                        }
                        if (userChatsList.size() != 0) {
                            chatId = userChatsList.get(userChatsList.size() - 1).getChatId();
                        }

                        updateData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void initUI() {
        messageET = findViewById(R.id.messageText);
        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (messageET.getText().toString().isEmpty()) {
                    binding.typeMessageArea.sendRecord.setVisibility(View.VISIBLE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.GONE);
                } else {
                    binding.typeMessageArea.sendRecord.setVisibility(View.GONE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (messageET.getText().toString().isEmpty()) {
                    binding.typeMessageArea.sendRecord.setVisibility(View.VISIBLE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.GONE);
                } else {
                    binding.typeMessageArea.sendRecord.setVisibility(View.GONE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (messageET.getText().toString().isEmpty()) {
                    binding.typeMessageArea.sendRecord.setVisibility(View.VISIBLE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.GONE);
                } else {
                    binding.typeMessageArea.sendRecord.setVisibility(View.GONE);
                    binding.typeMessageArea.sendMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.typeMessageArea.sendMessage.setOnClickListener(v -> {
            Toast.makeText(this, "Message  Sent ", Toast.LENGTH_SHORT).show();
            String text = binding.typeMessageArea.messageText.getText().toString().trim();
            if (text.length() == 0) return;
            System.out.println(chatId + " ***-***");
            if (chatId.isEmpty()) {
                chatId = new Date().getTime() + "";
            }
            Message message = new Message("-1", new Date().getTime(), sp.getString("userkey", ""), (receiverID), null, text);
            MyFirebaseProvider.DoSendMessage(message, chatId, (result, key) -> {
                if (result) binding.typeMessageArea.messageText.setText("");

            });

        });


        binding.typeMessageArea.sendRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1 ==0){
                    if(checkPermission()) {

                        AudioSavePathInDevice =
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                        CreateRandomAudioFileName(5) + "AudioRecording.mp3";
                        System.out.println(AudioSavePathInDevice);
                        MediaRecorderReady();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            binding.recordAnim.setVisibility(View.VISIBLE);
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        flag1=1;

                        Toast.makeText(SendOffActivity.this, "Recording started",
                                Toast.LENGTH_LONG).show();
                    } else {
                        requestPermission();
                    }

                }else if(flag1 == 1 ) {
                    mediaRecorder.stop();

                    binding.recordAnim.setVisibility(View.GONE);
                    String text = binding.typeMessageArea.messageText.getText().toString().trim();
                    final Message message = new Message("-1", new Date().getTime(), sp.getString("userkey", ""), receiverID, null, text);
                    binding.typeMessageArea.messageText.setText("");

                    StorageReference filePath = mAudioStorage.child("audio")
                            .child(new Date().getTime() + "");
                    Uri uri = Uri.fromFile(new File(AudioSavePathInDevice));

                    try {
                        UploadTask uploadTask;
                        uploadTask = filePath.putFile(uri);
                        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return filePath.getDownloadUrl();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                secureAudioURI = downloadUri.toString();
                                System.out.println(secureAudioURI);
                                message.setAudioUrl(secureAudioURI);
                                MyFirebaseProvider.DoSendMessage(message, chatId, (result1, key) -> {
                                    if (result1) ;
                                });
                            } else {
                                // Handle failures
                                // ...
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }}});


        binding.typeMessageArea.emoji.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.typeMessageArea.messageText, InputMethodManager.SHOW_IMPLICIT);
        });

        binding.typeMessageArea.attachFile.setOnClickListener(v -> {
            Toast.makeText(this, "File  Sent ", Toast.LENGTH_SHORT).show();
        });

        binding.typeMessageArea.sendImage.setOnClickListener(v -> CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).setMinCropWindowSize(500, 500) //to be square
                .start(SendOffActivity.this));

        usersRV = findViewById(R.id.usersRV);

        adapter = new UserAdapter(usersList, new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(String userKey) {
                receiverID = userKey;
                Toast.makeText(SendOffActivity.this, receiverID + " HANOOD", Toast.LENGTH_SHORT).show();
            }
        });

        usersRV.setAdapter(adapter);
    }

    private void loadLastChat() {
        messageList.clear();
        try {

            String chatId = userChatsList.get(userChatsList.size() - 1).getChatId();
            if (chatId != null) {
                MyFirebaseProvider.getChildDatabaseReference("chats").
                        child(chatId).child("Messages").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Message message = dataSnapshot.getValue(Message.class);
                        String mUid = message.getUid() + "";
                        if (mUid.equals(sp.getString("userkey", ""))) {
                            Message1 message1 = new Message1(message.getId(), message.getCreatedAt(), message.getUid(), message.getTo(), message.getImageUrl(), message.getMsg());
                            message1.setImageUrl(message.getImageUrl());
                            message1.setAudioUrl(message.getAudioUrl());
                            messageList.add(message1);
                            System.out.println(message1.msg);

                        } else {
                            Message2 message2 = new Message2(message.getId(), message.getCreatedAt(), message.getUid(), message.getTo(), message.getImageUrl(), message.getMsg());
                            message2.setImageUrl(message.getImageUrl());
                            message2.setAudioUrl(message.getAudioUrl());
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
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }


    private void loadUsers() {

        usersList.clear();
        MyFirebaseProvider.getChildDatabaseReference("Users").
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//                        usersList.clear();
                        User user = snapshot.getValue(User.class);

                        if (user.key.equals(sp.getString("userkey", ""))) {
                            System.out.println("The user is already exist");
                        } else {

                            usersList.add(user);
                            if (usersList.size() != 0) {
                                receiverID = usersList.get(0).key;
                                System.out.println("Here");
                            }

                        }
                        System.out.println(usersList.size() + " Heloo");
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

    @Override
    protected void onResume() {
        super.onResume();
        if (chatId != null)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(chatId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (chatId != null)
            FirebaseMessaging.getInstance().subscribeToTopic(chatId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
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
                    e.printStackTrace();
                }


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thum_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] dataBytes = baos.toByteArray();
                final long randomNumber = new Date().getTime();


                final StorageReference secure_thump_filePath = mImageStorage.
                        child("uploadImage").child("thump_file")
                        .child(sp.getString("userkey", "") + randomNumber + ".jpg");
                String text = binding.typeMessageArea.messageText.getText().toString().trim();
                final Message message = new Message("-1", new Date().getTime(), sp.getString("userkey", ""), receiverID, null, text);
                binding.typeMessageArea.messageText.setText("");
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
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
            } else
                Toast.makeText(SendOffActivity.this, "fail sent  your image !!! ", Toast.LENGTH_SHORT).show();
        }
    }

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
            ImageView messageImage, imageView2,start_message,pause_message;
            TextView timeTv;
            View audio_message;
            SeekBar seekBar;
            public MessageViewHolder1(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textViewMessage1);
                messageImage = itemView.findViewById(R.id.messageImage);

                audio_message= itemView.findViewById(R.id.audio_message);
                start_message= itemView.findViewById(R.id.start_message);
                pause_message= itemView.findViewById(R.id.pause_message);
                seekBar= itemView.findViewById(R.id.seekBar);
//                timeTv = itemView.findViewById(R.id.timeTv);
                imageView2 = itemView.findViewById(R.id.imageView3);
            }

            @RequiresApi(api = Build.VERSION_CODES.P)
            public void setData(final Message message) {

//                String timeAge = new TimeAgo(itemView.getContext(), LocaleChanger.getLocale().toString().equals("ar")).getTimeAgo(new Date(message.getCreatedAt()));
//                timeTv.setText(timeAge);

                if (message.getMsg().length() == 0)
                    textView.setVisibility(View.GONE);
                else
                    textView.setText(message.getMsg());  // de encrypt or decrypt !!

//                Utils.loadImage(itemView.getContext(), message.getImageUrl(), imageView2);


                if (message.getImageUrl() != null) {
                    messageImage.setVisibility(View.VISIBLE);
                    Utils.loadImage(itemView.getContext(), message.getImageUrl(), messageImage);
                    messageImage.setOnClickListener(v -> Log.d("TestImage", message.getImageUrl()));
                }
                if(message.getAudioUrl() == null){
                    System.out.println("NO URL ");
                }else{
                    System.out.println(message.getAudioUrl());
                }
                if (message.getAudioUrl() != null) {

                    audio_message.setVisibility(View.VISIBLE);
                    MediaPlayer mp = new MediaPlayer();
                    System.out.println(message.getAudioUrl());
                    try {
                        mp.setDataSource(message.getAudioUrl());

                        mp.prepare();
                        int duration = mp.getDuration();
                        System.out.println(duration + " Mohanda");
                        seekBar.setMax(duration);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    start_message.setOnClickListener(v->{
                        mp.start();
                        mp.setOnMediaTimeDiscontinuityListener(new MediaPlayer.OnMediaTimeDiscontinuityListener() {
                            @Override
                            public void onMediaTimeDiscontinuity(@NonNull MediaPlayer mp, @NonNull MediaTimestamp mts) {
                                seekBar.setProgress(mp.getCurrentPosition());

                            }
                        });
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                seekBar.setProgress(0);
                                pause_message.setVisibility(View.GONE);
                                start_message.setVisibility(View.VISIBLE);
                            }
                        });

                        start_message.setVisibility(View.GONE);
                        pause_message.setVisibility(View.VISIBLE);
                    });

                    pause_message.setOnClickListener(v->{
                        mp.pause();
                        pause_message.setVisibility(View.GONE);
                        start_message.setVisibility(View.VISIBLE);
                    });

                }
            }
        }


        class MessageViewHolder2 extends RecyclerView.ViewHolder {
            TextView textView, timeTv;
            View audio_message;
            View itemView;
            ImageView messageImage, imageView2,start_message,pause_message;
            public MessageViewHolder2(View itemView) {
                super(itemView);
                this.itemView = itemView;
                textView = itemView.findViewById(R.id.textViewMessage2);
                imageView2 = itemView.findViewById(R.id.imageView3);
                messageImage = itemView.findViewById(R.id.messageImage);
                audio_message= itemView.findViewById(R.id.audio_message);
                start_message= itemView.findViewById(R.id.start_message);
                pause_message= itemView.findViewById(R.id.pause_message);
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


                if (message.getAudioUrl() != null) {
                    audio_message.setVisibility(View.VISIBLE);
                    MediaPlayer mp = new MediaPlayer();
                    try {
                        mp.setDataSource(message.getAudioUrl());

                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    start_message.setOnClickListener(v->{
                        mp.start();
                        start_message.setVisibility(View.GONE);
                        pause_message.setVisibility(View.VISIBLE);
                    });

                    pause_message.setOnClickListener(v->{
                        mp.pause();
                        pause_message.setVisibility(View.GONE);
                        start_message.setVisibility(View.VISIBLE);

                    });




            }

        }


    }

}}