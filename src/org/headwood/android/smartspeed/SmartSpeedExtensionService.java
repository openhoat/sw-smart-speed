package org.headwood.android.smartspeed;

import android.os.Handler;

import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfoHelper;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

/**
 * The Sample Extension Service handles registration and keeps track of all
 * controls on all accessories.
 */
public class SmartSpeedExtensionService extends ExtensionService {

	public static final String LOG_TAG = "SmartSpeed";

	public SmartSpeedExtensionService() {
		super();
	}

	@Override
	public ControlExtension createControlExtension(String hostAppPackageName) {
		final boolean advancedFeaturesSupported = DeviceInfoHelper
				.isSmartWatch2ApiAndScreenDetected(this, hostAppPackageName);
		if (advancedFeaturesSupported) {
			return new SmartSpeedControl(hostAppPackageName, this,
					new Handler());
		} else {
			throw new IllegalArgumentException("No control for: "
					+ hostAppPackageName);
		}
	}

	@Override
	protected RegistrationInformation getRegistrationInformation() {
		return new SmartSpeedRegistrationInformation(this);
	}

	@Override
	protected boolean keepRunningWhenConnected() {
		return false;
	}
}
