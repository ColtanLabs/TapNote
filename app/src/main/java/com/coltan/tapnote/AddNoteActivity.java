package com.coltan.tapnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.coltan.tapnote.db.DatabaseHandler;
import com.coltan.tapnote.db.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddNoteActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider;
    private EditText etTitle, etTag, etNote;
    private Intent shareIntent;

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
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        String title = etTitle.getText().toString();
        String tag = etTag.getText().toString();
        String note = etNote.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        if (title.isEmpty() || title.equals("")) {
            if (note.isEmpty() || note.equals("")) {
                super.onBackPressed();
            } else {
                etTitle.setError("Please enter a title");
            }
        } else {
            addNote(title, note, tag, formattedDate);
            super.onBackPressed();
        }
    }

    private void addNote(String title, String note, String tag, String formattedDate) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.addNote(new Note(title, note, tag, formattedDate));
        db.close();
        Toast.makeText(this, "Successfully added note", Toast.LENGTH_SHORT).show();
    }
}