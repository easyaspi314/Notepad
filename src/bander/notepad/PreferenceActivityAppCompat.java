package bander.notepad;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import com.devin.notepad.R;

public class PreferenceActivityAppCompat extends ActionBarActivity {
	Fragment myFragment = new PrefsFragment();
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Startup.setAppCompatThemeFromPreferences(this, "Prefs");
		setContentView(R.layout.preferences_toolbar);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
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

		Intent prefsActivity = new Intent(this, Startup.class);
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
}
