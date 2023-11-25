package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExploreLocalityActivity extends AppCompatActivity implements OnMapReadyCallback {


    @BindView(R.id.btn_ok)
    Button btn_ok;

    private GoogleMap mGoogleMap;
    private boolean isMeeting = false;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_locality);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.LATITUDE)) {
            latitude = intent.getDoubleExtra(Constants.LATITUDE, 0.0d);
        }
        if (intent.hasExtra(Constants.LONGITUDE)) {
            longitude = intent.getDoubleExtra(Constants.LONGITUDE, 0.0d);
        }
        setMapView();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*viewing map*/
    private void setMapView() {
        FragmentManager fragmentManager = getSupportFragmentManager();/// getChildFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.detail_view_map);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.detail_view_map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        showMarkerOnMap();
    }

    /*Show the Map View*/
    private void showMarkerOnMap() {
       // mGoogleMap.clear();
        LatLng latLng;
        Marker marker;
        latLng = new LatLng(latitude, longitude);

        marker = mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                .position(latLng)
                .title("(" + latitude + "," + longitude + ")"));
        marker.setTag(0);/*
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));*/
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        // Zoom in, animating the camera.
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }


    /**
     * TO GET NEAREST LOCATIONS
     */
    private String getUrlForNearestLocations(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        int PROXIMITY_RADIUS = 10000;
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + Constants.MAP_API_KEY);
        /*Log.d("getUrl", googlePlacesUrl.toString());*/
        return (googlePlacesUrl.toString());
    }


}
