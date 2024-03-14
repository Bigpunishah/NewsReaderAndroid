package com.example.newreaderch10;


import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class RSSItem {

    private String title = null;
    private String description = null;
    private String link = null;
    private String pubDate = null;


    private SimpleDateFormat dateOutFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");

    private SimpleDateFormat dateInFormat = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss ZZZZ");

    public void setTitle(String title) { this.title = title;}

    public String getTitle(){return title;}

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return description;}

    public String getLink(){return link;}

    public void setLink(String link){this.link = link;}


    public void setPubDate(String pubDate){this.pubDate = pubDate;}

    public String getPubDate(){return pubDate;}

    public String getPubDateFormatted(){
        try{
            if(pubDate != null){
                Date date = dateInFormat.parse(pubDate);
                String pubDateFormatted = dateOutFormat.format(date);
                return pubDateFormatted;
            }
            else {
                return "No date in RSS feed";
            }
        }
        catch (ParseException e){
            return "No date in RSS feed";
        }
    }

}
