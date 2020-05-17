package com.pa.app.parkin;

import com.google.android.gms.maps.model.LatLng;

public class Horodateur {

    private String id;
    private LatLng geoPoint;
    private int numberOfPlaces;

    public Horodateur(String id, LatLng geoPoint, int numberOfPlaces) {
        this.id = id;
        this.geoPoint = geoPoint;
        this.numberOfPlaces = numberOfPlaces;
    }

    public Horodateur(String id, double lat, double lng, int numberOfPlaces) {
        this.id = id;
        this.geoPoint = new LatLng(lat, lng);
        this.numberOfPlaces = numberOfPlaces;
    }

    public String getId() {
        return id;
    }

    public LatLng getGeoPoint() {
        return geoPoint;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }
}
