package bander.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devin.notepad.R;

/** A test. This is to set up a password. It does not work yet. **/
public class PasswordSet extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		Startup.setThemeFromPreferences(this, "Password");
		setContentView(R.layout.password);
		Button disable = (Button) findViewById(R.id.disablePassword);
		disable.setVisibility(Button.VISIBLE);
		if (!settings.getString("password", "").equals("")
				&& settings.getBoolean("passEnabled", false)) {
			disable.setEnabled(true);

		}
	}

	public void login(View v) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		EditText passwordField = (EditText) findViewById(R.id.password);

		if (passwordField.getText().toString().equals("")) {
			passwordField.setError("This field cannot be empty.");
		} else {

			Editor editor = settings.edit();
			editor.putString("password", passwordField.getText().toString());
			editor.putBoolean("passEnabled", true);
			editor.commit();

			finish();
		}
	}

	public void disablePassword(View v) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = settings.edit();
		editor.putString("password", "");
		editor.putBoolean("passEnabled", false);
		editor.commit();
		finish();
	}

	private static class MyAlertDialogFragment extends DialogFragment {

		public static MyAlertDialogFragment newInstance(int title) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putInt("title", title);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int title = getArguments().getInt("title");

			return new AlertDialog.Builder(getActivity())
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(title)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									getActivity().finish();
								}
							})
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							}).create();
		}
	}
}
