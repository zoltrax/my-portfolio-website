package gbernat.flashlight.activities;

import gbernat.flashlight.R;
import gbernat.flashlight.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ScreenFlashlight extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_flashlight);
		
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.screenBrightness = 1;
		getWindow().setAttributes(params);

	}
	
	@Override
	public void onPause(){
		super.onPause();
	}

}
