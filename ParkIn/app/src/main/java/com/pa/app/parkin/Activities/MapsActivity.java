package com.pa.app.parkin.Activities;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pa.app.parkin.DataTasks.PlaceSearchTask;
import com.pa.app.parkin.Horodateur;
import com.pa.app.parkin.SearchContext;
import com.pa.app.parkin.R;
import com.pa.app.parkin.Utils.DatePickerFragment;
import com.pa.app.parkin.Utils.DevUtils;
import com.pa.app.parkin.Utils.PermissionManager;
import com.pa.app.parkin.Utils.TimePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, LocationListener {

    private GoogleMap mMap;
    private Calendar dateOfSearch;
    private double perimeter;
    private LatLng searchPoint;

    private DevUtils myUtils = DevUtils.getInstance();
    private Location lastKnownLocation;
    private LocationManager locationManager;

    private View searchBox;
    private EditText searchAddress;
    private Button searchDate;
    private Button searchHour;
    private EditText searchPerimeter;
    private ImageButton selectCurrentAddressButton;

    private Button searchButton;
    private TextView foundPlacesTextview;

    private Button parkingButton;
    private ImageButton positionButton;
    private ImageButton profileButton;

    private TextView foundPlacesForMarkerTextview;
    private Button selectPlacesMarkerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        searchBox = (View) findViewById(R.id.search_box);
        searchAddress = (EditText) findViewById(R.id.search_adress);
        searchDate = (Button) findViewById(R.id.search_date_button);
        searchHour = (Button) findViewById(R.id.search_hour_button);
        searchPerimeter = (EditText) findViewById(R.id.search_perimeter);
        selectCurrentAddressButton = (ImageButton) findViewById(R.id.current_address_button);
        searchButton = (Button) findViewById(R.id.search_button);
        foundPlacesTextview = (TextView) findViewById(R.id.found_places_textview);
        parkingButton = (Button) findViewById(R.id.parking_button);
        positionButton = (ImageButton) findViewById(R.id.position_button);
        profileButton = (ImageButton) findViewById(R.id.profile_button);
        foundPlacesForMarkerTextview = (TextView) findViewById(R.id.found_places_for_marker);
        selectPlacesMarkerButton = (Button) findViewById(R.id.select_horodateur_button);

        searchBox.setVisibility(View.GONE);
        searchAddress.setVisibility(View.GONE);
        searchDate.setVisibility(View.GONE);
        searchHour.setVisibility(View.GONE);
        searchPerimeter.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        foundPlacesTextview.setVisibility(View.GONE);
        selectCurrentAddressButton.setVisibility(View.GONE);
        foundPlacesForMarkerTextview.setVisibility(View.GONE);
        selectPlacesMarkerButton.setVisibility(View.GONE);

        positionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gotPermissions()) {
                    if (updateCurrentPosition()){
                        refreshMapToCurrentPosition();
                    } else {
                        myUtils.showToast(MapsActivity.this, getString(R.string.location_refresh_error));
                    }
                } else {
                    askForPermissions();
                    if (!gotPermissions()) {
                        myUtils.showToast(MapsActivity.this, getString(R.string.location_permission_error_message));
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
                    if (updateCurrentPosition()) {
                        updateSearchPointLocalisation();
                    } else {
                        myUtils.showToast(MapsActivity.this, getString(R.string.location_refresh_error));
                    }
                } else {
                    askForPermissions();
                    if (!gotPermissions()) {
                        myUtils.showToast(MapsActivity.this, getString(R.string.location_permission_error_message));
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
                    myUtils.showToast(MapsActivity.this, getString(R.string.address_error_message));
                } else if (perimeter <= 0) {
                    myUtils.showToast(MapsActivity.this, getString(R.string.perimeter_error_message));
                } else {
                    myUtils.showHide(searchBox);
                    myUtils.showHide(searchAddress);
                    myUtils.showHide(searchDate);
                    myUtils.showHide(searchHour);
                    myUtils.showHide(searchPerimeter);
                    myUtils.showHide(searchButton);
                    myUtils.showHide(selectCurrentAddressButton);

                    SearchContext searchContext = new SearchContext(searchPoint, perimeter, dateOfSearch);
                    PlaceSearchTask searchTask = new PlaceSearchTask();

                    ArrayList<Horodateur> availablePlaces = searchTask.searchPlaces(searchContext);

                    if (availablePlaces == null || availablePlaces.isEmpty()) {
                        myUtils.showToast(MapsActivity.this, getString(R.string.place_search_error));
                    } else {
                        int availablePlacesNumber = totalFoundPlacesNumber(availablePlaces);
                        ArrayList<MarkerOptions> placesMarkers = generatePlacesMarkers(availablePlaces);
                        if (placesMarkers.isEmpty()) {
                            myUtils.showToast(MapsActivity.this, getString(R.string.place_search_error_display_markers));
                        } else {
                            foundPlacesTextview.setText(getString(R.string.number_found_places_text, availablePlacesNumber));
                            myUtils.showHide(foundPlacesTextview);
                            refreshMapWithResearch(searchPoint, perimeter, placesMarkers);

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    myUtils.showHide(foundPlacesForMarkerTextview);
                                    myUtils.showHide(selectPlacesMarkerButton);
                                    myUtils.showHide(foundPlacesTextview);
                                    myUtils.showHide(parkingButton);
                                    myUtils.showHide(positionButton);
                                    myUtils.showHide(profileButton);
                                    return false;
                                }
                            });
                        }
                    }
                }

            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MapsActivity.this, UserActivity.class);
                startActivity(mapIntent);
            }
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng paris = new LatLng(48.8534, 2.3488);
        float zoomLevel = 16;

        mMap.addMarker(new MarkerOptions().position(paris).title("Marker Paris"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, zoomLevel));
