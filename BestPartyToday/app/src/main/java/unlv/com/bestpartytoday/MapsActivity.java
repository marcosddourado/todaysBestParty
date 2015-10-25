package unlv.com.bestpartytoday;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity  implements GoogleMap.OnCameraChangeListener {

    private static final int INITIAL_ZOOM_LEVEL = 12;


    private GoogleMap map;
    private Circle searchCircle;
    private GeoQuery geoQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfvehicles);
        Firebase.setAndroidContext(this);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        this.map = mapFragment.getMap();
        this.map.setMyLocationEnabled(true);

        // Add a new marker to the map
        Marker marker = this.map.addMarker(new MarkerOptions().position(new LatLng(36.112354, -115.172482)));

        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location usrLocation) {
                LatLng location = new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude());

                searchCircle = map.addCircle(new CircleOptions().center(location).radius(5000));
                searchCircle.setFillColor(Color.argb(30, 102, 163, 194));
                searchCircle.setStrokeColor(Color.argb(30, 0, 0, 0));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, INITIAL_ZOOM_LEVEL));
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
