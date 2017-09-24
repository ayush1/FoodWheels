package example.com.foodwheels;

import android.app.Application;

import example.com.foodwheels.di.component.ApplicationComponent;
import example.com.foodwheels.di.component.DaggerApplicationComponent;
import example.com.foodwheels.di.module.ApplicationModule;

/**
 * Created by ayushgarg on 23/09/17.
 */

public class MvpApp extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public MvpApp setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
        return this;
    }
}
