package gbernat.flashlight;

import gbernat.flashlight.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class StrobeScreen extends Activity {

    private LinearLayout mLinearLayout;

    private Handler mHander = new Handler();

    private boolean mActive = false;
    private boolean mSwap = true;   
    
    private static Camera cam;
    private static Parameters params;
    private boolean isFlashOn;
    
    private SeekBar frequencyBar;
    int progressChanged = 3;
    

	private final Runnable mRunnable = new Runnable() {

		int colors[] = {
				Color.BLUE,Color.CYAN,Color.GRAY,Color.MAGENTA,Color.RED,Color.YELLOW,Color.LTGRAY,Color.DKGRAY
		};
		int i = 0;
		public void run() {		
			
			if (mActive) {
				if (mSwap) {
					mLinearLayout.setBackgroundColor(colors[i]);
					if(i>=(colors.length-1)){
						i = 0;
					}else{
					i++;
					}
					mSwap = false;
					mHander.postDelayed(mRunnable, (progressChanged * 100));
					
				} else {
					mLinearLayout.setBackgroundColor(colors[i]);
					if(i>=(colors.length-1)){
						i = 0;
					}else{
					i++;
					}
					mSwap = true;

					mHander.postDelayed(mRunnable, (progressChanged * 100));
					
					flashLightOff();
					
				}
			}
		}
	};
    
    public void flashLightOn() {
	   
	    if (!isFlashOn ) {
	        if (cam != null || params != null) {
	            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
	            cam.setParameters(params);
	            cam.startPreview();
	            isFlashOn = true;
	        } else {
	            cam = Camera.open();
	            params = cam.getParameters();
	            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
	            cam.setParameters(params);
	            cam.startPreview();
	            isFlashOn = true;
	        }
	    }
	    else{
	        return;
	    }
	}

	/*
	 * This method turn off the LED camera flash light
	 */
	public void flashLightOff() {
	   
	    if (isFlashOn) {
	        if (cam != null || params != null) {
	            params.setFlashMode(Parameters.FLASH_MODE_OFF);
	            cam.setParameters(params);
	            cam.stopPreview();
	            isFlashOn = false;
	        }
	        else{
	            params = cam.getParameters();
	            params.setFlashMode(Parameters.FLASH_MODE_OFF);
	            cam.setParameters(params);
	            cam.stopPreview();
	            isFlashOn = false;  
	        }
	    }
	    else{
	        return;
	    }
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strobe_screen);
        mLinearLayout = (LinearLayout) findViewById(R.id.strobe);
        startStrobe();
        
        
        frequencyBar = (SeekBar) findViewById(R.id.frequency_bar);
        frequencyBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }
 
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
 
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
 
    }
    
    private void startStrobe() {        
        mActive = true;
        mHander.post(mRunnable);
    }
    
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