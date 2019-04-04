package com.jobfinder.ui.fragment;


import android.os.Bundle;
import android.view.View;

import com.jobfinder.AppClass;
import com.jobfinder.ui.activities.BaseActivity;
import com.jobfinder.utils.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected BaseActivity mBaseActivity;
    protected FragmentActivity context;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        Utils.hideKeyboard(AppClass.getActivity());
    }

    //showing progress dialog
    protected void showDialog() {
        Utils.AppProgressDialog.show(context);
    }

    public FragmentManager getSupportFragmentManager() {
        return context.getSupportFragmentManager();
    }

    public void popStack() {
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void popAllStack() {
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void setupComponents(View rootView) {
        initializeComponents(rootView);
        setupListeners(rootView);
    }

    abstract void initializeComponents(View rootView);

    abstract void setupListeners(View rootView);
}
