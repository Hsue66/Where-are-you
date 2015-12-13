package com.hsue.sue.pagelayout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sue on 2015-08-20.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context)
    {
        super(context, "Han.db", null ,1);

    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("Create table dic ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + "product TEXT, folder TEXT, serial TEXT, memo TEXT);");
        db.execSQL("Create table bookmark ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + "product TEXT);");
        db.execSQL("Create table recent ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + "product TEXT);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS dic");
        db.execSQL("DROP TABLE IF EXISTS bookmark");
        onCreate(db);
    }
}
