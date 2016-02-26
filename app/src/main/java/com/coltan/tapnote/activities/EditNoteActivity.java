package com.coltan.tapnote.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.coltan.tapnote.R;
import com.coltan.tapnote.db.DatabaseHandler;
import com.coltan.tapnote.db.Note;

public class EditNoteActivity extends BaseActivity {

    private EditText etTitle, etTag, etNote;
    private int id;
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
                return true;

            case R.id.action_delete:
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
                    //change your view and sort it by Alphabet
                    item.setIcon(R.drawable.ic_star_white_24dp);
                    starred = "1";
                    Log.d("Info", "Starred = " + starred);
                } else {
                    item.setIcon(R.drawable.ic_star_border_white_24dp);
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
        //overridePendingTransition(0, R.anim.slide_out_right);
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
