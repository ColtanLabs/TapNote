package com.rj.tapnote;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class BackupRestoreActivity extends ActionBarActivity {

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
                //Log.d("Click", "Backup pressed");
                exportDB();
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
                //Log.d("Click", "Restore pressed");
                importDB();
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

    private void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.rj.tapnote" + "//databases//" + "noteDB";
                // create a File object for the parent directory
                File tapNoteDirectory = new File("/sdcard/Tap Note/");
                // have the object build the directory structure, if needed.
                if (!tapNoteDirectory.exists()) {
                    tapNoteDirectory.mkdirs();
                }
                String backupDBPath = "//Tap Note//" + "tapnotedb";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(this, "Backup Successful!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Backup Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void importDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.rj.tapnote" + "//databases//" + "noteDB";
                String backupDBPath = "//Tap Note//" + "tapnotedb"; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Import Successful!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Import Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
