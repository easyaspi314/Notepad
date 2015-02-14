/*
 * Copyright (C) 2015 Devin Hussey
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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.devin.notepad.R;

public class PasswordFragment extends Fragment {

    private String firstPin = null;
    private EditText passField;
    private boolean isSetup;
    private String theRightThing;

    public static PasswordFragment newInstance(boolean isSetup) {
        PasswordFragment f = new PasswordFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("isSetup", isSetup);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        isSetup = getArguments().getBoolean("isSetup");
        View v = inflater.inflate(R.layout.password, container, false);
        passField = (EditText) v.findViewById(R.id.password);
        final Button login = (Button) v.findViewById(R.id.passwordSave);
        Button disable = (Button) v.findViewById(R.id.disablePassword);
        if (!isSetup) {
            login.setText(android.R.string.ok);
            disable.setVisibility(View.GONE);
        } else {
            login.setText("Next");
            disable.setVisibility(View.GONE);
        }

        login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pin = passField.getText().toString();
                theRightThing = PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString("password", "");
                // Password field is empty, can't have that, can we?
                if (TextUtils.isEmpty(pin)) {
                    passField.setError("This field can't be empty.");
                    // First pin set. Time to verify
                } else if (firstPin == null && isSetup) {
                    firstPin = pin;
                    reset(true);
                    login.setText("Save");
                    // This is for the password protection login
                } else if (pin.equals(theRightThing) && !isSetup) {
                    getActivity().finish();
                    // Saved.
                } else if (pin.equals(firstPin) && isSetup) {
                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(getActivity())
                            .edit();

                    editor.putBoolean("passEnabled", true);
                    editor.putString("password", pin);
                    editor.apply();
                    getActivity().finish();
                    // Password is wrong.
                } else {
                    reset(false);
                }
            }
        });
        return v;
    }

    public void reset(boolean isItRight) {
        if (!isItRight) {
            passField.setError("Password is incorrect. Try again.");
        }
        passField.setText("");
    }
}
