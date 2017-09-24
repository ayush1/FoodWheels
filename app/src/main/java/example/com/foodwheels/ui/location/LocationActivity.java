package example.com.foodwheels.ui.location;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.foodwheels.R;
import example.com.foodwheels.ui.base.BaseActivity;

/**
 * Created by ayushgarg on 23/09/17.
 */

public class LocationActivity extends BaseActivity {

    @BindView(R.id.bottom_sheet)
    View bottomSheet;

    @BindView(R.id.tv_save_address)
    TextView tvSaveAddress;

    private BottomSheetBehavior bottomSheetBehaviour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getActivityComponent().inject(this);
        setUnbinder(ButterKnife.bind(this));

        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

        tvSaveAddress.setTypeface(EasyFonts.robotoBlack(this));

        bottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehaviour.setPeekHeight(350);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }



}
