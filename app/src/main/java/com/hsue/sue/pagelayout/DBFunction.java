package com.hsue.sue.pagelayout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sue on 2015-08-20.
 */
public class DBFunction {

    SQLiteDatabase db;
    ContentValues row;
    DBHelper mHelper;
    DBFunction(Context context)
    {
        mHelper = new DBHelper(context);
    }

    /** INSERT 최근검색목록 **/
    public void Recentinsert(String product)
    {
        db = mHelper.getReadableDatabase();
        String sql = "select * from recent where product ='"+ product+"';";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){
            String pro = cursor.getString(0);
            Result += (pro +"\n");
        }
        if(Result.length() == 0) {
            db = mHelper.getWritableDatabase();
            row = new ContentValues();
            row.put("product", product);
            db.insert("recent", null, row);
        }
        cursor.close();
        mHelper.close();
    }

    /** INSERT 최근검색목록 **/
    public void Productedit(String product,String folder,String serial,String memo)
    {

        //String sql = "update dic" + " set folder = '" + folder +", serial='"+serial+", memo='"+memo+"' where product = "+ product+";";
       // db.execSQL(sql);
        db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("folder",folder);
        values.put("serial",serial);
        values.put("memo",memo);
        db.update("dic",values,"product='"+product+"'",null);
        mHelper.close();
    }

    /** SELECT 최근검색 불러오기 **/
    public String Recentselect()
    {
        db = mHelper.getReadableDatabase();
        String sql = "select * from recent;";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){

            String pro = cursor.getString(1);
            Result += (pro + "\n");
        }
        if(Result.length() == 0)
            Result = "empty";

        cursor.close();
        mHelper.close();

        return Result;
    }
    /** INSERT 북마크 **/
    public int BKinsert(String product)
    {
        int ret;
        db = mHelper.getReadableDatabase();
        String sql = "select * from bookmark where product ='"+ product+"';";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){
            String pro = cursor.getString(0);
            Result += (pro +"\n");
        }
        if(Result.length() == 0) {
            db = mHelper.getWritableDatabase();
            row = new ContentValues();
            row.put("product", product);
            db.insert("bookmark", null, row);
            ret = 1;
        }
        else
            ret = 0;

        cursor.close();
        mHelper.close();
        return ret;
    }

    /** INSERT 물건정보 **/
    public int DBinsert(String product, String folder, String serial_num,String text)
    {
        int ret;
        db = mHelper.getReadableDatabase();
        String sql = "select * from dic where product ='"+ product+"';";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){
            String idx = cursor.getString(0);
            String pro = cursor.getString(1);
            String fol = cursor.getString(2);
            Result += (idx+" : "+pro +" , "+fol +"\n");
        }
        if(Result.length() == 0) {
            db = mHelper.getWritableDatabase();
            row = new ContentValues();
            row.put("product", product);
            row.put("folder", folder);
            row.put("serial", serial_num);
            row.put("memo", text);
            db.insert("dic", null, row);
            ret = 1;
        }
        else
            ret = 0;

        cursor.close();
        mHelper.close();
        return ret;
    }

    /** DELETE 물건삭제 **/
    public int DBdelete(String product)
    {
        int ret;
        db = mHelper.getWritableDatabase();
        db.execSQL("delete from dic where product = '"+ product +"';");
        mHelper.close();
        String temp = DBselectall(product);
        if(temp.equals("empty"))
            ret = 1;
        else
            ret = 0;

        return ret;
    }

    /** DELETE 물건전체삭제 **/
    public void DBdeleteall()
    {
        db = mHelper.getWritableDatabase();
        db.delete("dic", null, null);
        mHelper.close();
    }

    /** SELECT 물건이름 자동완성 **/
    public String DBselect(String product)
    {
        db = mHelper.getReadableDatabase();
        String sql = "select * from dic where product like '"+ product+"%';";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){
            //String idx = cursor.getString(0);
            String pro = cursor.getString(1);
            //String fol = cursor.getString(2);
            Result += (pro + "\n");
        }
        if(Result.length() == 0)
            Result = "empty";

        cursor.close();
        mHelper.close();

        return Result;
    }

    /** SELECT 폴더 **/
    public String DBselF(String folder)
    {
        db = mHelper.getReadableDatabase();
        String sql = "select * from dic where folder ='"+ folder+"';";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){
            String pro = cursor.getString(1);
            Result += (pro +",");
        }
       // if(Result.length() == 0)
       //     Result = "empty";
        cursor.close();
        mHelper.close();
        return Result;
    }

    /** SELECT 북마크 **/
    public String DBselB()
    {
        db = mHelper.getReadableDatabase();
        String sql = "select product from bookmark;";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){
            String pro = cursor.getString(0);
            Result += (pro +",");
        }
       // if(Result.length() == 0)
       //     Result = "empty";
        cursor.close();
        mHelper.close();
        return Result;
    }

    /** SELECT 물건정보보기 **/
    public String DBselectall(String product)
    {
        db = mHelper.getReadableDatabase();
        String sql = "select * from dic where product like '"+ product+"';";
        Cursor cursor = db.rawQuery(sql,null);
        String Result = "";
        while(cursor.moveToNext()){
            String pro = cursor.getString(1);
            String fol = cursor.getString(2);
            String seri = cursor.getString(3);
            String memo = cursor.getString(4);
            Result += (pro +","+fol+","+seri+","+memo);
        }
        if(Result.length() == 0)
            Result = "empty";
        cursor.close();
        mHelper.close();
        return Result;
    }

}
