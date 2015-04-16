package com.simplebatteryindicator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.SystemClock;

public class NotificationService extends Service {
    public static final int NOTIFICATION_ID = 0;

    public static final String NOTIFICATION_UPDATE        = "com.simpleBatteryIndicator.UPDATE";
    public static final String NOTIFICATION_UPDATE_START  = "com.simpleBatteryIndicator.START";
    public static final String NOTIFICATION_UPDATE_STOP   = "com.simpleBatteryIndicator.STOP";
    public static final String NOTIFICATION_DELETE        = "com.simpleBatteryIndicator.DELETE";

    private PendingIntent       alarmIntent;

    public NotificationService () {
        super();
    }

    /**
     * Builds the notification that is shown on the Notification Bar on the Android System.
     * It has the battery level as the title and "Battery Left" as the subtitle.
     * @return The notification that is displayed.
     */
    private Notification buildNotification () {
        int level = getBatteryLevel();

        return new Notification.Builder( this )
                .setContentTitle( level + "%" )
                .setContentText( getResources().getString( R.string.battery_left ) )
                .setSmallIcon( getBatteryLevelIcon( level ) )
                .setAutoCancel( false ) // When the user goes to close the notification, it
                .setOngoing( true ) // Keep the notification up if you close it
                .build(); // Create the notification
    }

    /**
     * @param level The current battery level
     * @return The icon associated with the level
     */
    private int getBatteryLevelIcon( int level ) {
        // The icons are in order from 0 to 100. Their respective R.drawable value
        // is from that R.drawable.ic_000 to R.drawable.ic_100. The -1 is to correct
        // for the number of icons.
        return R.drawable.ic_000 + ( level - 1 );
    }

    private int getBatteryLevel () {
        IntentFilter intentFilter  = new IntentFilter( Intent.ACTION_BATTERY_CHANGED );
        Intent       batteryStatus = registerReceiver( null, intentFilter );

        int level = batteryStatus.getIntExtra( BatteryManager.EXTRA_LEVEL, -1 );
        int scale = batteryStatus.getIntExtra( BatteryManager.EXTRA_SCALE, -1 );

        return ( int ) ( level * 100 / ( float ) scale );
    }

    /**
     * This is called when the service is first created.
     */
    public void onCreate () {

    }

    /**
     * Whenever startService is called with this service, onStartCommand is always called.
     * @param intent What the service is supposed to do.
     * @param flags Not used at all
     * @param startID Not used at all
     * @return START_STICKY meaning that the service, if killed, will be restarted with a null intent
     */
    public int onStartCommand ( Intent intent, int flags, int startID ) {
        // When the service is restarted, like when the MainActivity is closed, the intent is null.
        // We check if its null because we will access the action of the intent.
        if ( intent == null ) {
            return START_STICKY;
        }
        // What action is the service going to do when the service is called.
        String action = intent.getAction().toString();

        NotificationManager notificationManager = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
        AlarmManager        alarmManager        = ( AlarmManager )        getSystemService( ALARM_SERVICE );

        switch ( action ) {
            case NOTIFICATION_UPDATE_START:
                // We set up the intent so that NOTIFICATION_UPDATE is called every period.
                Intent updateIntent = new Intent( this, NotificationService.class );
                updateIntent.setAction( NOTIFICATION_UPDATE );

                // We get the pendingIntent of the update to the service
                alarmIntent = PendingIntent.getService( this, 0, updateIntent, 0 );

                int updateSeconds = Preferences.getInt(Preferences.PERIOD, Preferences.PERIOD_DEFAULT);

                // Tell the alarmManager that we want to update every period
                alarmManager.setRepeating( AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), updateSeconds * 1000, alarmIntent );

                // fall through to update the notification
            case NOTIFICATION_UPDATE:
                notificationManager.notify(NOTIFICATION_ID, buildNotification());
                break;
            case NOTIFICATION_DELETE:
                notificationManager.cancel( NOTIFICATION_ID );

                // Fall through to stop the repeating update
            case NOTIFICATION_UPDATE_STOP:
                if (Preferences.getBoolean(Preferences.SAVING, Preferences.SAVING_DEFAULT)) {
                    alarmManager.cancel(alarmIntent);
                }
                break;
        }
        return START_STICKY;
    }

    public IBinder onBind ( Intent intent ) {
        return null;
    }

    /**
     * Called when the service is destroyed
     */
    public void onDestroy () {

    }
}
