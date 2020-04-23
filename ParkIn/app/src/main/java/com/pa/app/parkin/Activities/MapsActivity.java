package com.pa.app.parkin.Activities;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pa.app.parkin.R;
import com.pa.app.parkin.Utils.DatePickerFragment;

import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    private GoogleMap mMap;
    private Calendar dateOfSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        final View searchBox = (View) findViewById(R.id.search_box);
        final EditText searchAddress = (EditText) findViewById(R.id.search_adress);
        final Button searchDate = (Button) findViewById(R.id.search_date_button);
        final Button searchHour = (Button) findViewById(R.id.search_hour_button);
        final EditText searchPerimeter = (EditText) findViewById(R.id.search_perimeter);
        final Button searchButton = (Button) findViewById(R.id.search_button);

        final Button parkingButton = (Button) findViewById(R.id.parking_button);
        final ImageButton profileButton = (ImageButton) findViewById(R.id.profile_button);

        searchBox.setVisibility(View.GONE);
        searchAddress.setVisibility(View.GONE);
        searchDate.setVisibility(View.GONE);
        searchHour.setVisibility(View.GONE);
        searchPerimeter.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);


        parkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHide(searchBox);
                showHide(searchAddress);
                showHide(searchDate);
                showHide(searchHour);
                showHide(searchPerimeter);
                showHide(searchButton);
            }
        });

        searchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
//                int selectedYear = dateOfSearch.get(Calendar.YEAR);
//                int selectedMonth = dateOfSearch.get(Calendar.MONTH);
//                int selectedDay = dateOfSearch.get(Calendar.DAY_OF_MONTH);
//                searchDate.setText(selectedDate);
            }
        });

        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng paris = new LatLng(48.855964, 2.341282);
        mMap.setMinZoomPreference(13);
        mMap.addMarker(new MarkerOptions().position(paris).title("Marker in Paris"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
    }

    private void showHide(View view){
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateOfSearch = Calendar.getInstance();
        dateOfSearch.set(Calendar.YEAR, year);
        dateOfSearch.set(Calendar.MONTH, month);
        dateOfSearch.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth ,month, year);
        Button searchDate = (Button) findViewById(R.id.search_date_button);
        searchDate.setText(selectedDate);
    }
}
