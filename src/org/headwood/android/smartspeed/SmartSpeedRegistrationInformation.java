package org.headwood.android.smartspeed;

import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

public class SmartSpeedRegistrationInformation extends RegistrationInformation {

	private static final String EXTENSION_KEY_PREF = "EXTENSION_KEY_PREF";
	final Context mContext;
	private String extensionKey;

	protected SmartSpeedRegistrationInformation(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("context == null");
		}
		mContext = context;
	}

	@Override
	public synchronized String getExtensionKey() {
		if (TextUtils.isEmpty(extensionKey)) {
			final SharedPreferences pref = mContext.getSharedPreferences(
					EXTENSION_KEY_PREF, Context.MODE_PRIVATE);
			extensionKey = pref.getString(EXTENSION_KEY_PREF, null);
			if (TextUtils.isEmpty(extensionKey)) {
				extensionKey = UUID.randomUUID().toString();
				pref.edit().putString(EXTENSION_KEY_PREF, extensionKey)
						.commit();
			}
		}
		return extensionKey;
	}

	@Override
	public ContentValues getExtensionRegistrationConfiguration() {
		final String iconHostapp = ExtensionUtils.getUriString(mContext,
				R.drawable.icon);
		final String iconExtension = ExtensionUtils.getUriString(mContext,
				R.drawable.icon_extension);
		final String iconExtension48 = ExtensionUtils.getUriString(mContext,
				R.drawable.icon_extension48);
		final ContentValues values = new ContentValues();

		values.put(Registration.ExtensionColumns.NAME,
				mContext.getString(R.string.extension_name));
		values.put(Registration.ExtensionColumns.EXTENSION_KEY,
				getExtensionKey());
		values.put(Registration.ExtensionColumns.HOST_APP_ICON_URI, iconHostapp);
		values.put(Registration.ExtensionColumns.EXTENSION_ICON_URI,
				iconExtension);
		values.put(Registration.ExtensionColumns.EXTENSION_48PX_ICON_URI,
				iconExtension48);
		values.put(Registration.ExtensionColumns.NOTIFICATION_API_VERSION,
				getRequiredNotificationApiVersion());
		values.put(Registration.ExtensionColumns.PACKAGE_NAME,
				mContext.getPackageName());

		return values;
	}

	@Override
	public int getRequiredControlApiVersion() {
		return 1;
	}

	@Override
	public int getRequiredNotificationApiVersion() {
		return API_NOT_REQUIRED;
	}

	@Override
	public int getRequiredSensorApiVersion() {
		return API_NOT_REQUIRED;
	}

	@Override
	public int getRequiredWidgetApiVersion() {
		return API_NOT_REQUIRED;
	}

	@Override
	public int getTargetControlApiVersion() {
		return 2;
	}

	@Override
	public boolean isDisplaySizeSupported(int width, int height) {
		return width == SmartSpeedControl.getSupportedControlWidth(mContext)
				&& height == SmartSpeedControl
						.getSupportedControlHeight(mContext);
	}

	@Override
	public boolean supportsLowPowerMode() {
		return true;
	}
}
