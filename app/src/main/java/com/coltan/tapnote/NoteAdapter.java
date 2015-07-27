package com.coltan.tapnote;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.coltan.tapnote.db.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {

    private List<Note> mNoteList;
    private List<Note> appListSearch;
    private int mWhichFrag;
    private Context context;
    private View v;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView txtTag;
        public TextView txtDate;
        public View relRow;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.note_head);
            txtFooter = (TextView) v.findViewById(R.id.note_excerpt);
            txtTag = (TextView) v.findViewById(R.id.note_tag);
            txtDate = (TextView) v.findViewById(R.id.note_time);
            relRow = v.findViewById(R.id.row_view);
        }
    }

    /*public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }*/

    /*public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }*/

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoteAdapter(List<Note> note, int whichFrag, Context context) {
        this.mNoteList = note;
        mWhichFrag = whichFrag;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vHolder = new ViewHolder(v);
        return vHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Note mNote = mNoteList.get(position);
        holder.txtHeader.setText(mNote.getTitle());
        String note = mNote.getNote();
        note = note.replace("\n", " ");
        String subNote;
        if (note.length() > 45) {
            subNote = note.substring(0, 45);
            subNote = subNote.concat("...");
        } else {
            subNote = note;
        }
        holder.txtFooter.setText(subNote);
        if (mNote.getTag().equals("")) {
            holder.txtTag.setVisibility(View.INVISIBLE);
        } else {
            holder.txtTag.setText(mNote.getTag());
        }
        //Convert millisecond
        String time = TimeUtils.getTimeAgo(mNote.getDate(), context);
        holder.txtDate.setText(time);
        //Log.d("NoteAdapter", mNote.getDate());
        holder.relRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent i = new Intent(context, EditNoteActivity.class);
                i.putExtra("id", mNote.getID());
                context.startActivity(i);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        });
        holder.relRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(mNote.getTitle(), mNote.getNote());
                clipboard.setPrimaryClip(clip);
                Snackbar.make(v, "Copied to clipboard", Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Note> results = new ArrayList<>();
                if (appListSearch == null) {
                    appListSearch = mNoteList;
                }
                if (charSequence != null) {
                    if (appListSearch != null && appListSearch.size() > 0) {
                        for (final Note note : appListSearch) {
                            if (note.getTitle().toLowerCase().contains(charSequence.toString())) {
                                results.add(note);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (mWhichFrag == 0) {
                    if (filterResults.count > 0) {
                        HomeFragment.setResultsMessage(false);
                    } else {
                        HomeFragment.setResultsMessage(true);
                    }

                } else {
                    if (filterResults.count > 0) {
                        StarredFragment.setResultsMessage(false);
                    } else {
                        StarredFragment.setResultsMessage(true);
                    }
                }
                mNoteList = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }


        };

    }
}
