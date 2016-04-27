package embeddedlab.yonsei.cs.sojong_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UnlockReceiver extends BroadcastReceiver {
    public UnlockReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*Sent when the user is present after
         * device wakes up (e.g when the keyguard is gone)
         * */
        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            TinyDB tinyDB = new TinyDB(context.getApplicationContext());
            Long counter = tinyDB.getLong("counter", 0);
            tinyDB.putLong("counter", counter + 1);

            if(tinyDB.getInt("active_noti") == 0){
                tinyDB.putLong("counter", 0);
            }
        }
        /*Device is shutting down. This is broadcast when the device
         * is being shut down (completely turned off, not sleeping)
         * */
        else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {

        }
    }
}
