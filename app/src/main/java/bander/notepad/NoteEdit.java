/*
 * Copyright (C) 2011 BanderLabs
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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.devin.notepad.R;

import bander.provider.Note;

/**
 * Secondary activity for Notepad, shows details of a single note and allows
 * editing it.
 */
public class NoteEdit extends Activity {
	private static final int REVERT_ID = Menu.FIRST + 0;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int SEND_ID = Menu.FIRST + 2;
	private static final int PREFS_ID = Menu.FIRST + 3;

	private static final int STATE_EDIT = 0;
	private static final int STATE_INSERT = 1;

	private static final int EXIT = 0;

	private static final String[] PROJECTION = new String[] { Note._ID,
			Note.TITLE, Note.BODY, Note.CURSOR, Note.SCROLL_Y };

	private static final String ORIGINAL_NOTE = "originalNote";

	private int mState;
	private Uri mUri;

	private EditText mBodyText;
	private Note mOriginalNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setting the theme

		Notepad.setThemeFromPreferences(this);

		if (savedInstanceState != null) {
			final Object note = savedInstanceState.get(ORIGINAL_NOTE);
			if (note != null)
				mOriginalNote = (Note) note;
		}

		final Intent intent = getIntent();
		final String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action)
				|| Intent.ACTION_EDIT.equals(action)) {
			mState = STATE_EDIT;
			mUri = intent.getData();
		} else if (Intent.ACTION_INSERT.equals(action)) {
			mState = STATE_INSERT;
			if (mOriginalNote == null) {
				mUri = getContentResolver().insert(intent.getData(), null);
			} else {
				mUri = mOriginalNote.getUri();
			}

			setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));
		}

		if (mUri == null) {
			finish();
			return;
		}

		{
			setContentView(R.layout.edit);
			// Action Bar HomeAsUp
				ActionBar actionBar = getActionBar();
				if (actionBar != null) {
					actionBar.setDisplayHomeAsUpEnabled(true);
				}

			mBodyText = (EditText) findViewById(R.id.body);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(ORIGINAL_NOTE, mOriginalNote);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		// Font size
		float textSize = Float.valueOf(preferences.getString("textSize", "16"));
		mBodyText.setTextSize(textSize);
		// Monospace font
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean typeface = settings.getBoolean("typeface", true);
		if (typeface) {
			Typeface font = Typeface.MONOSPACE;
			mBodyText.setTypeface(font);
		} else {
			mBodyText.setTypeface(Typeface.SANS_SERIF);
		}
		// Auto-correct
		boolean input = preferences.getBoolean("inputType", true);
		if (input) {
			// AutoCorrect on
			mBodyText.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
					| InputType.TYPE_TEXT_FLAG_MULTI_LINE
					| InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
		} else {
			// AutoCorrect off
			mBodyText.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
					| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		}
		// Get rid of this!!! Cursor is deprecated!
		Cursor cursor = getContentResolver().query(mUri, PROJECTION, null, null, null);
		Note note = Note.fromCursor(cursor);
		cursor = null;

		if (note != null) {
			if (mOriginalNote == null)
				mOriginalNote = note;
			mBodyText.setTextKeepState(note.getBody());

			Boolean rememberPosition = preferences.getBoolean(
					"rememberPosition", true);
			if (rememberPosition) {
				mBodyText.setSelection(note.getCursor());
				mBodyText.scrollTo(0, note.getScrollY());
			}
		}
	}

	@Override
	public void onBackPressed() {
		showDialog(EXIT);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case EXIT:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
					.setTitle("Exit?")
					.setMessage("Do you want to save?")
					.setPositiveButton("Save",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							})
					.setNeutralButton("Discard",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									cancelNote();
								}
							}).setNegativeButton(android.R.string.cancel, null)
					.create();
		}
		return null;
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mUri != null) {
			String bodyText = mBodyText.getText().toString();
			int length = bodyText.length();

			if ((mState == STATE_INSERT) && isFinishing() && (length == 0)) {
				// If inserting and finishing and no text then delete the note.
				setResult(RESULT_CANCELED);
				deleteNote();
			} else {
				ContentValues values = mOriginalNote.getContentValues();
				if (values.containsKey(Note._ID))
					values.remove(Note._ID);

				if (mState == STATE_INSERT) {
					String[] lines = bodyText.split("[\n\\.]");
					String title = (lines.length > 0) ? lines[0]
							: getString(android.R.string.untitled);
					if (title.length() > 30) {
						int lastSpace = title.lastIndexOf(' ');
						if (lastSpace > 0) {
							title = title.substring(0, lastSpace);
						}
					}
					values.put(Note.TITLE, title);
				}
				values.put(Note.BODY, bodyText);
				values.put(Note.CURSOR, mBodyText.getSelectionStart());
				values.put(Note.SCROLL_Y, mBodyText.getScrollY());

				getContentResolver().update(mUri, values, null, null);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);

		if (mState == STATE_EDIT) {
			menu.add(0, REVERT_ID, 0, R.string.menu_revert).setIcon(
					android.R.drawable.ic_menu_revert);
			menu.add(0, DELETE_ID, 0, R.string.menu_delete).setIcon(
					android.R.drawable.ic_menu_delete);
		}
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String theme = preferences.getString("colorScheme", "Dark");
		String version = preferences.getString("themeType", "0");

		if (theme.equals("Light") && !version.equals("3")) {
			// Light theme, dark icons, but GB theme always has light icons.
			menu.add(0, SEND_ID, 0, R.string.menu_send)
					.setIcon(R.drawable.ic_action_share_light)
					.setShowAsActionFlags(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			return result;
		} else {
			// Dark theme, light icons.
			menu.add(0, SEND_ID, 0, R.string.menu_send)
					.setIcon(R.drawable.ic_action_share_dark)
					.setShowAsActionFlags(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			return result;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case DELETE_ID:
			deleteNote(this, mUri);
			return true;
		case REVERT_ID:
			mBodyText.setTextKeepState(mOriginalNote.getBody());
			return true;
		case SEND_ID:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, mBodyText.getText().toString());
			startActivity(Intent.createChooser(intent,
					getString(R.string.menu_send)));
			return true;
		case PREFS_ID:
			Intent prefsActivity = new Intent(this, SetPreferenceActivity.class);
			startActivity(prefsActivity);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Cancels the current edit, finishes the activity. */
	private void cancelNote() {
		if (mUri != null) {
			if (mState == STATE_EDIT) {
				ContentValues values = mOriginalNote.getContentValues();
				getContentResolver().update(mUri, values, null, null);
				mUri = null;
			} else if (mState == STATE_INSERT) {
				// Empty note was inserted on startup, clean up.
				deleteNote();
			}
		}
		setResult(RESULT_CANCELED);
		finish();
	}

	/** Deletes the current note. */
	private void deleteNote() {
		if (mUri != null) {
			getContentResolver().delete(mUri, null, null);
			mUri = null;
		}
	}

	/**
	 * Delete a note, confirm when preferred.
	 * 
	 * @param context
	 *            Context to use.
	 * @param uri
	 *            ID of the note to delete.
	 */
	private void deleteNote(Context context, Uri uri) {
		final Uri noteUri = uri;
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Boolean deleteConfirmation = preferences.getBoolean(
				"deleteConfirmation", true);
		if (deleteConfirmation) {
			AlertDialog alertDialog = new AlertDialog.Builder(context)
					// .setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.dialog_delete)
					.setMessage(R.string.delete_confirmation)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								// OnClickListener
								public void onClick(DialogInterface dialog,
										int which) {
									getContentResolver().delete(noteUri, null,
											null);
									finish();
								}
							}).setNegativeButton(android.R.string.cancel, null)
					.create();
			alertDialog.show();
		} else {
			getContentResolver().delete(noteUri, null, null);
			finish();
		}
	}

}
