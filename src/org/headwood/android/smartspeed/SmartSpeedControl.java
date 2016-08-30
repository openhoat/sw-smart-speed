package org.headwood.android.smartspeed;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;

class SmartSpeedControl extends ControlExtension implements LocationListener {

	public static int getSupportedControlHeight(Context context) {
		return context.getResources().getDimensionPixelSize(
				R.dimen.smart_watch_2_control_height);
	}

	public static int getSupportedControlWidth(Context context) {
		return context.getResources().getDimensionPixelSize(
				R.dimen.smart_watch_2_control_width);
	}

	private final Context mContext;
	private Location lastLocation;

	private final float minDistance = 1f;
	private final long minTime = 500l;

	private float totalDistance = 0f;
	private long totalDuration = 0l;
	private int maxSpeed = 0;

	SmartSpeedControl(final String hostAppPackageName, final Context context,
			Handler handler) {
		super(context, hostAppPackageName);
		if (handler == null) {
			throw new IllegalArgumentException("handler == null");
		}
		mContext = context;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (lastLocation != null) {
			final float distance = lastLocation.distanceTo(location);
			final long duration = location.getTime() - lastLocation.getTime();
			Log.d(SmartSpeedExtensionService.LOG_TAG, "done " + distance
					+ " meters in " + duration + " ms");
			totalDistance += distance;
			totalDuration += duration;
			final int speed = Math.round(distance / 1000
					/ (duration / (1000f * 60 * 60)));
			maxSpeed = Math.max(maxSpeed, speed);
			final int avgSpeed = Math.round(totalDistance / 1000
					/ (totalDuration / (1000f * 60 * 60)));
			sendText(R.id.textViewSpeed, String.valueOf(speed));
			sendText(R.id.textViewMaxSpeed, String.valueOf(maxSpeed));
			sendText(R.id.textViewAvgSpeed, String.valueOf(avgSpeed));
		}
		lastLocation = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onResume() {
		showLayout(R.layout.layout, null);
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		final LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				minTime, minDistance, this);
		Log.d(SmartSpeedExtensionService.LOG_TAG, "started");
	};

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onStop() {
		final LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
		super.onStop();
		Log.d(SmartSpeedExtensionService.LOG_TAG, "stopped");
	}

	@Override
	public void onTouch(ControlTouchEvent event) {
		super.onTouch(event);
		final int action = event.getAction();
		if (action == Control.Intents.TOUCH_ACTION_LONGPRESS) {
			maxSpeed = 0;
			totalDistance = 0f;
			totalDuration = 0l;
		}
		setScreenState(Control.Intents.SCREEN_STATE_AUTO);
	}
}
