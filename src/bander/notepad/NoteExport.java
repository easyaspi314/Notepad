package bander.notepad;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import bander.provider.*;
import com.devin.notepad.R;
import java.io.*;
//import com.devin.notepad.R;

public class NoteExport extends Activity {
	private static final String[] PROJECTION = new String[] { 
		Note._ID, Note.TITLE, Note.BODY
	};
	private static final int COLUMN_TITLE = 1;
	private static final int COLUMN_BODY = 2;

	private static final int DIALOG_EXPORT = 1;

	private TextView mProgressLabel;
	private ProgressBar mProgressBar;
	private Button mActionButton;

	private ExportTask mExportTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//	Setting the theme
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean theme=preferences.getBoolean("theme", true);
		if(theme){
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
		else{
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
		setContentView(R.layout.export);

		mProgressLabel = (TextView) findViewById(R.id.progress_label);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mActionButton = (Button) findViewById(R.id.action);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mProgressBar.setVisibility(View.GONE);

		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
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
		if (action.equals("bander.notepad.action.ACTION_EXPORT")) {
			showDialog(DIALOG_EXPORT);
		}
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        cancelExport();
    }
    	
	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch(id) {
		    case DIALOG_EXPORT:
		        dialog = new AlertDialog.Builder(this)
				//	.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.dialog_export)
					.setMessage(R.string.export_confirmation)
					.setPositiveButton(getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {					
							// OnClickListener
							public void onClick(DialogInterface dialog, int which) {
								startExport();
							}
						}
					)
					.setNegativeButton(getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {					
							// OnClickListener
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}
					)
					.create();
		        break;
		    default:
		        dialog = null;
		    }
	    return dialog;
	}

	private void startExport() {
		if (mExportTask == null || mExportTask.getStatus() == AsyncTask.Status.FINISHED) {
			mActionButton.setVisibility(View.VISIBLE);
			mActionButton.setText(android.R.string.cancel);
			mActionButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					cancelExport();
				}
			});
			mProgressBar.setVisibility(View.VISIBLE);
			mExportTask = (ExportTask) new ExportTask().execute();
		}
	}
	private void cancelExport() {
		if (mExportTask != null && mExportTask.getStatus() == AsyncTask.Status.RUNNING) {
			mExportTask.cancel(true);
			mExportTask = null;
			mActionButton.setVisibility(View.VISIBLE);
			mActionButton.setText(R.string.dialog_back);
			mActionButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
        }
	}
	
	private class ExportTask extends AsyncTask<Void, Integer, Integer> {
		private final File mDirectory;
		
		ExportTask() {
			File root = Environment.getExternalStorageDirectory();
			mDirectory = new File(root.getAbsolutePath() + "/notepad");
		}

		@Override
		public void onPreExecute() {
			mProgressLabel.setText(R.string.export_progress);
			mProgressBar.setProgress(0);
		}

		public Integer doInBackground(Void... params) {
			int exported = 0;
			try {
				mDirectory.mkdirs();
				Cursor cursor = getContentResolver().query(Note.CONTENT_URI, PROJECTION, null, null, Note.DEFAULT_SORT_ORDER);
				final int count = cursor.getCount();
				
				cursor.moveToFirst();
				
				for (int i = 0; i < count; i++) {
					publishProgress(i, count);
					if (isCancelled()) return null;
				
					cursor.moveToPosition(i);
					String title = cursor.getString(COLUMN_TITLE);
					title = title.replaceAll("[^\\w ]", "");
					File file = new File(
						mDirectory.getAbsolutePath() + File.separator + title + ".txt"
					);
					file.createNewFile();
					
					FileWriter writer = new FileWriter(file);
					writer.write(cursor.getString(COLUMN_BODY));
					writer.flush();
					writer.close();
					
					exported++;
				}
				publishProgress(count, count);
			}  catch (IOException e) {
				return null;
			}
			return exported;
		}

		@Override
		public void onProgressUpdate(Integer... values) {
			final ProgressBar progress = mProgressBar;
			progress.setMax(values[1]);
			progress.setProgress(values[0]);
		}

		@Override
		public void onCancelled() {
			
		}

		@Override
		public void onPostExecute(Integer backupCount) {
			if (backupCount == null) {
				mProgressLabel.setText(R.string.export_failed);
			} else {
				mProgressLabel.setText(
					getResources().getQuantityString(
						R.plurals.export_done, backupCount, 
						new Object[] { backupCount, mDirectory.getAbsolutePath() }
					)
				);
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
