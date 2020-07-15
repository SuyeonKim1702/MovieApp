package com.example.showmethemovie;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import android.os.Message;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class ApiMovie  implements Runnable {

    public String apiURL1 = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    public String apiURL2 = "https://openapi.naver.com/v1/search/movie.json?query=";
    public String key1 = "70f5974f27ae9e61b26c16abc5be1153";
    public String date;
    Request request;
    static int type;
    OkHttpClient client = new OkHttpClient();
    static int i;
    ApiMovie(String today,int type){ //1: 날짜 2: 없음 3: 검색어
        this.date = today;
        this.type = type;
    }

    public void run(){
        main();

    }

    public void main(){
        if(type == 1){
            String url = apiURL1+"?key="+key1+"&targetDt="+date;
            request = new Request.Builder()
                    .addHeader("sample", "sample")
                    .url(url)
                    .build();
            String result=" ";
            try {
                Response response = client.newCall(request)
                        .execute();
                result = response.body().string();


            } catch (IOException e) {
                e.printStackTrace();
            }

            parseData(result);

        }else if(type == 2){
            for(i=0;i<MainActivity.title_list.length;i++){
                String title = MainActivity.title_list[i].replaceAll("#","");
                String RealURL=apiURL2+title;

                        request = new Request.Builder()
                        .addHeader("X-Naver-Client-Id", "hqBD1Xqasz1BuwuhDZw4")
                        .addHeader("X-Naver-Client-Secret", "Qm6AuRDPLG")
                        .url(RealURL)
                        .build();

                String result=" ";
                try {
                    Response response = client.newCall(request)
                            .execute();
                    result = response.body().string();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                parseData(result);
            }

        }else if(type == 3){
            String RealURL=apiURL2+date+"&display=100"; // 네이버 + 검색어 ,100개 나열
            request = new Request.Builder()
                    .addHeader("X-Naver-Client-Id", "hqBD1Xqasz1BuwuhDZw4")
                    .addHeader("X-Naver-Client-Secret", "Qm6AuRDPLG")
                    .url(RealURL)
                    .build();

            String result=" ";
            try {
                Response response = client.newCall(request)
                        .execute();
                result = response.body().string();


            } catch (IOException e) {
                e.printStackTrace();
            }

            parseData(result);

        }


    }

        private static void parseData(String responseBody) {
           String title,image,rating,director_,actor_;
           JSONObject jsonObject = null;
           try {
               jsonObject = new JSONObject(responseBody.toString());
               if(type == 1){ // 영화 진흥원
                   JSONObject jsonObject1 = (JSONObject) jsonObject.get("boxOfficeResult");
                   JSONArray jsonArray = jsonObject1.getJSONArray("dailyBoxOfficeList");
                   for (int i = 0; i < jsonArray.length(); i++) {
                       JSONObject item = jsonArray.getJSONObject(i);
                       title = item.getString("movieNm");
                       MainActivity.title_list[i] = title;
                   }
                       MainActivity.mHandler.sendEmptyMessage(1);

                   }else if(type == 2){ //네이버 영화
                   JSONArray jsonArray = jsonObject.getJSONArray("items");
                    System.out.println(jsonArray);
                       JSONObject item = jsonArray.getJSONObject(0);
                       image = item.getString("image");
                       rating = item.getString("userRating");
                       director_= item.getString("director");
                   actor_ = item.getString("actor");

                       MainActivity.image_list[i] = image;
                       MainActivity.director_list[i] = director_;
                       MainActivity.actor_list[i] = actor_;
                       if(rating == null) MainActivity.rating_list[i] = (float)0.0;
                       else MainActivity.rating_list[i] = Float.parseFloat(rating);

                   MainActivity.mHandler.sendEmptyMessage(2);
               }else if(type == 3){ //검색어 입력 -> 이름, 평점, 사진, 개봉연도, 감독 , 출연진 (from 네이버)
                   JSONArray jsonArray = jsonObject.getJSONArray("items");
                   SearchActivity.movieArrayList.removeAll(SearchActivity.movieArrayList); //원래 있던거 다 삭제
                   for (int s = 0; s < jsonArray.length(); s++) {
                       JSONObject item = jsonArray.getJSONObject(s);
                       String image_ = item.getString("image");
                       String title_ = item.getString("title");
                       String date = item.getString("pubDate");
                       String director = item.getString("director");
                       String actor = item.getString("actor");
                       float rate = Float.parseFloat(item.getString("userRating"));
                       SearchActivity.movieArrayList.add(new Movie(title_,rate,image_,date,director,actor));
                       Message message = new Message();
                       message.what = 3;
                       message.arg1 = s;
                       message.arg2 = jsonArray.length();
                       SearchActivity.vHandler.sendMessage(message);
                   }


               }

           } catch (JSONException e) {
               e.printStackTrace();
           }
         }




}


