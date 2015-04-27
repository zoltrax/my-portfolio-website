package gbernat.flashlight;

import android.app.Application;
import android.content.Context;

public class Utils extends Application {
	
	private static Context context;

    public void onCreate(){
        super.onCreate();
        Utils.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Utils.context;
    }

}
