package com.freedeveloper.simple_news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//ver2.0 Activity -> AppAompatActvity 변경
public class NewsActivity extends AppCompatActivity {
    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    //news api url 저장하기 위한 변수
    private TextView TextView_api;

    //정보를 저장하기 위한 변수
    RequestQueue queue;

    //ver2.0 url 전역변수로 설정변경
    String URL = "https://newsapi.org/v2/top-headlines?country=kr&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
    String Menu = "주요뉴스 Top headlines";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mrecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mrecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);

        //ver2.0 TextView_menu 설정 추가


        queue = Volley.newRequestQueue(this);
        getNews();
        //1. 화면이 로딩 -> 뉴스 정보를 받아온다  완료
        //2. 정보 -> 어댑터 넘겨준다
        //3. 어댑터 -> 셋팅
        TextView_api_click();
    }

    public void getNews() {
        // Instantiate the RequestQueue.

        //ver2.0 menu 내용 변경 추가
        TextView TextView_menu = findViewById(R.id.TextView_menu);
        TextView_menu.setText(Menu);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("NEWS",response);

                        try {

                            JSONObject jsonObj = new JSONObject(response);

                            JSONArray arrayArticles = jsonObj.getJSONArray("articles");

                            //response ->> NewsData Class 분류
                            List<NewsData> news = new ArrayList<>();

                            for(int i = 0, j = arrayArticles.length(); i < j; i++) {
                                JSONObject obj = arrayArticles.getJSONObject(i);

                                Log.d("NEWS",obj.toString());

                                obj.getString("title");
                                obj.getString("urlToImage");
                                obj.getString("description");
                                obj.getString("url");


                                NewsData newsData = new NewsData();
                                newsData.setTitle(obj.getString("title"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                newsData.setContent(obj.getString("description"));
                                newsData.setUrl(obj.getString("url"));

                                news.add(newsData);
                            }

                            // specify an adapter (see also next example)
                            mAdapter = new MyAdapter(news, NewsActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Object  obj = v.getTag();
                                    if(obj != null){
                                        int position = (int) obj;
                                        ((MyAdapter)mAdapter).getNews(position);
                                        Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                                        intent.putExtra("news",((MyAdapter)mAdapter).getNews(position));
                                        startActivity(intent);
                                        //1. 본문
                                        //2. 전체
                                        //2-1 하나씩 다
                                        //2-2 한번에 다 NewsData 전체를 다
                                    }
                                }
                            });
                            mrecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void TextView_api_click() {
        TextView_api = findViewById(R.id.TextView_api);
        TextView_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://newsapi.org/s/south-korea-news-api");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    //ver2.0 메뉴 선택지 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //ver2.0 메뉴 선택시 url 주소 변경
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            //ver3.0 국가추가
            //KOREA
            case R.id.menu_topheadlines:{
                URL = "https://newsapi.org/v2/top-headlines?country=kr&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "주요뉴스 Top headlines";
                getNews();
                break;
            }
            case R.id.menu_business:{
                URL = "https://newsapi.org/v2/top-headlines?country=kr&category=business&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "경제 Business";
                getNews();
                break;
            }
            case R.id.menu_entertainment:{
                URL = "https://newsapi.org/v2/top-headlines?country=kr&category=entertainment&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "연예 Entertainment";
                getNews();
                break;
            }
            case R.id.menu_health:{
                URL = "https://newsapi.org/v2/top-headlines?country=kr&category=health&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "건강 Health";
                getNews();
                break;
            }
            case R.id.menu_science:{
                URL = "https://newsapi.org/v2/top-headlines?country=kr&category=science&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "과학 Science";
                getNews();
                break;
            }
            case R.id.menu_sports:{
                URL = "https://newsapi.org/v2/top-headlines?country=kr&category=sports&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "스포츠 Sports";
                getNews();
                break;
            }
            case R.id.menu_technology:{
                URL = "https://newsapi.org/v2/top-headlines?country=kr&category=technology&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "기술 Technology";
                getNews();
                break;
            }

            //USA
            case R.id.menu_topheadlines_usa:{
                URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "주요뉴스 Top headlines";
                getNews();
                break;
            }
            case R.id.menu_business_usa:{
                URL = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "경제 Business";
                getNews();
                break;
            }
            case R.id.menu_entertainment_usa:{
                URL = "https://newsapi.org/v2/top-headlines?country=us&category=entertainment&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "연예 Entertainment";
                getNews();
                break;
            }
            case R.id.menu_health_usa:{
                URL = "https://newsapi.org/v2/top-headlines?country=us&category=health&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "건강 Health";
                getNews();
                break;
            }
            case R.id.menu_science_usa:{
                URL = "https://newsapi.org/v2/top-headlines?country=us&category=science&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "과학 Science";
                getNews();
                break;
            }
            case R.id.menu_sports_usa:{
                URL = "https://newsapi.org/v2/top-headlines?country=us&category=sports&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "스포츠 Sports";
                getNews();
                break;
            }
            case R.id.menu_technology_usa:{
                URL = "https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "기술 Technology";
                getNews();
                break;
            }

            //CHINA
            case R.id.menu_topheadlines_chi:{
                URL = "https://newsapi.org/v2/top-headlines?country=cn&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "주요뉴스 Top headlines";
                getNews();
                break;
            }
            case R.id.menu_business_chi:{
                URL = "https://newsapi.org/v2/top-headlines?country=cn&category=business&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "경제 Business";
                getNews();
                break;
            }
            case R.id.menu_entertainment_chi:{
                URL = "https://newsapi.org/v2/top-headlines?country=cn&category=entertainment&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "연예 Entertainment";
                getNews();
                break;
            }
            case R.id.menu_health_chi:{
                URL = "https://newsapi.org/v2/top-headlines?country=cn&category=health&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "건강 Health";
                getNews();
                break;
            }
            case R.id.menu_science_chi:{
                URL = "https://newsapi.org/v2/top-headlines?country=cn&category=science&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "과학 Science";
                getNews();
                break;
            }
            case R.id.menu_sports_chi:{
                URL = "https://newsapi.org/v2/top-headlines?country=cn&category=sports&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "스포츠 Sports";
                getNews();
                break;
            }
            case R.id.menu_technology_chi:{
                URL = "https://newsapi.org/v2/top-headlines?country=cn&category=technology&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "기술 Technology";
                getNews();
                break;
            }

            //JAPAN
            case R.id.menu_topheadlines_jap:{
                URL = "https://newsapi.org/v2/top-headlines?country=jp&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "주요뉴스 Top headlines";
                getNews();
                break;
            }
            case R.id.menu_business_jap:{
                URL = "https://newsapi.org/v2/top-headlines?country=jp&category=business&apiKey=7cce4bb59aed4e98967c597fc2b9f032\n";
                Menu = "경제 Business";
                getNews();
                break;
            }
            case R.id.menu_entertainment_jap:{
                URL = "https://newsapi.org/v2/top-headlines?country=jp&category=entertainment&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "연예 Entertainment";
                getNews();
                break;
            }
            case R.id.menu_health_jap:{
                URL = "https://newsapi.org/v2/top-headlines?country=jp&category=health&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "건강 Health";
                getNews();
                break;
            }
            case R.id.menu_science_jap:{
                URL = "https://newsapi.org/v2/top-headlines?country=jp&category=science&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "과학 Science";
                getNews();
                break;
            }
            case R.id.menu_sports_jap:{
                URL = "https://newsapi.org/v2/top-headlines?country=jp&category=sports&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "스포츠 Sports";
                getNews();
                break;
            }
            case R.id.menu_technology_jap:{
                URL = "https://newsapi.org/v2/top-headlines?country=jp&category=technology&apiKey=7cce4bb59aed4e98967c597fc2b9f032";
                Menu = "기술 Technology";
                getNews();
                break;
            }

        }
        return true;
    }

    //ver3.1 뒤로가기 두번으로 종료
    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }
}