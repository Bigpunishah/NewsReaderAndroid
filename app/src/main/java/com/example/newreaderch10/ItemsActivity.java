package com.example.newreaderch10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsActivity extends Activity implements AdapterView.OnItemClickListener {

    private RSSFeed feed;
    private FileIO io;

    private TextView titleTextView;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        io = new FileIO(getApplicationContext());

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.itemsListView);

//        itemsListView.setOnClickListener(this);
        itemsListView.setOnItemClickListener(this);

        new DownloadFeed().execute();
    }//End onCreate

    class DownloadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            io.downloadFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed downloaded");
            new ReadFeed().execute();
        }
    }//end DownloadFeed

    class ReadFeed extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params){
            feed = io.readFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.d("News reader", "Feed read");

            //Update the display for the activity
            ItemsActivity.this.updateDisplay();
        }
    }//end ReadFeed

    public void updateDisplay(){

        if(feed == null){
            titleTextView.setText("Unable to get RSS feed");
            return;
        }
        //set the title for the feed
        titleTextView.setText(feed.getTitle());
        //get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();
        //create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        for (RSSItem item : items){
            HashMap<String, String> map = new HashMap<>();
            map.put("date", item.getPubDateFormatted());
            map.put("title", item.getTitle());
            data.add(map);
        }
        //create the resource, from and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "title"};
        int[] to = {R.id.pubDateTextView, R.id.titleTextView};
        //create & set the adapter
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
        Log.d("News reader", "Feed displayed");
    }//end update Display
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //get the item at the specified position
        RSSItem item = feed.getItem(position);

        //create an intent
        Intent intent = new Intent(this, ItemsActivity.class);

        intent.putExtra("pubDate", item.getPubDate());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("link", item.getLink());

        this.startActivity(intent);
    }//end onItemClick
}//end ItemsActivity





