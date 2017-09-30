package example.com.foodwheels.ui.location;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arsy.maps_library.MapRipple;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vstechlab.easyfonts.EasyFonts;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.foodwheels.R;
import example.com.foodwheels.di.component.DaggerLocationActivityComponent;
import example.com.foodwheels.di.component.LocationActivityComponent;
import example.com.foodwheels.di.module.LocationActivityModule;
import example.com.foodwheels.ui.base.BaseActivity;
import example.com.foodwheels.utils.AppConstants;
import example.com.foodwheels.utils.ViewUtils;

/**
 * Created by ayushgarg on 23/09/17.
 */

public class LocationActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ResultCallback<LocationSettingsResult> {

    @BindView(R.id.bottom_sheet)
    View bottomSheet;

    @BindView(R.id.tv_save_address)
    TextView tvSaveAddress;

    @BindView(R.id.tv_save_as)
    TextView tvSaveAs;

    @BindView(R.id.tv_manual)
    TextView tvManual;

    @BindView(R.id.tv_home)
    TextView tvHome;

    @BindView(R.id.tv_work)
    TextView tvWork;

    @BindView(R.id.tv_other)
    TextView tvOther;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.et_address)
    EditText et_address;

    private BottomSheetBehavior bottomSheetBehaviour;
    private boolean isAnimationRunning = false;
    private int EXPAND_VIEW_HEIGHT = 900;
    private int COLLAPSE_VIEW_HEIGHT = 435;
    private int COLLAPSE_BOTTOM_SHEET = 350;

    @Inject
    public Activity activity;

    @Inject
    LocationPresenter locationPresenter;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location location;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationRequest locationRequest;
    private LatLng latLng;

    protected static final String TAG = "LocationActivity";

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private int REQUEST_CHECK_SETTINGS = 1;
    private boolean requestingLocationUpdates;
    private String message;

    private MapRipple mapRipple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ButterKnife.bind(this);

        LocationActivityComponent activityComponent = DaggerLocationActivityComponent.builder()
                .locationActivityModule(new LocationActivityModule(this))
                .build();

        activityComponent.inject(this);

        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

        tvSaveAddress.setTypeface(EasyFonts.robotoBlack(this));
        tvSaveAs.setTypeface(EasyFonts.robotoRegular(this));
        tvManual.setTypeface(EasyFonts.robotoBlack(this));
        tvHome.setTypeface(EasyFonts.robotoRegular(this));
        tvWork.setTypeface(EasyFonts.robotoRegular(this));
        tvOther.setTypeface(EasyFonts.robotoRegular(this));

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.getSupportActionBar().hide();

        bottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehaviour.setPeekHeight(COLLAPSE_BOTTOM_SHEET);
                }

                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    ViewUtils.animateView(mapFragment.getView(), COLLAPSE_VIEW_HEIGHT, AppConstants.ANIMATION_DURATION);
                    ViewUtils.hideSoftKeyboard(activity);
                    isAnimationRunning = false;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset < 1 && !isAnimationRunning) {
                    ViewUtils.animateView(mapFragment.getView(), EXPAND_VIEW_HEIGHT, AppConstants.ANIMATION_DURATION);
                    ViewUtils.hideSoftKeyboard(activity);
                    isAnimationRunning = true;
                }
            }
        });

        buildGoogleApiCient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    protected void buildGoogleApiCient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                message = getString(R.string.permission_denied_message);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkLocationSettings();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            if (location != null) {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                updateLocationUI();
            }else{
                checkLocationSettings();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                checkLocationSettings();
        }
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                startProgressiveTimmer();
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(LocationActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "Unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Dialog not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CHECK_SETTINGS) {
            startProgressiveTimmer();
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        requestingLocationUpdates = true;
                    }
                });

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        requestingLocationUpdates = false;
                    }
                });
    }

    private void updateLocationUI() {
        if(location != null) {
            showMarker();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        if (location != null) {
            locationPresenter.getAddress(location.getLatitude(), location.getLongitude(), this, handler);
            showMarker();
        }
    }

    private void startProgressiveTimmer() {
        progressBar.setVisibility(View.VISIBLE);
        et_address.setText(R.string.identify_location);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
        if (mapRipple.isAnimationRunning()) {
            mapRipple.stopRippleMapAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected() && requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void showMarker() {
        map.clear();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);
        map.moveCamera(cameraUpdate);
        map.animateCamera(zoom);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(getString(R.string.source_location))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        progressBar.setIndeterminate(false);
        mapRipple = new MapRipple(map, new LatLng(location.getLatitude(), location.getLongitude()), this);

    }

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String address;
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    et_address.setText(address);
                    break;
                case 2:
                    Bundle responseBundle = msg.getData();
                    responseBundle.getString("response");
                    address = null;
                    break;
            }
        }
    };

    private void addMarkerToMap() {
        CameraUpdate riderCameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude));
        map.moveCamera(riderCameraUpdate);

        map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(getString(R.string.source_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);
        map.animateCamera(zoom);
    }
}
