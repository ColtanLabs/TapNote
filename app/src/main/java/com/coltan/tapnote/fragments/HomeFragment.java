package com.coltan.tapnote.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coltan.tapnote.DividerItemDecoration;
import com.coltan.tapnote.NoteAdapter;
import com.coltan.tapnote.R;
import com.coltan.tapnote.activities.AddNoteActivity;
import com.coltan.tapnote.data.Note;
import com.coltan.tapnote.data.NoteContract;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "HomeFragment";

    private static final int NOTE_LOADER_ID = 0;

    String[] projections = {NoteContract.NoteEntry._ID, NoteContract.NoteEntry.COLUMN_TITLE,
            NoteContract.NoteEntry.COLUMN_NOTE, NoteContract.NoteEntry.COLUMN_TAG,
            NoteContract.NoteEntry.COLUMN_DATE};

    private ArrayList<Note> mNote = new ArrayList<>();

    private Context mContext;
    private RecyclerView mRecyclerView;
    private MenuItem searchItem;
    private SearchView searchView;
    private static LinearLayout noResults;
    private RecyclerView.Adapter mAdapter;

    public static final String SORT_PREFS_NAME = "SortPrefFile";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mNote = savedInstanceState.getParcelableArrayList("note");
            /*Log.d(TAG, "onCreate: savedInstance");
            for (int i = 0; i < mNote.size(); i++) {
                Log.d(TAG, "onCreate: Title" + mNote.get(i).getTitle());
            }*/
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTE_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        noResults = (LinearLayout) v.findViewById(R.id.noResults);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        mAdapter = new NoteAdapter(mNote, 0, mContext);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("note", mNote);
        super.onSaveInstanceState(outState);
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
            SharedPreferences settings = mContext.getSharedPreferences(SORT_PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("sortMode", "date_oldest");
            editor.apply();
            mAdapter.notifyDataSetChanged();
            return true;
        }
        if (menuId == R.id.menu_action_sort_newest) {
            SharedPreferences settings = mContext.getSharedPreferences(SORT_PREFS_NAME, 0);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == NOTE_LOADER_ID) {
            loader = new CursorLoader(getActivity(),
                    NoteContract.NoteEntry.CONTENT_URI,
                    projections,
                    null,
                    null,
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            Log.d(TAG, "No data in database");
        }
        //Log.v(TAG, DatabaseUtils.dumpCursorToString(data));
        while (data.moveToNext()) {
            String id = data.getString(data.getColumnIndex(NoteContract.NoteEntry._ID));
            String title = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_TITLE));
            String note = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE));
            String tag = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_TAG));
            String date = data.getString(data.getColumnIndex(NoteContract.NoteEntry.COLUMN_DATE));
            Note n = new Note(id, title, note, tag, date);
            mNote.add(n);
        }
        data.close();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
