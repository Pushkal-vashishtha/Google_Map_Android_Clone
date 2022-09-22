package com.google.myapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.myapplication.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Button btn;
    EditText et1;
    LatLng lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        et1 = findViewById(R.id.et1);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try {
                    ArrayList<Address> locations = (ArrayList<Address>) geocoder.getFromLocationName(et1.getText().toString(), 5);
                    Address address = locations.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    mMap.animateCamera(cu);
                    MarkerOptions mo = new MarkerOptions();
                    mo.position(latLng);
                    mo.title(et1.getText().toString());
                    mMap.clear();
                    mMap.addMarker(mo);
                    String a = address.getAddressLine(0) + "--" + address.getAddressLine(1) + "--" + address.getLocality() + "--" + address.getCountryName();
                    et1.setText(a);
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.m1) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if (item.getItemId() == R.id.m2) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        if (item.getItemId() == R.id.m3) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        if (item.getItemId() == R.id.m4) {
            goToMyLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    void goToMyLocation() {
        if (lng == null) {
            LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    lng=new LatLng(location.getLatitude(),location.getLongitude());
    Toast.makeText(this,"Please wait..................",Toast.LENGTH_LONG).show();
}else{
    CameraUpdate update=CameraUpdateFactory.newLatLngZoom(lng,15);
    mMap.animateCamera(update);
    MarkerOptions m=new MarkerOptions();
    m.position(lng);
    mMap.clear();
    mMap.addMarker(m);
}
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this,"GPS WORKING......",Toast.LENGTH_SHORT).show();
        lng=new LatLng(location.getLatitude(),location.getLongitude());
    }
}