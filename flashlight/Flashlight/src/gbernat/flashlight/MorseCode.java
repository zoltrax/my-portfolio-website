package gbernat.flashlight;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MorseCode extends Activity {

	private Handler mHandler = new Handler();
	private static Camera cam;
	private static Parameters params;
	private boolean isFlashOn = false;
	private boolean mActive = false;
	private boolean mSwap = true;
	private boolean sosFlag = false;

	EditText messageEditText;
	TextView showMorseTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morse_code);

		messageEditText = (EditText) findViewById(R.id.morseCodeEditText);
		showMorseTextView = (TextView) findViewById(R.id.showMorseTextView);

		cam = Camera.open();
		params = cam.getParameters();

	}

	private final Runnable mRunnable = new Runnable() {

		public boolean flashing = true;
		int lenght = 0;

		public void run() {

			String sos = showMorseTextView.getText().toString();
			try {

				sos = sos.substring(0, (sos.length() - 1));
				int size = sos.length();
			} catch (Exception e) {

			}

			if (sosFlag == true) {
				sos = "... --- ...";
			}

			if (lenght >= (sos.length() * 2)) {
				lenght = 0;
				return;
			}

			int delay = 0;

			if (mActive) {

				if (mSwap) {

					mSwap = false;

					Log.v("kropka czy kreska", "" + sos.charAt(lenght / 2));

					if (sos.charAt(lenght / 2) == '.') {
						delay = 300;
						flashLightOn();
						Log.v("kropka", "kropka");
					} else if (sos.charAt(lenght / 2) == ' ') {
						delay = 300;
						Log.v("brak znaku", "brak znaku");
					} else if (sos.charAt(lenght / 2) == '-') {
						delay = 900;
						flashLightOn();
						Log.v("kreska", "kreska");

					} else {
						delay = 1300;
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
						cam.setParameters(params);
						cam.stopPreview();
						Log.v("is there something more", "no no no");
					}

					mHandler.postDelayed(mRunnable, (delay));
					Log.d("cyk", "pyk1" + lenght / 2);

				} else {

					mSwap = true;

					flashLightOff();

					mHandler.postDelayed(mRunnable, (300));

				}

			}

			lenght++;

		}
	};
	private SurfaceHolder mHolder;

	public void flashLightOn() {
		Log.d("Time", "On");
		if (!isFlashOn) {
			if (cam != null || params != null) {
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				try {
					cam.setParameters(params);
					cam.startPreview();
				} catch (Exception e) {

				}
				isFlashOn = true;
			} else {
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

	public void flashLightOff() {
		Log.d("Time", "Off");
		if (isFlashOn) {
			if (cam != null || params != null) {
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

	public void startLights(View v) {
		sosFlag = false;
		mActive = true;
		mHandler.post(mRunnable);
	}

	public void sos(View v) {
		mActive = true;
		mHandler.post(mRunnable);
		sosFlag = true;

	}

	public void stopLights(View v) {

		Log.v("test", "test");
		isFlashOn = false;

		mHandler.removeCallbacks(mRunnable);

		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		cam.setParameters(params);

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
				convertedString = convertedString + "  |  ";
			}

			else {
				// concat the converted letter
				convertedString = convertedString + getMorseChar;

				// Add a space between each letter
				if (!getMorseChar.equals(" ")) {
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

}
