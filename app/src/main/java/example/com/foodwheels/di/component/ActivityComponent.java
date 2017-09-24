package example.com.foodwheels.di.component;

import dagger.Component;
import example.com.foodwheels.di.PerActivity;
import example.com.foodwheels.di.module.ActivityModule;
import example.com.foodwheels.ui.location.LocationActivity;

/**
 * Created by ayushgarg on 22/09/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LocationActivity activity);
}
