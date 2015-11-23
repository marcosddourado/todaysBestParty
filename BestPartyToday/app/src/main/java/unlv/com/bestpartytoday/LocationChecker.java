package unlv.com.bestpartytoday;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationChecker extends FragmentActivity implements OnMapReadyCallback {
    private static final int INITIAL_ZOOM_LEVEL = 12;

    private GoogleMap map;
    private Circle circle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_checker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

        Intent intent = getIntent();
        float latitude = intent.getFloatExtra("eventLatitude", 0);
        float longitude = intent.getFloatExtra("eventLongitude", 0);
        String name = intent.getStringExtra("eventName");
        LatLng eventLocation = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name));

        circle = map.addCircle(new CircleOptions().center(eventLocation).radius(500));
        circle.setFillColor(Color.argb(30, 102, 163, 194));
        circle.setStrokeColor(Color.argb(30, 0, 0, 0));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, INITIAL_ZOOM_LEVEL));

        Log.i("maptest", "55");

        this.map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location usrLocation) {
                float[] distance = new float[2];

                Location.distanceBetween(usrLocation.getLatitude(), usrLocation.getLongitude(),
                        circle.getCenter().latitude, circle.getCenter().longitude, distance);

                Dialog locationDialog = new Dialog(LocationChecker.this, R.style.FullHeightDialog);

                locationDialog = new Dialog(LocationChecker.this, R.style.FullHeightDialog);
                locationDialog.setContentView(R.layout.location_checker_dialog);
                locationDialog.setCancelable(true);
                TextView text = (TextView) locationDialog.findViewById(R.id.checker_dialog_text);


                if (distance[0] <= circle.getRadius()) {
                    text.setText("Successfully rated. Have an awesome party! ");
                } else {
                    text.setText("You need to be inside the party to rank it.");
                }

                Button updateButton = (Button) locationDialog.findViewById(R.id.rank_dialog_button);
                final Dialog finalRankDialog = locationDialog;
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalRankDialog.dismiss();

                        Intent intent = new Intent(LocationChecker.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                locationDialog.show();
            }
        });
    }
}
