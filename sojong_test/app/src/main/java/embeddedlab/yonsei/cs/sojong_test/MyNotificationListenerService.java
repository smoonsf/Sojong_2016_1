package embeddedlab.yonsei.cs.sojong_test;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.List;

public class MyNotificationListenerService extends NotificationListenerService {
    final String TAG = "NLService";

    Handler handler;

    SQLiteDatabase database;
    MySQLiteOpenHelper sqLiteOpenHelper;

    Context ctx;

    static public ArrayList<Notification> notifications;

    @Override
    public void onCreate() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("RUNNING", true).apply();

        notifications = new ArrayList<>();

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

        TinyDB tinyDB = new TinyDB(this.getApplicationContext());
        tinyDB.putLong(sbn.getPackageName(), tinyDB.getLong("counter", 0));

        sqLiteOpenHelper = new MySQLiteOpenHelper(this, 1);
        database = sqLiteOpenHelper.getWritableDatabase();


        String[] args = {sbn.getPackageName()};
        Cursor cursor = database.query("apprank", null, "pname = ?", args, null, null, null);
        if(cursor.getCount()==0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("pname", sbn.getPackageName());
            database.insert("apprank", null, contentValues);
        } else {
            cursor.moveToFirst();
            double rankpoint = cursor.getDouble(cursor.getColumnIndex("rankpoint"));

            if(rankpoint < 1.5 && rankpoint > -1.5){
                notifications.add(sbn.getNotification());
            }
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {


        Log.d(TAG, "onNotificationRemoved called : " + sbn.getPackageName());


        TinyDB tinyDB = new TinyDB(this.getApplicationContext());

        tinyDB.putInt("active_noti", getActiveNotifications().length);
        Log.d(TAG, getActiveNotifications().length + "");
        //handler.post(new ToastRunnable("Notification Number : " + getActiveNotifications().length));



        Long init_counter = tinyDB.getLong(sbn.getPackageName(), 0);
        Long counter = tinyDB.getLong("counter", 0);
        Long count = counter - init_counter;



        String packageName = sbn.getPackageName();

        sqLiteOpenHelper = new MySQLiteOpenHelper(this, 1);
        database = sqLiteOpenHelper.getWritableDatabase();
        String[] args = {packageName};
        Cursor cursor = database.query("apprank", null, "pname = ?", args, null, null, null);
        if(cursor.getCount() != 0){
            cursor.moveToFirst();

            final Double rankpoint = cursor.getDouble(cursor.getColumnIndex("rankpoint"));
            Double newRankPoint = rankpoint;

            try {
                List<AndroidAppProcess> processes = ProcessManager.getRunningAppProcesses();
                if (processes != null) {
                    for (AndroidAppProcess process : processes) {
                        String processName = process.name;
                        if (processName.equals(packageName)) {
                            if (process.foreground == true)
                            {
                                //user clicked on notification
                                handler.post(new ToastRunnable("Notification Clicked : " + sbn.getPackageName()));
                                if(count <= 3){
                                    newRankPoint = (rankpoint * 9 + 3.0) / 10;
                                } else if(count <= 5){
                                    newRankPoint = (rankpoint * 9 + 2.0) / 10;
                                } else if(count <= 7){
                                    newRankPoint = (rankpoint * 9 + 1.0) / 10;
                                } else {
                                    newRankPoint = rankpoint;
                                }



                            }
                            else
                            {
                                //user swipe notification
                                handler.post(new ToastRunnable("Notification Removed : " + sbn.getPackageName()));
                                if(count <= 3){
                                    newRankPoint = (rankpoint * 9 - 3.0) / 10;
                                } else if(count <= 5){
                                    newRankPoint = (rankpoint * 9 - 2.0) / 10;
                                } else if(count <= 7){
                                    newRankPoint = (rankpoint * 9 - 1.0) / 10;
                                } else {
                                    newRankPoint = rankpoint;
                                }

                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                String error = e.toString();
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("pname", sbn.getPackageName());
            contentValues.put("rankpoint", newRankPoint);
            database.update("apprank", contentValues, "pname = ?", args);
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
