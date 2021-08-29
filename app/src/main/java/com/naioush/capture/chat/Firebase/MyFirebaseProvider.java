package com.naioush.capture.chat.Firebase;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naioush.capture.chat.Message;

import java.util.Date;


/**
 * Created by eyada on 23/11/2017.
 */

public class MyFirebaseProvider {
    private static DatabaseReference databaseReference;
    private static String currentUserUID;


    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null)
            databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference;
    }

    public static DatabaseReference getChildDatabaseReference(String childName) {
        return getDatabaseReference().child(childName);
    }


    public static void DoSendMessage(Message message, String chatID, final MessageListener messageListener) {
        String key = new Date().getTime() + "";
        message.setId(key);
        DatabaseReference messageNode = getChildDatabaseReference("chats").child(chatID).child("Messages").child(key);
        messageNode.setValue(message).addOnSuccessListener(aVoid -> messageListener.onResult(true, key)).addOnFailureListener(e -> messageListener.onResult(false, ""));
    }


    public static void getChatId(String currentUserId, final String userId, final String useridFiull, final GetChatIDListener getChatIDListener) {

        final DatabaseReference chatReference = getChildDatabaseReference("chats");
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String case1 = currentUserId + userId;
                String case2 = userId + currentUserId;

                String FinalKey = case1.compareTo(case2) > 0 ? case1 : case2;

                chatReference.child(FinalKey).child("createdDate").setValue(new Date().getTime());
                getChildDatabaseReference("chats").child(FinalKey).child("participants").child("p1").setValue(currentUserId);
                getChildDatabaseReference("chats").child(FinalKey).child("participants").child("p2").setValue(useridFiull);
                getChatIDListener.onResult(FinalKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


}
