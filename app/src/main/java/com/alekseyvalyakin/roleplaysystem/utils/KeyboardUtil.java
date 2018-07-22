package com.alekseyvalyakin.roleplaysystem.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mikepenz on 14.03.15.
 * This class implements a hack to change the layout padding on bottom if the keyboard is shown
 * to allow long lists with editTextViews
 * Basic idea for this solution found here: http://stackoverflow.com/a/9108219/325479
 */
public class KeyboardUtil {
    private View decorView;
    private View contentView;

    public KeyboardUtil(Activity act) {
        this.decorView = act.getWindow().getDecorView();
        ViewGroup content = act.findViewById(android.R.id.content);
        this.contentView = content.getChildAt(0);

        //only required on newer android versions. it was working on API level 19 (Build.VERSION_CODES.KITKAT)
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void enable() {
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void disable() {
        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }


    //a small helper to allow showing the editText focus
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //r will be populated with the coordinates of your view that area still visible.
            decorView.getWindowVisibleDisplayFrame(r);

            //get screen height and calculate the difference with the useable area from the r
            int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
            int diff = height - r.bottom;

            //if it could be a keyboard add the padding to the view
            if (diff != 0) {
                // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
                //check if the padding is 0 (if yes set the padding for the keyboard)
                if (contentView.getPaddingBottom() != diff) {
                    //set the padding of the contentView for the keyboard
                    contentView.setPadding(0, 0, 0, diff);
                }
            } else {
                //check if the padding is != 0 (if yes reset the padding)
                if (contentView.getPaddingBottom() != 0) {
                    //reset the padding of the contentView
                    contentView.setPadding(0, 0, 0, 0);
                }
            }
        }
    };


    /**
     * Helper to hide the keyboard
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        if (context instanceof Activity) {
            Activity act = (Activity) context;
            if (act.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }
    }
}
