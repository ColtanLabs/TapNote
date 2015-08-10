package com.coltan.tapnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class MainActivity extends BaseActivity {

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
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.home)).withIcon(FontAwesome.Icon.faw_home).withIdentifier(0),
                        new PrimaryDrawerItem().withName(getString(R.string.starred)).withIcon(FontAwesome.Icon.faw_star).withIdentifier(1),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(getString(R.string.backup_restore)).withIcon(GoogleMaterial.Icon.gmd_settings_backup_restore).withIdentifier(23),
                        new SecondaryDrawerItem().withName(getString(R.string.settings)).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(24),
                        new SecondaryDrawerItem().withName(getString(R.string.about)).withIcon(FontAwesome.Icon.faw_info).withIdentifier(25)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        int identifier = drawerItem.getIdentifier();
                        switch (identifier) {
                            case 0:
                                onHomeSelected();
                                toolbar.setTitle(getString(R.string.app_name));
                                break;
                            case 1:
                                onStarredSelected();
                                toolbar.setTitle(getString(R.string.starred));
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

    private void onHomeSelected() {
        Fragment newFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, newFragment);
        ft.commit();
    }

    private void onStarredSelected() {
        Fragment newFragment = new StarredFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, newFragment);
        ft.commit();
    }

    private void onBackupRestoreSelected() {
        Intent intent = new Intent(this, BackupRestoreActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
    }

    private void onSettingsSelected() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
    }

    private void onAboutSelected() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
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
