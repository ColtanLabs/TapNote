package com.coltan.tapnote;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coltan.tapnote.db.DatabaseHandler;
import com.coltan.tapnote.db.Note;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private List<String> mId = new ArrayList<>();
    private List<String> mHeader = new ArrayList<>();
    private List<String> mTag = new ArrayList<>();
    private ArrayList<String> mNote = new ArrayList<>();
    private ArrayList<Long> mDate = new ArrayList<>();

    private Context context;
    private RecyclerView mRecyclerView;
    private MenuItem searchItem;
    private SearchView searchView;
    private static LinearLayout noResults;
    RecyclerView.Adapter mAdapter;

    public static final String SORT_PREFS_NAME = "SortPrefFile";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        noResults = (LinearLayout) v.findViewById(R.id.noResults);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        initData();

        mAdapter = new NoteAdapter(createList(mId, mHeader, mNote, mTag, mDate), 0, context);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddNoteActivity.class));
            }
        });

        return v;
    }

    private void initData() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        List<Note> notes = db.getAllNotes();
        for (Note nt : notes) {
            mId.add(String.valueOf(nt.getID()));
            mHeader.add(nt.getTitle());
            mNote.add(nt.getNote());
            mTag.add(nt.getTag());
            mDate.add(nt.getDate());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == R.id.menu_action_sort_oldest) {
            SharedPreferences settings = context.getSharedPreferences(SORT_PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("sortMode", "date_oldest");
            editor.apply();
            mAdapter.notifyDataSetChanged();
            return true;
        }
        if (menuId == R.id.menu_action_sort_newest) {
            SharedPreferences settings = context.getSharedPreferences(SORT_PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("sortMode", "date_newest");
            editor.apply();
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            ((NoteAdapter) mRecyclerView.getAdapter()).getFilter().filter("");
        } else {
            ((NoteAdapter) mRecyclerView.getAdapter()).getFilter().filter(newText.toLowerCase());
        }

        return false;
    }

    public static void setResultsMessage(Boolean result) {
        if (result) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    if (searchItem.isVisible() && !searchView.isIconified()) {
                        searchView.onActionViewCollapsed();
                    } else {
                        getActivity().finish();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private List<Note> createList(List<String> id, List<String> title, List<String> note, List<String> tag, List<Long> date) {
        List<Note> res = new ArrayList<>();
        for (int i = 0; i < id.size(); i++) {
            Note noteInfo = new Note(Integer.parseInt(id.get(i)), title.get(i), note.get(i), tag.get(i), date.get(i));
            res.add(noteInfo);
        }

        return res;
    }
}