//
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                myUtils.showHide(foundPlacesForMarkerTextview);
//                myUtils.showHide(selectPlacesMarkerButton);
//                return false;
//            }
//        });
    }

    @SuppressLint("MissingPermission")
    public boolean updateCurrentPosition() {
        locationManager = (LocationManager) MapsActivity.this.getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);

                List<String> providers = locationManager.getProviders(true);

                for (String provider : providers) {
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (lastKnownLocation == null || l.getAccuracy() < lastKnownLocation.getAccuracy()) {
                        lastKnownLocation = l;
                    }
                }
                if(lastKnownLocation == null) {
                    Log.w("DEBUG", "LastKnownLocation is null");
                }
                return true;
            } else {
                myUtils.showToast(MapsActivity.this,  getString(R.string.location_refresh_error));
                return false;
            }
        } else {
            myUtils.showToast(MapsActivity.this,  "Un problème est survenu lors de la localisation");
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
    }

    private void refreshMapWithResearch(LatLng searchEpicenter, double perimeter, ArrayList<MarkerOptions> placesMarkers) {
        mMap.clear();
        int circleColor = getColor(R.color.mapsCircleColor);
        int circleBorderWidth = 2;
        float zoomLevel = myUtils.getZoomLevel(perimeter);

        mMap.addMarker(
                new MarkerOptions()
                        .position(searchEpicenter)
                        .title("Epicentre recherche")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );

        addPlaceMarkersToMap(placesMarkers);
        mMap.addCircle(
                new CircleOptions()
                        .center(searchEpicenter)
                        .radius(perimeter)
                        .strokeWidth(circleBorderWidth)
                        .fillColor(circleColor)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchEpicenter, zoomLevel));
    }

    private void refreshMapToCurrentPosition() {
        if (lastKnownLocation != null) {
            LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(
                    new MarkerOptions()
                            .position(currentLatLng)
                            .title("Position Marker")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
        }
    }

    private ArrayList<MarkerOptions> generatePlacesMarkers(ArrayList<Horodateur> places){
        ArrayList<MarkerOptions> myMarkers = new ArrayList<MarkerOptions>();
        for(int i = 0; i < places.size(); i++){
            Horodateur currentPlace = places.get(i);
            myMarkers.add(
                    new MarkerOptions()
                            .position(currentPlace.getGeoPoint())
                            .title(currentPlace.getNumberOfPlaces() + " Places")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
        }

        return myMarkers;
    }

    private void addPlaceMarkersToMap(ArrayList<MarkerOptions> markers){
        for(int i = 0; i < markers.size(); i++){
            mMap.addMarker(markers.get(i));
        }
    }

    private int totalFoundPlacesNumber(ArrayList<Horodateur> places){
        int total = 0;
        for (int i = 0; i < places.size(); i++){
            total += places.get(i).getNumberOfPlaces();
        }
        return total;
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

    private void updateSearchPointLocalisation(){
        searchPoint = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
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
}
