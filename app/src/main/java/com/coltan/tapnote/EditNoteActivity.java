package com.coltan.tapnote;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.coltan.tapnote.db.DatabaseHandler;
import com.coltan.tapnote.db.Note;

public class EditNoteActivity extends AppCompatActivity {

    private EditText etTitle, etTag, etNote;
    private int id;
    private Intent shareIntent;
    private String title, tag, note, mStar;
    private long time;
    private String starred = "0";


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
        starred = an.getStarred();
        //Log.d("Info", starred);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
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

        if (menuId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (menuId == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final Context context = this;
            builder.setMessage("Delete this note?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int did) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteNote(id);
                    NavUtils.navigateUpFromSameTask(EditNoteActivity.this);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", null);
            AppCompatDialog dialog = builder.create();
            dialog.show();

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

        if (menuId == R.id.action_star) {
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (starred == null) {
            starred = "0";
        }
        if (starred.equals("0")) {
            //Log.d("Info", "Starred = 0");
        } else {
            (menu.findItem(R.id.action_star)).setIcon(R.drawable.ic_star_white_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        title = etTitle.getText().toString();
        tag = etTag.getText().toString();
        note = etNote.getText().toString();

        time = System.currentTimeMillis();
        Log.d("EditNoteActivity", String.valueOf(time));

        if (title.isEmpty() || title.equals("")) {
            title = "Untitled";
        }
        UpdateTask ut = new UpdateTask();
        ut.execute();
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
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
            nt.setDate(time);
            nt.setStarred(starred);
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
                //Toast.makeText(EditNoteActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditNoteActivity.this, "Update Failed!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
