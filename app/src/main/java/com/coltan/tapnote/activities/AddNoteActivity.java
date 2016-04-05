package com.coltan.tapnote.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.coltan.tapnote.R;
import com.coltan.tapnote.data.NoteContract;


public class AddNoteActivity extends BaseActivity {

    private static final String TAG = "AddNoteActivity";

    private Context mContext;

    private TextInputEditText etTitle, etTag, etNote;
    private String title;
    private String tag;
    private String note;
    private String starred = "0";
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        mContext = this;

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
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_star:
                if (starred.equals("0")) {
                    item.setIcon(R.drawable.ic_star_white_24dp);
                    starred = "1";
                    Log.d("Info", "Starred = " + starred);
                } else {
                    item.setIcon(R.drawable.ic_star_border_white_24dp);
                    starred = "0";
                    Log.d("Info", "Starred = " + starred);
                }
                return true;

            case R.id.action_share:
                Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(etNote.getText().toString())
                        .createChooserIntent();
                startActivity(shareIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        title = etTitle.getText().toString();
        tag = etTag.getText().toString();
        note = etNote.getText().toString();
        time = System.currentTimeMillis();

        if (title.isEmpty() || title.equals("")) {
            if (note.isEmpty() || note.equals("")) {
                super.onBackPressed();
                NavUtils.navigateUpFromSameTask(this);
                finish();
            } else {
                title = "Untitled";
                AddTask at = new AddTask();
                at.execute();
                super.onBackPressed();
                NavUtils.navigateUpFromSameTask(this);
                finish();
            }
        } else {
            AddTask at = new AddTask();
            at.execute();
            super.onBackPressed();
            NavUtils.navigateUpFromSameTask(this);
            finish();
        }

    }

    private class AddTask extends AsyncTask<Void, Void, Uri> {

        protected Uri doInBackground(Void... args) {
            ContentValues noteInfoValues = new ContentValues();
            noteInfoValues.put(NoteContract.NoteEntry.COLUMN_TITLE, title);
            noteInfoValues.put(NoteContract.NoteEntry.COLUMN_NOTE, note);
            noteInfoValues.put(NoteContract.NoteEntry.COLUMN_TAG, tag);
            noteInfoValues.put(NoteContract.NoteEntry.COLUMN_DATE, time);
            noteInfoValues.put(NoteContract.NoteEntry.COLUMN_STARRED, starred);
            Log.d(TAG, "onClick: " + NoteContract.NoteEntry.CONTENT_URI);
            return mContext.getContentResolver()
                    .insert(NoteContract.NoteEntry.CONTENT_URI, noteInfoValues);
        }

        protected void onPostExecute(Uri newUri) {
            if (newUri == null) {
                Log.d(TAG, "onPostExecute: Failed to insert");
            } else {
                Log.d(TAG, "onPostExecute: Successful inserted" + newUri);
            }
        }
    }
}
