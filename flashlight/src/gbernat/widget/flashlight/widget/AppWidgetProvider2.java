package gbernat.widget.flashlight.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gbernat.flashlight.R;
import gbernat.flashlight.Utils;
import gbernat.flashlight.R.id;
import gbernat.flashlight.R.layout;
import gbernat.flashlight.activities.Main;
import gbernat.widget.flashlight.widget.AppWidgetProvider2;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AppWidgetProvider2 extends android.appwidget.AppWidgetProvider {

	public static String CLOCK_WIDGET_UPDATE = "gbernat.flashlight.8BITCLOCK_WIDGET_UPDATE";
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			Intent receiver = new Intent(context,
					FlashlightWidgetReceiver.class);

			receiver.setAction("gbernat.widget.flashlight.widget.intent.action.CHANGE_PICTURE");

			receiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, receiver, 0);

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.ofappwidgetlay2);

			views.setOnClickPendingIntent(R.id.imageButton22, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(), getClass().getName());
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		
		Intent receiver = new Intent(context,
				FlashlightWidgetReceiver.class);
		
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay2);
		receiver.setAction("gbernat.widget.flashlight.widget.intent.action.CHANGE_PICTURE");

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				0, receiver, 0);
		views.setOnClickPendingIntent(R.id.imageButton22, pendingIntent);
		
		int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
		for (int appWidgetID : ids) {
			appWidgetManager.updateAppWidget(appWidgetID, views);
		}
		
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {

		Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay2);
		
		Intent receiver = new Intent(context,
				FlashlightWidgetReceiver.class);

		receiver.setAction("gbernat.widget.flashlight.widget.intent.action.CHANGE_PICTURE");

		receiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				0, receiver, 0);


		views.setOnClickPendingIntent(R.id.imageButton22, pendingIntent);

		int minWidth = options
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
		int minHeight = options
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);


		appWidgetManager.updateAppWidget(appWidgetId, views);

		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {

		// TODO Auto-generated method stub
		ComponentName myWidget = new ComponentName(context,
				AppWidgetProvider2.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}

	private PendingIntent createClockTickIntent(Context context) {
		Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		
		

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 2);
		//alarmManager.setRepeating(type, triggerAtMillis, intervalMillis, operation);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
				5000, createClockTickIntent(context));
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(createClockTickIntent(context));
	}

}
