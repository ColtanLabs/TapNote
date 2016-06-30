package com.coltan.tapnote.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.coltan.tapnote.R;
import com.coltan.tapnote.UtilsApp;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class AboutActivity extends BaseActivity {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvAppName = (TextView) findViewById(R.id.app_name);
        tvAppName.setText(getResources().getString(R.string.app_name) + " " + UtilsApp.getAppVersionName(this) + " (" + UtilsApp.getAppVersionCode(this) + ")");

        CardView cvRate = (CardView) findViewById(R.id.about_googleplay);
        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.coltan.tapnote"));
                startActivity(intent);
            }
        });
        CardView cvCommunity = (CardView) findViewById(R.id.about_googleplus);
        cvCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://plus.google.com/communities/110446499770842385450"));
                startActivity(intent);
            }
        });

        CardView cvLicenses = (CardView) findViewById(R.id.about_licenses);
        cvLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LibsBuilder()
                        //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withFields(R.string.class.getFields())
                        .withActivityTitle(getString(R.string.about_license))
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .start(mContext);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
