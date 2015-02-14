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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.devin.notepad.R;

import de.psdev.licensesdialog.LicenseResolver;
import de.psdev.licensesdialog.LicensesDialogFragment;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense30;

/**
 * DialogFragment to show info about the app.
 */

public class About extends DialogFragment {

    public static About newInstance() {
        return new About();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())

                .setIcon(android.R.drawable.ic_dialog_info)

                .setTitle(R.string.about1)

                .setMessage(R.string.about2)

                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (event.getAction() == 1) {
                            if (keyCode == 24) {
                                Toast.makeText(getActivity(), "\u0057\u006f\u0077\u002c \u0079\u006f" +
                                        "\u0075\u0027\u0072\u0065 \u0067\u006f\u006f\u0064\u0021",
                                        Toast.LENGTH_LONG).show();
                                return true;
                            }
                        }
                        return false;
                    }
                })

                .setPositiveButton(R.string.licences,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // LGPLv3 is not included in the library, so we have to register it.
                                LicenseResolver.registerLicense(new GnuLesserGeneralPublicLicense30());
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
