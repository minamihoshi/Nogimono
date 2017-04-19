package org.nogizaka46.bean;

/**
 * Created by acer on 2017/4/18.
 */

public class BlogBean {


    /**
     * post : 1492530000
     * author : 斎藤ちはる
     * title : 座右の銘
     * summary : ちはるーむへようこそ今日のちはるーむではチョコミントアイスは歯磨き粉の味か否...
     * url : http://blog.nogizaka46.com/chiharu.saito/smph/2017/04/038171.php
     */

    private int post;
    private String author;
    private String title;
    private String summary;
    private String url;

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
