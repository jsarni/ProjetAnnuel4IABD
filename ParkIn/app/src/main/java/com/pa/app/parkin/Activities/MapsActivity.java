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
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.pa.app.parkin.DataTasks.PlaceSearchTask;
import com.pa.app.parkin.Horodateur;
import com.pa.app.parkin.SearchContext;
import com.pa.app.parkin.R;
import com.pa.app.parkin.Utils.DatePickerFragment;
import com.pa.app.parkin.Utils.DevUtils;
import com.pa.app.parkin.Utils.PermissionManager;
import com.pa.app.parkin.Utils.TimePickerFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, LocationListener {

    private GoogleMap mMap;
    private Calendar dateOfSearch;
    private double perimeter;
    private LatLng searchPoint;

    private DevUtils myUtils = DevUtils.getInstance();
    private Location lastKnownLocation;
    private Marker lastKnownPositionMarker;
    private Marker selectedPlacesMarker;
    private LocationManager locationManager;
    private Polyline route;

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
    private ImageButton rollBackToMainInterfaceButton;

    private View feedbackView;
    private TextView feedbackMessageTextView;
    private Button feedbackFoundPlaceButton;
    private Button feedbackNotFoundPlaceButton;
    private ImageView feedbackFoundImage;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean askForPlace = true;

    private double MIN_DISTANCE_BETWEEN_PLACE_AND_POSITION = 50;

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
        rollBackToMainInterfaceButton = (ImageButton) findViewById(R.id.go_back_button);

        feedbackView = (View) findViewById(R.id.feedback_view);
        feedbackMessageTextView = (TextView) findViewById(R.id.feedback_question);
        feedbackFoundPlaceButton = (Button) findViewById(R.id.found_place_button);
        feedbackNotFoundPlaceButton = (Button) findViewById(R.id.not_found_place_button);
        feedbackFoundImage = (ImageView) findViewById(R.id.found_places_image);

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
        rollBackToMainInterfaceButton.setVisibility(View.GONE);
        feedbackView.setVisibility(View.GONE);
        feedbackMessageTextView.setVisibility(View.GONE);
        feedbackFoundPlaceButton.setVisibility(View.GONE);
        feedbackNotFoundPlaceButton.setVisibility(View.GONE);
        feedbackFoundImage.setVisibility(View.GONE);

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
                askForPlace = false;
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
                                public boolean onMarkerClick(final Marker marker) {
                                    selectedPlacesMarker = marker;

                                    foundPlacesForMarkerTextview.setVisibility(View.VISIBLE);
                                    selectPlacesMarkerButton.setVisibility(View.VISIBLE);
                                    rollBackToMainInterfaceButton.setVisibility(View.VISIBLE);
                                    foundPlacesTextview.setVisibility(View.GONE);
                                    parkingButton.setVisibility(View.GONE);
                                    positionButton.setVisibility(View.GONE);
                                    profileButton.setVisibility(View.GONE);

                                    String nb_found_places = marker.getTitle();

                                    foundPlacesForMarkerTextview.setText(getString(R.string.number_found_places_for_marker_text, nb_found_places));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));



                                    return true;
                                }
                            });
                        }
                    }
                }

            }

        });
        selectPlacesMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawRouteToPlace(selectedPlacesMarker.getPosition());
                LatLng lastKnownPositionLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                LatLng middlePoint = myUtils.getMiddleLatLng(
                        lastKnownPositionLatLng,
                        selectedPlacesMarker.getPosition());
                double distance = myUtils.getDistanceInMeter(lastKnownPositionLatLng, selectedPlacesMarker.getPosition());
                float zoomLevel = myUtils.getZoomLevel(distance / 2);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, zoomLevel));
                myUtils.showHide(selectPlacesMarkerButton);
                askForPlace = true;
            }
        });

        rollBackToMainInterfaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUtils.showHide(rollBackToMainInterfaceButton);
                mMap.clear();

                foundPlacesForMarkerTextview.setVisibility(View.GONE);
                selectPlacesMarkerButton.setVisibility(View.GONE);
                rollBackToMainInterfaceButton.setVisibility(View.GONE);
                feedbackView.setVisibility(View.GONE);
                feedbackMessageTextView.setVisibility(View.GONE);
                feedbackFoundPlaceButton.setVisibility(View.GONE);
                feedbackNotFoundPlaceButton.setVisibility(View.GONE);
                feedbackFoundImage.setVisibility(View.GONE);

                parkingButton.setVisibility(View.VISIBLE);
                profileButton.setVisibility(View.VISIBLE);
                positionButton.setVisibility(View.VISIBLE);

                refreshMapToCurrentPosition();
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

        float zoomLevel = 17;

        askForPermissions();
        if (!gotPermissions()) {
            myUtils.showToast(MapsActivity.this, getString(R.string.location_permission_error_message));
        } else {

            createLocationRequest();
            createLocationUpdatesCallback();
            createFusedLocationClient();

            updateCurrentPosition();
            LatLng myPosition = myUtils.latLngFromLocation(lastKnownLocation);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, zoomLevel));

            startLocationUpdates();
        }
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
                        if (lastKnownPositionMarker != null) {
                            lastKnownPositionMarker.remove();
                        }
                        lastKnownPositionMarker = mMap.addMarker(
                                new MarkerOptions()
                                .position(myUtils.latLngFromLocation(lastKnownLocation))
                                .title("Position actuelle")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
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

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(250);
        locationRequest.setFastestInterval(125);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createFusedLocationClient(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
    }

    private void createLocationUpdatesCallback(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    lastKnownLocation = location;
                    if (lastKnownPositionMarker != null) {
                        lastKnownPositionMarker.remove();
                    }
                    lastKnownPositionMarker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(myUtils.latLngFromLocation(lastKnownLocation))
                                    .title("Position actuelle")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    );

                    if (lastKnownLocation != null && selectedPlacesMarker != null) {
                        LatLng myPosition = myUtils.latLngFromLocation(lastKnownLocation);
                        LatLng placePosition = selectedPlacesMarker.getPosition();

                        LatLng middlePoint = myUtils.getMiddleLatLng(myPosition, placePosition);
                        double distance = myUtils.getDistanceInMeter(myPosition, placePosition);

                        float zoomLevel = myUtils.getZoomLevel(distance / 2);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, zoomLevel));
                        if (distance < MIN_DISTANCE_BETWEEN_PLACE_AND_POSITION) {
                            if(askForPlace) {
                                manageFeedback();
                                askForPlace = false;
                            }
                        }
                        Log.e("POSITION ----", String.format("%f, %f", lastKnownPositionMarker.getPosition().latitude, lastKnownPositionMarker.getPosition().longitude));
                    }
                    Log.e("LOCATIONRES --", String.format("%f,%f",location.getLatitude(), location.getLongitude()));
                    Log.e("LOCATIONRES --", String.format("%f,%f",location.getLatitude(), location.getLongitude()));
                }
            }
        };
    }

    private void manageFeedback(){

//        foundPlacesForMarkerTextview.setVisibility(View.GONE);
//        selectPlacesMarkerButton.setVisibility(View.GONE);
//        feedbackView.setVisibility(View.VISIBLE);
//        feedbackMessageTextView.setVisibility(View.VISIBLE);
//        feedbackFoundPlaceButton.setVisibility(View.VISIBLE);
//        feedbackNotFoundPlaceButton.setVisibility(View.VISIBLE);

        myUtils.showHide(foundPlacesForMarkerTextview);
        myUtils.showHide(feedbackView);
        myUtils.showHide(feedbackMessageTextView);
        myUtils.showHide(feedbackFoundPlaceButton);
        myUtils.showHide(feedbackNotFoundPlaceButton);

        feedbackNotFoundPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUtils.showHide(foundPlacesForMarkerTextview);
                myUtils.showHide(feedbackView);
                myUtils.showHide(feedbackMessageTextView);
                myUtils.showHide(feedbackFoundPlaceButton);
                myUtils.showHide(feedbackNotFoundPlaceButton);

                selectedPlacesMarker = null;
            }
        });

        feedbackFoundPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUtils.showHide(feedbackView);
                myUtils.showHide(feedbackMessageTextView);
                myUtils.showHide(feedbackFoundPlaceButton);
                myUtils.showHide(feedbackNotFoundPlaceButton);
                myUtils.showHide(feedbackFoundImage);

                selectedPlacesMarker = null;

            }
        });
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
    @Override
    public void onLocationChanged(Location location) {

//        lastKnownLocation = location;
//        if (lastKnownPositionMarker != null) {
//            lastKnownPositionMarker.remove();
//        }
//        lastKnownPositionMarker = mMap.addMarker(
//                new MarkerOptions()
//                        .position(myUtils.latLngFromLocation(lastKnownLocation))
//                        .title("Position actuelle")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//        );
//
//        if (lastKnownLocation != null && selectedPlacesMarker != null) {
//            LatLng myPosition = myUtils.latLngFromLocation(lastKnownLocation);
//            LatLng placePosition = selectedPlacesMarker.getPosition();
//            if (myUtils.getDistanceInMeter(myPosition, placePosition) < MIN_DISTANCE_BETWEEN_PLACE_AND_POSITION) {
//                Log.e("Location ----------", "Marche" + myUtils.getDistanceInMeter(myPosition, placePosition));
//            }
//
//        }
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

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchEpicenter, zoomLevel));
    }

    private void refreshMapToCurrentPosition() {
        if (lastKnownLocation != null) {
            LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            if (lastKnownPositionMarker != null) {
                lastKnownPositionMarker.remove();
            }
            lastKnownPositionMarker = mMap.addMarker(
                    new MarkerOptions()
                            .position(currentLatLng)
                            .title("Position Marker")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
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

    private void prepareMapForRoute() {
        if (route != null) {
            route.remove();
        }
    }
    private void drawRouteToPlace(LatLng placePosition){
        prepareMapForRoute();

        GeoApiContext geoContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyBGH9V1SEo7G6ppxzmoIbTBT31gW059Iqo")
                .build();
        ArrayList path = new ArrayList();

        String origin = String.format(
                "%s,%s",
                String.valueOf(lastKnownLocation.getLatitude()).replace(',', '.'),
                String.valueOf(lastKnownLocation.getLongitude()).replace(',', '.')
        );
        String destination = String.format(
                "%s,%s",
                String.valueOf(placePosition.latitude).replace(',', '.'),
                String.valueOf(placePosition.longitude).replace(',', '.')
        );

        DirectionsApiRequest request = DirectionsApi.getDirections(geoContext, origin, destination);
        try {
            DirectionsResult result = request.await();

            if (result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("ROUTE ERROR", ex.getMessage());
            myUtils.showToast(MapsActivity.this, "Problème lors du calcul de l'itinéraire");
        }

        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            route = mMap.addPolyline(opts);
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

//    @SuppressLint("MissingPermission")
//    protected void startLocationUpdates() {
//        locationManager = (LocationManager) MapsActivity.this.getSystemService(LOCATION_SERVICE);
//
//        if (locationManager != null) {
//            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
//
//                List<String> providers = locationManager.getProviders(true);
//
//                for (String provider : providers) {
//                    Location l = locationManager.getLastKnownLocation(provider);
//                    if (l == null) {
//                        continue;
//                    }
//                    if (lastKnownLocation == null || l.getAccuracy() < lastKnownLocation.getAccuracy()) {
//                        lastKnownLocation = l;
//                    }
//                }
//                if (lastKnownLocation == null) {
//                    Log.w("DEBUG", "LastKnownLocation is null");
//                }
//            }
//        }
//    }

    protected void stopLocationUpdates() {
        locationManager = (LocationManager) MapsActivity.this.getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.removeUpdates(this);
            }
        }
    }
}
