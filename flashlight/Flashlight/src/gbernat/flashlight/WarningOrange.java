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

public class WarningOrange extends Activity {

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

	private final Runnable mRunnable = new Runnable() {

		public void run() {

			if (mActive) {
				if (mSwap) {
					mLinearLayoutTop.setBackgroundColor(Color.rgb(232, 118, 0));
					mLinearLayoutBottom.setBackgroundColor(Color.BLACK);
					mSwap = false;

					mHander.postDelayed(mRunnable, (300));
					
				} else {
					mLinearLayoutTop.setBackgroundColor(Color.BLACK);
					mLinearLayoutBottom.setBackgroundColor(Color.rgb(232, 118,
							0));
					mSwap = true;

					mHander.postDelayed(mRunnable, (300));
					
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
