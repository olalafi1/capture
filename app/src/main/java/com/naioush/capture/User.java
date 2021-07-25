package com.naioush.capture;

public class User {
  public   String key,verificationId,verifyCode,Name,Mobile,Email,countryPrefix,photo;

   public User(){}
   public User(    String key,String verificationId,String verifyCode,String Name,String Mobile,
             String Email,String countryPrefix,String photo
    ){

        this.key=key;
        this.verificationId=verificationId;
        this.verifyCode=verifyCode;
        this.Name=Name;
        this.Mobile=Mobile;
        this.Email=Email;
        this.countryPrefix=countryPrefix;
        this.photo=photo;

    }

}
