package example.com.foodwheels.ui.presenter;

import android.content.Context;
import android.os.Handler;

/**
 * Created by ayushgarg on 28/09/17.
 */

public interface BasePresenter {

    interface ProvideLocationPresenter{
        void getAddress(double lat, double lng, Context context, Handler handler);
    }

}
