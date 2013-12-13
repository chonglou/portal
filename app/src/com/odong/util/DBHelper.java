package com.odong.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.odong.Constants;

/**
 * Created by flamen on 13-12-9.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Settings (kkk VARCHAR(255) NOT NULL, vvv TEXT NOT NULL);";
        Log.d(Constants.LOG_NAME, "创建表:" + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(Constants.LOG_NAME, "暂未");
    }

    private final static int VERSION = 1;
    private final static String NAME = "portal.db";
}
