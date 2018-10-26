package com.backcountrydesigngroup.android.pandasleuth;

public class article {

    // String value - name of article
    private String mName;

    // String value - section article has been placed in
    private String mSection;

    // String value - date
    private String mDate;

    // String value - byline
    private String mByline;

    // String value - url
    private String mURL;


    // article constructor
    public article(String name, String byline, String date, String section, String url) {
        mName = name;
        mByline = byline;
        mDate = date;
        mSection = section;
        mURL = url;
    }

    // article setter, if needed
    public void setArticle(String name, String byline, String date, String section, String url) {
        mName = name;
        mByline = byline;
        mDate = date;
        mSection = section;
        mURL = url;
    }

    // getter method for the article name
    public String getName(){
        return mName;
    }

    // getter method for the article author
    public String getByline(){
        return mByline;
    }

    // getter method for the article date
    public String getDate(){
        return mDate;
    }

    // getter method for the article section name
    public String getSection(){
        return mSection;
    }

    // getter method for the article web URL
    public String getArticleURL(){
        return mURL;
    }

}