package com.example.showmethemovie;


import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ApiMovieDetail implements Runnable {

    public String apiURL1 = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";
    public static String apiURL2 = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";
    public static String key1 = "70f5974f27ae9e61b26c16abc5be1153";
    public static String title;
    public static String director,actor,imageLink;
    public static float rating;
    static Request request;
    static int type;
    static Boolean from_main;
    static OkHttpClient client = new OkHttpClient();
    static int i;
    ApiMovieDetail(String title, String director,String actor, String imageLink,float rating){ //1: 날짜 2: 없음 3: 검색어
        this.title = title;
        this.director = director;
        this.actor = actor;
        this.imageLink = imageLink;
        this.rating = rating;
        this.from_main = false;
    }

    ApiMovieDetail(Boolean type, String title, String director,String actor, String imageLink,float rating){ //1: 날짜 2: 없음 3: 검색어
        this.from_main = type;
        this.title = title;
        this.director = director;
        this.actor = actor;
        this.imageLink = imageLink;
        this.rating = rating;
    }

    public void run(){
        main();

    }

    public void main(){
        String url = "https://openapi.seoul.go.kr:8088/"+"4e57424e45726b6434367854745061"+"/json/bikeList/1/50/";
           // String url = apiURL1+"?key="+key1+"&movieNm="+title+"&directorNm="+director;
           // System.out.println(url+"이고");
            request = new Request.Builder()
                    .addHeader("sample", "sample")
                    .url(url)
                    .build();
            String result=" ";
            try {
                Response response = client.newCall(request)
                        .execute();
                result = response.body().string();

                parseData(result);
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

        private static void parseData(String responseBody) {

        System.out.println(responseBody+"이다");
           String code;
           JSONObject jsonObject = null;
           try {
               jsonObject = new JSONObject(responseBody);
                // 영화 진흥원

                   JSONObject jsonObject1 = (JSONObject) jsonObject.get("movieListResult");
                   JSONArray jsonArray = jsonObject1.getJSONArray("movieList");
              // System.out.println(jsonArray+"이고");
               if(jsonArray.length()==0){
                   Exception e = new Exception();
                   throw e;
               }
                   JSONObject item = jsonArray.getJSONObject(0);
                       code = item.getString("movieCd");


               String url2 = apiURL2+"?key="+key1+"&movieCd="+code; //두번째 검색 url
               request = new Request.Builder()
                       .addHeader("sample", "sample")
                       .url(url2)
                       .build();
               String result=" ";

                   Response response = client.newCall(request)
                           .execute();
                   result = response.body().string();
                   jsonObject = new JSONObject(result);
                   // 영화 진흥원
                   JSONObject jsonObject2 = (JSONObject)((JSONObject) jsonObject.get("movieInfoResult")).get("movieInfo");
                   String showTime = jsonObject2.getString("showTm");
                   String openDate = jsonObject2.getString("openDt");
                   JSONArray jsonArray3 = jsonObject2.getJSONArray("genres");
                   JSONArray jsonArray2 = jsonObject2.getJSONArray("audits");
                   String age = ((JSONObject)jsonArray2.getJSONObject(0)).getString("watchGradeNm");
                   String genre = ((JSONObject)jsonArray3.getJSONObject(0)).getString("genreNm");

                   if(from_main)  MainActivity.movieforpass = new Movie(title,director,actor,imageLink,openDate,showTime,genre,age,rating);

                  else SearchActivity.movieforpass = new Movie(title,director,actor,imageLink,openDate,showTime,genre,age,rating);
                   if(from_main) MainActivity.gHandler.sendEmptyMessage(0);
                       else
                   SearchActivity.tHandler.sendEmptyMessage(0);

           } catch (Exception e){
               if(from_main) MainActivity.gHandler.sendEmptyMessage(1);
                   else SearchActivity.tHandler.sendEmptyMessage(1);

           }
        }




}


