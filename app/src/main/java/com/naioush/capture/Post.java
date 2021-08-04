package com.naioush.capture;

import java.util.ArrayList;
import java.util.Map;

public class Post {
   public String Date,title,photo,classification,location,privacy,status,key,createdBy;
   public   Post(){}


    public Post(String postKey, String formattedDate, String title, String photo, String classification, String location, String privacy, String status, String userKey) {

        this.key=postKey;
        this.Date=formattedDate;
        this.title=title;
        this.photo=photo;
        this.classification=classification;
        this.location=location;
        this.privacy=privacy;
        this.status=status;
        this.createdBy=userKey;

    }
}
