package com.rj.tapnote;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.rj.tapnote.db.DatabaseHandler;
import com.rj.tapnote.db.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNoteActivity extends ActionBarActivity {

    EditText etTitle, etTag, etNote;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        id = getIntent().getIntExtra("id", 1);
        //Toast.makeText(this, "ID " + id, Toast.LENGTH_SHORT).show();

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
        etTag.setText(an.getTag());
        etNote.setText(an.getNote());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int menuId = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (menuId == R.id.action_settings) {
            return true;
        }
        if (menuId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
        }
        if (menuId == R.id.action_delete) {
            DatabaseHandler db = new DatabaseHandler(this);
            db.deleteNote(id);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String title = etTitle.getText().toString();
        String tag = etTag.getText().toString();
        String note = etNote.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        if (title.isEmpty() || title.equals("")) {
            etTitle.setError("Please enter a title");
        } else {
            Note nt = new Note();
            nt.setID(id);
            nt.setTitle(title);
            nt.setTag(tag);
            nt.setNote(note);
            nt.setDate(formattedDate);
            Log.d("ID", String.valueOf(id));
            DatabaseHandler db = new DatabaseHandler(this);

            int a = db.updateNote(nt);
            if (a == 1) {
                Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Unable to update", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
