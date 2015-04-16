package com.simplebatteryindicator;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 *      An activity is a process that has a UI. When the user starts the application, onCreate is first
 *  called. onCreate sets up the UI and anything else that needs to be done before the application
 *  is running. We set up the UI, ScreenReceiver, and Preferences.
 *      After onCreate is done, onCreateOptionsMenu is called as well. This sets up the options menu
 *  that is in the top right corner of the application. Right now, all we have is the settings button.
 *
 *  Once all this setup is done, the application is finally running.
 *
 *  When the user presses the main button, buttonPressed is called. This handles create the NotificationService
 *  and updating the UI is match the current state of the program.
 *
 *  When the user presses one of the options menu buttons, onOptionsItemSelected is called. We only
 *  have Settings right now, so the SettingsActivity is called.
 */
public class MainActivity extends Activity {

    /**
     * Sets the button to match the parameters.
     * @param button The button we want to change
     * @param textID The R.id that contains the text we want to display
     * @param textColorID The R.color that the text is displayed in
     * @param backgroundColorID The R.color that the background of the button is displayed in
     */
    private void setButton(Button button, int textID, int textColorID, int backgroundColorID) {
        button.setText(           getResources().getString(textID));
        button.setTextColor(      getResources().getColor( textColorID));
        button.setBackgroundColor(getResources().getColor( backgroundColorID));
    }

    private void changeButtonToStop(Button button) {
        setButton(button, R.string.button_stop, R.color.button_stop_text, R.color.button_stop_background);
    }

    private void changeButtonToStart(Button button) {
        setButton(button, R.string.button_start, R.color.button_start_text, R.color.button_start_background);
    }

    private void startNotificationService() {
        // The intent to start the NotificationService
        Intent startIntent = new Intent(this, NotificationService.class);
        // The service is to start the update process
        startIntent.setAction(NotificationService.NOTIFICATION_UPDATE_START);

        startService(startIntent);
    }

    private void stopNotificationService() {
        Intent stopIntent = new Intent(this, NotificationService.class); // The intent to stop the NotificationService
        stopIntent.setAction(NotificationService.NOTIFICATION_DELETE); // Tell the service to delete the notification

        startService(stopIntent); // This is to send the message to delete the notification
        stopService(stopIntent); // Now stop the service
    }

    /**
     * When the user presses the main button, this is called.
     * @param view The view for the main button.
     */
    public void buttonPressed(View view) {
        // We will update the button to indicate whether the service is running or not and
        // start or stop the indicator service.
        Button button = (Button)view;

        // The text that is displayed in the button is used to determine whether or not to stop
        // or start the service.
        String text = button.getText().toString();

        // If the button says "Start", we start the service
        if (text.equals(getResources().getString(R.string.button_start))) {
            startNotificationService();

            // Update the UI to show the change
            changeButtonToStop(button);
        // The button says "Stop", so stop the service
        } else {
            stopNotificationService();

            // Update the UI
            changeButtonToStart(button);
        }
    }

    /**
     * Determines if the service with the given classServiceName is running.
     * @param classServiceName
     * @return Is the service running
     */
    private boolean isServiceRunning(String classServiceName) {
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(classServiceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called when the activity is being created. This is used to set up the UI and any other
     * things.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set the view as defined in the layout
        setContentView(R.layout.activity_main);

        // Create the receiver to tell us if the screen has turned off or on
        ScreenReceiver screenReceiver = new ScreenReceiver();
        // Tell the Android System that it want to check for when the screen turns off or on
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");

        // Tell the Android to register this new receiver
        registerReceiver(screenReceiver, filter);

        // Get the button
        Button button = (Button)findViewById(R.id.button);

        // Check to see if the NotificationService is already running
        // and change the button to indicate that
        if (isServiceRunning(NotificationService.class.getName())) {
            changeButtonToStop(button);
        } else {
            changeButtonToStart(button);
        }

        // Set up the preferences
        Preferences.setUpPreferences(this);
    }

    /**
     * Called when the options menu is being created.
     * @param menu The current menu
     * @return If it worked or some shit
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    /**
     * When user presses one of the options in the activity menu. It passes in the MenuItem that was
     * pressed
     * @param item The option that was pressed
     * @return No idea what this does right now.
     */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // If the user pressed the settings button
        if ( id == R.id.action_settings ) {
            // Set up the intent to start the the settings activity
            Intent intent = new Intent(this, SettingsActivity.class);

            // Start the settings activity
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
