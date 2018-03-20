package com.ltf.util;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by justin on 16/12/3.
 */
public class SnackbarUtils {

    public static void showSnackbar(View view, int resId) {
        showSnackbar(view, view.getResources().getString(resId));
    }

    public static void showSnackbar(View view, String text) {

        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        View v = snackbar.getView();
        v.setBackgroundColor(0xc0000000);
        ((TextView) v.findViewById(R.id.snackbar_text)).setTextColor(0xffffffff);
        snackbar.show();
    }
}
