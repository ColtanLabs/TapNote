package com.coltan.tapnote.activities;

import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.coltan.tapnote.R;
import com.coltan.tapnote.data.NoteContract;

public class EditNoteActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Fix rotate device crash
    private static final String TAG = "EditNoteActivity";

    private static final int NOTE_LOADER_ID = 0;

    private Context mContext;
    private TextInputEditText etTitle, etTag, etNote;
    private String mNoteId;
    private String title, tag, note;
    private long time;
    private String starred = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mNoteId = getIntent().getStringExtra("id");
        mContext = this;

        getLoaderManager().initLoader(NOTE_LOADER_ID, null, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitle = (TextInputEditText) findViewById(R.id.editTitle);
        etTag = (TextInputEditText) findViewById(R.id.editTag);
        etNote = (TextInputEditText) findViewById(R.id.noteContent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Delete this note?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int did) {
                        int mRowsDeleted = getContentResolver().delete(
                                NoteContract.NoteEntry.CONTENT_URI,
                                NoteContract.NoteEntry._ID + "=?",
                                new String[]{mNoteId}
                        );
                        Log.d(TAG, "Rows deleted " + mRowsDeleted);
                        NavUtils.navigateUpFromSameTask(EditNoteActivity.this);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AppCompatDialog dialog = builder.create();
                dialog.show();
                return true;

            case R.id.action_copy:
                // Gets a handle to the clipboard service.
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // Creates a new text clip to put on the clipboard
                ClipData clip = ClipData.newPlainText(etTitle.getText().toString(), etNote.getText().toString());
                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_share:
                Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(etNote.getText().toString())
                        .createChooserIntent();
                startActivity(shareIntent);
                return true;

            case R.id.action_star:
                if (starred.equals("0")) {
                    item.setIcon(R.drawable.ic_action_star);
                    starred = "1";
                    Log.d("Info", "Starred = " + starred);
                } else {
                    item.setIcon(R.drawable.ic_action_star);
                    starred = "0";
                    Log.d("Info", "Starred = " + starred);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (starred == null) {
            starred = "0";
        }
        if (starred.equals("0")) {
            //Log.d("Info", "Starred = 0");
        } else {
            (menu.findItem(R.id.action_star)).setIcon(R.drawable.ic_action_star);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(NOTE_LOADER_ID, null, this);
    }

    @Override
    public void onBackPressed() {
        //Saving the change
        title = etTitle.getText().toString();
        tag = etTag.getText().toString();
        note = etNote.getText().toString();
        time = System.currentTimeMillis();
        if (title.isEmpty() || title.equals("")) {
            title = "Untitled";
        }
        UpdateTask ut = new UpdateTask();
        ut.execute();

        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        //finish();
    }

    //Setting the note values
    private void setNoteInfo(String title, String note, String tag, String date, String star) {
        etTitle.setText(title);
        etTitle.setSelection(etTitle.getText().length());
        etTag.setText(tag);
        etTag.setSelection(etTag.getText().length());
        etNote.setText(note);
        etNote.setSelection(etNote.getText().length());
        starred = star;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == NOTE_LOADER_ID) {
            //Log.d(TAG, "onCreateLoader: " + mNoteId);
            loader = new CursorLoader(mContext,
                    NoteContract.NoteEntry.CONTENT_URI,
                    null,
                    NoteContract.NoteEntry._ID + " = ?",
                    new String[]{mNoteId},
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            Log.d(TAG, "No data in database");
        }
        if (data.moveToFirst()) {
            String title = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_TITLE));
            String note = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE));
            String tag = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_TAG));
            String date = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_DATE));
            String starred = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_STARRED));
            setNoteInfo(title, note, tag, date, starred);
        }
        data.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class UpdateTask extends AsyncTask<Void, Void, Integer> {
        protected Integer doInBackground(Void... args) {
            ContentValues mUpdateValues = new ContentValues();
            mUpdateValues.put(NoteContract.NoteEntry.COLUMN_TITLE, title);
            mUpdateValues.put(NoteContract.NoteEntry.COLUMN_NOTE, note);
            mUpdateValues.put(NoteContract.NoteEntry.COLUMN_TAG, tag);
            mUpdateValues.put(NoteContract.NoteEntry.COLUMN_DATE, time);
            mUpdateValues.put(NoteContract.NoteEntry.COLUMN_STARRED, starred);

            int mRowsUpdated = getContentResolver().update(
                    NoteContract.NoteEntry.CONTENT_URI,
                    mUpdateValues,
                    NoteContract.NoteEntry._ID + "=?",
                    new String[]{mNoteId}
            );
            return mRowsUpdated;
        }

        protected void onPostExecute(Integer nRowsUpdated) {
            if (nRowsUpdated == 0) {
                Log.d(TAG, "onPostExecute: Failed to update");
            } else {
                Log.d(TAG, "onPostExecute: Successful update" + nRowsUpdated);
            }
        }
    }
}
