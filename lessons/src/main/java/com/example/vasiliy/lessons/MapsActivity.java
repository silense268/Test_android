package com.example.vasiliy.lessons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String LogTag = "Maps_Activity";
    String[] gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double longtituda ;
        double altituda ;
        mMap = googleMap;
        Intent intent = getIntent();
        Log.d(LogTag, "MapStart" );
        gps = intent.getStringArrayExtra("koor");
        BitmapDescriptor markerIcon;
        for (int i = 0; i<gps.length ; i=i+1) {
            if(i==(gps.length-1)){
                markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            }
            else {
                markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            }
            String minibs[] = gps[i].split("@");
            Log.d(LogTag, "koor" + minibs[0] + " " + minibs[2] + " " + minibs[3]);

            longtituda = Double.parseDouble(minibs[2].replace(",", "."));
            altituda = Double.parseDouble(minibs[3].replace(",", "."));
            mMap.addMarker(new MarkerOptions()
                    .icon(markerIcon)
                    .position(new LatLng(longtituda, altituda)).title((minibs[0] + " " + minibs[1])));
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(55, 61), 8);
        mMap.moveCamera(cameraUpdate);

    }
}
