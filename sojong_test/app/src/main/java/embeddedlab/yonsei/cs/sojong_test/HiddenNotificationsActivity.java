package embeddedlab.yonsei.cs.sojong_test;

import android.app.Notification;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HiddenNotificationsActivity extends AppCompatActivity {
    SQLiteDatabase database;
    MySQLiteOpenHelper sqLiteOpenHelper;

    List<Object> notificationList;

    RecyclerView recyclerView_notification;
    LinearLayoutManager linearLayoutManager;
    NotificationRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_notifications);

        recyclerView_notification = (RecyclerView) findViewById(R.id.recyclerView_noti);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_notification.setLayoutManager(linearLayoutManager);



        TinyDB tinyDB = new TinyDB(this.getApplicationContext());

        notificationList = tinyDB.getListObject("hidden_notis", StatusBarNotification.class);

        adapter = new NotificationRecyclerViewAdapter((List<StatusBarNotification>)(List)notificationList);

        recyclerView_notification.setAdapter(adapter);



    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
