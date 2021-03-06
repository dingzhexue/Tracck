package com.gpit.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by liulei on 8/7/15.
 */
public class SysUtils {
    public static long getCodeBuildTime(Context context) {
        try{
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);

            File file = new File(ai.sourceDir);
            long time = file.lastModified();

            /*
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                time = ze.getLastModifiedTime().toMillis();
            } else {
                time = ze.getTime();
            }
            */


            return time;
        } catch(Exception e) {
        }

        return 0;
    }
}
