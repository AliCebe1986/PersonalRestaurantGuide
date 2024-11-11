package ca.gbc.comp3074.personalrestaurantguide.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.gbc.comp3074.personalrestaurantguide.R;
import ca.gbc.comp3074.personalrestaurantguide.models.Restaurant;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        if (getIntent().hasExtra("restaurant")) {
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        } else {

            Log.e("MapActivity", "No restaurant information found.");
            finish();
            return;
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("MapActivity", "Could not create map.");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = getLocationFromAddress(restaurant.getAddress());
        if (location != null) {
            mMap.addMarker(new MarkerOptions().position(location).title(restaurant.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        } else {

            Log.e("MapActivity", "Location not found.");
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;

        try {

            addressList = geocoder.getFromLocationName(strAddress, 1);
            if (addressList == null || addressList.isEmpty()) {
                return null;
            }
            Address location = addressList.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

