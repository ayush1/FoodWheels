package example.com.foodwheels.di.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import example.com.foodwheels.di.PerActivity;
import example.com.foodwheels.ui.base.BasePresenter;
import example.com.foodwheels.ui.location.LocationPresenter;

/**
 * Created by ayushgarg on 22/09/17.
 */

@Module
public class LocationActivityModule {
    private Activity activity;

    public LocationActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity(){
        return activity;
    }

    @Provides
    @PerActivity
    public LocationPresenter provideLocationPresenter(){
        return new LocationPresenter();
    }

}
