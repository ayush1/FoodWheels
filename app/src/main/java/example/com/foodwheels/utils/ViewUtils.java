package example.com.foodwheels.utils;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by ayushgarg on 21/09/17.
 */

public class ViewUtils {

    @SuppressWarnings("ResourceType")
    @SuppressLint("ShowToast")
    public static void showToastMessage(Context context, String msg) {

        try {
            if (msg != null && !msg.equalsIgnoreCase("")) {
                Toast.makeText(context.getApplicationContext(), msg, 4000)
                        .show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void animateView(final View view, int newHeight, int duration) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(params.height, newHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (view != null) {
                    int val = (int) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = val;
                    view.setLayoutParams(params);
                    view.requestLayout();
                }
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
