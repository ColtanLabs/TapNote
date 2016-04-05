package com.coltan.tapnote.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Robert Mathew on 4/4/16.
 */
public class NoteProvider extends ContentProvider {

    private static final String TAG = "NoteProvider";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NoteDbHelper mOpenHelper;

    /* Codes for the UriMatcher */
    private static final int NOTE = 100;
    private static final int NOTE_WITH_ID = 101;


    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NoteContract.CONTENT_AUTHORITY;

        // Note
        matcher.addURI(authority, NoteContract.PATH_NOTE, NOTE);
        matcher.addURI(authority, NoteContract.PATH_NOTE + "/#", NOTE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new NoteDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            // "note"
            case NOTE:
                cursor = mOpenHelper.getReadableDatabase().query(
                        NoteContract.NoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return cursor;

            // "note/*"
            case NOTE_WITH_ID:
                cursor = mOpenHelper.getReadableDatabase().query(
                        NoteContract.NoteEntry.TABLE_NAME,
                        projection,
                        NoteContract.NoteEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return cursor;
            default:
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case NOTE:
                return NoteContract.NoteEntry.CONTENT_TYPE;

            case NOTE_WITH_ID:
                return NoteContract.NoteEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case NOTE:
                long _id = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = NoteContract.NoteEntry.buildNoteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        switch (sUriMatcher.match(uri)) {
            case NOTE:
                rowsDeleted = database.delete(
                        NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Undefined URI code: " + uri);
        }

        // Null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case NOTE:
                rowsUpdated = db.update(NoteContract.NoteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
