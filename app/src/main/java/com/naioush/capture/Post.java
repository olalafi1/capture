package com.naioush.capture;

import java.util.ArrayList;
import java.util.Map;

public class Post {
   public String Date,title,NO_likes,photo,classification,location,privacy,status,key,createdBy;
   public   Post(){}

   public Post( String key,   String Date,String title,String NO_likes,String photo,
             String classification,String location,String privacy,String status,String createdBy
    ){
this.key=key;
        this.Date=Date;
        this.title=title;
        this.NO_likes=NO_likes;
        this.photo=photo;
        this.classification=classification;
        this.location=location;
        this.privacy=privacy;
        this.status=status;
        this.createdBy=createdBy;

    }
  public   Post( String key,   String Date,String title,String photo,
             String classification,String location,String privacy,String status,String createdBy
    ){
this.key=key;
        this.Date=Date;
        this.title=title;
        this.NO_likes=NO_likes;
        this.photo=photo;
        this.classification=classification;
        this.location=location;
        this.privacy=privacy;
        this.status=status;
      this.createdBy=createdBy;

    }
}
