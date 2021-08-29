package com.naioush.capture.chat;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Message {
    String id;
    long createdAt;
    String uid;
    String to;
    String imageUrl;
    String msg;

    public Message() {
    }

    public Message(String id, long createdAt, String uid, String to , String imageUrl, String msg) {
        this.id = id;
        this.createdAt = createdAt;
        this.uid = uid;
        this.to = to;
        this.imageUrl = imageUrl;
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return createdAt == message.createdAt &&
                uid == message.uid &&
                id.equals(message.id) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, uid, imageUrl, msg);
    }
}

