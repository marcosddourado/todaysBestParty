package com.example.kyle.testprogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Event> events = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvents();
        putEventsOnView();
    }

    private void addEvents() {
        events.add(new Event("Study Session","12:00PM-4:00PM",4F,1));
        events.add(new Event("Review Session","11:00PM-2:00PM",5F,1));
        events.add(new Event("Greek Week","2:00PM-5:00PM",2.5F,1));
        events.add(new Event("Free Food","3:00PM-4:00PM",1F,1));
        events.add(new Event("Closed Building","12:00PM-4:00PM",3.5F,1));
        events.add(new Event("Study Session","12:00PM-4:00PM",4F,1));
        events.add(new Event("Review Session","11:00PM-2:00PM",5F,1));
        events.add(new Event("Greek Week","2:00PM-5:00PM",2.5F,1));
        events.add(new Event("Free Food","3:00PM-4:00PM",1F,1));
        events.add(new Event("Closed Building","12:00PM-4:00PM",3.5F,1));
    }

    private void putEventsOnView() {
        ArrayAdapter<Event> adaptor = new myListAdaptor();
        ListView list = (ListView) findViewById(R.id.eventListView);
        list.setAdapter(adaptor);
    }

    private class myListAdaptor extends ArrayAdapter<Event> {
        public myListAdaptor() {
            super(MainActivity.this,R.layout.eventlistlayout, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView= convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.eventlistlayout, parent, false);

            Event currentEvent = events.get(position);

            TextView nameText = (TextView) itemView.findViewById(R.id.list_eventName);
            nameText.setText(currentEvent.getName());

            TextView timeText = (TextView) itemView.findViewById(R.id.list_eventTime);
            timeText.setText(currentEvent.getTime());

            RatingBar makeRate= (RatingBar) itemView.findViewById(R.id.list_eventRating);
            makeRate.setRating(currentEvent.getRating());

            return itemView;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
