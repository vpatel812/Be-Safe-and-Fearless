package com.bSecure;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class ShakeWakeupService extends Service implements SensorEventListener {
	private static final int FORCE_THRESHOLD = 1000;
	private static final int TIME_THRESHOLD = 20;
	private static final int SHAKE_TIMEOUT = 350;
	private static final int SHAKE_DURATION = 1000;
	private static final int SHAKE_COUNT = 3;
	Context context = this;
	private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
	private long mLastTime;
	private Context mContext;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;
	SensorManager mSensorEventManager;

	Sensor mSensor;

	// BroadcastReceiver for handling ACTION_SCREEN_OFF.
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Check action just to be on the safe side.
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				Log.v("bsecure", "bsecure");
				// Unregisters the listener and registers it again.
				mSensorEventManager.unregisterListener(ShakeWakeupService.this);
				mSensorEventManager.registerListener(ShakeWakeupService.this,
						mSensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("shake service startup", "registering for shake");

		mContext = getApplicationContext();
		// Obtain a reference to system-wide sensor event manager.
		mSensorEventManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);

		// Get the default sensor for accel
		mSensor = mSensorEventManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		// Register for events.
		mSensorEventManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		// TODO I'll only register at screen off. I don't have a use for shake
		// while not in sleep (yet)

		// Register our receiver for the ACTION_SCREEN_OFF action. This will
		// make our receiver
		// code be called whenever the phone enters standby mode.
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);

	}

	@Override
	public void onDestroy() {
		// Unregister our receiver.
		unregisterReceiver(mReceiver);

		// Unregister from SensorManager.
		mSensorEventManager.unregisterListener(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't need a IBinder interface.
		return null;
	}

	// -------------end of the tutorial besides the accuracy and sensor change
	// stubs

	public void onShake() {
		// Poke a user activity to cause wake?
		WidgetReceiver wr = new WidgetReceiver();
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(300);
		Intent red = new Intent(ShakeWakeupService.this, WidgetReceiver.class);
		red.setAction("CHANGE_PICTURER");
		wr.onReceive(context, red);
		Toast.makeText(getApplicationContext(), "Shake On", Toast.LENGTH_LONG)
				.show();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not used right now
	}

	// Used to decide if it is a shake
	@SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
			return;
		long now = System.currentTimeMillis();

		if ((now - mLastForce) > SHAKE_TIMEOUT) {
			mShakeCount = 0;
		}

		if ((now - mLastTime) > TIME_THRESHOLD) {
			long diff = now - mLastTime;
			float speed = Math.abs(event.values[SensorManager.DATA_X]
					+ event.values[SensorManager.DATA_Y]
					+ event.values[SensorManager.DATA_Z] - mLastX - mLastY
					- mLastZ)
					/ diff * 10000;
			if (speed > FORCE_THRESHOLD) {
				if ((++mShakeCount >= SHAKE_COUNT)
						&& (now - mLastShake > SHAKE_DURATION)) {
					mLastShake = now;
					mShakeCount = 0;

					// call the reaction you want to have happen
					onShake();
				}
				mLastForce = now;
			}
			mLastTime = now;
			mLastX = event.values[SensorManager.DATA_X];
			mLastY = event.values[SensorManager.DATA_Y];
			mLastZ = event.values[SensorManager.DATA_Z];
		}

	}

}
