package com.coltan.tapnote;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.coltan.tapnote.db.DatabaseHandler;

public class DeleteDatabasePreference extends DialogPreference {

    private final Context mContext;

    public DeleteDatabasePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        //setDialogLayoutResource(R.layout.dialog_delete_database);
        setPositiveButtonText(R.string.delete);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            // deal with persisting your values here
            DatabaseHandler dh = new DatabaseHandler(mContext);
            dh.deleteAllNote();
            Toast.makeText(mContext, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
