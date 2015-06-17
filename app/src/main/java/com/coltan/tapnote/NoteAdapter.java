package com.coltan.tapnote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> mDataset, mSubHeader, mTag;
    private ArrayList<String> appListSearch;
    private int mWhichFrag;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView txtTag;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.note_head);
            txtFooter = (TextView) v.findViewById(R.id.note_excerpt);
            txtTag = (TextView) v.findViewById(R.id.note_tag);
        }
    }

    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoteAdapter(ArrayList<String> myHeader, ArrayList<String> mySubHeader, ArrayList<String> myTag, int whichFrag) {
        mDataset = myHeader;
        mSubHeader = mySubHeader;
        mTag = myTag;
        mWhichFrag = whichFrag;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vHolder = new ViewHolder(v);
        return vHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //final String name = mDataset.get(position);
        holder.txtHeader.setText(mDataset.get(position));
        holder.txtFooter.setText(mSubHeader.get(position));
        if (mTag.get(position).equals("")) {
            holder.txtTag.setVisibility(View.INVISIBLE);
        } else {
            holder.txtTag.setText(mTag.get(position));
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<String> results = new ArrayList<>();
                if (appListSearch == null) {
                    appListSearch = mDataset;
                }
                if (charSequence != null) {
                    if (appListSearch != null && appListSearch.size() > 0) {
                        for (final String rNote : appListSearch) {
                            if (rNote.toLowerCase().contains(charSequence.toString())) {
                                results.add(rNote);
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
                mDataset = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }


        };

    }
}
