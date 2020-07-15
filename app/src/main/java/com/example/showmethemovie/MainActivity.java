package com.example.showmethemovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.nhn.android.naverlogin.OAuthLogin;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    public static Handler gHandler, mHandler;
    public static String title_list[] = new String[10];
    public static String image_list[] = new String[10];
    public static float rating_list[] = new float[10];
    public static String director_list[] = new String[10];
    public static String actor_list[] = new String[10];
    static Movie movieforpass;
    Toolbar toolbar;
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Context context = this;
    TextView email,name;
    View header;
    Menu menu;
    MenuItem log;
   CircleImageView profile_image;



    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        toolbar =findViewById(R.id.main_toolbar_title);
        navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 제목 안 보이게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -8); //8일전 기준
        final String date = sdf.format(calendar.getTime());
        System.out.println(date+"입니다 !!!");

        SharedPreferences test = getSharedPreferences("test", MODE_PRIVATE);
        final SharedPreferences.Editor editor2 = test.edit();

        ApiMovie apiMovie = new ApiMovie(date, 1); // 사진 요청인지 여부를 여기서 확인 가능.
        Thread apithread = new Thread(apiMovie);
        apithread.start();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) { //네이버인지 영화진흥원인지
                if (msg.what == 1) { //영화제목 from 영화진흥원
                    //for(int l=0;l<title_list.length;l++)
                    //   System.out.println(title_list[l]+"이다");


                    ApiMovie apiMovie2 = new ApiMovie(date, 2); // 사진 요청인지 여부를 여기서 확인 가능.
                    Thread apithread2 = new Thread(apiMovie2);
                    apithread2.start();
                } else{ //이미지 from 네이버
                    //
                    if (image_list.length == 10) {
                        for (int l = 0; l < image_list.length; l++)
                            // System.out.println(image_list[l]+"이다");
                            pagerAdapter = new ViewPagerAdapter(MainActivity.this);
                        viewPager.setAdapter(pagerAdapter);
                        viewPager.setPageMargin(-300);
                        viewPager.setHorizontalFadingEdgeEnabled(true);
                        viewPager.setFadingEdgeLength(30);





                    }

                }

            }

        };


        gHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) { //네이버인지 영화진흥원인지
                if (msg.what == 0) {

                    Intent intent = new Intent(getApplicationContext(), Detail.class);
                    intent.putExtra("Movie",movieforpass);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "상세정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        };


        //로그인 여부 확인하기

        header = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        log = menu.findItem(R.id.log);
        name = header.findViewById(R.id.name);
        email = header.findViewById(R.id.email);
        profile_image = header.findViewById(R.id.profileimage);

        if(!OAuthLogin.getInstance().getState(context).toString().equals("NEED_LOGIN") && LogIn.mUserInfoMap!= null ){//로그인이 되어있는 상태
            name.setText(LogIn.mUserInfoMap.get("name")+"님, 반갑습니다!");
            email.setText(LogIn.mUserInfoMap.get("email"));

            Picasso.with(context)
                    .load(LogIn.mUserInfoMap.get("profile_image"))
                    .into(profile_image);


            log.setTitle("로그아웃");
        }else{
            name.setText("비회원 상태로 이용 중 입니다");
            email.setText("");
            log.setTitle("로그인");
            profile_image.setImageResource(R.drawable.profile_icon);
        }



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId(); // 선택된 아이템의 id 가져오기
                String title = menuItem.getTitle().toString(); //선택된 아이템의 title(?)

                if(id == R.id.account){
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.setting){
                    Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.log) {
                    //로그인 여부 확인
                    if (!OAuthLogin.getInstance().getState(context).toString().equals("NEED_LOGIN")){
                        Toast.makeText(context, "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
                        OAuthLogin.getInstance().logout(context);
                        name.setText("비회원 상태로 이용 중 입니다");
                        email.setText("");
                        log.setTitle("로그인");
                        profile_image.setImageResource(R.drawable.profile_icon);
                        editor2.clear();
                        editor2.commit();


                }else{  // 로그인
                        Intent intent = new Intent(getApplicationContext(), LogIn.class);
                        startActivity(intent);
                        finish();
                    }

                }

                return true;
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때(햄버거 버튼)
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}

