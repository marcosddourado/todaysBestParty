package unlv.com.bestpartytoday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Event> events = new ArrayList<Event>();
    private ImageButton nearbyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        getDataFromFB();

        nearbyButton = (ImageButton) findViewById(R.id.nearbyButton);
        nearbyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent
                        (MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFB() {

        Firebase eventsRef = new Firebase("https://fiery-heat-4759.firebaseio.com/Events");

        Query queryRef = eventsRef.orderByChild("rating");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                String name = String.valueOf(snapshot.child("name").getValue());
                String time = String.valueOf(snapshot.child("time").getValue());
                float rating = Float.parseFloat(String.valueOf(snapshot.child("rating").getValue()));
                String image = String.valueOf(snapshot.child("image").getValue());

                addEvents(name, time, rating, image);
                putEventsOnView();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void addEvents(String name, String time, float rating, String image) {
        events.add(new Event(name, time, rating, image));
    }

    private void putEventsOnView() {
        ArrayAdapter<Event> adaptor = new myListAdaptor();
        ListView list = (ListView) findViewById(R.id.eventListView);
        list.setAdapter(adaptor);
    }

    private class myListAdaptor extends ArrayAdapter<Event> {
        public myListAdaptor() {
            super(MainActivity.this, R.layout.eventlistlayout, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView= convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.eventlistlayout, parent, false);
            }


            //reversing the order of the list and setting it on the Actvity
            Event currentEvent = events.get(events.size()-(position+1));

            TextView nameText = (TextView) itemView.findViewById(R.id.list_eventName);
            nameText.setText(currentEvent.getName());

            String background = currentEvent.getImage();
            int drawableID = getResources().getIdentifier(background, "drawable", getPackageName());
            itemView.setBackgroundResource(drawableID);

            TextView timeText = (TextView) itemView.findViewById(R.id.list_eventTime);
            timeText.setText(currentEvent.getTime());

            RatingBar makeRate= (RatingBar) itemView.findViewById(R.id.list_eventRating);
            makeRate.setRating(currentEvent.getRating());

            return itemView;
        }

    }

}
