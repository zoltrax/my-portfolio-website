package gbernat.flashlight;

import gbernat.flashlight.AppWidgetProvider;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {
	
	
	 
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

            // Get the layout for the App Widget and attach an on-click listener
            // to the button//
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay);
            views.setOnClickPendingIntent(R.id.imageButton2, pendingIntent);
            
            for (int appWidgetId2 : appWidgetIds) {
            	Bundle options=appWidgetManager.getAppWidgetOptions(appWidgetId2);
            	onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId2,
            	options);
            	}
          // views.
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }	    
	    
	  }
	  
	  @Override
	  public void onAppWidgetOptionsChanged(Context context,
	          AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

	 
	      Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
	      
	     

           // Get the layout for the App Widget and attach an on-click listener
           // to the button//
           RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ofappwidgetlay);
           

	      int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
	      int minHeight = options
	              .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

	      int columns = getCellsForSize(minWidth);
	      if(columns>1){
          views.setViewVisibility(R.id.layoutOnOff, View.VISIBLE);
	      }else{
	    	  views.setViewVisibility(R.id.layoutOnOff, View.GONE);
	      }
	      
	         
	      appWidgetManager.updateAppWidget(appWidgetId, views);
	      appWidgetManager.updateAppWidget(appWidgetId,
	              getRemoteViews(context, minWidth, minHeight));

	      super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
	              newOptions);
	  }
	  
	  private RemoteViews getRemoteViews(Context context, int minWidth,
		        int minHeight) {
		
		    int rows = getCellsForSize(minHeight);
		    int columns = getCellsForSize(minWidth);

		    if (columns > 1) {
		        // Get 4 column widget remote view and return
		    	
		    
		    	
		    	return new RemoteViews(context.getPackageName(),
		                R.layout.ofappwidgetlay);
		    	
		    	
		    } else {
		                    // Get appropriate remote view.
		        return new RemoteViews(context.getPackageName(),
		                R.layout.ofappwidgetlay);
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
		ComponentName myWidget = new ComponentName(context, AppWidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);
	}
	  

}
