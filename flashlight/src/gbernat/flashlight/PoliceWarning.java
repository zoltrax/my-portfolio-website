package gbernat.flashlight;

import gbernat.flashlight.R;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

public class PoliceWarning extends Activity {

	private LinearLayout mLinearLayoutTop;
	private LinearLayout mLinearLayoutBottom;

	private Handler mHander = new Handler();

	private boolean mActive = false;
	private boolean mSwap = true;

	private static Camera cam;
	private static Parameters params;
	private boolean isFlashOn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warning_orange);
		mLinearLayoutTop = (LinearLayout) findViewById(R.id.linearLayout_main11top);
		mLinearLayoutBottom = (LinearLayout) findViewById(R.id.linearLayout_main11bottom);
		startStrobe();

	}

	private void startStrobe() {
		mActive = true;
		mHander.post(mRunnable);
	}

	int count =0;
	
	private final Runnable mRunnable = new Runnable() {
			
		public void run() {
			if (mActive) {
				
				if(count<4){
					mLinearLayoutBottom.setBackgroundColor(Color.BLACK);
					if (mSwap) {
						count++;
						mLinearLayoutTop.setBackgroundColor(Color.BLUE);
						mSwap = false;
						mHander.postDelayed(mRunnable, (145));						

					} else {
						mLinearLayoutTop.setBackgroundColor(Color.WHITE);
						mSwap = true;
						mHander.postDelayed(mRunnable, (50));

					}
				
				}else{
					mLinearLayoutTop.setBackgroundColor(Color.BLACK);
					
					if(count>6) count=0;
					
					if (mSwap) {
						count++;
						mLinearLayoutBottom.setBackgroundColor(Color.WHITE);
						mSwap = false;
						mHander.postDelayed(mRunnable, (50));
						

					} else {
						mLinearLayoutBottom.setBackgroundColor(Color.RED);
						mSwap = true;

						mHander.postDelayed(mRunnable, (145));

					}
				}	
					
			}
		}
	};
	 @Override
		protected void onPause() {
			super.onPause();
			/**
			 * Release Camera
			 */

			mHander.removeCallbacks(mRunnable);
			if (cam != null) {
				cam.stopPreview();
				cam.release();
				cam = null;
			}

			finish();
		}

}
