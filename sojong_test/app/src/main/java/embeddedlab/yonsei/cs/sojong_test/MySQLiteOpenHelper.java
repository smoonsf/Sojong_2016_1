package embeddedlab.yonsei.cs.sojong_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by alexmoon on 2016. 1. 8..
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{

    Context context;

    public MySQLiteOpenHelper(Context ctx, int version) {
        // Database이름은 실제 단말상에서 생성될 파일이름입니다. data/data/package명/databases/DATABASE_NAME식으로 저장
        super(ctx, Environment.getExternalStorageDirectory().getPath() + "/rank.db", null, version);    // 제일 마지막 인자 : 버젼, 만약 버젼이 높아지면 onUpgrade를 수행한다.
        context =  ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        sql = "create table apprank ("
                + "pname text primary key, "
                + "rankpoint double default 2.0);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists apprank";
        db.execSQL(sql);

        onCreate(db);
    }
}
