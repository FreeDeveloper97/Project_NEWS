package com.freedeveloper.simple_news;

import java.io.Serializable;

public class NewsData implements Serializable {
    private String title;
    private String urlToImage;
    private String content;
    private String Url;

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }
}
