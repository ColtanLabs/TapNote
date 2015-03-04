package com.rj.tapnote;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    String[] noteHeadings, noteExcerpts, noteTags;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Resources res = getResources();
        noteHeadings = res.getStringArray(R.array.note_head);
        noteExcerpts = res.getStringArray(R.array.note_excerpt);
        noteTags = res.getStringArray(R.array.note_tag);

        list = (ListView) findViewById(R.id.main_list);
        TapNote a = new TapNote(this, noteHeadings, noteExcerpts, noteTags);
        list.setAdapter(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Just clicked Setting", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

class TapNote extends ArrayAdapter<String> {

    Context context;
    String[] headArray, excerptArray, tagArray;

    TapNote(Context c, String[] noteHeadings, String[] noteExcerpts, String[] noteTags) {
        super(c, R.layout.single_row1, R.id.note_head, noteHeadings);
        this.context = c;
        this.headArray = noteHeadings;
        this.excerptArray = noteExcerpts;
        this.tagArray = noteTags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row1, parent, false);
        TextView tnHead = (TextView) row.findViewById(R.id.note_head);
        TextView tnExcerpt = (TextView) row.findViewById(R.id.note_excerpt);
        TextView tnTag = (TextView) row.findViewById(R.id.note_tag);
        tnHead.setText(headArray[position]);
        tnExcerpt.setText(excerptArray[position]);
        tnTag.setText(tagArray[position]);

        return row;
    }
}
