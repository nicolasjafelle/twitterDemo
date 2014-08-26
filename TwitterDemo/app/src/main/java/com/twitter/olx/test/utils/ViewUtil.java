package com.twitter.olx.test.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import java.lang.reflect.Field;

import roboguice.inject.InjectView;

/**
 * Util class that provides useful generic methods.
 * Created by nicolas on 7/23/13.
 */
public class ViewUtil {

	/**
	 * Method that allows Roboguice 2.0 to injectViews inside CustomViews. This is a workaround for
	 * Roboguice.injectMember().
	 * @param view
	 */
	public static void reallyInjectViews(View view) {
		final Field[] fields = view.getClass().getDeclaredFields();
		for (final Field f : fields) {
			final InjectView annotation = f.getAnnotation(InjectView.class);
			if (annotation != null) {
				final int id = annotation.value();
				final View findViewById = view.findViewById(id);
                if (findViewById!=null){
                    f.setAccessible(true);
                    try {
                        f.set(view, findViewById);
                    } catch (final IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (final IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
			}
		}
	}


	/**
	 * Unbind all drawables in the given view. It could be a view or viewGroup, or even better a root View.
	 * @param view
	 */
	public static void unbindDrawables(View view) {
		try {
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
				if (!(view instanceof AdapterView)) {
					((ViewGroup) view).removeAllViews();
				}
			}
			view = null;
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
	}

    public static void showKeyboard(Context context){
        InputMethodManager inputMgr = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }



}
