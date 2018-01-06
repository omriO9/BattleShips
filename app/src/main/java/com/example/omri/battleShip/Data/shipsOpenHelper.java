package com.example.omri.battleShip.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.omri.battleShip.R;

/**
 * Created by Mark on 28/12/2017.
 */

public class shipsOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = shipsOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    public static final String SCORES_TABLE = "scores";
    public static final String COL_1 = "name";
    public static final String COL_2 = "score";
    public static final String COL_3 = "difficulty";
    private static final String COL_4 = "latitude";
    private static final String COL_5 = "longitude";
    private SQLiteDatabase dataBase;

    public shipsOpenHelper(Context context) {
        // The reason of passing null is you want the standard SQLiteCursor behaviour
        super(context, context.getResources().getString(R.string.app_name) + ".db", null, DATABASE_VERSION);
    }

    private String SCORES_TABLE_CREATE(String name, String score,String difficulty, String latitude ,String longitude) {
        return "CREATE TABLE " + SCORES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + name + " TEXT,"+score + " FLOAT,"+difficulty+" TEXT,"+latitude +" DOUBLE,"+longitude+" DOUBLE)";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORES_TABLE_CREATE(COL_1,COL_2,COL_3,COL_4,COL_5));
        this.dataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // idk if needed but must implement.
        db.execSQL("DROP TABLE IF EXISTS "+ SCORES_TABLE);
        onCreate(db);
    }

    public long put(ContentValues values) {
        long rowId;

        SQLiteDatabase database = this.getWritableDatabase();

        rowId = database.insertWithOnConflict(SCORES_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();

        return rowId;
    }

    public long put(String name, Float score,String difficulty,double latitude , double longitude) {
        ContentValues values = new ContentValues();
        values.put(COL_1, name);
        values.put(COL_2, score);
        values.put(COL_3, difficulty);
        values.put(COL_4, latitude);
        values.put(COL_5, longitude);
        Log.d(TAG, "put: name="+name+" | score="+score+" | difficulty="+difficulty +" | latitude=" + latitude + " | longitude=" + longitude);
        return this.put(values);
    }

    public String get(String key, String defaultValue) {
        String returnValue = defaultValue;
        Cursor cursor = getCursor(key);

        if (cursor.moveToFirst()) {
            returnValue = cursor.getString(0);
        }

        return returnValue;
    }

    public Cursor getCursor(String key) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT Value FROM " + SCORES_TABLE + " where Key = '" + key + "'"; // Instead of "SELECT * FROM"
        Cursor cursor = database.rawQuery(selectQuery, null);

        return cursor;
    }
}
