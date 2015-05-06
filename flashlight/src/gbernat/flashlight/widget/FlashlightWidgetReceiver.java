package gbernat.flashlight.widget;

import java.util.Calendar;

import gbernat.flashlight.R;
import gbernat.flashlight.R.drawable;
import gbernat.flashlight.R.id;
import gbernat.flashlight.R.layout;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FlashlightWidgetReceiver extends BroadcastReceiver {
    private static boolean isLightOn = false;
    private static Camera camera;
    

    @Override
    public void onReceive(Context context, Intent intent) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay);
            RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay2);
    		views2.setOnClickPendingIntent(R.id.imageButton22, AppWidgetProvider2.buildButtonPendingIntent(context));
    		views.setOnClickPendingIntent(R.id.imageButton2, AppWidgetProvider.buildButtonPendingIntent(context));
    		views.setOnClickPendingIntent(R.id.buttonGoToApp, AppWidgetProvider.buildButtonPendingIntent2(context));
            if(intent.getAction().equals("gbernat.flashlight.intent.action.CHANGE_PICTURE")){
    			updateWidgetPictureAndButtonListener(context);
    		}

            if(isLightOn) {
                    views.setImageViewResource(R.id.imageButton2, R.drawable.power_off);
                    views2.setImageViewResource(R.id.imageButton22, R.drawable.power_off);
            } else {
                    views.setImageViewResource(R.id.imageButton2, R.drawable.power_on);
                    views2.setImageViewResource(R.id.imageButton22, R.drawable.power_on);
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(new ComponentName(context,     AppWidgetProvider.class),
                                                                             views);
            
            appWidgetManager.updateAppWidget(new ComponentName(context,     AppWidgetProvider2.class),
                    views2);
            

            if (isLightOn) {
                    if (camera != null) {
                            camera.stopPreview();
                            camera.release();
                            camera = null;
                            isLightOn = false;
                    }

            } else {
                    // Open the default i.e. the first rear facing camera.
                    camera = Camera.open();

                    
                    if(camera == null) {
                            Toast.makeText(context, "no camera", Toast.LENGTH_SHORT).show();
                    } else {
                            // Set the torch flash mode
                            Parameters param = camera.getParameters();
                            
                            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            try {
                                    camera.setParameters(param);
                                    camera.startPreview();
                                    isLightOn = true;
                            } catch (Exception e) {
                                    Toast.makeText(context, "no flash", Toast.LENGTH_SHORT).show();
                            }
                    }
            }
    }
    
    private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay);
		RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay2);
		//remoteViews.setViewVisibility(R.id.layoutOnOff, View.GONE);
		//remoteViews2.setO
		remoteViews2.setOnClickPendingIntent(R.id.imageButton22, AppWidgetProvider2.buildButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.imageButton2, AppWidgetProvider.buildButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.buttonGoToApp, AppWidgetProvider.buildButtonPendingIntent(context));

		AppWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
		AppWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews2);
	}
    
 
	
    
}
