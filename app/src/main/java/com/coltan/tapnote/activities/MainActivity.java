package com.coltan.tapnote.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.coltan.tapnote.R;
import com.coltan.tapnote.UtilsApp;
import com.coltan.tapnote.fragments.HomeFragment;
import com.coltan.tapnote.fragments.StarredFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends BaseActivity {

    Toolbar toolbar;
    private Drawer result = null;
    private static final String TAG = "MainActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mContext = this;

        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.home)).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(getString(R.string.starred)).withIcon(FontAwesome.Icon.faw_star).withIdentifier(2),
                        new DividerDrawerItem(),
                        /*new SecondaryDrawerItem().withName(getString(R.string.backup_restore)).withIcon(GoogleMaterial.Icon.gmd_swap).withIdentifier(23),*/
                        new SecondaryDrawerItem().withName(getString(R.string.settings)).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(24),
                        new SecondaryDrawerItem().withName(getString(R.string.feedback)).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(26).withSelectable(false),
                        new SecondaryDrawerItem().withName(getString(R.string.about)).withIcon(FontAwesome.Icon.faw_info).withIdentifier(25)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        long identifier = iDrawerItem.getIdentifier();
                        switch ((int) identifier) {
                            case 1:
                                onHomeSelected();
                                toolbar.setTitle(getString(R.string.app_name));
                                break;
                            case 2:
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
                            case 26:
                                sendMail();
                                break;
                        }
                        return false;
                    }
                })

                .build();
        result.setSelection(1);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void sendMail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"coltan.labs@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject).concat(" " + UtilsApp.getAppVersionName(mContext) + " (" + UtilsApp.getAppVersionCode(mContext) + ")"));
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, getString(R.string.mail_chooser_title)));
    }

    private void onHomeSelected() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();
    }

    private void onStarredSelected() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new StarredFragment()).commit();
    }

    private void onBackupRestoreSelected() {
        Intent intent = new Intent(this, BackupRestoreActivity.class);
        startActivity(intent);
    }

    private void onSettingsSelected() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void onAboutSelected() {
        Intent intent = new Intent(this, AboutActivity.class);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
