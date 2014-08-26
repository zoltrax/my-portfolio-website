package gbernat.flashlight;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import gbernat.flashlight.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
	
	Calendar now = Calendar.getInstance();
	Calendar last = Calendar.getInstance(); 

	private static Camera camera;
	private static Parameters parameters;
	
	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity

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
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;

	    
	    //now.setTime(new Date());
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
		mSensorManager.unregisterListener(mSensorListener);
		super.onPause();
		Log.v("onPause", "onPause");
		setFlashOff();
	}
	
	public void showDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create();

		// Setting Dialog Title
		alertDialog.setTitle(getApplication().getString(R.string.app_name));
		//alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//alertDialog.setContentView(R.layout.appwidgetlay);

		// Setting Dialog Message
		alertDialog.setMessage(getApplication().getString(R.string.label_busy_camera));

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
	
	  @Override
	  protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  }
	  
	  private final SensorEventListener mSensorListener = new SensorEventListener() {


		    public void onAccuracyChanged(Sensor sensor, int accuracy) {
		    	
		    	
			     // Calendar now = Calendar.getInstance(); 
			      //
			     
			     Log.v("cos","cos");
			     
		    }

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				  float x = event.values[0];
			      float y = event.values[1];
			      float z = event.values[2];
			      mAccelLast = mAccelCurrent;
			      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
			      float delta = mAccelCurrent - mAccelLast;
			      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
			      
			     
			     
			      if (mAccel > 12) {
			    	  now.setTime(new Date()); 
				      //Log.v("now time",""+now.getTimeInMillis());		 
				      long diff = now.getTimeInMillis() - last.getTimeInMillis();
			    	  //Log.v("diff","diff "+diff);
			    	  	if(diff>750){
			    	  		last.setTime(now.getTime()); 
			    	  		if (camera == null) {
			    	  			setFlashOn();
			    	  		} else {
			    	  			setFlashOff();
			    	  		}
			    	  	}
			    	  //	Toast.makeText(getApplicationContext(), "shake it baybe shake it", 1000).show();
				   }
			  }
			
		  };

}
