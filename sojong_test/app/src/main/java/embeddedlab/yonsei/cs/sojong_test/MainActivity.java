package embeddedlab.yonsei.cs.sojong_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button_start, button_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button_start = (Button) findViewById(R.id.button_start);
        button_end = (Button) findViewById(R.id.button_end);

        final Intent notiService = new Intent(this, MyNotificationListenerService.class);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                if(!sharedPreferences.getBoolean("RUNNING", false))
                    v.getContext().startService(notiService);
            }
        });

        button_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                if(sharedPreferences.getBoolean("RUNNING", false))
                    v.getContext().stopService(notiService);
            }
        });


    }
}
