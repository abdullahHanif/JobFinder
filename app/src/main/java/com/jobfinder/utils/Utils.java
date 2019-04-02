package com.jobfinder.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.jobfinder.R;

public class Utils {

    public static class AppProgressDialog {
        static ProgressDialog progressDialog;
        static boolean isShowing = false;

        public static void show(Activity context) {
            try {
                if (context != null && !context.isFinishing() && !isShowing) {

                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressDialog.setProgressStyle(R.style.CustomDialogStyle);
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);

                    progressDialog.show();

                    if (progressDialog != null) {
                        isShowing = true;
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void dismiss() {
            try {
                if (progressDialog != null) progressDialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog = null;
                isShowing = false;
            }
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
