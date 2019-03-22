package com.milan.brtshelper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Comparator;
import java.util.List;

public class NearbyStationsActivity extends AppCompatActivity {

    int no_of_stations=40;
    Station stations[];
    LocationListener locationListener;
    LocationManager locationManager;
    DistanceArray distanceFromStations[] = new DistanceArray[no_of_stations] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_stations);
        ImageView img = (ImageView)findViewById(R.id.imageview_back_nearby);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        stations=Station.getStations(this.getAssets());
         locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);

         locationListener = new LocationListener() {
             @Override
             public void onLocationChanged(Location location) {

                 for(int i=0;i<no_of_stations;i++)
                 {
                     distanceFromStations[i]=new DistanceArray();
                     distanceFromStations[i].setS(stations[i].name);
                     distanceFromStations[i].setDist((distFrom(stations[i].latitude,stations[i].longnitude,location.getLatitude(),location.getLongitude()))/1000);
                 }

                 ArrayList<DistanceArray> display = new ArrayList<DistanceArray>();

                 Arrays.sort(distanceFromStations, new MyComparator());
                 for(int k=0;k<no_of_stations;k++) {
                     display.add(distanceFromStations[k]);
                     Log.i("sorted stations", Float.toString((display.get(k).dist/1000))+"  "+display.get(k).s);

                 }


                 RecyclerView recyclerView = (RecyclerView) findViewById(R.id.nearbyRecyclerView);

                 MyNearbyStationAdapter adapter = new MyNearbyStationAdapter(NearbyStationsActivity.this, display);
                 recyclerView.setAdapter(adapter);

                 LinearLayoutManager layoutManager = new LinearLayoutManager(NearbyStationsActivity.this);
                 layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                 recyclerView.setLayoutManager(layoutManager);

                 recyclerView.setItemAnimator(new DefaultItemAnimator());



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
         };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.ACCESS_FINE_LOCATION)){}
            else {
                ActivityCompat.requestPermissions(this , new String[] {Manifest.permission.ACCESS_FINE_LOCATION} ,1);
            }
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,50,locationListener);

    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    class MyComparator implements Comparator<DistanceArray>
    {

        @Override
        public int compare(DistanceArray o1,DistanceArray o2) {
            return (int)(o1.dist-o2.dist);
        }
    }
}
