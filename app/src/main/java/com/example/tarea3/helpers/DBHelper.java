package com.example.tarea3.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Tarea3ISC581SQLITE.DB";
    private static final Integer DB_VERSION = 1;

    public static final String PRODUCT_ID = "id";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String MARCA_ID = "id";
    public static final String B64_IMG = "image";

    private  static final String MARCAS = "CREATE TABLE IF NOT EXISTS CATEGORIES(" + MARCA_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " VARCHAR UNIQUE NOT NULL)";
    private static final String PRODUCTOS = "CREATE TABLE IF NOT EXISTS PRODUCTS(" +
            PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " VARCHAR NOT NULL, " + PRICE + " INTEGER NOT NULL," + B64_IMG + " TEXT NOT NULL, MARCA INTEGER NOT NULL ,FOREIGN KEY(MARCA) REFERENCES CATEGORIES("+MARCA_ID+"))" ;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MARCAS);
        sqLiteDatabase.execSQL(PRODUCTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("Cambios en la base de datos");
    }
}
