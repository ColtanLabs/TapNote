package com.rj.tapnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.rj.tapnote.db.DatabaseHandler;
import com.rj.tapnote.db.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements RecyclerItemClickListener.OnItemClickListener {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mId = new ArrayList<>();
    private ArrayList<String> mHeader = new ArrayList<>();
    private ArrayList<String> mSubHeader = new ArrayList<>();
    private ArrayList<String> mTag = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.show();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DatabaseHandler db = new DatabaseHandler(this);
        List<Note> notes = db.getAllNotes();
        for (Note nt : notes) {
            mId.add(String.valueOf(nt.getID()));
            mHeader.add(nt.getTitle());
            String note = nt.getNote();
            note = note.replace("\n", " ");
            String cnote;
            if (note.length() > 25) {
                cnote = note.substring(0, 25);
                cnote = cnote.concat("...");
            } else {
                cnote = note;
            }
            mSubHeader.add(cnote);
            mTag.add(nt.getTag());
        }

        mAdapter = new NoteAdapter(mHeader, mSubHeader, mTag);
        mRecyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddNoteActivity.class));
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @Override
    public void onItemClick(View childView, int position) {
        // Do something when an item is clicked.
        Toast.makeText(this, "Single tap " + position, Toast.LENGTH_SHORT).show();
        int id = Integer.parseInt(mId.get(position));
        Intent i = new Intent(this, EditNoteActivity.class);
        i.putExtra("id", id);
        startActivity(i);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        // Do another thing when an item is long pressed.
        Toast.makeText(this, "Long tap " + position, Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }
}