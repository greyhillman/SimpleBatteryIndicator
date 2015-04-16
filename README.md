SimpleBatteryIndicator
======================


	This is an app for Android 5.1 that displays a notification about the percent battery left on the phone.

	Here is some high level understanding of what main certain classes do.

Activity
--------


	An Activity is a GUI Application. This is what you interact with when using an application like Chrome, Downloads, Drive, etc.. The functions that are called when you click on a button, for example, are defined in a class that extends Activity (in our case this is MainActivity). This class handles all the details about running the current GUI application.


	When the Activity is first called when pressing the application icon or some other way, onCreate is called. This is where you set up your GUI application to be used.

Intent
------


	An Intent is used like how you think it does. I have an intent to start this new activity, or I have an intent to open this link using a browser.

	Example:
		Intent intent = new Intent(this, SettingsActivity.class);

	This intent is used to start the SettingsActivity. The intent includes who wants this intent and who should receive the intent namely, this and SettingsActivity. The second paremeter is sort of a unique identifier for the class.

Receiver
--------

	This is a class that receives certain messages from the Android Sytem like when the Screen turns on or off. When this happens, a message is sent to the Receiver with the message. It is then up to you to determine what you should do with that message.
	In the case of this program, we will receiver the Screen on or off messages via the ScreenReceiver class which extends Receiver.


