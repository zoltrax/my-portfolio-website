package gbernat.flashlight;

import java.io.IOException;

import gbernat.flashlight.R;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class StrobeLight extends Activity implements OnClickListener {

	Handler mHandler = new Handler();
	private static Camera cam;
	private static Parameters params;
	private boolean isFlashOn = false;
	private boolean mActive = false;
	private boolean mSwap = true;

	private SeekBar frequencyBar;
	int progressChanged = 100;

	ToggleButton onOff;

	private final Runnable mRunnable = new Runnable() {

		public void run() {
			if (mActive) {
				if (mSwap) {

					mSwap = false;
					flashLightOn();
					mHandler.postDelayed(mRunnable, (progressChanged * 100));
					Log.d("cyk", "pyk1");
				} else {

					mSwap = true;
					flashLightOff();
					mHandler.postDelayed(mRunnable, (progressChanged * 100));
					Log.v("dym", "dym");
					Log.d("cyk", "pyk2");
				}
			}
		}
	};
	private SurfaceHolder mHolder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.strobe_light);

		cam = Camera.open();
		params = cam.getParameters();

		onOff = (ToggleButton) findViewById(R.id.toggleButton_on_off);

		onOff.setOnClickListener(this);

		frequencyBar = (SeekBar) findViewById(R.id.frequency_bar);
		frequencyBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progressChanged = progress;
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				Toast.makeText(StrobeLight.this,
						"seek bar progress:" + progressChanged,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void startStrobe() {
		mActive = true;
		mHandler.post(mRunnable);
	}

	public void flashLightOn() {
		Log.d("Time", "On");
		if (!isFlashOn) {
			if (cam != null || params != null) {

				Log.v("ech", "ech");
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				cam.setParameters(params);
				cam.startPreview();
				isFlashOn = true;
			} else {

				Log.v("ech1", "ech1");
				cam = Camera.open();
				params = cam.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				cam.setParameters(params);
				cam.startPreview();
				isFlashOn = true;
			}
		} else {
			return;
		}
	}

	/*
	 * This method turn off the LED camera flash light
	 */
	public void flashLightOff() {
		Log.d("Time", "Off");
		if (isFlashOn) {
			if (cam != null || params != null) {
				params = cam.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				cam.setParameters(params);
				cam.stopPreview();
				isFlashOn = false;
			} else {
				params = cam.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				cam.setParameters(params);
				cam.stopPreview();
				isFlashOn = false;
			}

		} else {
			return;
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// TODO Auto-generated method stub
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mHolder = holder;
		try {
			cam.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		cam.stopPreview();
		mHolder = null;

	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * Release Camera
		 */
		// isFlashOn = false;
		// if (cam != null) {
		// cam.release();
		// cam = null;
		// }
		// onOff.setChecked(false);
		// cam.stopPreview();
		// cam.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * Check if light was on when App was paused. If so, turn back on.
		 */
		// isFlashOn = false;
		// if (params != null
		// && params.getFlashMode() == Parameters.FLASH_MODE_TORCH) {
		// if (cam == null)
		// cam = Camera.open();
		// cam.setParameters(params);
		// onOff.setChecked(true);
		// }
	}

	@Override
	public void onBackPressed() {

		if (cam != null) {
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			cam.setParameters(params);
			// cam.startPreview();
			// cam.release();
			// isFlashOn = false;
			// cam = null;
			// params = null;

			mHandler.removeCallbacks(mRunnable);
			cam.stopPreview();
			cam.release();
			cam = null;
		}
		finish();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == onOff) {

			if (onOff.isChecked()) {
				// if(cam==null){
				startStrobe();
				// }
			} else {

				// cam = Camera.open();
				// params = cam.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				cam.setParameters(params);
				cam.startPreview();
				// cam.release();
				mHandler.removeCallbacks(mRunnable);
			}

		}

	}

}
