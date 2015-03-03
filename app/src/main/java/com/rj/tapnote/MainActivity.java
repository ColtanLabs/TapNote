package com.rj.tapnote;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

    public ListView list;

    public String[] noteHeadings;
    String[] noteExcerpts;
    String[] noteTags;
    Resources res = getResources();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public String[] getRes() {

        noteHeadings = res.getStringArray(R.array.note_head);
        noteExcerpts = res.getStringArray(R.array.note_excerpt);
        noteTags = res.getStringArray(R.array.note_tag);

        return noteHeadings;

    }


    @Override
    public View findViewById(int id) {
        list = (ListView) findViewById(R.id.main_list);
        return list;
    }

}

class TapNote extends ArrayAdapter<String> {

    TapNote(Context c, String[] noteHeadings) {
        super(c, R.layout.single_row, R.id.note_head, noteHeadings);

    }
}
