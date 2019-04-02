package com.jobfinder.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

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
}
