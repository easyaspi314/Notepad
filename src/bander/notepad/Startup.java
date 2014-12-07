package bander.notepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.devin.notepad.R;

//import com.devin.notepad.R;

public class Startup extends Activity {

	/*
	 * Tiny NoDisplay bridge activity which fixes the theme switch eyesore and
	 * allows the NoteList to restart after Settings, as to not conflict with
	 * themes.
	 * 
	 * NoteList starts Preferences(Dark).class finishes, and on Settings, onBack
	 * pressed, launches this to restart the activity.
	 * 
	 * Ya I can't really comment this right...
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences mSettings = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean passEnabled = mSettings.getBoolean("passEnabled", true);
		String password = mSettings.getString("password", "");
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			if (passEnabled && !password.equals("")) {
				Intent intent = new Intent(this, Password.class);
				startActivity(intent);
				finish();
			}
		}

		// PreferenceManager.setDefaultValues(this, R.xml.preferences,
		// false);
		Log.i("Hello!",
				"Welcome to Notepad! I see you are pulling a logcat on me... ");

		String theme = mSettings.getString("themeType", "0");
		if (!theme.equals("0")) {
			Intent intent = new Intent(this, NoteListLight.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(this, NoteListAppCompat.class);
			startActivity(intent);
			finish();
		}

	}

	// Setting the themes. Take 2.
	public static void setThemeFromPreferences(Context context, String classType) {
		SharedPreferences mSettings = PreferenceManager
				.getDefaultSharedPreferences(context);
		String color = mSettings.getString("colorScheme", "Dark");
		String theme = mSettings.getString("themeType", "0");

		// Lollipop theme
		if (theme.equals("0")) {

		}
		// KitKat-styled theme
		if (theme.equals("1")) {
			if (color.equals("Light")) {
				Log.v("Info", "Setting theme to KitKat Light");
				context.setTheme(R.style.Theme_KitKat_Light);

			} else if (color.equals("Mixed")) {
				Log.v("Info", "Setting theme to KitKat Light/Dark theme");
				context.setTheme(R.style.Theme_KitKat_Light_DarkActionBar);

			} else {
				Log.v("Info", "Setting theme to KitKat Dark");
				context.setTheme(R.style.Theme_KitKat_Dark);
			}

		}
		// Default Holo theme
		else if (theme.equals("2")) {
			if (color.equals("Light")) {
				Log.v("Info", "Setting theme to Holo Light");
				context.setTheme(android.R.style.Theme_Holo_Light);
			} else if (color.equals("Mixed")) {
				Log.v("Info", "Setting theme to Holo Light/Dark theme");
				context.setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
			} else {
				Log.v("Info", "Setting theme to Holo Dark");
				context.setTheme(android.R.style.Theme_Holo);
			}
		}
		// Device Default
		else if (theme.equals("3")) {
			if (color.equals("Light")) {
				Log.v("Info", "Setting theme to Device Default Light theme");
				context.setTheme(android.R.style.Theme_DeviceDefault_Light);
			} else if (color.equals("Mixed")) {
				Log.v("Info", "Setting theme to Device Default Light/Dark");
				context.setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
			} else {
				Log.v("Info", "Setting theme to Device Default theme");
				context.setTheme(android.R.style.Theme_DeviceDefault);
			}
		}
		// Gingerbread
		else if (theme.equals("4")) {
			if (color.equals("Light")) {
				context.setTheme(android.R.style.Theme_Light);
			} else if (color.equals("Mixed")) {
				context.setTheme(R.style.Theme_WithActionBar);
			} else {
				context.setTheme(R.style.Theme);
			}
		}
		// fallback for error.
		else {
			if (color.equals("Light")) {
				context.setTheme(R.style.Theme_KitKat_Light);
			} else if (color.equals("Mixed")) {
				context.setTheme(R.style.Theme_KitKat_Light_DarkActionBar);
			} else {
				context.setTheme(R.style.Theme_KitKat_Dark);
			}
		}
	}

	// Dialog Themes
	public static void setDialogThemeFromPreferences(Context context) {
		SharedPreferences mSettings = PreferenceManager
				.getDefaultSharedPreferences(context);
		String color = mSettings.getString("colorScheme", "Dark");
		String theme = mSettings.getString("themeType", "0");

		// Holo theme
		if (theme.equals("1") | theme.equals("2")) {
			if (color.equals("Light") | color.equals("Mixed")) {
				context.setTheme(android.R.style.Theme_Holo_Light_Dialog);
			} else {
				context.setTheme(android.R.style.Theme_Holo_Dialog);
			}

		}
		// Device Default
		else if (theme.equals("3")) {
			if (color.equals("Light") | color.equals("Mixed")) {
				context.setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
			} else {
				context.setTheme(android.R.style.Theme_DeviceDefault_Dialog);
			}
		}
		// Gingerbread
		else if (theme.equals("4")) {
			context.setTheme(android.R.style.Theme_Dialog);

		}
		// fallback for error.
		else {
			if (color.equals("Light") | color.equals("Mixed")) {
				context.setTheme(android.R.style.Theme_Holo_Light_Dialog);
			} else {
				context.setTheme(android.R.style.Theme_Holo_Dialog);
			}
		}
	}

	public static void setAppCompatThemeFromPreferences(Context context,
			String classType) {
		SharedPreferences mSettings = PreferenceManager
				.getDefaultSharedPreferences(context);
		String theme = mSettings.getString("themeType", "0");
		if (theme.equals("0")) {
			context.setTheme(R.style.Theme_Material);
		} else {
			// This is the wrong class. I hope that this works.
			if (classType.equals("Prefs")) {
				Intent intent = new Intent();
				intent.setClass(context, SetPreferenceActivity.class);
				context.startActivity(intent);
			} else if (classType.equals("Edit")) {
				Intent intent = new Intent();
				intent.setClass(context, NoteEdit.class);
				context.startActivity(intent);
			} else if (classType.equals("NoteList")) {
				Intent intent = new Intent();
				intent.setClass(context, NoteListLight.class);
				context.startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(context, Startup.class);
				context.startActivity(intent);
			}
		}
	}
}
