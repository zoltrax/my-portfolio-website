package gbernat.flashlight.widget;

import java.util.Calendar;
import java.util.List;

import gbernat.flashlight.R;
import gbernat.flashlight.R.drawable;
import gbernat.flashlight.R.id;
import gbernat.flashlight.R.layout;
import gbernat.flashlight.Utils;
import gbernat.flashlight.activities.MorseCode;
import gbernat.flashlight.activities.StrobeLight;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FlashlightWidgetReceiver extends BroadcastReceiver {
	private static boolean isLightOn = false;

	public static String update = "gbernat.flashlight.widget.intent.action.UPDATE";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Utils.isRunning) {
			updateWidgetPictureAndButtonListener3(context);
		} else {
			if (intent.getAction().equals(
					"gbernat.flashlight.widget.intent.action.CHANGE_PICTURE")) {
				updateWidgetPictureAndButtonListener(context);
			}

			if (intent.getAction().equals(
					"gbernat.flashlight.widget.intent.action.UPDATE")) {
				updateWidgetPictureAndButtonListener2(context);
			}
		}

	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay2);
		RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay);

		if (Utils.cam != null) {
			Utils.cam.stopPreview();
			Utils.cam.release();
			Utils.cam = null;
			isLightOn = false;
			remoteViews.setImageViewResource(R.id.imageButton22,
					R.drawable.power_off);
			remoteViews2.setImageViewResource(R.id.imageButton2,
					R.drawable.power_off);

		} else {
			Utils.cam = Camera.open();
			remoteViews.setImageViewResource(R.id.imageButton22,
					R.drawable.power_on);
			remoteViews2.setImageViewResource(R.id.imageButton2,
					R.drawable.power_on);

			if (Utils.cam == null) {
				Toast.makeText(context, "no camera", Toast.LENGTH_SHORT).show();
			} else {
				Parameters param = Utils.cam.getParameters();

				param.setFlashMode(Parameters.FLASH_MODE_TORCH);
				try {
					Utils.cam.setParameters(param);
					Utils.cam.startPreview();
					isLightOn = true;

				} catch (Exception e) {
					Toast.makeText(context, "no flash", Toast.LENGTH_SHORT);
				}
			}
		}

		AppWidgetProvider2.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
		AppWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews2);
	}

	private void updateWidgetPictureAndButtonListener2(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay2);
		RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay);

		if (!FlashlightWidgetReceiver.isLightOn) {

			remoteViews.setImageViewResource(R.id.imageButton22,
					R.drawable.power_off);
			remoteViews2.setImageViewResource(R.id.imageButton2,
					R.drawable.power_off);

		} else {

			remoteViews.setImageViewResource(R.id.imageButton22,
					R.drawable.power_on);
			remoteViews2.setImageViewResource(R.id.imageButton2,
					R.drawable.power_on);
		}

		AppWidgetProvider2.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
		AppWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews2);

	}

	private void updateWidgetPictureAndButtonListener3(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay2);
		RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(),
				R.layout.ofappwidgetlay);
		
		remoteViews.setImageViewResource(R.id.imageButton22,
				android.R.drawable.presence_busy);
		remoteViews2.setImageViewResource(R.id.imageButton2,
				android.R.drawable.presence_busy);

		AppWidgetProvider2.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
		AppWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews2);
	}

	public static void setOnOff(boolean flag) {
		isLightOn = flag;
	}

	public static boolean getOnOff() {
		return isLightOn;
	}

	public static void sendUpdateIntent(Context context) {
		Intent i = new Intent(context, StrobeLight.class);
		i.setAction(StrobeLight.reset);
		context.sendBroadcast(i);
	}

}
