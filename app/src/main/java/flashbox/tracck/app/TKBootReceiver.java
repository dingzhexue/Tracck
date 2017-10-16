package flashbox.tracck.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gpit.android.logger.RemoteLogger;

/**
 * Created by administrator on 8/29/17.
 * Common notification receiver will be used separated receiver per app
 */
public class TKBootReceiver extends BroadcastReceiver {
    private final static String TAG = TKBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteLogger.d(TAG, intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            RemoteLogger.d(TAG, "Boot Completed");
        }
    }

}
