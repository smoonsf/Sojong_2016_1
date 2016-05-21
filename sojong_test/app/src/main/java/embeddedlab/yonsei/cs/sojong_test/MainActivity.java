package embeddedlab.yonsei.cs.sojong_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button_start, button_end;

    SQLiteDatabase database;
    MySQLiteOpenHelper sqLiteOpenHelper;

    List<String> appNames;
    List<Double> ranks;
    List<Drawable> icons;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RankRecyclerViewAdapter rankRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appNames = new ArrayList<String>();
        ranks = new ArrayList<Double>();
        icons = new ArrayList<Drawable>();

        sqLiteOpenHelper = new MySQLiteOpenHelper(this, 1);
        database = sqLiteOpenHelper.getReadableDatabase();

        PackageManager packageManager = getPackageManager();


        Cursor cursor = database.query("apprank", null, null, null, null, null, "rankpoint desc");

        Log.d("MainActivity", cursor.getCount() + "");
        while (cursor.moveToNext()){
            Double rankPoint = cursor.getDouble(cursor.getColumnIndex("rankpoint"));
            String packageName = cursor.getString(cursor.getColumnIndex("pname"));
            ApplicationInfo applicationInfo;
            Drawable applicationIcon;
            try {
                applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                applicationIcon = packageManager.getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                applicationInfo = null;
                applicationIcon = null;
            }

            appNames.add(packageName);
            ranks.add(rankPoint);
            icons.add(applicationIcon);


//            String appName = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
//            if(appName.contentEquals("(unknown)")){
//                database.delete("apprank", "pname = " + packageName, null);
//            } else {
//                appNames.add(appName);
//                ranks.add(rankPoint);
//                icons.add(applicationIcon);
//            }

        }



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_rank);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        rankRecyclerViewAdapter = new RankRecyclerViewAdapter(appNames, ranks, icons);
        recyclerView.setAdapter(rankRecyclerViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_noti){
//            Intent intent = new Intent(this, HiddenNotificationsActivity.class);
//            startActivity(intent);

            TinyDB tinyDB = new TinyDB(this.getApplicationContext());
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            MyNotificationListenerService myNotificationListenerService = new MyNotificationListenerService();



            int i = 0;
            if(myNotificationListenerService.notifications != null) {
                for (Notification noti : myNotificationListenerService.notifications) {
                    notificationManager.notify(i++, noti);
                    myNotificationListenerService.notifications.clear();
                }
            }
            //tinyDB.putListObject("hidden_notis", new ArrayList<Object>());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
