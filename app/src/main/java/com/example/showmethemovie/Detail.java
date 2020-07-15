package com.example.showmethemovie;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.example.showmethemovie.MainActivity.image_list;
import static com.example.showmethemovie.MainActivity.rating_list;
import static com.example.showmethemovie.MainActivity.title_list;

public class Detail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_content);

        Movie movie = (Movie)getIntent().getSerializableExtra("Movie");

        TextView title = findViewById(R.id.tv_detail_title);

        title.setText(movie.getTitle());
        TextView date = findViewById(R.id.tv_detail_date);
        date.setText(movie.getOpenDate()+ "개봉");
        TextView age = findViewById(R.id.tv_detail_age);
        if(!movie.getAge().equals(""))
        age.setText(movie.getAge());
        TextView genre = findViewById(R.id.tv_detail_genre);
        genre.setText(movie.getGenre());
        TextView director = findViewById(R.id.tv_detail_director);
        director.setText(movie.getDirector());
        TextView actor = findViewById(R.id.tv_detail_actor);
        actor.setText(movie.getActor());
        TextView time = findViewById(R.id.tv_detail_time);
        time.setText(movie.getTime()+"분");
        ImageView profile = findViewById(R.id.profile);
        Picasso.with(Detail.this)
                .load(movie.getImage_link())
                .into(profile);
        RatingBar ratingBar = findViewById(R.id.rb_detail_ratingBar);
        ratingBar.setRating(movie.getrate());






    }

}