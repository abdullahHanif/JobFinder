package com.jobfinder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class AppClass extends Application {
    Context context;
    Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        registerLifeCycleActivity();
    }

    private void registerLifeCycleActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
              AppClass.this.activity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
