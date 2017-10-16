package com.gpit.android.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.gpit.android.library.BuildConfig;

import java.io.File;

public class FileUtils {
    public final static String AAC = "AAC";
    public final static String JPEG = "jpeg";

    /************************** File ***********************/
    public static File createSharedFile(Context context, String directory, String fileName) {
        File file = null;

        try {
            file = new File(context.getExternalFilesDir(directory), fileName);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Uri getSharedUriFromSharedFile(Context context, File file) {
        Uri uri = null;
        try {
            uri = FileProvider.getUriForFile(context, context.getApplicationInfo().packageName + ".file_provider",
                    file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uri;
    }

    public static File createSharedTempFile(Context context, String prefix, String suffix, String directory) {
        File file = null;

        // Create an image file name
        String fileName = prefix + System.currentTimeMillis() + suffix;
        file = createSharedFile(context, directory, fileName);
        file.deleteOnExit();

        return file;
    }

    /**
     * Create empty image file in the storage.
     * @param context
     * @return uri of image file or null if file was not created.
     */
    public static File createEmptyImageFile(Context context) {
        return createSharedTempFile(context, JPEG, null, Environment.DIRECTORY_PICTURES);
    }
}
