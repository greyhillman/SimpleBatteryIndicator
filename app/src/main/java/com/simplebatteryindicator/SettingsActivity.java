package com.simplebatteryindicator;

import android.app.Activity;
import android.nfc.FormatException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;


public class SettingsActivity extends Activity {

    public void settingsBatterySavingPressed(View view) {
        Button button = (Button)findViewById(R.id.settings_savingButton);

        String text = button.getText().toString();

        if (getResources().getString(R.string.settings_batterySaving_On).equals(text)) {
            Preferences.setBoolean(Preferences.SAVING, true);

            button.setText(           getResources().getString(R.string.settings_batterySaving_Off));
            button.setTextColor(      getResources().getColor(R.color.button_stop_text));
            button.setBackgroundColor(getResources().getColor(R.color.button_stop_background));
        } else {
            Preferences.setBoolean(Preferences.SAVING, false);

            button.setText(           getResources().getString(R.string.settings_batterySaving_On));
            button.setTextColor(      getResources().getColor(R.color.button_start_text));
            button.setBackgroundColor(getResources().getColor(R.color.button_start_background));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Preferences.setUpPreferences(this);

        EditText editText = (EditText)findViewById(R.id.settings_periodEdit);
        editText.setText("" + Preferences.getInt(Preferences.PERIOD, 60));

        Button button = (Button)findViewById(R.id.settings_savingButton);
        if (Preferences.getBoolean(Preferences.SAVING, false)) {
            button.setText(           getResources().getString(R.string.settings_batterySaving_Off));
            button.setTextColor(      getResources().getColor(R.color.button_stop_text));
            button.setBackgroundColor(getResources().getColor(R.color.button_stop_background));
        } else {
            button.setText(           getResources().getString(R.string.settings_batterySaving_On));
            button.setTextColor(      getResources().getColor(R.color.button_start_text));
            button.setBackgroundColor(getResources().getColor(R.color.button_start_background));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onDestroy() {
        EditText editText = (EditText)findViewById(R.id.settings_periodEdit);

        try {
            int seconds = Integer.parseInt(editText.getText().toString());
            Preferences.setInt(Preferences.PERIOD, seconds);
        } catch (Exception e) {

        }
        super.onDestroy();
    }
}
