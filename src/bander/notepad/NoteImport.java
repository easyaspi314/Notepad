package bander.notepad;

import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import bander.provider.*;
import com.devin.notepad.R;
import java.io.*;

//import com.devin.notepad.R;

public class NoteImport extends Activity {
	private static final String[] PROJECTION = new String[] { Note._ID,
			Note.TITLE, Note.BODY };
	private static final int COLUMN_ID = 0;

	private static final int DIALOG_IMPORT = 1;

	private TextView mProgressLabel;
	private ProgressBar mProgressBar;
	private Button mActionButton;

	private ImportTask mImportTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setting the theme
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean theme = preferences.getBoolean("theme", true);
		if (theme) {
			String pref_Theme = preferences.getString("themeType", "");

			if (pref_Theme.trim().equalsIgnoreCase("0")) {
				setTheme(android.R.style.Theme_Holo_Light_Dialog);
			} else if (pref_Theme.trim().equalsIgnoreCase("1")) {
				setTheme(android.R.style.Theme_Holo_Light_Dialog);
			} else if (pref_Theme.trim().equalsIgnoreCase("2")) {
				setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
			}

			else if (pref_Theme.trim().equalsIgnoreCase("3")) {
				setTheme(android.R.style.Theme_Dialog);
			}
		} else {
			preferences = PreferenceManager.getDefaultSharedPreferences(this);

			// read preference
			String pref_Theme = preferences.getString("themeType", "");

			if (pref_Theme.trim().equalsIgnoreCase("0")) {
				setTheme(android.R.style.Theme_Holo_Dialog);
			} else if (pref_Theme.trim().equalsIgnoreCase("1")) {
				setTheme(android.R.style.Theme_Holo_Dialog);
			} else if (pref_Theme.trim().equalsIgnoreCase("2")) {
				setTheme(android.R.style.Theme_DeviceDefault_Dialog);
			}

			else if (pref_Theme.trim().equalsIgnoreCase("3")) {
				setTheme(android.R.style.Theme_Dialog);
			}
		}
		setContentView(R.layout.export);

		mProgressLabel = (TextView) findViewById(R.id.progress_label);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mActionButton = (Button) findViewById(R.id.action);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mProgressBar.setVisibility(View.GONE);

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			mProgressLabel.setText(R.string.error_nomedia);
			mActionButton.setVisibility(View.VISIBLE);
			mActionButton.setText(R.string.dialog_back);
			mActionButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
			return;
		}
		mProgressLabel.setText("");
		mActionButton.setVisibility(View.GONE);

		final String action = getIntent().getAction();
		if (action.equals("bander.notepad.action.ACTION_IMPORT")) {
			showDialog(DIALOG_IMPORT);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		cancelImport();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_IMPORT:
			dialog = new AlertDialog.Builder(this)
					// .setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.dialog_import)
					.setMessage(R.string.import_confirmation)
					.setPositiveButton(getString(android.R.string.ok),
							new DialogInterface.OnClickListener() {
								// OnClickListener
								public void onClick(DialogInterface dialog,
										int which) {
									startImport();
								}
							})
					.setNegativeButton(getString(android.R.string.cancel),
							new DialogInterface.OnClickListener() {
								// OnClickListener
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	private void startImport() {
		if (mImportTask == null
				|| mImportTask.getStatus() == AsyncTask.Status.FINISHED) {
			mActionButton.setVisibility(View.VISIBLE);
			mActionButton.setText(android.R.string.cancel);
			mActionButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					cancelImport();
				}
			});
			mProgressBar.setVisibility(View.VISIBLE);
			mImportTask = (ImportTask) new ImportTask().execute();
		}
	}

	private void cancelImport() {
		if (mImportTask != null
				&& mImportTask.getStatus() == AsyncTask.Status.RUNNING) {
			mImportTask.cancel(true);
			mImportTask = null;
			mActionButton.setVisibility(View.VISIBLE);
			mActionButton.setText(R.string.dialog_back);
			mActionButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
		}
	}

	private class ImportTask extends AsyncTask<Void, Integer, Integer> {
		private final File mDirectory;

		ImportTask() {
			File root = Environment.getExternalStorageDirectory();
			mDirectory = new File(root.getAbsolutePath() + "/notepad");
		}

		@Override
		public void onPreExecute() {
			mProgressLabel.setText(R.string.import_progress);
			mProgressBar.setProgress(0);
		}

		public Integer doInBackground(Void... params) {
			int imported = 0;
			try {
				mDirectory.mkdirs();

				File[] files = mDirectory.listFiles(new FileFilter() {
					public boolean accept(File file) {
						return (file.isFile() && file.canRead() && file
								.getName().endsWith(".txt"));
					}
				});

				final int count = files.length;
				for (int i = 0; i < count; i++) {
					publishProgress(i, count);
					if (isCancelled())
						return null;

					File file = files[i];

					String title = file.getName();
					title = title.substring(0, title.length() - 4); // strip off
																	// ".txt"

					char[] buffer = new char[(int) file.length()];
					FileReader reader = new FileReader(file);
					reader.read(buffer);
					reader.close();
					final String body = new String(buffer);

					ContentValues values = new ContentValues();
					values.put(Note.TITLE, title);
					values.put(Note.BODY, body);
					Cursor cursor = getContentResolver().query(
							Note.CONTENT_URI, PROJECTION, Note.TITLE + "=?",
							new String[] { title }, Note.DEFAULT_SORT_ORDER);

					if ((cursor.getCount() == 1) && cursor.moveToFirst()) {
						long id = cursor.getLong(COLUMN_ID);
						Uri uri = ContentUris.withAppendedId(Note.CONTENT_URI,
								id);
						getContentResolver().update(uri, values, null, null);
					} else {
						getContentResolver().insert(Note.CONTENT_URI, values);
					}
					cursor = null;

					imported++;
				}

				publishProgress(count, count);
			} catch (IOException e) {
				return null;
			}
			return imported;
		}

		@Override
		public void onProgressUpdate(Integer... values) {
			final ProgressBar progress = mProgressBar;
			progress.setMax(values[1]);
			progress.setProgress(values[0]);
		}

		@Override
		public void onCancelled() {
			//
		}

		@Override
		public void onPostExecute(Integer importCount) {
			if (importCount == null) {
				mProgressLabel.setText(R.string.import_failed);
			} else {
				mProgressLabel.setText(getResources().getQuantityString(
						R.plurals.import_done,
						importCount,
						new Object[] { importCount,
								mDirectory.getAbsolutePath() }));
			}
			mActionButton.setVisibility(View.VISIBLE);
			mActionButton.setText(R.string.dialog_back);
			mActionButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});

		}
	}

}
