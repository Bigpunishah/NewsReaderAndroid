package com.example.newreaderch10;

import android.content.Context;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class FileIO {

    private final String URL_STRING = "https://news.google.com/rss";
    private final String FILENAME = "news_feed.xml";
    private Context context = null;

    //FileIO class constructor
    public FileIO (Context context){
        this.context = context;
    }

    public void downloadFile(){
        try{
            //get the URL
            URL url = new URL(URL_STRING);

            //get the input stream
            InputStream in = url.openStream();

            //get output stream
            FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            //read input & write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while(bytesRead != -1){
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            out.close();
            in.close();
        }
        catch (IOException e){
            Log.e("News reader", e.toString());
        }
    }//end downloadFile

    public RSSFeed readFile(){
        try {
            //get the xml reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlReader = parser.getXMLReader();

            //set content handler
            RSSFeedHandler theRssHandler = new RSSFeedHandler();
            xmlReader.setContentHandler(theRssHandler);

            //read the file from internal storage
            FileInputStream in = context.openFileInput(FILENAME);

            //parse the data
            InputSource is = new InputSource(in);
            xmlReader.parse(is);

            //set the feed in the activity
            RSSFeed feed = theRssHandler.getFeed();
            return feed;
        }
        catch (Exception e){
            Log.e("News reader", e.toString());
            return null;
        }
    }//end readFile
}//end FileIO
