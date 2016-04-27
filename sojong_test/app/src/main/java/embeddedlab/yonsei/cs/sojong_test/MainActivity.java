package embeddedlab.yonsei.cs.sojong_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        database = sqLiteOpenHelper.getWritableDatabase();

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
}
