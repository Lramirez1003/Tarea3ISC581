package com.example.tarea3.dbModels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.tarea3.helpers.DBHelper;

public class MarcaDB {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public MarcaDB(Context context){
        this.context = context;
    }

    public MarcaDB open() throws SQLException {
        dbHelper = new DBHelper(this.context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name){
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME, name);
        sqLiteDatabase.insert("CATEGORIES", null, values);
    }

    public Cursor fetchAll() {
        String[] columns = new String[]{DBHelper.MARCA_ID, DBHelper.NAME};
        Cursor cursor = sqLiteDatabase.query("CATEGORIES",columns,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchByID(Integer id) {
        String[] columns = new String[]{DBHelper.MARCA_ID, DBHelper.NAME};
        Cursor cursor = sqLiteDatabase.query("CATEGORIES",columns,DBHelper.MARCA_ID + "= ?",new String[]{id.toString()},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchByName(String name) {
        String[] columns = new String[]{DBHelper.MARCA_ID, DBHelper.NAME};
        Cursor cursor = sqLiteDatabase.query("CATEGORIES",columns,DBHelper.NAME + " = ? " , new String[]{name},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Boolean delete(Integer id) {
        if(sqLiteDatabase.query("PRODUCTS", new String[]{DBHelper.PRODUCT_ID}, "MARCA = ?", new String[]{id.toString()},null,null,null).getCount()>0){
            return Boolean.FALSE;
        }else{
            sqLiteDatabase.delete("CATEGORIES", "id = ?", new String[]{id.toString()});
            return Boolean.TRUE;
        }
    }
}
