package gbernat.flashlight;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import gbernat.flashlight.R;
import gbernat.flashlight.ShakeDetector.OnShakeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CameraProfile;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Main extends Activity implements OnClickListener {

	ImageButton onOff;
	ImageButton setting;

	Button btn1;
	Button btn2;
	Button btn3;
	Button btn4;
	Button btn5;
	Button btn6;

	Calendar now = Calendar.getInstance();
	Calendar last = Calendar.getInstance();

	private static Camera camera;
	private static Parameters parameters;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;
	private SeekBar sb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		onOff = (ImageButton) findViewById(R.id.toggleButton1);
		setting = (ImageButton) findViewById(R.id.settingButton1);

		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn3 = (Button) findViewById(R.id.button3);
		btn4 = (Button) findViewById(R.id.button4);
		btn5 = (Button) findViewById(R.id.button5);
		btn6 = (Button) findViewById(R.id.button6);

		onOff.setOnClickListener(this);
		setting.setOnClickListener(this);

		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mShakeDetector = new ShakeDetector();
		mShakeDetector.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake(int count) {
				handleShakeEvent(count);
			}
		});

	}

	protected void handleShakeEvent(int count) {

		if (camera != null) {
			setFlashOff();
			return;
		} else {
			setFlashOn();
		}
	}

	@Override
	public void onClick(View v) {

		if (v == onOff) {

			if (camera == null) {
				setFlashOn();
			} else {
				setFlashOff();
			}

		} else if (v == btn1) {

			Intent strobeLight = new Intent(this, StrobeLight.class);
			if (camera != null) {
				setFlashOff();
			}
			startActivity(strobeLight);

		} else if (v == btn2) {

			Intent strobeColors = new Intent(this, StrobeScreen.class);
			startActivity(strobeColors);

		} else if (v == btn3) {

			Intent morse = new Intent(this, MorseCode.class);
			if (camera != null) {
				setFlashOff();
			}
			startActivity(morse);

		} else if (v == btn4) {

			Intent warningOrange = new Intent(this, WarningOrange.class);
			startActivity(warningOrange);

		} else if (v == btn5) {

			Intent policeWarning = new Intent(this, PoliceWarning.class);
			startActivity(policeWarning);

		} else if (v == btn6) {
			Intent flashScreen = new Intent(this, ScreenFlashlight.class);
			startActivity(flashScreen);
		} else if (v == setting) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.alert_dialog_settings,
					(ViewGroup) findViewById(R.id.sensivityLayout));
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setView(layout);
			final AlertDialog alertDialog = builder.create();
			alertDialog.show();
			sb = (SeekBar) layout.findViewById(R.id.sensivityDialogSeekbar);
			Button ok = (Button) layout.findViewById(R.id.okDialogButton);
			setting.setImageResource(R.drawable.setting_icon2);
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SharedPreferences sharedPref = PreferenceManager
							.getDefaultSharedPreferences(Utils.getAppContext());
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putInt(getString(R.string.shake_sensivity),
							sb.getProgress());
					editor.commit();
					setting.setImageResource(R.drawable.setting_icon);
					alertDialog.dismiss();
				}
			});

			Button no = (Button) layout.findViewById(R.id.noDialogButton);
			no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setting.setImageResource(R.drawable.setting_icon);
					alertDialog.dismiss();
				}
			});

			alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int keyCode,
						KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						setting.setImageResource(R.drawable.setting_icon);
						alertDialog.dismiss();
					}
					return true;
				}
			});

			final TextView dialogProgress = (TextView) layout
					.findViewById(R.id.dialogProgress);
			sb.setMax(20);
			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(Utils.getAppContext());
			int sensivity_sk = sharedPref.getInt(
					getString(R.string.shake_sensivity), 666);

			if (sensivity_sk == 666) {

				sb.setProgress(9);

			} else {

				sb.setProgress(sensivity_sk);
				dialogProgress.setText("" + sensivity_sk * 5
						+ getString(R.string.percent));

			}

			sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// Do something here with new value

					float percent = progress * 5;

					dialogProgress.setText("" + percent
							+ getString(R.string.percent));

					float sensivityPercentValue = (progress * 0.11f) + 1.3f;

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
			});

		}

	}

	private void setFlashOn() {
		try {
			if (camera == null)
				camera = Camera.open();
			parameters = camera.getParameters();
			parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameters);
			onOff.setImageResource(R.drawable.power_on);
		} catch (Exception e) {
			showDialog();
		}
	}

	private void setFlashOff() {
		try {
			if (camera != null) {
				onOff.setImageResource(R.drawable.power_off);
				parameters = camera.getParameters();
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		} catch (Exception e) {
			showDialog();
		}

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	private SurfaceHolder mHolder;

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mHolder = holder;
		try {
			camera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		mHolder = null;

	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mShakeDetector);
	}

	public void showDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create();
		// Setting Dialog Title
		alertDialog.setTitle(getApplication().getString(R.string.app_name));
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// alertDialog.setContentView(R.layout.appwidgetlay);
		// Setting Dialog Message
		alertDialog.setMessage(getApplication().getString(
				R.string.label_busy_camera));
		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
			}
		});
		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mShakeDetector, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
	}

}
