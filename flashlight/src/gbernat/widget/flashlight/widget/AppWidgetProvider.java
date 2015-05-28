package gbernat.widget.flashlight.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gbernat.flashlight.R;
import gbernat.flashlight.Utils;
import gbernat.flashlight.R.id;
import gbernat.flashlight.R.layout;
import gbernat.flashlight.activities.Main;
import gbernat.flashlight.activities.MorseCode;
import gbernat.flashlight.activities.ScreenFlashlight;
import gbernat.flashlight.activities.StrobeLight;
import gbernat.widget.flashlight.widget.AppWidgetProvider;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

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

			Intent intent2 = new Intent(context, Main.class);

			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			PendingIntent pendingIntent2 = PendingIntent.getActivity(context,
					0, intent2, 0);

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.ofappwidgetlay);
			views.setOnClickPendingIntent(R.id.imageButton2, pendingIntent);
			views.setOnClickPendingIntent(R.id.buttonGoToApp, pendingIntent2);

			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {

			}

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	
		if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {

			ComponentName thisAppWidget = new ComponentName(
					context.getPackageName(), getClass().getName());
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			Intent intent2;
			if (Utils.pausedActiv == null) {
				intent2 = new Intent(context, Main.class);

			} else if (Utils.pausedActiv == 1) {

				intent2 = new Intent(context, ScreenFlashlight.class);
			} else if (Utils.pausedActiv == 2) {

				intent2 = new Intent(context, StrobeLight.class);
			} else if (Utils.pausedActiv == 3) {

				intent2 = new Intent(context, MorseCode.class);
			} else {

				intent2 = new Intent(context, Main.class);
			}

			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			PendingIntent pendingIntent2 = PendingIntent.getActivity(context,
					0, intent2, 0);
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.ofappwidgetlay);
			views.setOnClickPendingIntent(R.id.buttonGoToApp, pendingIntent2);
			
			Intent receiver = new Intent(context,
					FlashlightWidgetReceiver.class);

			receiver.setAction("gbernat.widget.flashlight.widget.intent.action.CHANGE_PICTURE");

			//receiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, receiver, 0);
			views.setOnClickPendingIntent(R.id.imageButton2, pendingIntent);

			int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
			for (int appWidgetID : ids) {
				updateAppWidget(context, appWidgetManager, appWidgetID, intent);
				appWidgetManager.updateAppWidget(appWidgetID, views);
			}
		}
	}

	public static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId, Intent intent) {
		String currentTime = df.format(new Date());
		Intent batteryIntent = context.getApplicationContext()
				.registerReceiver(null,
						new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay);
		updateViews.setTextViewText(R.id.textViewBatterySt, "" + level + " %");

		appWidgetManager.updateAppWidget(appWidgetId, updateViews);
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {

		Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay);

		Intent receiver = new Intent(context, FlashlightWidgetReceiver.class);

		receiver.setAction("gbernat.widget.flashlight.widget.intent.action.CHANGE_PICTURE");

		receiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				receiver, 0);

		Intent intent2 = new Intent(context, Main.class);
		PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0,
				intent2, 0);

		views.setOnClickPendingIntent(R.id.imageButton2, pendingIntent);
		views.setOnClickPendingIntent(R.id.buttonGoToApp, pendingIntent2);

		int minWidth = options
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
		int minHeight = options
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

		views.setViewVisibility(R.id.layoutOnOff, View.VISIBLE);

		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}

	private RemoteViews getRemoteViews(Context context, int minWidth,
			int minHeight) {

		int rows = getCellsForSize(minHeight);
		int columns = 2;

		return new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay);

	}

	private static int getCellsForSize(int size) {
		int n = 2;
		while (70 * n - 30 < size) {
			++n;
		}
		return n - 1;
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		// TODO Auto-generated method stub
		ComponentName myWidget = new ComponentName(context,
				AppWidgetProvider.class);
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

	public static PendingIntent buildButtonPendingIntent(Context context) {
		Intent intent = new Intent();

		intent.setAction("gbernat.widget.flashlight.intent.action.CHANGE_PICTURE");
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent buildButtonPendingIntent2(Context context) {
		Intent intent = new Intent(context, Main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		return pendingIntent;
	}
}
