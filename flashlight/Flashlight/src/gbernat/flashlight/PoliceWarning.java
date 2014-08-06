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

	private final Runnable mRunnable = new Runnable() {

		public void run() {

			if (mActive) {
				if (mSwap) {
					mLinearLayoutTop.setBackgroundColor(Color.BLUE);
					mLinearLayoutBottom.setBackgroundColor(Color.BLACK);
					mSwap = false;
					mHander.postDelayed(mRunnable, (300));
					Log.d("cyk", "pyk1");
				} else {
					mLinearLayoutTop.setBackgroundColor(Color.BLACK);
					mLinearLayoutBottom.setBackgroundColor(Color.RED);
					mSwap = true;

					mHander.postDelayed(mRunnable, (300));
					Log.v("dym", "dym");
					Log.d("cyk", "pyk2");
				}
			}
		}
	};

}
