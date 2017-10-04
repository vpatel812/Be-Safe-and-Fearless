package com.bSecure;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetActivity extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetId) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		remoteViews.setOnClickPendingIntent(R.id.greenbutton, buildButtonPendingIntentg(context));
		remoteViews.setOnClickPendingIntent(R.id.yellowbutton, buildButtonPendingIntenty(context));
		remoteViews.setOnClickPendingIntent(R.id.redbutton, buildButtonPendingIntentr(context));
		pushWidgetUpdate(context, remoteViews);
	}

	public static PendingIntent buildButtonPendingIntentg(Context context) {
		Intent intent = new Intent();
		intent.setAction("CHANGE_PICTUREG");
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent buildButtonPendingIntenty(Context context) {
		Intent intent = new Intent();
		intent.setAction("CHANGE_PICTUREY");
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent buildButtonPendingIntentr(Context context) {
		Intent intent = new Intent();
		intent.setAction("CHANGE_PICTURER");
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,
				WidgetActivity.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}