package com.example.leaflens.Entity;

public class News {

    private String newsId;
    private String newsTitle;
    private String text;

    private String author;

    private String date;

    public News(String newsId, String newsTitle, String author, String text, String date)
    {
        this.newsId = newsId;
        this.newsTitle = newsTitle;
        this.author = author;
        this.text = text;
        this.date = date;
    }


    public void setNewsId(String newsId)
    {
        this.newsId = newsId;
    }

    public String getNewsId()
    {
        return newsId;
    }

    public void setNewsTitle(String title)
    {
        newsTitle = title;
    }

    public String getNewsTitle()
    {
        return this.newsTitle;
    }
    public void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {return text;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getAuthor()
    {
        return author;
    }
}
