package com.coltan.tapnote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.coltan.tapnote.db.DatabaseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class BackupRestoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // display backup path
        TextView backupPath = (TextView) findViewById(R.id.tv_backup_path);
        String path = getBackupFolder().toString();
        backupPath.setText(getString(R.string.backup_folder) + " " + path);

        //Backup button
        Button btnBackup = (Button) findViewById(R.id.btn_backup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupDatabaseTask bdt = new BackupDatabaseTask();
                bdt.execute();
            }
        });

        //Restore
        final Button btnRestore = (Button) findViewById(R.id.btn_restore_backup);
        final CheckBox cbRestore = (CheckBox) findViewById(R.id.cb_backup_confirm);
        cbRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRestore.isChecked()) {
                    btnRestore.setEnabled(true);
                } else {
                    btnRestore.setEnabled(false);
                }
            }
        });
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestoreDatabaseTask rdt = new RestoreDatabaseTask();
                rdt.execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private File getBackupFolder() {
        return new File(Environment.getExternalStorageDirectory(), "Tap Note");
    }

    private class BackupDatabaseTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... args) {
            String errorMsg;
            File dbFile = getApplication().getDatabasePath(DatabaseHandler.DATABASE_NAME);
            File exportDir = getBackupFolder();
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, dbFile.getName());
            errorMsg = null;
            try {
                file.createNewFile();
                FileInputStream in = new FileInputStream(dbFile);
                FileOutputStream out = new FileOutputStream(file);
                FileChannel inChannel = in.getChannel();
                FileChannel outChannel = out.getChannel();
                outChannel.transferFrom(inChannel, 0, inChannel.size());
                try {
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                } finally {
                    if (inChannel != null) {
                        inChannel.close();
                    }
                    if (outChannel != null) {
                        outChannel.close();
                    }
                }
                in.close();
                out.close();
            } catch (IOException e) {
                errorMsg = e.getMessage();
            }
            return errorMsg;
        }

        protected void onPostExecute(String errorMsg) {
            if (errorMsg == null) {
                Toast.makeText(BackupRestoreActivity.this, "Backup Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BackupRestoreActivity.this, "Backup Failed!", Toast.LENGTH_LONG).show();
                Log.d("Error", errorMsg);
            }
        }
    }

    private class RestoreDatabaseTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... args) {
            File dbBackupFile = new File(getBackupFolder(), "notedb");
            if (!dbBackupFile.exists()) {
                return getString(R.string.import_failed_nofile);
            } else if (!dbBackupFile.canRead()) {
                return getString(R.string.import_failed_noread);
            }
            File dbFile = getApplication().getDatabasePath(DatabaseHandler.DATABASE_NAME);
            getApplication().deleteDatabase(DatabaseHandler.DATABASE_NAME);

            try {
                dbFile.createNewFile();
                FileInputStream in = new FileInputStream(dbBackupFile);
                FileOutputStream out = new FileOutputStream(dbFile);
                FileChannel inChannel = in.getChannel();
                FileChannel outChannel = out.getChannel();
                outChannel.transferFrom(inChannel, 0, inChannel.size());
                try {
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                } finally {
                    if (inChannel != null) {
                        inChannel.close();
                    }
                    if (outChannel != null) {
                        outChannel.close();
                    }
                }
                in.close();
                out.close();

                return null;
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        protected void onPostExecute(String errorMsg) {
            if (errorMsg == null) {
                Toast.makeText(BackupRestoreActivity.this, "Restore Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BackupRestoreActivity.this, "Restore Failed!", Toast.LENGTH_LONG).show();
                Log.d("Error", errorMsg);
            }
        }
    }
}
