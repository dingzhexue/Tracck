package com.gpit.android.util;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import junit.framework.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UiUtils {
	public static void removeParentView(View view) {
		if ((view.getParent()) != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
	}

	public static boolean containView(ViewGroup parent, View childView) {
		for (int i = 0 ; i < parent.getChildCount() ; i++) {
			View subView = parent.getChildAt(i);
			if (subView == childView) return true;
		}

		return false;
	}

    public static void setVisibleAll(ViewGroup parent, int visibility) {
        setVisibleAll(parent, visibility, true);
    }

	public static void setVisibleAll(ViewGroup parent, int visibility, boolean includeOwn) {
        if (includeOwn) {
            parent.setVisibility(visibility);
        }
		for (int i = 0 ; i < parent.getChildCount() ; i++) {
			View subView = parent.getChildAt(i);
            subView.setVisibility(visibility);

			if (subView instanceof ViewGroup) {
				setVisibleAll((ViewGroup) subView, visibility, includeOwn);
			}
		}
	}
}
