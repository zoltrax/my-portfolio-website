package gbernat.flashlight.activities;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import gbernat.flashlight.R;
import gbernat.flashlight.Utils;
import gbernat.flashlight.R.id;
import gbernat.flashlight.R.layout;
import gbernat.flashlight.R.string;
import gbernat.flashlight.controls.MyEditText;
import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.text.AndroidCharacter;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class MorseCode extends Activity {

	private Handler mHandler = new Handler();
	private static Camera cam;
	private static Parameters params;
	private boolean isFlashOn = false;
	private boolean mActive = false;
	private boolean mSwap = true;
	private boolean sosFlag = false;
	PowerManager powerManager;
	WakeLock wakeLock;
	boolean isRunning = false;
	int count = 0;

	TextSwitcher speedText;
	MyEditText messageEditText;
	TextView showMorseTextView;

	protected static int lenght;
	protected static String sos;

	Button btnStart;
	Button btnSos;
	Button btnPlus;
	Button btnMinus;

	String textToShow[] = { "1x", "2x", "3x", "4x", "5x", "6x" };
	int messageCount = textToShow.length;
	int currentIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morse_code);

		getActionBar().hide();

		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MyWakelockTag");

		btnStart = (Button) findViewById(R.id.button2);
		btnSos = (Button) findViewById(R.id.button4);
		btnPlus = (Button) findViewById(R.id.button6);
		btnMinus = (Button) findViewById(R.id.button5);

		speedText = (TextSwitcher) findViewById(R.id.textSwitcher1);
		showMorseTextView = (TextView) findViewById(R.id.showMorseTextView);
		messageEditText = (MyEditText) findViewById(R.id.morseCodeEditText);
		messageEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

		speedText.setFactory(new ViewFactory() {

			@SuppressLint("ResourceAsColor")
			public View makeView() {
				TextView myText = new TextView(MorseCode.this);
				myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
				myText.setTextSize(54);
				myText.setTextColor(getResources().getColor(R.color.Red));
				return myText;
			}
		});

		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in);

		speedText.setInAnimation(in);

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(Utils.getAppContext());
		currentIndex = sharedPref.getInt("morseSpeed", 0);
		
		Animation fadeOut = new AlphaAnimation(1, 0.2f);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setFillAfter(true);
		fadeOut.setDuration(400);

		if (currentIndex == 0) {
					
			btnMinus.startAnimation(fadeOut);
			btnMinus.setEnabled(false);

		} else if (currentIndex == 5) {
			
			btnPlus.startAnimation(fadeOut);
			btnPlus.setEnabled(false);

		}

		speedText.setText(textToShow[currentIndex]);

		messageEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

				showMorseTextView.setText(stringConvert(messageEditText
						.getText().toString()));
				// showMorseTextView.get;
				// messageEditText.scrollTo(x, y);
				Log.v("line", "" + showMorseTextView.getHeight());

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		showMorseTextView.setMovementMethod(new ScrollingMovementMethod());

		messageEditText
				.setOnEditorActionListener(new MyEditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
						}
						return false;
					}

				});

		View.OnKeyListener onEnterSubmitView = new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					Log.v("hee", "hee");
					// submitView(v);
					return true;
				}
				return false;
			}
		};

		try {
			cam = Camera.open();
			params = cam.getParameters();
		} catch (Exception e) {
			showDialog();
		}

	}

	public Runnable mRunnable = new Thread() {

		private boolean mActive = true;
		int speed = 1;

		public void run() {
			isRunning = true;
			speed = currentIndex + 1;

			if (count == 0) {
				btnStart.setEnabled(false);
				btnSos.setEnabled(false);
				Animation fadeOut = new AlphaAnimation(1, 0.2f);
				fadeOut.setInterpolator(new AccelerateInterpolator());
				fadeOut.setFillAfter(true);
				fadeOut.setDuration(1000);
				btnStart.startAnimation(fadeOut);
				btnSos.startAnimation(fadeOut);
				count++;
			}
			if (lenght < 1) {
				flashLightOff();
				btnStart.setEnabled(true);
				btnSos.setEnabled(true);
				Animation fadeIn = new AlphaAnimation(0.2f, 1);
				fadeIn.setInterpolator(new AccelerateInterpolator());
				fadeIn.setFillAfter(true);
				fadeIn.setDuration(300);
				isRunning = false;
				btnStart.startAnimation(fadeIn);
				btnSos.startAnimation(fadeIn);
				count = 0;
				return;
			}

			int delay = 0;

			if (mActive) {

				if (mSwap) {

					mSwap = false;

					if (sos.charAt(lenght - 1) == '.') {
						// Log.v("lenght", "1 " + lenght);
						delay = 300;
						flashLightOn();
						Log.v("kropka", "kropka");
					} else if (sos.charAt(lenght - 1) == ' ') {
						// Log.v("lenght", "2 " + lenght);
						delay = 900;
						Log.v("brak znaku", "brak znaku");
					} else if (sos.charAt(lenght - 1) == '-') {
						// Log.v("lenght", "3 " + lenght);
						delay = 900;
						flashLightOn();
						Log.v("kreska", "kreska");

					} else {
						// Log.v("lenght", "4 " + lenght);
						delay = 2100;
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
						cam.setParameters(params);
						cam.stopPreview();
						Log.v("is there something more", "no no no " + delay);
					}
					mHandler.postDelayed(this, (delay / speed));
					Log.v("lenght", "7 " + delay / speed);
					lenght--;
				} else {
					Log.v("lenght", "6 " + delay / speed);
					mSwap = true;

					flashLightOff();
					mHandler.postDelayed(this, (300 / speed));

				}

			}

		}

	};
	private SurfaceHolder mHolder;

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

	public void startLights(View v) {

		mHandler.removeCallbacks(mRunnable);
		mHandler.removeCallbacksAndMessages(null);
		if (showMorseTextView.getText().toString().length() > 0) {
			sos = new StringBuilder(showMorseTextView.getText().toString())
					.reverse().toString().substring(1);
			Log.v("sos", sos);
			// sosFlag = false;
			lenght = sos.length();
			mActive = true;

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.RESULT_UNCHANGED_SHOWN);

			mHandler.post(mRunnable);
		}
	}

	public void sos(View v) {

		// sosFlag = true;
		Log.v("isRunning", "" + isRunning);

		mHandler.removeCallbacks(mRunnable);
		mHandler.removeCallbacksAndMessages(null);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);

		mActive = true;
		sos = "... --- ...";
		mHandler.post(mRunnable);
		lenght = sos.length();
		Log.v("2", "2");

	}

	public void stopLights(View v) {

		Log.v("test", "test");
		isFlashOn = false;

		mActive = false;
		mHandler.removeCallbacks(mRunnable);
		count = 0;
		btnStart.setEnabled(true);
		btnSos.setEnabled(true);
		Animation fadeIn = new AlphaAnimation(0.2f, 1);
		fadeIn.setInterpolator(new AccelerateInterpolator());
		fadeIn.setFillAfter(true);
		fadeIn.setDuration(300);
		isRunning = false;
		btnStart.startAnimation(fadeIn);
		btnSos.startAnimation(fadeIn);

		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		cam.setParameters(params);

	}

	public void speedPlus(View v) {

		currentIndex++;

		Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
		Animation out = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_bottom);
		speedText.clearAnimation();
		speedText.setOutAnimation(out);
		speedText.setInAnimation(in);

		// If index reaches maximum reset it
		if (currentIndex > 5) {
			currentIndex = 5;
			
		} else {
			if(currentIndex==5){
			Animation fadeOut = new AlphaAnimation(1, 0.2f);
			fadeOut.setInterpolator(new AccelerateInterpolator());
			fadeOut.setFillAfter(true);
			fadeOut.setDuration(400);
			btnPlus.setEnabled(false);
			btnPlus.startAnimation(fadeOut);
			speedText.setText(textToShow[currentIndex]);
			}
			
			speedText.setText(textToShow[currentIndex]);
		}
		
		if(currentIndex==1){
			btnMinus.setEnabled(true);
			Animation fadeOut = new AlphaAnimation(0.2f,1);
			fadeOut.setInterpolator(new AccelerateInterpolator());
			fadeOut.setFillAfter(true);
			fadeOut.setDuration(400);
			btnMinus.startAnimation(fadeOut);
		}

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(Utils.getAppContext());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("morseSpeed", currentIndex);
		editor.commit();

	}

	public void speedMinus(View v) {

		Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_up2);
		Animation out = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_bottom2);
		speedText.clearAnimation();
		speedText.setOutAnimation(in);
		speedText.setInAnimation(out);

		currentIndex--;
		// If index reaches maximum reset it
		if (currentIndex < 0) {
			currentIndex = 0;
			

		}else{
			
			if(currentIndex==0){
				Animation fadeOut = new AlphaAnimation(1, 0.2f);
				fadeOut.setInterpolator(new AccelerateInterpolator());
				fadeOut.setFillAfter(true);
				fadeOut.setDuration(400);
				btnMinus.setEnabled(false);
				btnMinus.startAnimation(fadeOut);
			}
			
		}
			speedText.setText(textToShow[currentIndex]);
		if(currentIndex==4){
			btnPlus.setEnabled(true);
			Animation fadeOut = new AlphaAnimation(0.2f,1);
			fadeOut.setInterpolator(new AccelerateInterpolator());
			fadeOut.setFillAfter(true);
			fadeOut.setDuration(400);
			btnPlus.startAnimation(fadeOut);
		}

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(Utils.getAppContext());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("morseSpeed", currentIndex);
		editor.commit();
	}

	public void translate(View v) {
		showMorseTextView.setText(stringConvert(messageEditText.getText()
				.toString()));
	}

	public static String stringConvert(String userString) {
		String currentChar;
		String getMorseChar;
		String convertedString = "";

		for (int i = 0; i < userString.length(); i++) {
			// Get character at i position
			currentChar = userString.charAt(i) + "";

			// convert character to morse code
			getMorseChar = convert(currentChar);

			// seperate words with the | symbol
			if (getMorseChar.equals(" ")) {
				Log.v("convertedString", convertedString + "l");
				convertedString = convertedString.substring(0,
						convertedString.length() - 1)
						+ "|";
				Log.v("convertedString", convertedString);
			}

			else {
				// concat the converted letter
				convertedString = convertedString + getMorseChar;

				// Add a space between each letter
				if (!getMorseChar.equals(" ") && (!convertedString.equals("|"))) {
					convertedString = convertedString + " ";
				}
			}
		}

		return convertedString;

	}

	public static String convert(String toEncode) {
		String morse = toEncode;

		if (toEncode.equalsIgnoreCase("a"))
			morse = ".-";
		if (toEncode.equalsIgnoreCase("b"))
			morse = "-...";
		if (toEncode.equalsIgnoreCase("c"))
			morse = "-.-.";
		if (toEncode.equalsIgnoreCase("d"))
			morse = "-..";
		if (toEncode.equalsIgnoreCase("e"))
			morse = ".";
		if (toEncode.equalsIgnoreCase("f"))
			morse = "..-.";
		if (toEncode.equalsIgnoreCase("g"))
			morse = "--.";
		if (toEncode.equalsIgnoreCase("h"))
			morse = "....";
		if (toEncode.equalsIgnoreCase("i"))
			morse = "..";
		if (toEncode.equalsIgnoreCase("j"))
			morse = ".---";
		if (toEncode.equalsIgnoreCase("k"))
			morse = "-.-";
		if (toEncode.equalsIgnoreCase("l"))
			morse = ".-..";
		if (toEncode.equalsIgnoreCase("m"))
			morse = "--";
		if (toEncode.equalsIgnoreCase("n"))
			morse = "-.";
		if (toEncode.equalsIgnoreCase("o"))
			morse = "---";
		if (toEncode.equalsIgnoreCase("p"))
			morse = ".--.";
		if (toEncode.equalsIgnoreCase("q"))
			morse = "--.-";
		if (toEncode.equalsIgnoreCase("r"))
			morse = ".-.";
		if (toEncode.equalsIgnoreCase("s"))
			morse = "...";
		if (toEncode.equalsIgnoreCase("t"))
			morse = "-";
		if (toEncode.equalsIgnoreCase("u"))
			morse = "..-";
		if (toEncode.equalsIgnoreCase("v"))
			morse = "...-";
		if (toEncode.equalsIgnoreCase("w"))
			morse = ".--";
		if (toEncode.equalsIgnoreCase("x"))
			morse = "-..-";
		if (toEncode.equalsIgnoreCase("y"))
			morse = "-.--";
		if (toEncode.equalsIgnoreCase("z"))
			morse = "--..";
		if (toEncode.equalsIgnoreCase("0"))
			morse = "-----";
		if (toEncode.equalsIgnoreCase("1"))
			morse = ".----";
		if (toEncode.equalsIgnoreCase("2"))
			morse = "..---";
		if (toEncode.equalsIgnoreCase("3"))
			morse = "...--";
		if (toEncode.equalsIgnoreCase("4"))
			morse = "....-";
		if (toEncode.equalsIgnoreCase("5"))
			morse = ".....";
		if (toEncode.equalsIgnoreCase("6"))
			morse = "-....";
		if (toEncode.equalsIgnoreCase("7"))
			morse = "--...";
		if (toEncode.equalsIgnoreCase("8"))
			morse = "---..";
		if (toEncode.equalsIgnoreCase("9"))
			morse = "----.";
		if (toEncode.equalsIgnoreCase("."))
			morse = ".-.-";
		if (toEncode.equalsIgnoreCase(","))
			morse = "--..--";
		if (toEncode.equalsIgnoreCase("?"))
			morse = "..--..";

		return morse;
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

	public void showDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(MorseCode.this)
				.create();

		// Setting Dialog Title
		alertDialog.setTitle(getApplication().getString(R.string.app_name));

		// Setting Dialog Message
		alertDialog.setMessage(getApplication().getString(
				R.string.label_busy_camera));

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();

	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * Release Camera
		 */

	}

}
