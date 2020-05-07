package com.pa.app.parkin.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;

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
        LatLng p1 = null;

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
}
