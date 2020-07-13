package com.pa.app.parkin.Utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.List;

public class DevUtils {

    private static DevUtils INSTANCE = new DevUtils();

    public static DevUtils getInstance()
    {
        return INSTANCE;
    }

    public void showHide(View view){
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public String formattedDateToString(Calendar date) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);


        return String.format("%02d/%02d/%d", day ,month, year);
    }

    public String formattedDateToString2(Calendar date) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);


        return String.format("%02d-%02d-%d", day ,month, year);
    }

    public String formattedHourToString(Calendar date){
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minute);
    }

    public double parseStringToDouble(String number){
        double parsedNumber;
        try {
            parsedNumber = Double.parseDouble(number);
        } catch (Exception ex){
            parsedNumber = -1;
        }
        return parsedNumber;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
            return null;
        }
        return p1;
    }

    public int getZoomLevel(double radius) {
        double scale = radius / 500;
        return (int) (15.5 - Math.log(scale) / Math.log(2));
    }

    public void showToast(Activity activity, String message){
        Context context = activity.getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public double getDistanceInMeter(LatLng pointA, LatLng pointB) {
        Location sourceLoc = new Location(LocationManager.GPS_PROVIDER);
        sourceLoc.setLatitude(pointA.latitude);
        sourceLoc.setLongitude(pointA.longitude);
        Location destLoc = new Location(LocationManager.GPS_PROVIDER);
        destLoc.setLatitude(pointB.latitude);
        destLoc.setLongitude(pointB.longitude);
        return sourceLoc.distanceTo(destLoc);
    }

    public LatLng getMiddleLatLng(LatLng pointA, LatLng pointB) {
        return new LatLng((pointA.latitude + pointB.latitude) /2, (pointA.longitude + pointB.longitude) /2);
    }

    public LatLng latLngFromLocation(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
