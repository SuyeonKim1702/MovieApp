package com.example.showmethemovie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import static com.example.showmethemovie.MainActivity.actor_list;
import static com.example.showmethemovie.MainActivity.director_list;
import static com.example.showmethemovie.MainActivity.image_list;
import static com.example.showmethemovie.MainActivity.rating_list;
import static com.example.showmethemovie.MainActivity.title_list;

public class ViewPagerAdapter extends PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext = null ;
    Bitmap bmp = null;
    int im;
    ImageView poster;

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public ViewPagerAdapter(Context context) {
        mContext = context ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null ;
        im =position;
       // View page = inflater.inflate(R.layout.YOUR_PAGE, null);
        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.page, container, false);
            Button bt = (Button) view.findViewById(R.id.bt);
            TextView title = view.findViewById(R.id.tv_page_title);
            TextView rating = view.findViewById(R.id.tv_page_ticketing);
            poster = (ImageView)view.findViewById(R.id.poster);
             bt.setOnClickListener(new Button.OnClickListener(){
                 @Override
                 public void onClick(View v) {
                     System.out.println(title_list[position]+"보냅니다");
                     String d = director_list[position].substring(0,director_list[position].indexOf("|"));
                     ApiMovieDetail apiMovieDetail = new ApiMovieDetail(true,title_list[position],d,actor_list[position],image_list[position],rating_list[position]);
                     Thread apithread = new Thread(apiMovieDetail);
                     apithread.start();

                 }
             });

            Picasso.with(container.getContext())
                    .load(image_list[im])
                    .into(poster);
            title.setText((position+1)+". "+title_list[position]);
            rating.setText("평점 "+rating_list[position]);
            //new RequestApiTask().execute(); //호출


        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // 전체 페이지 수는 10개로 고정.
        return 10;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }





}

