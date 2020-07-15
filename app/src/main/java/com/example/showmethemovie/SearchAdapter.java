package com.example.showmethemovie;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class SearchAdapter extends BaseAdapter {

    Movie movie;
    private ArrayList<Movie> mMovieList;
    private LayoutInflater mInflate;
    static View v;



    SearchAdapter(ArrayList<Movie> data, Context context){
        mMovieList = data; // 글 목록 리스트를 불러옴
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class UserViewHolder {
        TextView title,director,actor,year;
        ImageView poster;
        RatingBar ratingBar;



        public void bind(String title, String director, String actor,String year, float rate, String image_link) {
            this.title.setText(Html.fromHtml(title));
            this.director.setText(Html.fromHtml(director));
            this.actor.setText(Html.fromHtml(actor));
            this.year.setText(year);
            this.ratingBar.setRating(rate);

            if(!image_link.equals("")){
                Picasso.with(v.getContext())
                        .load(image_link)
                        .into(this.poster);
            }else{
                this.poster.setImageResource(R.drawable.no);
            }


        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

                UserViewHolder userViewHolder;
                v = convertView;
                if (v == null) {
                    v = mInflate.inflate(R.layout.item_layout, parent, false);
                    userViewHolder = new UserViewHolder();
                    userViewHolder.title = v.findViewById(R.id.title);
                    userViewHolder.director = v.findViewById(R.id.director);
                    userViewHolder.actor = v.findViewById(R.id.actor);
                    userViewHolder.year = v.findViewById(R.id.year);
                    userViewHolder.poster = v.findViewById(R.id.iv);
                    userViewHolder.ratingBar = v.findViewById(R.id.ratingBar);
                    v.setTag(userViewHolder); //태그를 이용해 저장
                } else {
                    userViewHolder = (UserViewHolder) v.getTag(); //태그를 이용해 받아오기
                }
                System.out.println(position);

                userViewHolder.bind(mMovieList.get(position).getTitle(),
                        mMovieList.get(position).getDirector(),
                        mMovieList.get(position).getActor(),
                        mMovieList.get(position).getyear(),
                        mMovieList.get(position).getrate(),
                        mMovieList.get(position).getImage_link()); //아이템과 어댑터 바인딩
                return v;


    }

}


