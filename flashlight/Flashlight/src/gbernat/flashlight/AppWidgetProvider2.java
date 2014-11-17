package gbernat.flashlight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gbernat.flashlight.AppWidgetProvider2;
import android.annotation.TargetApi;
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

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AppWidgetProvider2 extends android.appwidget.AppWidgetProvider {
	
	public static String CLOCK_WIDGET_UPDATE = "gbernat.flashlight.8BITCLOCK_WIDGET_UPDATE";
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");

	 
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	    final int N = appWidgetIds.length;
	    super.onUpdate(context, appWidgetManager, appWidgetIds);
	   
	    for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
           
            // Create an Intent to launch ExampleActivity
            Intent receiver = new Intent(context,  FlashlightWidgetReceiver.class);
           // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            
            receiver.setAction("COM_FLASHLIGHT");
            
            //Intent receiver = new Intent(context,  FlashlightWidgetReceiver.class);
	    	// receiver.setAction("gbernat.flashlight.intent.action.CHANGE_PICTURE");
            
           
            receiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);
           
            
            Intent intent2 = new Intent(context, Main.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button//
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay2);
            views.setOnClickPendingIntent(R.id.imageButton22, pendingIntent);
            //views.setOnClickPendingIntent(R.id.buttonGoToApp, pendingIntent2);
            
            
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1){
            	
            	for (int appWidgetId2 : appWidgetIds) {
                	Bundle options=appWidgetManager.getAppWidgetOptions(appWidgetId2);
                	onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId2,
                	options);
                	}
                // Do something for froyo and above versions
            } 
            
            
            
            
            
          // views.
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }	    
	    
	  }
	  
		@Override
		public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		}
		

		
		
		
	  
	  @Override
	  public void onAppWidgetOptionsChanged(Context context,
	          AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

	 
	      Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
	      
	     

           // Get the layout for the App Widget and attach an on-click listener
           // to the button//
           RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay2);
           

	      int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
	      int minHeight = options
	              .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

	      int columns = getCellsForSize(minWidth);
	      //if(columns>1){
          //views.setViewVisibility(R.id.layoutOnOff, View.VISIBLE);
	     // }else{
	     //	  views.setViewVisibility(R.id.layoutOnOff, View.GONE);
	     //  }
	      
	         
	      appWidgetManager.updateAppWidget(appWidgetId, views);
	      appWidgetManager.updateAppWidget(appWidgetId,
	              getRemoteViews(context, minWidth, minHeight));

	      super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
	              newOptions);
	  }
	  
	  private RemoteViews getRemoteViews(Context context, int minWidth,
		        int minHeight) {
		
		    int rows = getCellsForSize(minHeight);
		    int columns = 2;//getCellsForSize(minWidth);

		    if (columns > 1) {
		        // Get 4 column widget remote view and return
		    	
		    
		    	
		    	return new RemoteViews(context.getPackageName(),
		                R.layout.ofappwidgetlay2);
		    	
		    	
		    } else {
		                    // Get appropriate remote view.
		        return new RemoteViews(context.getPackageName(),
		                R.layout.ofappwidgetlay2);
		    }
		}
	  
	  private static int getCellsForSize(int size) {
		  int n = 2;
		  while (70 * n - 30 < size) {
		    ++n;
		  }
		  return n - 1;
		 }

	public static void pushWidgetUpdate(Context context,
			RemoteViews remoteViews) {
		// TODO Auto-generated method stub
		ComponentName myWidget = new ComponentName(context, AppWidgetProvider2.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);
	}
	
	private PendingIntent createClockTickIntent(Context context) {
	    Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    return pendingIntent;
	}
	
	@Override
	public void onEnabled(Context context) {
	        super.onEnabled(context);
	       // Log.d(LOG_TAG, "Widget Provider enabled.  Starting timer to update widget every second");
	        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	 
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.add(Calendar.SECOND, 2);
	        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 5000
	, createClockTickIntent(context));
	}
	 
	@Override
	public void onDisabled(Context context) {
	        super.onDisabled(context);
	      //  Log.d(LOG_TAG, "Widget Provider disabled. Turning off timer");
	        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        alarmManager.cancel(createClockTickIntent(context));
	}  

}
