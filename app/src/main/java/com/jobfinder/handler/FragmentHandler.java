package com.jobfinder.handler;

import androidx.annotation.AnimRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


public class FragmentHandler {
    public static void replaceFragment(FragmentActivity context, Fragment fragment, int resId, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(resId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentActivity context, Fragment fragment, int resId, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(resId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void addFragmentWithAnimation(FragmentActivity context, Fragment fragment, int resId, boolean addToBackStack, @AnimRes int enter,
                                                @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
        fragmentTransaction.add(resId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentWithAnimation(FragmentActivity context, Fragment fragment, int resId, boolean addToBackStack, @AnimRes int enter,
                                                    @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
        fragmentTransaction.replace(resId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void addFragmentWithAnimation(FragmentActivity context, Fragment fragment, int resId, boolean addToBackStack, @AnimRes int enter,
                                                @AnimRes int exit) {
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(enter, exit);
        fragmentTransaction.add(resId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
