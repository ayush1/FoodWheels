package example.com.foodwheels.ui.base;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by ayushgarg on 28/09/17.
 */

public interface BasePresenter {

    interface ProvideLocationPresenter{
        void getAddress(double lat, double lng, Context context, Handler handler);
        GoogleApiClient getApiClient(Context context, GoogleApiClient.ConnectionCallbacks connectionCallbacks,
                                     GoogleApiClient.OnConnectionFailedListener connectionFailedListener);
        LocationRequest getLocationRequest(long locationUpdateInterval);
        LocationSettingsRequest getLocationSettingRequest(LocationRequest locationRequest);
    }

    interface ProvideLocationViewPresenter{
        void showProgressBar();
        void showMarker();
    }

}
