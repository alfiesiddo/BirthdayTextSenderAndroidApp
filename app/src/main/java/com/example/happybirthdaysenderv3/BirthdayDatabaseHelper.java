package com.example.happybirthdaysenderv3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BirthdayDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "birthdays_db";
    static final String TABLE_PERSON = "person";
    static final String KEY_MOBILENUM = "mobileNum";
    static final String KEY_NAME = "name";
    static final String KEY_BIRTHDAY = "birthday";

    static final String KEY_TEXT = "text";

    public BirthdayDatabaseHelper(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_PERSON_TABLE = "CREATE TABLE " + TABLE_PERSON + "(" + KEY_MOBILENUM + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_BIRTHDAY + " TEXT," + KEY_TEXT + " TEXT" + ")";
        db.execSQL(CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        //onCreate(db);
        if (oldVersion < 2) {
            // Upgrade from version 1 to 2, add the new column
            db.execSQL("ALTER TABLE " + TABLE_PERSON + " ADD COLUMN " + KEY_TEXT + " TEXT");
        }
    }
}
