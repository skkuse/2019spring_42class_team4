package com.example.docking_milkyway;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class GPSHelper extends Service implements LocationListener {
    private final Context Gcontext;
    Location loca = new Location("dummyProvide");
    protected LocationManager locaManager;

    boolean isGpsEnabled = false;
    boolean isNetEnabled = false;
    int hasFineLocationPermission;
    int hasCoarseLocationPermission;
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    public static final long MIN_TIME_UPDATES = 1000 * 60 * 1;

    public double lat;    // 위도
    public double lon;    // 경도

   public GPSHelper(Context context, LocationManager locationManager){
        this.Gcontext = context;
        this.locaManager = locationManager;

        getlocation();
    }

    public Location getlocation() {

        try {
            isGpsEnabled = locaManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGpsEnabled) {
                Log.d("상아", "getlocation : GPS 사용가능" + isGpsEnabled);    }
            else{
                Log.d("상아", "getlocation : GPS 사용불가" + isGpsEnabled);
            }

            isNetEnabled = locaManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetEnabled) {
                Log.d("상아", "getlocation : NET 사용가능" + isNetEnabled);    }
            else{
                Log.d("상아", "getlocation : NET 사용불가" + isNetEnabled);
            }

            // check network & gps
            if (!isGpsEnabled && !isNetEnabled) {
                // 네트워크 gps 모두 안되는 경우
                Log.d("상아","getlocation :현재 위치 정보를 받아올 수 없습니다.");
                Toast.makeText(Gcontext.getApplicationContext(), "현재 위치 정보를 받아올 수 없습니다.", Toast.LENGTH_LONG).show();
                return null;
            }



        } catch (Exception e) {
            e.printStackTrace();
            Log.d("상아", "getlocation : 퍼미션 실패");
            return null;
        }

        try {
            // if Net Enabled - get location using Net
            if (isNetEnabled) {
                locaManager.requestLocationUpdates(locaManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locaManager != null) {
                    loca = locaManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.d("상아", "getlocation :네트워크로 위치정보 가져오기 성공");
                    if (loca != null) {
                        lat = loca.getLatitude();
                        lon = loca.getLongitude();
                        Log.d("상아", "getlocation :lat : "+lat+" / lon : "+lon);
                    }
                }
            }

            // else if gps Enabled = get location using Gps
            else if (isGpsEnabled) {
                locaManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locaManager != null) {
                    loca = locaManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.d("상아", "getlocation :GPS로 위치정보 가져오기 성공");
                    if (loca != null) {
                        lat = loca.getLatitude();
                        lon = loca.getLongitude();
                        Log.d("상아", "getlocation :lat : "+lat+" / lon : "+lon);
                    }
                }
            }

        } catch (SecurityException se ){
            Log.d("상아", "getlocation : secu Exception");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("상아", "getlocation : Location setting 실패");
            Log.d("상아"," : "+e.toString());
            return null;
        }
        return loca;
    }

    public double getLatitude() {
        if(loca != null)    {   lat = loca.getLatitude();   }
        return lat;
    }

    public double getLongitude() {
       if(loca != null)     {   lon = loca.getLongitude();  }
       return lon;
   }

    @Override
    public IBinder onBind(Intent intent) {  return null;    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}


}
