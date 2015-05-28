package gbernat.flashlight;

import android.app.Application;
import android.content.Context;
import android.hardware.Camera;

public class Utils extends Application {
	
	private static Context context;
	public static Camera cam;
	public static boolean isRunning;

    public void onCreate(){
        super.onCreate();
        Utils.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Utils.context;
    }

}
