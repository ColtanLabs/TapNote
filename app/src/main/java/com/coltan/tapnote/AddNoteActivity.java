package com.coltan.tapnote;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.coltan.tapnote.db.DatabaseHandler;
import com.coltan.tapnote.db.Note;


public class AddNoteActivity extends BaseActivity {

    private EditText etTitle, etTag, etNote;
    private String title;
    private String tag;
    private String note;
    private String starred = "0";
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitle = (EditText) findViewById(R.id.editTitle);
        etTag = (EditText) findViewById(R.id.editTag);
        etNote = (EditText) findViewById(R.id.noteContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        mShareActionProvider.setShareIntent(setMyShareIntent(shareIntent));
        return true;
    }

    // Call to update the share intent
    private Intent setMyShareIntent(Intent shareIntent) {
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide what to do with it.
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, etTitle.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, etNote.getText().toString());

        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.action_star) {
            if (starred.equals("0")) {
                //change your view and sort it by Alphabet
                item.setIcon(R.drawable.ic_star_white_24dp);
                starred = "1";
                Log.d("Info", "Starred = " + starred);
            } else {
                item.setIcon(R.drawable.ic_star_border_white_24dp);
                starred = "0";
                Log.d("Info", "Starred = " + starred);
            }
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

    private class AddTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... args) {
            DatabaseHandler db = new DatabaseHandler(AddNoteActivity.this);
            db.addNote(new Note(title, note, tag, time, starred));
            db.close();
            return null;
        }

        protected void onPostExecute(String errorMsg) {
            if (errorMsg == null) {
                //Toast.makeText(AddNoteActivity.this, "Added Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddNoteActivity.this, "Added Failed!", Toast.LENGTH_LONG).show();
                //Log.d("Error", errorMsg);
            }
        }
    }
}
