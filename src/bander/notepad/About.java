package bander.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import com.devin.notepad.R;
import de.psdev.licensesdialog.LicensesDialogFragment;

//import com.devin.notepad.R;

/**
 * Shows the LicencesDialog fragment. It is too hacky. This requires work.
 * Closing the LicenceDialog exits with a blank dialog that won't automatically
 * close. If I set the theme to NoDisplay, it breaks the app. Is there a way to
 * launch this from preferences.xml? I can't get an onClick working in
 * preferences.java. Do you like my awesome justification that I did? ;)
 */

/* It is now an About screen. It's all good... */

public class About extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
	
				.setIcon(android.R.drawable.ic_dialog_info)
				
				.setTitle(R.string.about1)
			
				.setMessage(R.string.about2)

				
				.setPositiveButton(R.string.licences,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Log.i("WHAT?!?!", "You are even looking at the licenses!? Who are you, anyway?!");
								final LicensesDialogFragment fragment = LicensesDialogFragment
										.newInstance(R.raw.notices, false);
								fragment.show(getActivity().getSupportFragmentManager(), null);
							}
						})

				
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
	}
}
