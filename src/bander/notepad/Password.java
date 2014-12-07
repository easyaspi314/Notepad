package bander.notepad;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.devin.notepad.R;

public class Password extends Activity {

	TextView password;
	EditText passwordField;
	TextView wrong;
	String passwordString;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.password);
		password = (TextView) findViewById(R.id.enterpassword);
		wrong = (TextView) findViewById(R.id.password_wrong);
		passwordField = (EditText) findViewById(R.id.password);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		passwordString = preferences.getString("password", "");
	}

	@Override
	public void onBackPressed() {
		// Can't back out of this one!
	}

	public void login(View view) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		passwordField = (EditText) findViewById(R.id.password);
		passwordString = preferences.getString("password", "");
		wrong = (TextView) findViewById(R.id.password_wrong);
		if (passwordField.getText().toString() == passwordString
				|| passwordField.getText().toString().equals(passwordString)) {
			Intent i = new Intent(Password.this, Startup.class);
			i.putExtra("noPassword", true);
			startActivity(i);
			finish();
		} else {
			passwordField.setError("Try again.");
			passwordField.setText("");
		}
	}
}
