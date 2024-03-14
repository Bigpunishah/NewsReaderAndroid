package com.example.newreaderch10;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSFeedHandler extends DefaultHandler {

    private RSSFeed feed;
    private RSSItem item;

    //Define booleans to determine various parsed fields

    private boolean feedTitleHasBeenParsed = false;
    private boolean feedPubDateHasBeenRead = false;


    private boolean isTitle = false;
    private boolean isDescription = false;
    private boolean isLink = false;
    private boolean isPubDate = false;  //pub date for feed
    private boolean isOrigLink = false; //pub date for items

    public RSSFeed getFeed(){
        return feed;
    }

    @Override
    public void startDocument() throws SAXException{
        feed = new RSSFeed();
        item = new RSSItem();
    }

    @Override
    public void endDocument() throws SAXException{
        //This wont e used but if processing is needed after the parser finishes we can add it here.
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException{

        if(qName.equals("item")){
            item = new RSSItem();
            return;
        }
        else if(qName.equals("title")){
            isTitle = true;
            return;
        }
        else if(qName.equals("description")){
            isDescription = true;
            return;
        }
        else if(qName.equals("link")){
            isLink = true;
            return;
        }
        else if(qName.equals("pubDate")){   //pub date fo feed
            isPubDate = true;
            return;
        }
        else if(qName.equals("feedburner:origLink")){   //pub date for item
            isOrigLink = true;
            return;
        }

    }//End startElement

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException{

        if(qName.equals("item")){
//            feed.addItem(item);
            feed.addItems(item);
            return;
        }//end endElement
    }//end RSSFeedHandler

    @Override
    public void characters(char ch[], int start, int length){
        String s = new String(ch, start, length);
        //parse the title
        if(isTitle){
            if(feedTitleHasBeenParsed == false){
                feed.setTitle(s);
                feedTitleHasBeenParsed = true;
            }
            else {
                if(s.startsWith("<")){
                    item.setTitle("No title available.");
                }
                else if(s.length() > 60){
                    //set s as description
                    item.setDescription(s);

                    //truncate s and set as title
                    int endIndex = s.indexOf(" ", 50);
                    if(endIndex == -1){
                        endIndex = 60;
                    }
                    String title = s.substring(0, endIndex);
                    title += "...";
                    item.setTitle(title);
                }
                else {
                    item.setTitle(s);
                    item.setDescription(s); //only necessary for very short descriptions
                }
            }
            isTitle = false;
        } // end parse Title

        else if(isLink){
            item.setLink(s);
            isLink = false;
        }// end parse link
        else if(isDescription){
            if(!s.startsWith("<")){    //Make sure s does not start with <
                item.setDescription(s);
            }
            isDescription = false;
        }//end parse description
        else if(isPubDate){
            if(feedPubDateHasBeenRead == false){
                feed.setPubDate(s);
                feedPubDateHasBeenRead = true;

            }
            else {
                item.setPubDate(s);
            }
            isPubDate = false;
        }// end pub date
        else if(isOrigLink){
            String dateParts[] = s.split("/"); // get date from url
            String date;
            if(dateParts.length > 8){
                String year = dateParts[3];
                String month = dateParts[4];
                String day = dateParts[5];
                if(s.startsWith("http://www.cnn.com/videos")){
                    year = dateParts[5];
                    month = dateParts[6];
                    day = dateParts[7];
                }
                date = year + "-" + month + "-" + day;
            }
            else{
                date = "No publication date available";
            }
            item.setPubDate(date);  //use yyy-MM-dd format
            isOrigLink = false;
        }//end item publication date
    }//End characters
}//End RSSFeedHandler

