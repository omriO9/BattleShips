package com.example.omri.battleShip;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.omri.battleShip.Data.shipsOpenHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;


public class MapFragment extends Fragment implements OnMapReadyCallback ,LocationListener {

    private static final String TAG = TableFragment.class.getSimpleName();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private Location currentLocation =null;
    private LocationManager locationManager;
    private boolean didAlreadyRequestLocationPermission;

    private String difficulty;
    private shipsOpenHelper dbHelper;
    private SQLiteDatabase db ;
    private HashMap <Marker,Integer> markersMap;


    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;



    public MapFragment(){

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper= new shipsOpenHelper(getActivity());// might be getContext @@@
        db =dbHelper.getReadableDatabase();
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        markersMap = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        googleMap = (GoogleMap)
//
//       LatLngBounds latLngBounds = new LatLngBounds(new LatLng(32.109333,34.855499),new LatLng(32.175,34.90694));
//       googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.getCenter(), 10));
//      //  showMarkers(difficulty);


        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);

        if(mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (requestLocationPermissionsIfNeeded(false)) {
            boolean isAllowedToUseLocation = isPermissionForLocationServicesGranted();
            if (isAllowedToUseLocation) {
                double LatLong[] = getLocation();
                CameraPosition currentLocation = CameraPosition.builder().target(new LatLng(LatLong[0],LatLong[1])).zoom(12).bearing(0).tilt(45).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentLocation));
            }
        }

        //googleMap.addMarker(new MarkerOptions().position(new LatLng(LatLong[0],LatLong[1])).title("You Are Here!"));

//        googleMap.addMarker(new MarkerOptions().position(new LatLng(32.178195,34.907610)).title("BOOM"));
//        CameraPosition boom = CameraPosition.builder().target(new LatLng(32.178195,34.907610)).zoom(16).bearing(0).tilt(45).build();


        if (getArguments()!=null) {
            difficulty = getArguments().getString("difficulty");
        }
        showMarkers(difficulty);
    }




    @SuppressLint("MissingPermission")
    public double[] getLocation() {

        Location location;
        double latitude = -1, longitude = -1;

        // checking if user have the necessary permissions , if not we are asking him to allow those permissions

            if (currentLocation == null) {
                // get current location
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            float metersToUpdate = 1;
            long intervalMilliseconds = 1000;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, intervalMilliseconds, metersToUpdate, this);

        if (currentLocation != null) {
            latitude = currentLocation.getLatitude();
            Log.d(TAG, "getLocation: latitude: " + latitude);
            longitude = currentLocation.getLongitude();
            Log.d(TAG, "getLocation: longitude: " + longitude);
        }

        return new double[]{latitude,longitude};
    }

    private boolean isPermissionForLocationServicesGranted() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (!(getActivity().getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        getActivity().getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));

    }

    private boolean requestLocationPermissionsIfNeeded(boolean byUserAction) {
        boolean isAccessGranted;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
            String coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION;
            isAccessGranted = getActivity().getApplicationContext().checkSelfPermission(fineLocationPermission) == PackageManager.PERMISSION_GRANTED &&
                    getActivity().getApplicationContext().checkSelfPermission(coarseLocationPermission) == PackageManager.PERMISSION_GRANTED;
            if (!isAccessGranted) { // The user blocked the location services of THIS app / not yet approved

                if (!didAlreadyRequestLocationPermission || byUserAction) {
                    didAlreadyRequestLocationPermission = true;
                    String[] permissionsToAsk = new String[]{fineLocationPermission, coarseLocationPermission};
                    // IllegalArgumentException: Can only use lower 16 bits for requestCode
                    ActivityCompat.requestPermissions(getActivity(), permissionsToAsk, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        } else {
            // Because the user's permissions started only from Android M and on...
            isAccessGranted = true;
        }

        return isAccessGranted;
    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                getLocation();
                break;
        }
    }

    private void showMarkers(String difficulty) {

        db.beginTransaction();

        String selectQuery = "SELECT * FROM "+shipsOpenHelper.SCORES_TABLE+" WHERE difficulty='"+difficulty+"' ORDER BY score ASC LIMIT 10";
        Cursor cursor = db.rawQuery(selectQuery,null);

        try{
        if(cursor.getCount() >0){
            while (cursor.moveToNext()) {

                String name = cursor.getString(1);
                double latitude = cursor.getDouble(4);
                double longitude = cursor.getDouble(5);

                Log.d(TAG, "showMarkers: " + latitude + " " + longitude);

                LatLng locationToSetMarkerAt = new LatLng(latitude, longitude);
                //Marker marker = //new MarkerOptions().position(temp).title(name);
                final int id = cursor.getInt(0);
                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(locationToSetMarkerAt).title(name));
                markersMap.put(marker,id);
                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                  @Override
                  public boolean onMarkerClick(Marker marker) {
                      Log.d(TAG, "onMarkerClick: Marker ="+marker.getPosition().toString());
                      Log.d(TAG, "onMarkerClick: mapvalues:"+markersMap.toString());
                      if (markersMap.get(marker)!=null) {
                          marker.showInfoWindow();

                          int id = markersMap.get(marker).intValue();
                          Log.d(TAG, "onMarkerClick: id="+id);
                      }

                   //   sendUpdateToActivity(id);
                  return true;
                  }
                });



            }


        }
            db.setTransactionSuccessful();
        }catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

    }




}
