package example.com.foodwheels.di.component;

import dagger.Component;
import example.com.foodwheels.di.PerActivity;
import example.com.foodwheels.di.module.LocationActivityModule;
import example.com.foodwheels.ui.location.LocationActivity;

/**
 * Created by ayushgarg on 22/09/17.
 */

@PerActivity
@Component(modules = LocationActivityModule.class)
public interface LocationActivityComponent {
    void inject(LocationActivity activity);
}
