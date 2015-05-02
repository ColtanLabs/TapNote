package com.coltan.tapnote;

import android.os.Bundle;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

public class AboutActivity extends LibsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setIntent(new Libs.Builder().withFields(R.string.class.getFields()).withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR).withActivityTitle(getString(R.string.about)).intent(this));

        super.onCreate(savedInstanceState);
    }
}
