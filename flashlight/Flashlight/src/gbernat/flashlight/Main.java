package gbernat.flashlight;

import java.io.IOException;

import gbernat.flashlight.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CameraProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Main extends Activity implements OnClickListener {

	ImageButton onOff;

	Button btn1;
	Button btn2;
	Button btn3;
	Button btn4;
	Button btn5;
	Button btn6;
	

	private static Camera camera;
	private static Parameters parameters;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		onOff = (ImageButton) findViewById(R.id.toggleButton1);

		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn3 = (Button) findViewById(R.id.button3);
		btn4 = (Button) findViewById(R.id.button4);
		btn5 = (Button) findViewById(R.id.button5);
		btn6 = (Button) findViewById(R.id.button6);

		onOff.setOnClickListener(this);
		// onOff.setChecked(false);

		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);

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
			startActivity(strobeLight);

		} else if (v == btn2) {

			Intent strobeColors = new Intent(this, StrobeScreen.class);
			startActivity(strobeColors);

		} else if (v == btn3) {

			Intent morse = new Intent(this, MorseCode.class);
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
		}

	}

	private void setFlashOn() {
		try{
		if (camera == null)
			camera = Camera.open();
		parameters = camera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(parameters);
		onOff.setImageResource(R.drawable.power_on);
		}catch(Exception e){
			//Toast.makeText(this, "Your camera is busy, perhaps another App or Widget uses it. Please ", 2000).show();
			showDialog();
		}
	}

	private void setFlashOff() {
		try{
		if (camera != null) {
			onOff.setImageResource(R.drawable.power_off);
			parameters = camera.getParameters();
			parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
		}catch(Exception e){
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
		Log.v("onPause", "onPause");
		setFlashOff();
	}
	
	public void showDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Flashlight");
		//alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//alertDialog.setContentView(R.layout.appwidgetlay);

		// Setting Dialog Message
		alertDialog.setMessage("Your camera is busy, perhaps another App or Widget uses it. Please turn off.");

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
