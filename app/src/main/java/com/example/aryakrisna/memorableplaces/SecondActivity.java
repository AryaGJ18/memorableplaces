package com.example.aryakrisna.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class SecondActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
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
        Intent intent1 = getIntent();
        int place = intent1.getIntExtra("place", 0);
        if(place == 0) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //mMap.clear();
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    //mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    String address = "";
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            address = "";

                            if (addresses.get(0).getThoroughfare() != null) {
                                address += addresses.get(0).getThoroughfare() + " ";
                            }

                            if (addresses.get(0).getLocality() != null) {
                                address += addresses.get(0).getLocality() + " ";
                            }

                            if (addresses.get(0).getPostalCode() != null) {
                                address += addresses.get(0).getPostalCode() + " ";
                            }

                            if (addresses.get(0).getAdminArea() != null) {
                                address += addresses.get(0).getAdminArea() + "";
                            }

                            if (address.isEmpty()) {
                                address = "Couldn't find your address";
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions().position(userLocation).title(address));
                }


                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //mMap.clear();
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            }
        }else {
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(MainActivity.locations.get(intent1.getIntExtra("placeNumber",0)).latitude);
            placeLocation.setLongitude(MainActivity.locations.get(intent1.getIntExtra("placeNumber",0)).longitude);

            centerMapOnLocation(placeLocation, MainActivity.places.get(intent1.getIntExtra("placeNumber",0)));
        }


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String address = "";
                try{
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(addresses != null && addresses.size() > 0){
                        address = "";

                        if(addresses.get(0).getThoroughfare() != null){
                            address += addresses.get(0).getThoroughfare() + " ";
                        }

                        if(addresses.get(0).getLocality() != null){
                            address += addresses.get(0).getLocality() + " ";
                        }

                        if(addresses.get(0).getPostalCode() != null){
                            address += addresses.get(0).getPostalCode() + " ";
                        }

                        if(addresses.get(0).getAdminArea() != null) {
                            address += addresses.get(0).getAdminArea() + "";
                        }

                        if(address.isEmpty()){
                            address = "Couldn't find your address";
                        }



                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                mMap.addMarker(new MarkerOptions().position(latLng).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                MainActivity.places.add(address);
                MainActivity.locations.add(latLng);
                MainActivity.arrayAdapter.notifyDataSetChanged();

            }
        });



    }
    public void centerMapOnLocation(Location location, String title) {
        if (location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
        }
    }
}
