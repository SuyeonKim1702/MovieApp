package com.example.showmethemovie;


import java.io.Serializable;

public class Movie implements Serializable {


    private String title;
    private float rate;
    private String image_link ;
    private String year;
    private String director;
    private String actor;
    private String openDate ;
    private String showTime;
    private String genre;
    private String age;


    public Movie(String title, float rate, String image_link, String year, String director, String actor){
        this.title = title;
        this.rate = rate;
        this.image_link = image_link;
        this.year = year;
        this.director = director;
        this.actor = actor;
    }

   public Movie(String title,String director,String actor,String imageLink,String openDate,String showTime,String genre,
                String age,float rating){

       this.title = title;
       this.rate = rating;
       this.director = director;
       this.actor = actor;
       this.age = age;
       this.image_link = imageLink;
       this.openDate = openDate;
       this.showTime = showTime;
       this.genre = genre;

   }

    public String getTitle() {
        return title;
    }

    public String getImage_link() {
        return image_link;
    }

    public float getrate() { return rate; }
    public String getDirector() {
        return director;
    }
    public String getActor() {
        return actor;
    }
    public String getyear() {
        return year;
    }
    public String getOpenDate() {
        return openDate;
    }
    public String getTime() {
        return showTime;
    }
    public String getGenre() {
        return genre;
    }
    public String getAge() {
        return age;
    }

}
