package com.example.vasiliy.lessons;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


class SaveData {
    private String LogTag="Save_Data";
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String > arrayToScreen;
    ContentValues cv;
    Cursor c;
    String resStatus;

    SaveData(Context context){
        dbHelper = new DBHelper(context);
    }

    void SavData(final String numberBTS, final String comment)
                 {
        db = dbHelper.getWritableDatabase();
        //Log.d(LogTag,"SavData Start");
        cv = new ContentValues();
        cv.put("numberBTS", numberBTS);//1
        cv.put("Comment", comment);//2
        db.insert("incident", null, cv);
        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "InfoBs", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // создаем таблицу с полями: номер бс, комментарий.
            db.execSQL("create table InfoBs ("
                    + "id integer primary key autoincrement,"
                    + "numberBTS,"
                    + "Comment"
                    + ");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase dbSMS, int oldVersion, int newVersion) {
        }
    }

}

