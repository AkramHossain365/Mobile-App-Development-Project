package com.example.smarthallmanagement;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {

    private static final String TAG = "NavigationHelper";

    public static void setupBottomNavigation(Activity activity, BottomNavigationView bottomNavigation) {

        if (activity == null || bottomNavigation == null) {
            Log.e(TAG, "Activity or BottomNavigationView is null");
            return;
        }

        bottomNavigation.setOnItemSelectedListener(item -> {

            try {
                int id = item.getItemId();
                Class<?> targetClass = null;

                if (id == R.id.nav_home) {
                    targetClass = MainActivity.class;
                } else if (id == R.id.nav_meal) {
                    targetClass = MealActivity.class;
                } else if (id == R.id.nav_notices) {
                    targetClass = NoticesActivity.class;
                } else if (id == R.id.nav_complaint) {
                    targetClass = ComplaintActivity.class;
                }

                if (targetClass != null) {
                    if (activity.getClass().equals(targetClass)) {
                        return true;
                    }

                    Intent intent = new Intent(activity, targetClass);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    activity.startActivity(intent);
                    return true;
                }
            } catch (Exception e) {
                Log.e(TAG, "Navigation error: " + e.getMessage());
            }

            return false;
        });
    }
}
