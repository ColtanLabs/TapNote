package com.coltan.tapnote;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.coltan.tapnote.db.DatabaseHandler;

public class DeleteDatabasePreference extends DialogPreference {

    private Context mContext;

    public DeleteDatabasePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        //setDialogLayoutResource(R.layout.dialog_delete_database);
        setPositiveButtonText(R.string.delete);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        // view is your layout expanded and added to the dialog
        // find and hang on to your views here, add click listeners etc
        // basically things you would do in onCreate
        //mTextView = (TextView)view.findViewById(R.Id.mytextview);
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
