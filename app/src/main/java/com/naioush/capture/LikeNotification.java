package com.naioush.capture;

public class LikeNotification {
    public String userKey,postId,status;

    public LikeNotification(){}
    public LikeNotification( String userKey,String postId,String status){
        this.postId=postId;
        this.userKey=userKey;
        this.status=status;


    }
}
