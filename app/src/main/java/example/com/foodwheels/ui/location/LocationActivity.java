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

    @BindView(R.id.tv_save_as)
    TextView tvSaveAs;

    @BindView(R.id.tv_manual)
    TextView tvManual;

    @BindView(R.id.tv_home)
    TextView tvHome;

    @BindView(R.id.tv_work)
    TextView tvWork;

    @BindView(R.id.tv_other)
    TextView tvOther;

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
        tvSaveAs.setTypeface(EasyFonts.robotoRegular(this));
        tvManual.setTypeface(EasyFonts.robotoBlack(this));
        tvHome.setTypeface(EasyFonts.robotoRegular(this));
        tvWork.setTypeface(EasyFonts.robotoRegular(this));
        tvOther.setTypeface(EasyFonts.robotoRegular(this));

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
