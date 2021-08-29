package com.naioush.capture.chat.Firebase;

public interface GetChatIDListener {
    public  void onResult(String chatId);
    public  void onFail(String NewchatId);
}
