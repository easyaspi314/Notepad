package bander.notepad;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import bander.provider.Note;
//import com.devin.notepad.R;
import com.devin.notepad.R;

/** Secondary activity for Notepad, shows title of a single note and allows editing it. */

//	I set the theme to Dialog. Can this be set to have real buttons?
public class TitleEdit extends Activity
{
	public static final String EDIT_TITLE_ACTION = "bander.notepad.action.EDIT_TITLE";

	private static final int REVERT_ID 		= Menu.FIRST + 0;
	private static final int PREFS_ID 		= Menu.FIRST + 1;

	private static final String[] PROJECTION = new String[] { 
		Note._ID, Note.TITLE
	};

	private static final int COLUMN_INDEX_TITLE = 1;

	private static final String ORIGINAL_TITLE = "originalTitle";

	private EditText mTitleText;

	private Uri mUri;
	private Cursor mCursor;
	private String mOriginalTitle;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (savedInstanceState != null)
		{
			mOriginalTitle = savedInstanceState.getString(ORIGINAL_TITLE);
		}
		boolean theme=preferences.getBoolean("theme", true);
		if (theme)
		{
			String pref_Theme = preferences.getString("themeType", "");

			if (pref_Theme.trim().equalsIgnoreCase("0"))
			{
				setTheme(android.R.style.Theme_Holo_Light_Dialog); 
			}
			else if (pref_Theme.trim().equalsIgnoreCase("1"))
			{
				setTheme(android.R.style.Theme_Holo_Light_Dialog);
			}
			else if (pref_Theme.trim().equalsIgnoreCase("2"))
			{
				setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
			}

			else if (pref_Theme.trim().equalsIgnoreCase("3"))
			{
				setTheme(android.R.style.Theme_Dialog);
			}
		}
		else
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(this);

			//read preference
			String pref_Theme = preferences.getString("themeType", "");

			if (pref_Theme.trim().equalsIgnoreCase("0"))
			{
				setTheme(android.R.style.Theme_Holo_Dialog); 
			}
			else if (pref_Theme.trim().equalsIgnoreCase("1"))
			{
				setTheme(android.R.style.Theme_Holo_Dialog);
			}
			else if (pref_Theme.trim().equalsIgnoreCase("2"))
			{
				setTheme(android.R.style.Theme_DeviceDefault_Dialog);
			}

			else if (pref_Theme.trim().equalsIgnoreCase("3"))
			{
				setTheme(android.R.style.Theme_Dialog);
			}
		}
		setContentView(R.layout.edit_title);
		getWindow().setLayout(LayoutParams.MATCH_PARENT /* width */ , LayoutParams.WRAP_CONTENT);
		mUri = getIntent().getData();
		//	Get rid of this!!! Cursor is deprecated!
		mCursor = getContentResolver().query(mUri, PROJECTION, null, null, null);

		mTitleText = (EditText) this.findViewById(R.id.title);

		Button confirmButton = (Button) findViewById(R.id.confirm);
		Button cancelButton = (Button) findViewById(R.id.cancel);

		confirmButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view)
				{
					finish();
				}
			});
		cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view)
				{
					cancelEdit();
				}
			});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putString(ORIGINAL_TITLE, mOriginalTitle);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		//	Get rid of this!!! Cursor is deprecated!
		if (mCursor != null)
		{
			mCursor.moveToFirst();
			String title = mCursor.getString(COLUMN_INDEX_TITLE);
			mTitleText.setTextKeepState(title);

			if (mOriginalTitle == null)
			{
				mOriginalTitle = title;
			}
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		//	Get rid of this!!! Cursor is deprecated!
		if (mCursor != null)
		{
			ContentValues values = new ContentValues();
			values.put(Note.TITLE, mTitleText.getText().toString());
			getContentResolver().update(mUri, values, null, null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		boolean result = super.onCreateOptionsMenu(menu);

		menu.add(0, REVERT_ID, 0, R.string.menu_revert)
			.setIcon(android.R.drawable.ic_menu_revert);

		menu.add(0, PREFS_ID, 0, R.string.menu_prefs)
			.setIcon(android.R.drawable.ic_menu_preferences);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case REVERT_ID:
				mTitleText.setTextKeepState(mOriginalTitle);
				return true;
			case PREFS_ID:
				Intent prefsActivity = new Intent(this, SetPreferenceActivity.class);
				startActivity(prefsActivity);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Cancels the current edit, finishes the activity. */
	private final void cancelEdit()
	{
		//	Get rid of this!!! Cursor is deprecated!
		if (mCursor != null)
		{
			//	mCursor.close();
			mCursor = null;
			ContentValues values = new ContentValues();
			values.put(Note.TITLE, mOriginalTitle);
			getContentResolver().update(mUri, values, null, null);
		}
		setResult(RESULT_CANCELED);
		finish();
	}

}
