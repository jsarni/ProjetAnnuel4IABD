package com.pa.app.parkin;

import com.google.android.gms.maps.model.LatLng;

public class Horodateur {

    private LatLng geoPoint;
    private int numberOfPlaces;

    public Horodateur(double lat, double lng, int numberOfPlaces) {
        this.geoPoint = new LatLng(lat, lng);
        this.numberOfPlaces = numberOfPlaces;
    }

    public LatLng getGeoPoint() {
        return geoPoint;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }
}
