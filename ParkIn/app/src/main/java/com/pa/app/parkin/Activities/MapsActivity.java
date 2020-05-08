package com.pa.app.parkin.Activities;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pa.app.parkin.R;
import com.pa.app.parkin.Utils.DatePickerFragment;
import com.pa.app.parkin.Utils.DevUtils;
import com.pa.app.parkin.Utils.PermissionManager;
import com.pa.app.parkin.Utils.TimePickerFragment;

import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private GoogleMap mMap;
    private Calendar dateOfSearch;
    private double perimeter;
    private LatLng searchPoint;

    private DevUtils myUtils = DevUtils.getInstance();
    private Location lastKnownLocation;
    private FusedLocationProviderClient locationProvider;

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
        final ImageButton selectCurrentAddressButton = (ImageButton) findViewById(R.id.current_address_button);

        final Button searchButton = (Button) findViewById(R.id.search_button);

        final Button parkingButton = (Button) findViewById(R.id.parking_button);
        final ImageButton positionButton = (ImageButton) findViewById(R.id.position_button);
        final ImageButton profileButton = (ImageButton) findViewById(R.id.profile_button);

        searchBox.setVisibility(View.GONE);
        searchAddress.setVisibility(View.GONE);
        searchDate.setVisibility(View.GONE);
        searchHour.setVisibility(View.GONE);
        searchPerimeter.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        selectCurrentAddressButton.setVisibility(View.GONE);

        positionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gotPermissions()) {
                    locationProvider = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

                    locationProvider.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            lastKnownLocation = location;
                        }
                    });
                    refreshMapToCurrentPosition();
                } else {
                    askForPermissions();
                    if(!gotPermissions()){
                        Context context = getApplicationContext();
                        CharSequence errorText = getString(R.string.location_permission_error_message);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, errorText, duration);
                        toast.show();
                    }
                }
            }
        });

        parkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUtils.showHide(searchBox);
                myUtils.showHide(searchAddress);
                myUtils.showHide(searchDate);
                myUtils.showHide(searchHour);
                myUtils.showHide(searchPerimeter);
                myUtils.showHide(searchButton);
                myUtils.showHide(selectCurrentAddressButton);
            }
        });

        selectCurrentAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gotPermissions()) {
                    locationProvider = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

                    locationProvider.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            lastKnownLocation = location;
                            searchPoint = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    });
                } else {
                    askForPermissions();
                    if(!gotPermissions()){
                        Context context = getApplicationContext();
                        CharSequence errorText = getString(R.string.location_permission_error_message);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, errorText, duration);
                        toast.show();
                    }
                }
                searchAddress.setText("Position Actuelle");
            }
        });
        searchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        searchHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("buttons", "clicked on search button to find places");
                Context context = getApplicationContext();

                String address = searchAddress.getText().toString();
                String strPerimeter = searchPerimeter.getText().toString();

                perimeter = myUtils.parseStringToDouble(strPerimeter);
                if (!address.equals("Position Actuelle")) {
                    searchPoint = myUtils.getLocationFromAddress(context, address);
                }
                if (searchPoint == null) {
                    CharSequence errorText = getString(R.string.address_error_message);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, errorText, duration);
                    toast.show();
                } else if (perimeter <= 0) {
                    CharSequence errorText = getString(R.string.perimeter_error_message);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, errorText, duration);
                    toast.show();
                } else {
                    myUtils.showHide(searchBox);
                    myUtils.showHide(searchAddress);
                    myUtils.showHide(searchDate);
                    myUtils.showHide(searchHour);
                    myUtils.showHide(searchPerimeter);
                    myUtils.showHide(searchButton);
                    myUtils.showHide(selectCurrentAddressButton);
                    refreshMapWithResearch(searchPoint, perimeter);
                }
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

        LatLng paris = new LatLng(48.8534, 2.3488);

        mMap.setMinZoomPreference(13);
        mMap.addMarker(new MarkerOptions().position(paris).title("Marker Paris"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
    }

    private void refreshMapWithResearch(LatLng searchEpicenter, double perimeter) {
        mMap.clear();
        int circleColor = getColor(R.color.mapsCircleColor);
        int circleBorderWidth = 2;
        float zoomLevel = myUtils.getZoomLevel(perimeter);
        mMap.addMarker(new MarkerOptions().position(searchEpicenter).title("Epicentre recherche"));
        mMap.addCircle(new CircleOptions().center(searchEpicenter).radius(perimeter).strokeWidth(circleBorderWidth).fillColor(circleColor));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchEpicenter, zoomLevel));
    }

    private void refreshMapToCurrentPosition() {
        if (lastKnownLocation != null) {
            LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Position Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateOfSearch = Calendar.getInstance();
        dateOfSearch.set(Calendar.YEAR, year);
        dateOfSearch.set(Calendar.MONTH, month);
        dateOfSearch.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Button searchDate = (Button) findViewById(R.id.search_date_button);
        searchDate.setText(myUtils.formattedDateToString(dateOfSearch));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute){
        dateOfSearch.set(Calendar.HOUR_OF_DAY, selectedHour);
        dateOfSearch.set(Calendar.MINUTE, selectedMinute);

        Button searchHour = (Button) findViewById(R.id.search_hour_button);
        searchHour.setText(myUtils.formattedHourToString(dateOfSearch));
    }

    private boolean gotPermissions(){
        return ((ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED));
    }

    private void askForPermissions(){
        if (!gotPermissions()) {
            PermissionManager permissionManager = PermissionManager.getInstance();
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    permissionManager.COARSE_AND_FINE_LOCATION_PERMISSION_CODE);
        }
    }
}
