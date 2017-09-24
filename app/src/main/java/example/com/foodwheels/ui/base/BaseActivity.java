package example.com.foodwheels.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.Unbinder;
import example.com.foodwheels.MvpApp;
import example.com.foodwheels.di.component.ActivityComponent;
import example.com.foodwheels.di.component.DaggerActivityComponent;
import example.com.foodwheels.di.module.ActivityModule;

/**
 * Created by ayushgarg on 21/09/17.
 */

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;
    private Unbinder unBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getApplicationComponent())
                .build();
    }

    public ActivityComponent getActivityComponent(){
        return activityComponent;
    }

    public void setUnbinder(Unbinder unBinder) {
        unBinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if(unBinder != null){
            unBinder.unbind();
        }
        super.onDestroy();
    }
}
