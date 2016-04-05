package com.coltan.tapnote.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coltan.tapnote.data.NoteContract.NoteEntry;

public class NoteDbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "noteDB";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME + "("
                + NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NoteEntry.COLUMN_TITLE + " TEXT,"
                + NoteEntry.COLUMN_NOTE + " TEXT,"
                + NoteEntry.COLUMN_TAG + " TEXT,"
                + NoteEntry.COLUMN_DATE + " TEXT,"
                + NoteEntry.COLUMN_STARRED + " INTEGER" + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + NoteEntry.TABLE_NAME + " ADD " + NoteEntry.COLUMN_STARRED + " INTEGER");
                // we want both updates, so no break statement here...
        }
    }

    public void deleteAllNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + NoteEntry.TABLE_NAME);
        db.close();
    }
}
