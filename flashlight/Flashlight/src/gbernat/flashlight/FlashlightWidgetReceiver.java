package gbernat.flashlight;

import java.util.Calendar;

import gbernat.flashlight.AppWidgetProvider;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FlashlightWidgetReceiver extends BroadcastReceiver {
    private static boolean isLightOn = false;
    private static Camera camera;
    

    @Override
    public void onReceive(Context context, Intent intent) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay);
            
            if(intent.getAction().equals("gbernat.flashlight.intent.action.CHANGE_PICTURE")){
    			updateWidgetPictureAndButtonListener(context);
    		}

            if(isLightOn) {
                    views.setImageViewResource(R.id.imageButton2, R.drawable.power_off);
            } else {
                    views.setImageViewResource(R.id.imageButton2, R.drawable.power_on);
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(new ComponentName(context,     AppWidgetProvider.class),
                                                                             views);

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
		remoteViews.setViewVisibility(R.id.layoutOnOff, View.GONE);
		//remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());
		
		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		//remoteViews.setOnClickPendingIntent(R.id.widget_button, MyWidgetProvider.buildButtonPendingIntent(context));
		
		AppWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
    
	
    
}
