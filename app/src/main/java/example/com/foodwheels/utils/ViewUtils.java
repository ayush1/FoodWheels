package example.com.foodwheels.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

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
}
