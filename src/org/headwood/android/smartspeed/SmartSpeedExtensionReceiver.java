package org.headwood.android.smartspeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmartSpeedExtensionReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		intent.setClass(context, SmartSpeedExtensionService.class);
		context.startService(intent);
	}
}
