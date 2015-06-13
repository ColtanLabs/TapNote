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

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        createDrawer();
    }

    private void createDrawer() {
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(0),
                        new PrimaryDrawerItem().withName("Starred").withIcon(FontAwesome.Icon.faw_star).withIdentifier(1),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Backup/Restore").withIcon(GoogleMaterial.Icon.gmd_settings_backup_restore).withIdentifier(23),
                        new SecondaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(24),
                        new SecondaryDrawerItem().withName("About").withIcon(FontAwesome.Icon.faw_info).withIdentifier(25)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        int identifier = drawerItem.getIdentifier();
                        switch (identifier) {
                            case 0:
                                onHomeSelected();
                                toolbar.setTitle("Tap Note");
                                break;
                            case 1:
                                onStarredSelected();
                                toolbar.setTitle("Starred");
                                break;
                            case 23:
                                onBackupRestoreSelected();
                                break;
                            case 24:
                                onSettingsSelected();
                                break;
                            case 25:
                                onAboutSelected();
                                break;
                        }
                        return false;
                    }
                })
                .build();
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        result.setSelectionByIdentifier(0);
    }

    private void onAboutSelected() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void onHomeSelected() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, newFragment);
        ft.commit();
    }

    private void onStarredSelected() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new StarredFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, newFragment);
        ft.commit();
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
