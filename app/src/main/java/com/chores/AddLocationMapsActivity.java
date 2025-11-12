package com.chores;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.chores.databinding.ActivityAddLocationMapsBinding;

public class AddLocationMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat=51.54533471115648,lng=0.009689958867871074;
    private ActivityAddLocationMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddLocationMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        binding.btnConfirmLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.putExtra("lat",lat+"");
                intent.putExtra("lng",lng+"");
                setResult(100,intent);
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng defaultLoc = new LatLng(51.54533471115648, 0.009689958867871074);
        mMap.addMarker(new MarkerOptions().position(defaultLoc).title("Marker in East London"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 16.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng newLoc)
            {
                lat = newLoc.latitude;
                lng = newLoc.longitude;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(newLoc).title("Marker in East London"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 16.0f));
            }
        });
        /*double lng = Double.parseDouble(getIntent().getStringExtra("lng"));
        double lat = Double.parseDouble(getIntent().getStringExtra("lat"));

        LatLng TutorialsPoint = new LatLng(lng, lat);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(getIntent().getStringExtra("sname"))
                .snippet("You have to Pay:"+getIntent().getStringExtra("fee")));*/
    }
}