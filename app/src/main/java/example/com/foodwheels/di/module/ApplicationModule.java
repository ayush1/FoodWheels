package example.com.foodwheels.di.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ayushgarg on 23/09/17.
 */

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideContext(){
        return application;
    }

    @Provides
    Application provideApplication(){
        return application;
    }
}
