package embeddedlab.yonsei.cs.sojong_test;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.List;

public class MyNotificationListenerService extends NotificationListenerService {
    final String TAG = "NLService";

    Handler handler;

    @Override
    public void onCreate() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("RUNNING", true).apply();

        handler = new Handler();

        Toast.makeText(this, "서비스 시작", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("RUNNING", false).apply();

        Toast.makeText(this, "서비스 종료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.d(TAG, "onNotificationPosted called");
        handler.post(new ToastRunnable("Notification Posted : " + sbn.getPackageName()));

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {


        String notificationPackageName = sbn.getNotification().contentIntent.getCreatorPackage();

        Log.d(TAG, "onNotificationRemoved called : " + notificationPackageName);


        String packageName = sbn.getPackageName();
        try {
            List<AndroidAppProcess> processes = ProcessManager.getRunningAppProcesses();
            if (processes != null) {
                for (AndroidAppProcess process : processes) {
                    String processName = process.name;
                    if (processName.equals(packageName)) {
                        if (process.foreground ==true)
                        {
                            //user clicked on notification
                            handler.post(new ToastRunnable("Notification Clicked : " + sbn.getPackageName()));
                        }
                        else
                        {
                            //user swipe notification
                            handler.post(new ToastRunnable("Notification Removed : " + sbn.getPackageName()));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            String error = e.toString();
        }

    }


    private class ToastRunnable implements Runnable {
        String mText;

        public ToastRunnable(String text) {
            mText = text;
        }

        @Override
        public void run(){
            Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_SHORT).show();
        }
    }
}
