package gbernat.flashlight.activities;

import java.io.IOException;

import gbernat.flashlight.R;
import gbernat.flashlight.R.id;
import gbernat.flashlight.R.layout;
import gbernat.flashlight.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class StrobeLight extends Activity implements OnClickListener {

	Handler mHandler = new Handler();
	private static Camera cam;
	private static Parameters params;
	private boolean isFlashOn = false;
	private boolean mActive = false;
	private static boolean mSwap = true;
	private SeekBar frequencyBar;
	int progressChanged = 100;
	private TextView timeTextView;
	
	PowerManager powerManager;
	WakeLock wakeLock;

	ToggleButton onOff;

	private final Runnable mRunnable = new Runnable() {

		public void run() {
			if (mActive) {
				if (mSwap) {

					flashLightOn();
					mHandler.postDelayed(mRunnable, (progressChanged * 100));
					mSwap = false;
				} else {

					flashLightOff();
					mHandler.postDelayed(mRunnable, (progressChanged * 100));
					mSwap = true;

				}
			}
		}
	};
	private SurfaceHolder mHolder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.strobe_light);
		
		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
		        "MyWakelockTag");
		
		wakeLock.acquire();

		try {
			cam = Camera.open();
			params = cam.getParameters();
		} catch (Exception e) {
			showDialog();
		}

		onOff = (ToggleButton) findViewById(R.id.toggleButton_on_off);
		onOff.setOnClickListener(this);

		timeTextView = (TextView) findViewById(R.id.timeTextView);
		timeTextView.setText(getString(R.string.label_flash_time) + " " + 10
				+ "s");

		frequencyBar = (SeekBar) findViewById(R.id.frequency_bar);
		frequencyBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progressChanged = 100 - progress;
				timeTextView.setText(getString(R.string.label_flash_time) + " "
						+ (progressChanged * 100 / 1000F) + getString(R.string.time_unit_seconds));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				if (mActive) {
					mHandler.removeCallbacks(mRunnable);
					mSwap = true;
					startStrobe();
				}

			}
		});
	}

	private void startStrobe() {
		mHandler.removeCallbacks(mRunnable);
		mSwap = true;
		mActive = true;
		mHandler.post(mRunnable);
	}

	public void flashLightOn() {
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		cam.setParameters(params);
		cam.startPreview();
		isFlashOn = true;
	}

	/*
	 * This method turn off the LED camera flash light
	 */
	public void flashLightOff() {
		params = cam.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		cam.setParameters(params);
		cam.stopPreview();
		isFlashOn = false;
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
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {

		if (cam != null) {
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			cam.setParameters(params);
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
				startStrobe();
			} else {
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				cam.setParameters(params);
				cam.startPreview();
				mActive = false;
				mHandler.removeCallbacks(mRunnable);
			}

		}

	}

	public void showDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(StrobeLight.this)
				.create();
		// Setting Dialog Title
		alertDialog.setTitle(getApplication().getString(R.string.app_name));
		// Setting Dialog Message
		alertDialog.setMessage(getApplication().getString(
				R.string.label_busy_camera));
		// Setting OK Button
		alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int which) {
						// Write your code here to execute after dialog closed
						finish();
					}
				});

		alertDialog.show();
	}

}
