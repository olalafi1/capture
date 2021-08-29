package com.naioush.capture.chat;

import android.os.Parcel;
import android.os.Parcelable;


import com.naioush.capture.User;

public class Chat implements Comparable<Chat>, Parcelable {
    String id;
    long createdAt;
    User user1;
    User user2;
    long chatlastDate;
    String lastMessage;

    public Chat() {
    }

    protected Chat(Parcel in) {
        id = in.readString();
        createdAt = in.readLong();
        user1 = in.readParcelable(User.class.getClassLoader());
        user2 = in.readParcelable(User.class.getClassLoader());
        chatlastDate = in.readLong();
        lastMessage = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

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

    public User getUser1() {
        return user1;
    }

    public void setUser1(User supplier) {
        this.user1 = supplier;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User customer) {
        this.user2 = customer;
    }

    public long getChatlastDate() {
        return chatlastDate;
    }

    public void setChatlastDate(long chatlastDate) {
        this.chatlastDate = chatlastDate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int compareTo(Chat chat) {
        return (int) (chat.getChatlastDate() - getChatlastDate());

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(createdAt);
        dest.writeParcelable(user1, flags);
        dest.writeParcelable(user2, flags);
        dest.writeLong(chatlastDate);
        dest.writeString(lastMessage);
    }
}
