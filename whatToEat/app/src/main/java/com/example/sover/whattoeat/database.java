package com.example.sover.whattoeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by sover on 2018/1/3.
 */

public class database {
    public SQLiteDatabase db = null;
    private final static String DATABASE_NAME = "db1.db";
    private final static String TABLE_NAME = "table01";
    private final static String _ID = "_id";
    private final static String FOOD = "food";

    private final static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + FOOD + " STRING) ";

    private Context mCtx = null;

    public database(Context ctx) {
        this.mCtx = ctx;
    }

    public void open() throws SQLException {
        db = mCtx.openOrCreateDatabase(DATABASE_NAME, 0, null);
        try {
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
        }
    }

    public void close(){
        db.close();
    }

    public ArrayList<Integer> getallid(){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Cursor cursor = getall();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(_ID));
                ids.add(id);
            } while (cursor.moveToNext());
        }
        return ids;
    }

    public Cursor getall(){
        return db.query(TABLE_NAME, new String[] {_ID, FOOD},null, null, null, null, null, null);
    }

    public Cursor get(long rowId) throws SQLException{
        Cursor mCursor = db.query(TABLE_NAME, new String[] {_ID, FOOD}, _ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long append(String food){
        ContentValues args = new ContentValues();
        args.put(FOOD, food);
        return db.insert(TABLE_NAME, null, args);
    }

    public boolean delete(long rowId) {
        return db.delete(TABLE_NAME, _ID + "=" + rowId, null) > 0;
    }

    public boolean update(long rowId, String food){
        ContentValues args = new ContentValues();
        args.put(FOOD, food);
        return db.update(TABLE_NAME, args, _ID + "=" + rowId, null) > 0; // return true or false
    }

}
