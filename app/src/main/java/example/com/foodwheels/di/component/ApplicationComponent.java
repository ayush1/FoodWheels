package example.com.foodwheels.di.component;

import javax.inject.Singleton;

import dagger.Component;
import example.com.foodwheels.MvpApp;
import example.com.foodwheels.di.module.ApplicationModule;

/**
 * Created by ayushgarg on 23/09/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MvpApp app);
}
