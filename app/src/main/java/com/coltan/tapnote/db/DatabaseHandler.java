package com.coltan.tapnote.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "noteDB";

    // Notes table name
    private static final String TABLE_NOTE = "notes";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "note";
    private static final String KEY_TAG = "tag";
    private static final String KEY_DATE = "date";
    private static final String KEY_STARRED = "starred";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_NOTE + " TEXT," + KEY_TAG + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_STARRED + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + TABLE_NOTE + " ADD " + KEY_STARRED + " INTEGER");
                // we want both updates, so no break statement here...
        }
    }

    /* All CRUD(Create, Read, Update, Delete) Operations */

    //Adding new note
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_NOTE, note.getNote());
        values.put(KEY_TAG, note.getTag());
        values.put(KEY_DATE, note.getDate());
        values.put(KEY_STARRED, note.getStarred());
        db.insert(TABLE_NOTE, null, values);
        db.close();
    }

    // Getting single note
    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE, new String[]{
                        KEY_TITLE, KEY_NOTE, KEY_TAG, KEY_DATE, KEY_STARRED}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getString(4));
        db.close();
        return note;
    }

    // Getting All notes
    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setTag(cursor.getString(3));
                note.setDate(cursor.getLong(4));
                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return noteList;
    }

    // Updating single note
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_TAG, note.getTag());
        values.put(KEY_NOTE, note.getNote());
        values.put(KEY_DATE, note.getDate());
        values.put(KEY_STARRED, note.getStarred());
        // updating row
        return db.update(TABLE_NOTE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getID())});
    }

    // Deleting single note
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTE);
        db.close();
    }

    // Getting all starred notes
    public List<Note> getAllStarredNotes() {
        List<Note> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE + " WHERE " + KEY_STARRED + "=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setTag(cursor.getString(3));
                note.setDate(cursor.getLong(4));
                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return noteList;
    }


//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }

}
