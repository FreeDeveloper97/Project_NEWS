package com.freedeveloper.simple_news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

public class NewsDetailActivity extends AppCompatActivity {
    private NewsData news;
    private TextView TextView_title, TextView_content, TextView_URL;
    public SimpleDraweeView ImageView_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        setComp();
        getNewsDetail();
        setNews();
        TextView_URL_click();
    }

    //기본 컴포넌트 셋팅
    public void setComp() {
        TextView_title = findViewById(R.id.TextView_title);
        TextView_content = findViewById(R.id.TextView_content_detail);
        ImageView_title = findViewById(R.id.ImageView_title);
        TextView_URL = findViewById(R.id.TextView_URL);
    }

    //이전 액티비티에서 받아오는 인텐트
    public void getNewsDetail() {
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bld = intent.getExtras();

            Object obj = bld.get("news");
            if(obj != null && obj instanceof NewsData) {
                this.news = (NewsData) obj;
            }
        }
    }

    //이전 액티비티에서 받아오는 인텐트에서 정보를 확인하여 뉴스표시
    public void setNews() {
        if(this.news != null) {
            String title = this.news.getTitle();
            if(title != null) {
                TextView_title.setText(title);
            }
            String content = this.news.getContent();
            //ver3.0 null -> "null" 수정
            if(content != "null" || content != null) {
                //전체 본문은 url 값의 실제 뉴스 사이트에 있으며, 해당 전체 본문을 불러오기 위해서는 스크래핑 (크롤링) 기술로 읽어와야 합니다.
                TextView_content.setText(content);
            }
            else {
                TextView_content.setText("내용이 없습니다");
            }
            Uri uri = Uri.parse(news.getUrlToImage());
            ImageView_title.setImageURI(uri);
        }
    }

    public void TextView_URL_click () {
        TextView_URL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri TextViewUri = Uri.parse(news.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, TextViewUri);
                startActivity(intent);
            }
        });
    }
}

