/*
 * Copyright (C) 2014-2015 Devin Hussey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bander.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.devin.notepad.R;

public class PreferenceActivityAppCompat extends ActionBarActivity {
	Fragment myFragment = new AppCompatPrefsFragment();

    Toolbar toolbar;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Notepad.setAppCompatThemeFromPreferences(this, "Prefs");
		setContentView(R.layout.preferences_toolbar);

        SharedPreferences mSettings = PreferenceManager
                .getDefaultSharedPreferences(this);

        ViewStub stub = (ViewStub) findViewById(R.id.toolbarWrapper);
        if (mSettings.getBoolean("darkAppCompatTheme", false))
            stub.setLayoutResource(R.layout.toolbar_dark);
        else
            stub.setLayoutResource(R.layout.toolbar_light);
        stub.inflate();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Notepad.setToolbarColor(this);
        setSupportActionBar(toolbar);
        if (mSettings.getBoolean("darkAppCompatTheme", false))
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        else
            toolbar
                    .setNavigationIcon(IconTintFactory
                            .setDarkMaterialColor(R.drawable.abc_ic_ab_back_mtrl_am_alpha, this));
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onBackPressed();
                }
            });
        }
		// Starting the Fragment
		Log.i("INFO", "Starting PreferenceFragment...");

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.prefContent, myFragment);
		transaction.commit();
	}

	@Override
	public void onBackPressed() {

		Intent prefsActivity = new Intent(this, Notepad.class);
		prefsActivity.putExtra("noPassword", true);
		startActivity(prefsActivity);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    public void updateToolbar(ActionBarActivity activity) {
        this.recreate();
    }
}
