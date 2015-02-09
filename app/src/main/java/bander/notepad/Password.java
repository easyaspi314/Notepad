/*
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
		if (passwordField.getText().toString().equals(passwordString)) {
			Intent i = new Intent(Password.this, Notepad.class);
			i.putExtra("noPassword", true);
			startActivity(i);
			finish();
		} else {
			passwordField.setError("Try again.");
			passwordField.setText("");
		}
	}
}
