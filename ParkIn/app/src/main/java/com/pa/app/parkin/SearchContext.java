package com.pa.app.parkin;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class SearchContext {
    private LatLng position;
    private double perimeter;
    private Calendar dateTimeContext;

    public SearchContext(LatLng position, double perimeter, Calendar dateTimeContext) {
        this.position = position;
        this.perimeter = perimeter;
        this.dateTimeContext = dateTimeContext;
    }

    public LatLng getPosition() {
        return position;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public Calendar getDateTimeContext() {
        return dateTimeContext;
    }


}
