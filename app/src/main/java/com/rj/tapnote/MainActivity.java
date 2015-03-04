package com.rj.tapnote;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {


    String[] noteHeadings;
    String[] noteExcerpts;
    String[] noteTags;

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();


        noteHeadings = res.getStringArray(R.array.note_head);
        noteExcerpts = res.getStringArray(R.array.note_excerpt);
        noteTags = res.getStringArray(R.array.note_tag);


        list = (ListView) findViewById(R.id.main_list);


        TapNote a = new TapNote(this, noteHeadings, noteExcerpts, noteTags);
        list.setAdapter(a);
    }
}

class TapNote extends ArrayAdapter<String>{

    Context context;
    String[] headArray;
    String[] excerptArray;
    String[] tagArray;

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
