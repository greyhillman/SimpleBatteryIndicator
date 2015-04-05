package com.simplebatteryindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *  This is a receiver for whether the the screen has turned on or off. The receiver is kept throughout
 *  the lifecycle of the service. It is only stopped when unregisterReceiver is called on this.
 */
public class ScreenReceiver extends BroadcastReceiver {

    public ScreenReceiver() {
        super();
    }

    /**
     *  When the Android System detects that the screen has turned on or off, this method is called
     *  with the intent of the screen turning off or on.
     * @param context Passed in by the Android System. Not used.
     * @param intent Passed in by the Android System. What the screen just did, turn on or off.
     */
    public void onReceive (Context context, Intent intent) {
        // What the screen just did
        String action = intent.getAction();

        // Set up the intent for the Notification Service
        Intent notificationIntent = new Intent(context, NotificationService.class);

        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            notificationIntent.setAction(NotificationService.NOTIFICATION_UPDATE_STOP);

        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            notificationIntent.setAction(NotificationService.NOTIFICATION_UPDATE_START);
        }

        // "Send" a message to the service to either start or stop the updating
        context.startService(notificationIntent);
    }
}
