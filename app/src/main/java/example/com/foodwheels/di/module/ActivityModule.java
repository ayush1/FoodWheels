package example.com.foodwheels.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ayushgarg on 22/09/17.
 */

@Module
public class ActivityModule {
    private AppCompatActivity appCompatActivity;

    public ActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    Context provideContext(){
        return appCompatActivity;
    }

    @Provides
    AppCompatActivity provideActivity(){
        return appCompatActivity;
    }
}
