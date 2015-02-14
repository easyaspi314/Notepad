/*
 * Copyright 2014 Peter Heisig
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.psdev.licensesdialog.licenses;

import android.content.Context;
import com.devin.notepad.R;

/**
 * For MaterialDialog and MaterialPreferences, licenced under the LGPLv3.
 */
public class GnuLesserGeneralPublicLicense30 extends License {

    public GnuLesserGeneralPublicLicense30() {}

    @Override
    public String getName() {
        return "GNU Lesser General Public License 3.0";
    }

    @Override
    public String getSummaryText(final Context context) {
        return getContent(context, R.raw.lgpl_30_summary);
    }

    @Override
    public String getFullText(final Context context) {
        return getContent(context, R.raw.lgpl_30_full);
    }

    @Override
    public String getVersion() {
        return "3.0";
    }

    @Override
    public String getUrl() {
        return "https://www.gnu.org/licenses/lgpl.html";
    }

}