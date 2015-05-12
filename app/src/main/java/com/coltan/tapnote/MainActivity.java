package com.coltan.tapnote;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Drawer.Result result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        createDrawer();
    }

    private void createDrawer() {
        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.ic_action_home).withIdentifier(0),
                        new SectionDrawerItem().withName(R.string.labels),
                        new PrimaryDrawerItem().withName("Personal").withIcon(R.drawable.ic_action_label).withIdentifier(5),
                        new PrimaryDrawerItem().withName("Work").withIcon(R.drawable.ic_action_label).withIdentifier(6),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Backup/Restore").withIdentifier(23),
                        new SecondaryDrawerItem().withName("Settings").withIdentifier(24)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        int identifier = drawerItem.getIdentifier();
                        switch (identifier) {
                            case 0:
                                onHomeSelected();
                                break;
                            case 5:
                                onPersonalSelected();
                                break;
                            case 23:
                                onBackupRestoreSelected();
                                break;
                            case 24:
                                onSettingsSelected();
                                break;
                        }
                    }
                })
                .withSelectedItem(0)
                .build();
        result.setSelectionByIdentifier(0);
        //result.openDrawer();
        //result.closeDrawer();
        //result.isDrawerOpen();
        //result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        //result.addItem(..);
    }

    private void onHomeSelected() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, newFragment);
        ft.commit();
    }

    private void onPersonalSelected() {

    }

    private void onBackupRestoreSelected() {
        Intent intent = new Intent(this, BackupRestoreActivity.class);
        startActivity(intent);
    }

    private void onSettingsSelected() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

}
