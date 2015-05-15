package gbernat.flashlight.activities;

import java.io.IOException;

import gbernat.flashlight.R;
import gbernat.flashlight.R.id;
import gbernat.flashlight.R.layout;
import gbernat.flashlight.R.string;
import gbernat.flashlight.Utils;
import gbernat.widget.flashlight.widget.AppWidgetProvider2;
import gbernat.widget.flashlight.widget.FlashlightWidgetReceiver;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
	// private static Camera cam;
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
	public static String reset = "gbernat.flashlight.widget.intent.action.RESET";

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
			if (Utils.cam == null) {
				Log.v("null", "null");
				Utils.cam = Camera.open();
				params = Utils.cam.getParameters();
			}
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
						+ (progressChanged * 100 / 1000F)
						+ getString(R.string.time_unit_seconds));
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
		Utils.isRunning = true;
	}

	public void flashLightOn() {

		try {
			if (Utils.cam == null) {

				Utils.cam = Camera.open();
				params = Utils.cam.getParameters();
			}
		} catch (Exception e) {
			showDialog();
		}
		// try{
		params = Utils.cam.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		Utils.cam.setParameters(params);
		Utils.cam.startPreview();
		isFlashOn = true;
		// }catch(Exception e){

		// }
	}

	// IntentFilter filter = new IntentFilter(reset);
	// BroadcastReceiver receiver = new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // DO YOUR STUFF
	// Log.v("strobe","receive");
	// }
	// };

	/*
	 * This method turn off the LED camera flash light
	 */
	public void flashLightOff() {
		// try{
		try {
			if (Utils.cam == null) {

				Utils.cam = Camera.open();
				params = Utils.cam.getParameters();
			}
		} catch (Exception e) {
			showDialog();
		}

		params = Utils.cam.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		Utils.cam.setParameters(params);
		Utils.cam.stopPreview();
		isFlashOn = false;

		// }catch(Exception e){

		// }
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// TODO Auto-generated method stub
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		mHolder = holder;
		try {
			Utils.cam.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		Utils.cam.stopPreview();
		mHolder = null;

	}

	@Override
	protected void onPause() {
		super.onPause();
		Utils.pausedActiv = 2;
		// Utils.isRunning = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if (Utils.cam == null) {

				Utils.cam = Camera.open();
				params = Utils.cam.getParameters();
			}
		} catch (Exception e) {
			showDialog();
		}
		Utils.pausedActiv = 2;

	}

	@Override
	public void onBackPressed() {

		if (Utils.cam != null) {
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			Utils.cam.setParameters(params);
			mHandler.removeCallbacks(mRunnable);
			Utils.cam.stopPreview();
			Utils.cam.release();
			Utils.cam = null;
		}
		Intent returnBtn = new Intent(getApplicationContext(), Main.class);
		returnBtn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(returnBtn);
		Utils.isRunning = false;
		finish();
	}

	// public static void sendUpdateIntent(Context context)
	// {
	// Intent i = new Intent(context, AppWidgetProvider2.class);
	// i.setAction(AppWidgetProvider2.block);
	// context.sendBroadcast(i);
	// }
	//
	// public static void sendUpdateIntent2(Context context)
	// {
	// Intent i = new Intent(context, AppWidgetProvider2.class);
	// i.setAction(AppWidgetProvider2.unblock);
	// context.sendBroadcast(i);
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == onOff) {
			if (onOff.isChecked()) {
				startStrobe();
				// sendUpdateIntent(getApplicationContext());
			} else {
				try {
					params.setFlashMode(Parameters.FLASH_MODE_OFF);
					Utils.cam.setParameters(params);
					Utils.cam.startPreview();
					mActive = false;
					mHandler.removeCallbacks(mRunnable);
					Utils.isRunning = false;
				} catch (Exception e) {

				}
				// sendUpdateIntent2(getApplicationContext());
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
