package bander.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

public class SetPreferenceActivity extends FragmentActivity {
	Fragment myFragment = new PrefsFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Startup.setThemeFromPreferences(this, "Prefs");
		// Starting the Fragment
		Log.i("INFO", "Starting PreferenceFragment...");

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(android.R.id.content, myFragment);
		transaction.commit();
	}

	@Override
	public void onBackPressed() {
		Intent prefsActivity = new Intent(this, Startup.class);
		prefsActivity.putExtra("noPassword", true);
		startActivity(prefsActivity);
		finish();

	}
}
