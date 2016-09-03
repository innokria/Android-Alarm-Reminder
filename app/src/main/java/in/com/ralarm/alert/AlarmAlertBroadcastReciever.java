/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package in.com.ralarm.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import in.com.ralarm.Alarm;
import in.com.ralarm.MainActivity;
import in.com.ralarm.R;
import in.com.ralarm.service.AlarmServiceBroadcastReciever;

public class AlarmAlertBroadcastReciever extends BroadcastReceiver {

	static final String TAG = "GCMDemso";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	Context ctx;


	@Override
	public void onReceive(Context context, Intent intent) {

		ctx = context;
		System.out.println("aka===AlarmAlertBroadcastReciever entry point");
		StaticWakeLock.lockOn(context);
		Bundle bundle = intent.getExtras();
		final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

		System.out.println("akam===alarm alert="+"="+alarm.getAlarmName());
		sendNotification(alarm.getAlarmName() );


	}


	private void sendNotification(String extras) {



		mNotificationManager = (NotificationManager)
				ctx.getSystemService(Context.NOTIFICATION_SERVICE);


		// decide to open global push or single
		PendingIntent contentIntent;


		contentIntent = PendingIntent.getActivity(ctx, 0,
				new Intent(ctx, MainActivity.class), 0);


		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
				.setSmallIcon(R.drawable.glyphicons_016_bin)
				//   .setContentTitle(extras.getString("title"))
				.setContentTitle("fire for "+extras)
				.setStyle(new NotificationCompat.BigTextStyle().bigText("message"))
				.setContentText("message")
				.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

	}
}
