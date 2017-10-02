package example.com.foodwheels.ui.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import example.com.foodwheels.ui.base.BasePresenter;

/**
 * Created by ayushgarg on 28/09/17.
 */

public class LocationPresenter implements BasePresenter.ProvideLocationPresenter {

    @Override
    public void getAddress(final double lat, final double lng, final Context context, final Handler handler) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        result = address.getAddressLine(0);
                    }
                } catch (IOException e) {
                    Log.e("Location Address Loader", "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        result = " Unable to get address for this location.";
                        bundle.putString("response", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    @Override
    public GoogleApiClient getApiClient(Context context, GoogleApiClient.ConnectionCallbacks connectionCallbacks,
                                        GoogleApiClient.OnConnectionFailedListener connectionFailedListener) {
        return new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public LocationRequest getLocationRequest(long locationUpdateInterval) {
        return new LocationRequest()
            .setInterval(locationUpdateInterval)
            .setFastestInterval(locationUpdateInterval/2)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public LocationSettingsRequest getLocationSettingRequest(LocationRequest locationRequest) {
        return new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
    }
}
