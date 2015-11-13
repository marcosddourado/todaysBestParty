package unlv.com.bestpartytoday;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
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
                float latitude = Float.parseFloat(String.valueOf(snapshot.child("latitude").getValue()));
                float longitude = Float.parseFloat(String.valueOf(snapshot.child("longitude").getValue()));

                addEvents(name, time, rating, image, latitude, longitude);
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

    private void addEvents(String name, String time, float rating, String image, float latitude, float longitude) {
        events.add(new Event(name, time, rating, image, latitude, longitude));
    }

    private void putEventsOnView() {
        ArrayAdapter<Event> adapter = new myListAdapter();
        ListView list = (ListView) findViewById(R.id.eventListView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event currentEvent = events.get(events.size()-(position+1));

                createRankingDialog(currentEvent);
            }
        });

        list.setAdapter(adapter);
    }

    private void createRankingDialog(final Event currentEvent) {
        Dialog rankDialog = new Dialog(MainActivity.this, R.style.FullHeightDialog);
        RatingBar ratingBar = new RatingBar(getApplicationContext());

        rankDialog = new Dialog(MainActivity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(3);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText(currentEvent.getName());

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        final Dialog finalRankDialog = rankDialog;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalRankDialog.dismiss();

                Intent intent = new Intent(MainActivity.this, LocationChecker.class);
                intent.putExtra("eventLatitude", currentEvent.getLatitude());
                intent.putExtra("eventLongitude", currentEvent.getLongitude());
                intent.putExtra("eventName", currentEvent.getName());
                startActivity(intent);
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    private class myListAdapter extends ArrayAdapter<Event> {
        public myListAdapter() {
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
