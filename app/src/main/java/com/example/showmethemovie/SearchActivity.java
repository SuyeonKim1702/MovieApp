package com.example.showmethemovie;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    String keyword;
    EditText et;
    Button bt;
    ListView lv;
    SearchAdapter searchAdapter;
    static Handler vHandler,tHandler;
    static ArrayList<Movie> movieArrayList = new ArrayList<>();
    static Movie movieforpass;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et = findViewById(R.id.et_search_keyword);
        bt = findViewById(R.id.bt_search_search);
        lv = findViewById(R.id.lv_search_lv);
        searchAdapter = new SearchAdapter(movieArrayList, SearchActivity.this);
        lv.setAdapter(searchAdapter);

        vHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 3){

                   if(msg.arg2 == msg.arg1+1){
                       searchAdapter.notifyDataSetChanged();
                   }
                }

            }

        };

        tHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    System.out.println(movieforpass.getActor());
                    System.out.println(movieforpass.getAge());

                    Intent intent = new Intent(getApplicationContext(), Detail.class);
                    intent.putExtra("Movie",movieforpass);
                    startActivity(intent);

                }else if(msg.what == 1){
                    Toast.makeText(SearchActivity.this, "상세정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }

        };


        lv.setOnItemClickListener(listener);



        bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
            keyword = et.getText().toString();
               ApiMovie apiMovie = new ApiMovie(keyword,3); // 검색어 타입
                Thread apithread = new Thread(apiMovie);
                apithread.start();
            }

        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        movieArrayList.removeAll(movieArrayList);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object obj = lv.getAdapter().getItem(position);
            Movie a = (Movie)obj;
            String title = a.getTitle();
            title= title.replace("<b>","");
            title= title.replace("</b>","");
            String actor = a.getActor();
            actor = actor.replaceAll("|","");
            String director = a.getDirector();
            String imageLink = a.getImage_link();
            float rating = a.getrate();
            director = director.substring(0,director.indexOf("|"));
           // System.out.println(a.getTitle()+"과"+director);
            ApiMovieDetail apiMovieDetail = new ApiMovieDetail(title,director,actor,imageLink,rating);
            Thread apithread = new Thread(apiMovieDetail);
            apithread.start();




            }


    };



}

