package com.twitter.olx.test.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import roboguice.fragment.RoboDialogFragment;

/**
 * Helper class that helps you show RoboDialogFragments. You can use it with Roboguice and injected RoboDialogFragments.
 * It is based on the DialogFragmentHelper class of the Github Android app.<br><br>
 * https://github.com/github/android/blob/master/app/src/main/java/com/github/mobile/ui/DialogFragmentHelper.java
 */
public abstract class DialogFragmentHelper extends RoboDialogFragment {

    private static final String TAG = "dialog_fragment_helper";

    /**
     * Show dialog
     */
    public static void show(FragmentActivity activity, RoboDialogFragment fragment, Bundle arguments) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment current = manager.findFragmentByTag(TAG);
        if (current != null) {
            transaction.remove(current);
//            transaction.addToBackStack(null);
	        transaction.commit();

        }
	    if(fragment.getArguments() == null) {
		    fragment.setArguments(arguments);
	    }else {
		    fragment.getArguments().putAll(arguments);
	    }
        fragment.show(manager, TAG);
    }

}
