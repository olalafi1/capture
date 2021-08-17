package com.naioush.capture;

public class LikeNotification {
    public String userKey,postId,status,key;

    public LikeNotification(){}
    public LikeNotification( String userKey,String postId,String status,String key){
        this.postId=postId;
        this.userKey=userKey;
        this.status=status;
        this.key=key;


    }
}
