package com.coltan.tapnote;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.coltan.tapnote.db.DatabaseHandler;
import com.coltan.tapnote.db.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    EditText etTitle, etTag, etNote;
    private int id;
    private Intent shareIntent;
    String title, tag, note, formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        id = getIntent().getIntExtra("id", 1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitle = (EditText) findViewById(R.id.editTitle);
        etTag = (EditText) findViewById(R.id.editTag);
        etNote = (EditText) findViewById(R.id.noteContent);

        DatabaseHandler db = new DatabaseHandler(this);
        Note an = db.getNote(id);
        etTitle.setText(an.getTitle());
        etTitle.setSelection(etTitle.getText().length());
        etTag.setText(an.getTag());
        etTag.setSelection(etTag.getText().length());
        etNote.setText(an.getNote());
        etNote.setSelection(etNote.getText().length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);

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
        int menuId = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (menuId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (menuId == R.id.action_delete) {
            DatabaseHandler db = new DatabaseHandler(this);
            db.deleteNote(id);
            NavUtils.navigateUpFromSameTask(this);
            finish();
            return true;
        }
        if (menuId == R.id.action_copy) {
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // Creates a new text clip to put on the clipboard
            ClipData clip = ClipData.newPlainText(etTitle.getText().toString(), etNote.getText().toString());
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
        if (menuId == R.id.action_share) {
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        title = etTitle.getText().toString();
        tag = etTag.getText().toString();
        note = etNote.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        if (title.isEmpty() || title.equals("")) {
            title = "Untitled";
        }
        UpdateTask ut = new UpdateTask();
        ut.execute();
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }

    private class UpdateTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... args) {
            Note nt = new Note();
            nt.setID(id);
            nt.setTitle(title);
            nt.setTag(tag);
            nt.setNote(note);
            nt.setDate(formattedDate);
            Log.d("ID", String.valueOf(id));
            DatabaseHandler db = new DatabaseHandler(EditNoteActivity.this);
            int a = db.updateNote(nt);
            db.close();
            if (a == 1) {
                return null;
            } else {
                return "Failed";
            }
        }

        protected void onPostExecute(String errorMsg) {
            if (errorMsg == null) {
                Toast.makeText(EditNoteActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditNoteActivity.this, "Update Failed!", Toast.LENGTH_LONG).show();
                Log.d("Error", errorMsg);
            }
        }
    }
}
