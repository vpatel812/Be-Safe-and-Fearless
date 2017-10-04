package com.bSecure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetReceiver extends BroadcastReceiver {

	Context con;
	SharedPreferences mode;
	SharedPreferences mmode;
	SharedPreferences gmode;
	SharedPreferences ymode;
	SharedPreferences rmode;
	public static final String PREFS_NAME = "MyPrefs";
	public static final String PREFS_NAMEM = "Mmode";
	public static final String PREFS_NAMEG = "MyPrefsGFile";
	public static final String PREFS_NAMEY = "MyPrefsYFile";
	public static final String PREFS_NAMER = "MyPrefsRFile";
	SharedPreferences.Editor meditor;
	SharedPreferences.Editor editor;

	@Override
	public void onReceive(Context context, Intent intent) {
		mode = context.getSharedPreferences(PREFS_NAME, 0);
		gmode = context.getSharedPreferences(PREFS_NAMEG, 0);
		ymode = context.getSharedPreferences(PREFS_NAMEY, 0);
		rmode = context.getSharedPreferences(PREFS_NAMER, 0);
		mmode = context.getSharedPreferences(PREFS_NAMEM, 0);
		editor = mode.edit();

		meditor = mmode.edit();

		if (intent.getAction().equals("CHANGE_PICTUREG")) {

			if (mode.getBoolean("yellowimage", false)) {
				updateWidgetPictureAndButtonListenery(context);

			}
			if (mode.getBoolean("redimage", false)) {
				updateWidgetPictureAndButtonListenerr(context);
			}
			updateWidgetPictureAndButtonListenerg(context);
		}
		if (intent.getAction().equals("CHANGE_PICTUREY")) {
			if (mode.getBoolean("greenimage", false)) {
				updateWidgetPictureAndButtonListenerg(context);
			}
			if (mode.getBoolean("redimage", false)) {
				updateWidgetPictureAndButtonListenerr(context);
			}
			updateWidgetPictureAndButtonListenery(context);
		}
		if (intent.getAction().equals("CHANGE_PICTURER")) {
			if (mode.getBoolean("greenimage", false)) {
				updateWidgetPictureAndButtonListenerg(context);
			}
			if (mode.getBoolean("yellowimage", false)) {
				updateWidgetPictureAndButtonListenery(context);
			}
			updateWidgetPictureAndButtonListenerr(context);
		}
		editor.commit();
	}

	private void updateWidgetPictureAndButtonListenerg(Context context) {
		con = context;
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		remoteViews.setImageViewResource(R.id.greenbutton, getImageToSetg());
		remoteViews.setOnClickPendingIntent(R.id.greenbutton,
				WidgetActivity.buildButtonPendingIntentg(context));
		WidgetActivity.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);

	}

	private void updateWidgetPictureAndButtonListenery(Context context) {
		con = context;
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		remoteViews.setImageViewResource(R.id.yellowbutton, getImageToSety());

		remoteViews.setOnClickPendingIntent(R.id.yellowbutton,
				WidgetActivity.buildButtonPendingIntenty(context));
		WidgetActivity.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
	}

	private void updateWidgetPictureAndButtonListenerr(Context context) {
		con = context;
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		remoteViews.setImageViewResource(R.id.redbutton, getImageToSetr());

		remoteViews.setOnClickPendingIntent(R.id.redbutton,
				WidgetActivity.buildButtonPendingIntentr(context));
		WidgetActivity.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
	}

	private int getImageToSetg() {

		if (mode.getBoolean("greenimage", false)) {

			Toast.makeText(con, "Green Mode OFF...", Toast.LENGTH_LONG).show();
			AlarmScheduler.cancelAlarm(con);

			editor.putBoolean("greenimage", false);
			editor.commit();

			return R.drawable.greenwidgeticon;

		} else {

			Toast.makeText(con, "Green Mode ON...", Toast.LENGTH_LONG).show();

			meditor.putString("mode", "green");
			meditor.commit();

			AlarmScheduler.scheduleAlarms(con, "green");

			editor.putBoolean("greenimage", true);

			editor.commit();

			return R.drawable.pausegreen;
		}
	}

	private int getImageToSety() {
		if (mode.getBoolean("yellowimage", false)) {

			Toast.makeText(con, "Yellow Mode OFF...", Toast.LENGTH_LONG).show();
			AlarmScheduler.cancelAlarm(con);
			Log.v("ymm","fail");
			editor.putBoolean("yellowimage", false);
			editor.commit();

			return R.drawable.yellowwidgeticon;

		} else {

			Toast.makeText(con, "Yellow Mode ON...", Toast.LENGTH_LONG).show();

			meditor.putString("mode", "yellow");
			meditor.commit();

			AlarmScheduler.scheduleAlarms(con, "yellow");

			editor.putBoolean("yellowimage", true);
			editor.commit();

			return R.drawable.pauseyellow;
		}

	}

	private int getImageToSetr() {
		if (mode.getBoolean("redimage", false)) {

			Toast.makeText(con, "Red Mode OFF...", Toast.LENGTH_LONG).show();
			AlarmScheduler.cancelRAlarm(con);

			editor.putBoolean("redimage", false);
			editor.commit();

			return R.drawable.rediwidgetcon;

		} else {

			Toast.makeText(con, "Red Mode ON...", Toast.LENGTH_LONG).show();

			meditor.putString("mode", "red");
			meditor.commit();

			AlarmScheduler.scheduleAlarms(con, "red");

			editor.putBoolean("redimage", true);
			editor.commit();

			return R.drawable.pause;
		}
	}
}