package unlv.com.bestpartytoday;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity  implements GoogleMap.OnCameraChangeListener {

    private static final int INITIAL_ZOOM_LEVEL = 12;

    private GoogleMap map;
    private Circle searchCircle;
    private LatLng location;
    private ImageButton listButton;
    private GeoQuery geoQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_maps);
        Firebase.setAndroidContext(this);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        this.map = mapFragment.getMap();
        this.map.setMyLocationEnabled(true);

        this.map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location usrLocation) {
                location = new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude());

                searchCircle = map.addCircle(new CircleOptions().center(location).radius(5000));
                searchCircle.setFillColor(Color.argb(30, 102, 163, 194));
                searchCircle.setStrokeColor(Color.argb(30, 0, 0, 0));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, INITIAL_ZOOM_LEVEL));
            }
        });

        setMarkers(this.map);

        listButton = (ImageButton) findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent
                        (MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setMarkers(final GoogleMap map) {

        Firebase eventsRef = new Firebase("https://fiery-heat-4759.firebaseio.com/Events");

        Query queryRef = eventsRef.orderByChild("rating");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                String name = String.valueOf(snapshot.child("name").getValue());
                float latitude = Float.parseFloat(String.valueOf(snapshot.child("latitude").getValue()));
                float longitude = Float.parseFloat(String.valueOf(snapshot.child("longitude").getValue()));
                String desc = String.valueOf(snapshot.child("short_desc").getValue());
                if(latitude != 0) {
                    map.addMarker(new MarkerOptions().snippet(desc)
                            .position(new LatLng(latitude, longitude))
                            .title(name));
                }
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

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        this.geoQuery.setRadius(radius/1000);
    }
}
