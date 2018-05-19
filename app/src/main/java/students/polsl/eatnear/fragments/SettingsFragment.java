package students.polsl.eatnear.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import students.polsl.eatnear.R;

public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int preferencesCount = preferenceScreen.getPreferenceCount();
        for(int i=0; i<preferencesCount; i++){
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)){
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), "summary"));
            }

        }
    }

    private void setPreferenceSummary(Preference preference, String value){
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if(index == -1 )
                preference.setSummary("summary_sample");
            else
                preference.setSummary(listPreference.getEntries()[index]);
        }
        else if (preference instanceof EditTextPreference){
            preference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference != null){
            if (!(preference instanceof CheckBoxPreference)){
                setPreferenceSummary(preference, sharedPreferences.getString(key, "summary_sample"));
            }
        }
    }
}