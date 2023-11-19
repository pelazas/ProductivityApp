package com.example.productivityapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class FragmentManager {
    private FragmentActivity activity;

    public FragmentManager(FragmentActivity activity) {
        this.activity = activity;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
