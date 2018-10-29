package com.backcountrydesigngroup.android.pandasleuth;
/**     Copyright (C) 2016 The Android Open Source Project

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **/

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.util.prefs.Preferences;

public class SettingsActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.settings_activity);
        }

        public static class PandaPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.settings_main);

                Preference topic = findPreference(getString(R.string.settings_topic_key));
                Preference section = findPreference(getString(R.string.settings_section_key));
                Preference sortBy = findPreference(getString(R.string.settings_order_by_key));
                bindPreferenceSummaryToValue(topic);
                bindPreferenceSummaryToValue(section);
                bindPreferenceSummaryToValue(sortBy);
            }

            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                // The code in this method takes care of updating the displayed preference summary after it has been changed
                String stringValue = value.toString();
                preference.setSummary(stringValue);
                return true;
            }

            private void bindPreferenceSummaryToValue(Preference preference) {
                preference.setOnPreferenceChangeListener(this);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                String preferenceString = preferences.getString(preference.getKey(), "");
                onPreferenceChange(preference, preferenceString);
            }
        }
    }
