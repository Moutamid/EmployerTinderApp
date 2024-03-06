package com.moutimid.tinder.helpers;

import android.app.Activity;
import android.content.Intent;


public class QuickHelp

{
    // process to account creation



    /**
     * method to go to any activity
     */
    public static void goToActivityAndFinish(Activity currentActivity, Class<?> otherActivity, String extra, int extraValue) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        mainIntent.putExtra(extra, extraValue);
        currentActivity.startActivity(mainIntent);
        currentActivity.finish();
    }

    public static void goToActivityAndFinish(Activity currentActivity, Class<?> otherActivity) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(mainIntent);
        currentActivity.finish();
    }

    /**
     * method to go to any activity
     */
    public static void goToActivityWithNoClean(Activity currentActivity, Class<?> otherActivity) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        currentActivity.startActivity(mainIntent);
    }

}